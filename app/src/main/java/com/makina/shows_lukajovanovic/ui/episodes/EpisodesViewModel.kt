package com.makina.shows_lukajovanovic.ui.episodes

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

	private val observerEpisodes = Observer<Map<Int, List<Episode>>> {t ->
		episodesMutableLiveData.value = t[ showId ] ?: listOf()
	}

	private val showMutableLiveData = MutableLiveData<Show>()
	val showLiveData: LiveData<Show>
		get() = showMutableLiveData

	val show: Show?
		get() = showLiveData.value

	private val observerShow = Observer<Map<Int, Show>> {t ->
		showMutableLiveData.value = t[ showId ]
	}

	init {
		EpisodesRepository.episodesMapLiveData?.observeForever(observerEpisodes)
		ShowsRepository.showsMapLiveData?.observeForever(observerShow)
	}

	override fun onCleared() {
		EpisodesRepository.episodesMapLiveData?.removeObserver(observerEpisodes)
		ShowsRepository.showsMapLiveData?.removeObserver(observerShow)
	}
}