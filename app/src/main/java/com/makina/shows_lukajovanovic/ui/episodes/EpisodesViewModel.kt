package com.makina.shows_lukajovanovic.ui.episodes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.makina.shows_lukajovanovic.data.model.LikeStatus
import com.makina.shows_lukajovanovic.data.model.ShowDetailsResponse
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.makina.shows_lukajovanovic.data.repository.CommentsRepository
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


	private val likeStatusMutableLiveData = MutableLiveData<LikeStatus>()
	val likeStatusLiveData: LiveData<LikeStatus>
		get() = likeStatusMutableLiveData
	val likeStatus: LikeStatus?
		get() = likeStatusLiveData.value

	private val observerLikeStatusList = Observer<List<LikeStatus>> {response ->
		for(i in response) {
			if(i.showId != showId) continue
			likeStatusMutableLiveData.value = i
			return@Observer
		}
		EpisodesRepository.saveLikeStatus(LikeStatus(id = 0, showId = showId))
	}

	init {
		EpisodesRepository.showDetailsResponseLiveData.observeForever(observerShowDetailsResponse)
		EpisodesRepository.likeStatusListLiveData().observeForever(observerLikeStatusList)
		EpisodesRepository.observe(showId)
	}

	fun getData() {
		EpisodesRepository.fetchDataFromWeb(showId)
	}

	fun postLikeStatus(showId: String, likeStatus: Int) {
		val oldStatus = likeStatusLiveData.value ?: LikeStatus()
		if(oldStatus.likeStatus == likeStatus) {return}
		EpisodesRepository.postLikeStatus(
			LikeStatus(id = likeStatusLiveData.value?.id ?: 0, showId = showId, likeStatus = likeStatus),
			oldStatus
		)
	}

	override fun onCleared() {
		EpisodesRepository.cancelCalls()
		EpisodesRepository.showDetailsResponseLiveData.removeObserver(observerShowDetailsResponse)
		EpisodesRepository.likeStatusListLiveData().removeObserver(observerLikeStatusList)
	}
}