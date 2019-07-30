package com.makina.shows_lukajovanovic.ui.episodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.makina.shows_lukajovanovic.data.model.Episode
import com.makina.shows_lukajovanovic.data.model.EpisodesFragmentResponse
import com.makina.shows_lukajovanovic.data.model.Show
import com.makina.shows_lukajovanovic.data.repository.EpisodesRepository
import com.makina.shows_lukajovanovic.data.repository.ShowsRepository

class EpisodesViewModel(val showId: String) : ViewModel(), Observer<EpisodesFragmentResponse> {
	private val episodesFragmentResponseMutableLiveData = MutableLiveData<EpisodesFragmentResponse>()
	val episodesFragmentResponseLiveData: LiveData<EpisodesFragmentResponse>
		get() = episodesFragmentResponseMutableLiveData
	val episodesFragmentResponse: EpisodesFragmentResponse?
		get() = episodesFragmentResponseLiveData.value

	init {
		EpisodesRepository.episodesFragmentResponseLiveData?.observeForever(this)
		EpisodesRepository.observe(showId)
	}

	override fun onChanged(response: EpisodesFragmentResponse?) {
		episodesFragmentResponseMutableLiveData.value = response
	}

	fun getData() {
		EpisodesRepository.fetchDataFromWeb(showId)
	}

	override fun onCleared() {
		EpisodesRepository.episodesFragmentResponseLiveData?.removeObserver(this)
	}
}