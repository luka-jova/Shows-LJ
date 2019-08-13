package com.makina.shows_lukajovanovic.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makina.shows_lukajovanovic.data.model.EpisodeDetailsResponse
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import retrofit2.Response

object EpisodeDetailsRepository {

	private var episodeDetailsResponseMutableLiveData = MutableLiveData<EpisodeDetailsResponse>()
	val episodeDetailsResponseLiveData: LiveData<EpisodeDetailsResponse>
		get() = episodeDetailsResponseMutableLiveData

	fun observe(showId: String, episodeId: String) {
		for(i in EpisodesRepository.showDetailsResponseLiveData.value?.episodesList ?: mutableListOf()) {
			if(i.episodeId == episodeId) {
				episodeDetailsResponseMutableLiveData.value =
					EpisodeDetailsResponse(i, EpisodesRepository.showDetailsResponseLiveData.value?.status ?: ResponseStatus.FAIL)
				return
			}
		}
		//TODO handle error
	}
}