package com.makina.shows_lukajovanovic.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makina.shows_lukajovanovic.data.model.Episode
import com.makina.shows_lukajovanovic.data.model.EpisodesListResponse
import com.makina.shows_lukajovanovic.data.network.Api
import com.makina.shows_lukajovanovic.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object EpisodesRepository {

	private val episodes: MutableMap<String, MutableList<Episode>> = mutableMapOf()
	private val episodesMapMutableLiveData = MutableLiveData<Map<String, List<Episode>>>()
	val episodesMapLiveData: LiveData<Map<String, List<Episode>>>
		get() = episodesMapMutableLiveData

	fun addEpisode(showId: String, newEpisode: Episode) {
		if(showId !in episodes) {
			episodes[ showId ] = mutableListOf()
		}
		episodes[ showId ]?.add(newEpisode)
		episodesMapMutableLiveData.value = episodes
	}

	//TODO jel smijem napraviti dva apia?
	private val apiService = RetrofitClient.retrofitInstance?.create(Api::class.java)
	fun fetchDataFromWeb(showId: String) {
		apiService?.getEpisodesListById(showId)?.enqueue(object : Callback<EpisodesListResponse> {

			override fun onFailure(call: Call<EpisodesListResponse>, t: Throwable) {
				//do something when we get error, always notify the user
				Log.d("tigar", "failed in onFailure")
				t.printStackTrace()
			}

			override fun onResponse(call: Call<EpisodesListResponse>, response: Response<EpisodesListResponse>) {
				with(response) {
					if (isSuccessful && body() != null) {
						Log.d("tigar", EpisodesListResponse(
							episodesList= body()?.episodesList,
							isSuccessful = true
						).toString())
						for(i in body()?.episodesList ?: listOf()) {
							addEpisode(showId, i)
						}
					} else {
						Log.d("tigar", "failed in onResponse")
					}
				}
			}
		})

	}

}