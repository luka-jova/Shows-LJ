package com.makina.shows_lukajovanovic.data.repository

import androidx.lifecycle.MutableLiveData
import com.makina.shows_lukajovanovic.data.model.Episode
import com.makina.shows_lukajovanovic.data.model.ShowDetailsResponse
import com.makina.shows_lukajovanovic.data.model.EpisodesListResponse
import com.makina.shows_lukajovanovic.data.model.ShowResponse
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.makina.shows_lukajovanovic.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object EpisodesRepository {
	private val showDetailsResponseData = mutableMapOf<String, ShowDetailsResponse>()
	private val showDetailsResponseMutableLiveData = MutableLiveData<ShowDetailsResponse>()
	val showDetailsResponseLiveData
		get() = showDetailsResponseMutableLiveData

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

}