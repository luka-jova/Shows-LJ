package com.makina.shows_lukajovanovic.ui.episodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.makina.shows_lukajovanovic.data.model.LikeStatusList
import com.makina.shows_lukajovanovic.data.model.ShowDetailsResponse
import com.makina.shows_lukajovanovic.data.repository.EpisodesRepository

class EpisodesViewModel(val showId: String) : ViewModel() {
	private val showDetailsResponseMutableLiveData = MutableLiveData<ShowDetailsResponse>()
	val showDetailsResponseLiveData: LiveData<ShowDetailsResponse>
		get() = showDetailsResponseMutableLiveData
	val showDetailsResponse: ShowDetailsResponse?
		get() = showDetailsResponseLiveData.value

	private val observerShowDetailsResponse = Observer<ShowDetailsResponse> {response ->
		showDetailsResponseMutableLiveData.value = response
	}


	private val likeStatusListMutableLiveData = MutableLiveData<LikeStatusList>()
	val likeStatusListLiveData: LiveData<LikeStatusList>
		get() = likeStatusListMutableLiveData
	val likeStatusList: LikeStatusList?
		get() = likeStatusListLiveData.value

	private val observerLikeStatusList = Observer<LikeStatusList> {response ->
		likeStatusListMutableLiveData.value = response
	}

	init {
		EpisodesRepository.showDetailsResponseLiveData.observeForever(observerShowDetailsResponse)
		EpisodesRepository.likeStatusListLiveData.observeForever(observerLikeStatusList)
		EpisodesRepository.observe(showId)
	}

	fun getData() {
		EpisodesRepository.fetchDataFromWeb(showId)
	}

	fun postLikeStatus(showId: String, likeStatus: Int) {
		EpisodesRepository.postLikeStatus(showId, likeStatus)
	}

	override fun onCleared() {
		EpisodesRepository.cancelCalls()
		EpisodesRepository.showDetailsResponseLiveData.removeObserver(observerShowDetailsResponse)
		EpisodesRepository.likeStatusListLiveData.removeObserver(observerLikeStatusList)
	}
}