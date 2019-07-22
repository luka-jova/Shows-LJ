package com.makina.shows_lukajovanovic.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makina.shows_lukajovanovic.data.model.Episode

object EpisodesRepository {

	private val episodes: MutableMap<Int, MutableList<Episode>> = mutableMapOf()
	private val curEpisodesMutableLiveData = MutableLiveData<List <Episode>>()
	private var observingShowId = -1

	fun episodesLiveDataById(showId: Int): LiveData<List <Episode> >? {
		if(showId !in episodes) episodes[ showId ] = mutableListOf()
		curEpisodesMutableLiveData.value = episodes[ showId ]
		observingShowId = showId
		return curEpisodesMutableLiveData
	}

	fun addEpisode(showId: Int, newEpisode: Episode) {
		if(showId !in episodes) {
			episodes[ showId ] = mutableListOf()
		}
		episodes[ showId ]?.add(newEpisode)
		if(observingShowId == showId) {
			curEpisodesMutableLiveData.value = episodes[ showId ]
		}
	}

}