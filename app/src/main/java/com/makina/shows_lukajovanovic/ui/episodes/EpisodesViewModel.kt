package com.makina.shows_lukajovanovic.ui.episodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.makina.shows_lukajovanovic.data.model.ShowDetailsResponse
import com.makina.shows_lukajovanovic.data.repository.EpisodesRepository

class EpisodesViewModel(val showId: String) : ViewModel(), Observer<ShowDetailsResponse> {
	private val showDetailsResponseMutableLiveData = MutableLiveData<ShowDetailsResponse>()
	val showDetailsResponseLiveData: LiveData<ShowDetailsResponse>
		get() = showDetailsResponseMutableLiveData
	val showDetailsResponse: ShowDetailsResponse?
		get() = showDetailsResponseLiveData.value

	init {
		EpisodesRepository.showDetailsResponseLiveData.observeForever(this)
		EpisodesRepository.observe(showId)
	}

	override fun onChanged(response: ShowDetailsResponse?) {
		showDetailsResponseMutableLiveData.value = response
	}

	fun getData() {
		EpisodesRepository.fetchDataFromWeb(showId)
	}

	override fun onCleared() {
		EpisodesRepository.showDetailsResponseLiveData.removeObserver(this)
	}
}