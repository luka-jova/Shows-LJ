package com.makina.shows_lukajovanovic.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import com.makina.shows_lukajovanovic.ShowsApp
import com.makina.shows_lukajovanovic.data.database.LikeStatusDatabase
import com.makina.shows_lukajovanovic.data.model.*
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.makina.shows_lukajovanovic.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

object EpisodesRepository {
	const val LIKE_STATUS = 0
	const val DISLIKE_STATUS = 1
	const val NONE_STATUS = 2

	private val showDetailsResponseData = mutableMapOf<String, ShowDetailsResponse>()
	private val showDetailsResponseMutableLiveData = MutableLiveData<ShowDetailsResponse>()
	val showDetailsResponseLiveData
		get() = showDetailsResponseMutableLiveData


	var listener: RepositoryInfoHandler? = null

	fun observe(showId: String) {
		showDetailsResponseMutableLiveData.value = showDetailsResponseData[showId]
	}

	fun addEpisode(showId: String, newEpisode: Episode) {
		if (showId !in showDetailsResponseData) {
			showDetailsResponseData[showId] = ShowDetailsResponse(null, mutableListOf(), ResponseStatus.SUCCESS)
		}
		showDetailsResponseData[showId]?.episodesList?.add(newEpisode)
		showDetailsResponseMutableLiveData.value = showDetailsResponseData[showId]
	}

	fun fetchDataFromWeb(showId: String) {
		/*if(showId in showDetailsResponseData && showDetailsResponseData[ showId ]?.status == ResponseStatus.SUCCESS
			|| showDetailsResponseData[ showId ]?.status == ResponseStatus.DOWNLOADING) {
			//Data is already downloaded or currently downloading
			return
		}*/
		if (showId in showDetailsResponseData && showDetailsResponseData[showId]?.status == ResponseStatus.DOWNLOADING) {
			//Data is currently downloading
			return
		}
		showDetailsResponseMutableLiveData.value = ShowDetailsResponse(status = ResponseStatus.DOWNLOADING)
		var episodesListResponse: EpisodesListResponse? = null
		var showResponse: ShowResponse? = null
		fun updateData() {
			if (episodesListResponse != null && showResponse != null) {
				var status: Int = ResponseStatus.FAIL
				if ((showResponse?.isSuccessful ?: false) and (episodesListResponse?.isSuccessful ?: false))
					status = ResponseStatus.SUCCESS

				showDetailsResponseData[showId] =
					ShowDetailsResponse(
						show = showResponse?.show,
						episodesList = episodesListResponse?.episodesList?.toMutableList() ?: mutableListOf(),
						status = status
					)
				showDetailsResponseMutableLiveData.value = showDetailsResponseData[showId]
			}
		}

		RetrofitClient.apiService?.getEpisodesListById(showId)?.enqueue(object : Callback<EpisodesListResponse> {
			override fun onFailure(call: Call<EpisodesListResponse>, t: Throwable) {
				episodesListResponse = EpisodesListResponse(isSuccessful = false)
				updateData()
			}

			override fun onResponse(call: Call<EpisodesListResponse>, response: Response<EpisodesListResponse>) {
				with(response) {
					if (isSuccessful && body() != null) {
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
					if (isSuccessful && body() != null) {
						showResponse = ShowResponse(
							show = body()?.show,
							isSuccessful = true
						)
					} else {
						showResponse = ShowResponse(isSuccessful = false)
					}
				}
				updateData()
			}
		})

	}

	private var callLikeStatus: Call<Show>? = null
	fun postLikeStatus(likeStatus: LikeStatus, oldStatus: LikeStatus) {
		val token = AuthorizationRepository.tokenResponseLiveData.value?.token ?: ""
		Log.d("tigar", "stari status: $oldStatus")
		callLikeStatus =
			if (likeStatus.likeStatus == LIKE_STATUS)
				RetrofitClient.apiService?.postLike(token, likeStatus.showId)
			else
				RetrofitClient.apiService?.postDislike(token, likeStatus.showId)

		saveLikeStatus(oldStatus.copy(status = ResponseStatus.DOWNLOADING))
		callLikeStatus?.enqueue(object : Callback<Show> {
			override fun onFailure(call: Call<Show>, t: Throwable) {
				saveLikeStatus(oldStatus.copy(status = ResponseStatus.FAIL))
				if(call.isCanceled) return
				listener?.displayMessage("Error", "Connection error")
			}

			override fun onResponse(call: Call<Show>, response: Response<Show>) {
				if (!response.isSuccessful || response.body() == null) {
					saveLikeStatus(oldStatus.copy(status = ResponseStatus.FAIL))
					listener?.displayMessage("Error", "Could not post your response")
					return
				}

				saveLikeStatus(likeStatus.copy(status = ResponseStatus.SUCCESS))
				val newLikeCount = response.body()?.likeNumber ?: (showDetailsResponseData[ likeStatus.showId ]?.show?.likeNumber ?: 0)

				showDetailsResponseData[ likeStatus.showId ]?.show?.likeNumber = newLikeCount
				showDetailsResponseMutableLiveData.value = showDetailsResponseData[ likeStatus.showId ]
			}
		})
	}

	fun cancelCalls() {
		callLikeStatus?.cancel()
		//TODO makni progress bar
	}

	private val databaseLikeStatus: LikeStatusDatabase = Room.databaseBuilder(
		ShowsApp.instance,
		LikeStatusDatabase::class.java,
		"likestatus-database"
	)
		.fallbackToDestructiveMigration()
		.build()
	private val executor = Executors.newSingleThreadExecutor()

	fun likeStatusListLiveData(): LiveData<List<LikeStatus>> = databaseLikeStatus.likeStatusDao().getAll()

	fun saveLikeStatus(likeStatus: LikeStatus) {
		//TODO zasto je likeStatusListLiveData().value == null???
		if(likeStatus.id == 0) {
			executor.execute {
				databaseLikeStatus.likeStatusDao().insert(likeStatus)
			}
			return
		}
		executor.execute {
			databaseLikeStatus.likeStatusDao().update(likeStatus)
		}
	}

}