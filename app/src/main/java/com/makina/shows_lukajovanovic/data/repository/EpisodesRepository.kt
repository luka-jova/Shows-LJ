package com.makina.shows_lukajovanovic.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makina.shows_lukajovanovic.data.model.*
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.makina.shows_lukajovanovic.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object EpisodesRepository {
	const val LIKE_STATUS = 0
	const val DISLIKE_STATUS = 1
	const val NONE_STATUS = 2

	private val showDetailsResponseData = mutableMapOf<String, ShowDetailsResponse>()
	private val showDetailsResponseMutableLiveData = MutableLiveData<ShowDetailsResponse>()
	val showDetailsResponseLiveData
		get() = showDetailsResponseMutableLiveData

	private val likeStatusList = LikeStatusList()
	private val likeStatusListMutableLiveData = MutableLiveData<LikeStatusList>()
	val likeStatusListLiveData: LiveData<LikeStatusList>
		get() = likeStatusListMutableLiveData

	var listener: RepositoryInfoHandler? = null

	fun observe(showId: String) {
		showDetailsResponseMutableLiveData.value = showDetailsResponseData[ showId ]
	}

	fun addEpisode(showId: String, newEpisode: Episode) {
		if(showId !in showDetailsResponseData) {
			showDetailsResponseData[ showId ] = ShowDetailsResponse(null, mutableListOf(), ResponseStatus.SUCCESS)
		}
		showDetailsResponseData[ showId ]?.episodesList?.add(newEpisode)
		showDetailsResponseMutableLiveData.value = showDetailsResponseData[ showId ]
	}

	fun fetchDataFromWeb(showId: String) {
		if(showId in showDetailsResponseData && showDetailsResponseData[ showId ]?.status == ResponseStatus.SUCCESS
			|| showDetailsResponseData[ showId ]?.status == ResponseStatus.DOWNLOADING) {
			//Data is already downloaded or currently downloading
			return
		}
		showDetailsResponseMutableLiveData.value = ShowDetailsResponse(status = ResponseStatus.DOWNLOADING)
		var episodesListResponse: EpisodesListResponse? = null
		var showResponse: ShowResponse? = null
		fun updateData() {
			if(episodesListResponse != null && showResponse != null) {
				var status: Int = ResponseStatus.FAIL
				if((showResponse?.isSuccessful ?: false) and (episodesListResponse?.isSuccessful ?: false))
					status = ResponseStatus.SUCCESS

				showDetailsResponseData[ showId ] =
					ShowDetailsResponse(
						show = showResponse?.show,
						episodesList = episodesListResponse?.episodesList?.toMutableList() ?: mutableListOf(),
						status = status
					)
				showDetailsResponseMutableLiveData.value = showDetailsResponseData[ showId ]
			}
		}

		RetrofitClient.apiService?.getEpisodesListById(showId)?.enqueue(object : Callback<EpisodesListResponse> {
			override fun onFailure(call: Call<EpisodesListResponse>, t: Throwable) {
				episodesListResponse = EpisodesListResponse(isSuccessful = false)
				updateData()
			}

			override fun onResponse(call: Call<EpisodesListResponse>, response: Response<EpisodesListResponse>) {
				with(response) {
					if(isSuccessful && body() != null) {
						episodesListResponse = EpisodesListResponse(
							episodesList = body()?.episodesList,
							isSuccessful = true
						)
					} else {
						episodesListResponse = EpisodesListResponse(isSuccessful = false)
					}
				}
				updateData()
			}
		})

		RetrofitClient.apiService?.getShowById(showId)?.enqueue(object : Callback<ShowResponse> {
			override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
				showResponse = ShowResponse(isSuccessful = false)
				updateData()
			}

			override fun onResponse(call: Call<ShowResponse>, response: Response<ShowResponse>) {
				with(response) {
					if(isSuccessful && body() != null) {
						showResponse = ShowResponse(
							show = body()?.show,
							isSuccessful = true
						)
					} else {
						showResponse= ShowResponse(isSuccessful = false)
					}
				}
				updateData()
			}
		})

	}

	private var callLikeStatus: Call<Show>? = null

	fun postLikeStatus(showId: String, likeStatus: Int) {
		if(likeStatusList.status == ResponseStatus.DOWNLOADING) return
		val token = AuthorizationRepository.tokenResponseLiveData.value?.token ?: ""

		callLikeStatus =
			if(likeStatus == LIKE_STATUS)
				RetrofitClient.apiService?.postLike(token, showId)
			else
				RetrofitClient.apiService?.postDislike(token, showId)

		likeStatusList.status = ResponseStatus.DOWNLOADING
		likeStatusListMutableLiveData.value = likeStatusList

		callLikeStatus?.enqueue(object: Callback<Show> {
			override fun onFailure(call: Call<Show>, t: Throwable) {
				if(!call.isCanceled) {
					listener?.displayMessage("Error", "Connection error")
					likeStatusList.status = ResponseStatus.FAIL
					likeStatusListMutableLiveData.value = likeStatusList
				}
			}

			override fun onResponse(call: Call<Show>, response: Response<Show>) {
				if(!response.isSuccessful || response.body() == null) {
					listener?.displayMessage("Error", "Could not submit your response.")
					likeStatusList.status = ResponseStatus.FAIL
					likeStatusListMutableLiveData.value = likeStatusList
					return
				}
				likeStatusList.status = ResponseStatus.SUCCESS
				likeStatusList.likeStatus[ showId ] = likeStatus
				likeStatusListMutableLiveData.value = likeStatusList

				val newLikeCount = response.body()?.likeNumber ?: (showDetailsResponseData[ showId ]?.show?.likeNumber ?: 0)

				showDetailsResponseData[ showId ]?.show?.likeNumber = newLikeCount
				listener?.displayMessage("bok", "${showDetailsResponseData[showId]?.show?.likeNumber}")
				showDetailsResponseMutableLiveData.value = showDetailsResponseData[ showId ]

			}
		})
	}

	fun cancelCalls() {
		if(likeStatusList.status == ResponseStatus.DOWNLOADING) {
			listener?.displayMessage("Cancel calls", "")
			likeStatusListMutableLiveData.value?.status = ResponseStatus.FAIL
			callLikeStatus?.cancel()
		}

	}

}