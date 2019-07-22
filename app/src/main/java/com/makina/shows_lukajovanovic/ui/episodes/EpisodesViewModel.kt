package com.makina.shows_lukajovanovic.ui.episodes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.makina.shows_lukajovanovic.data.model.Episode
import com.makina.shows_lukajovanovic.data.model.Show
import com.makina.shows_lukajovanovic.data.repository.EpisodesRepository
import com.makina.shows_lukajovanovic.data.repository.ShowsRepository

class EpisodesViewModel(val showId: Int) : ViewModel() {
	private val episodesMutableLiveData = MutableLiveData<List<Episode>>()
	val episodesLiveData: LiveData<List<Episode>>
		get() = episodesMutableLiveData

	val episodesList: List<Episode>
		get() = episodesLiveData.value ?: listOf()

	private val observerEpisodes = Observer<List<Episode>> {episodesList ->
		episodesMutableLiveData.value = episodesList
	}

	private val showMutableLiveData = MutableLiveData<Show>()
	val showLiveData: LiveData<Show>
		get() = showMutableLiveData

	val show: Show?
		get() = showLiveData.value

	private val observerShow = Observer<Show> {show ->
		showMutableLiveData.value = show
	}

	init {
		EpisodesRepository.episodesLiveDataById(showId)?.observeForever(observerEpisodes)
		ShowsRepository.showLiveDataById(showId)?.observeForever(observerShow)
	}

	override fun onCleared() {
		EpisodesRepository.episodesLiveDataById(showId)?.removeObserver(observerEpisodes)
		ShowsRepository.showLiveDataById(showId)?.removeObserver(observerShow)
	}
}