package com.makina.shows_lukajovanovic.ui.episodes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.makina.shows_lukajovanovic.data.model.Episode
import com.makina.shows_lukajovanovic.data.model.Show
import com.makina.shows_lukajovanovic.data.repository.EpisodesRepository

class EpisodesViewModel : ViewModel(), Observer<List<Episode>> {
	var showId: Int = -1
	private val episodesMutableLiveData = MutableLiveData<List<Episode>>()

	val episodesLiveData: LiveData<List<Episode>>
		get() = episodesMutableLiveData

	val episdesList: List<Episode>
		get() = episodesLiveData.value ?: listOf()

	fun initialize() {
		if(EpisodesRepository.episodesLiveDataById(showId) == null) {Log.d("tigar", "wrong id $showId")}
		EpisodesRepository.episodesLiveDataById(showId)?.observeForever(this)
	}

	override fun onChanged(episodesList: List<Episode>?) {
		episodesMutableLiveData.value = episodesList
	}

	fun addEpisode(newEpisode: Episode) {
		EpisodesRepository.addEpisode(showId, newEpisode)
	}

	override fun onCleared() {
		EpisodesRepository.episodesLiveDataById(showId)?.removeObserver(this)
	}
}