package com.makina.shows_lukajovanovic.ui.episodes.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.makina.shows_lukajovanovic.data.model.EpisodeDetailsResponse
import com.makina.shows_lukajovanovic.data.repository.EpisodeDetailsRepository
import com.makina.shows_lukajovanovic.data.repository.EpisodesRepository

class EpisodeDetailsViewModel : ViewModel(), Observer<EpisodeDetailsResponse>{
	private val episodeDetailsResponseMutableLiveData = MutableLiveData<EpisodeDetailsResponse>()
	val episodeDetailsResponseLiveData: LiveData<EpisodeDetailsResponse>
		get() = episodeDetailsResponseMutableLiveData

	init {
		EpisodeDetailsRepository.episodeDetailsResponseLiveData.observeForever(this)
	}

	override fun onChanged(response: EpisodeDetailsResponse?) {
		episodeDetailsResponseMutableLiveData.value = response
	}

	fun setupEpisode(showId: String, episodeId: String) {
		EpisodeDetailsRepository.observe(showId, episodeId)
	}

	override fun onCleared() {
		EpisodeDetailsRepository.episodeDetailsResponseLiveData.removeObserver(this)
	}
}