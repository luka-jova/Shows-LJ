package com.makina.shows_lukajovanovic.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makina.shows_lukajovanovic.data.model.Episode

object EpisodesRepository {

	private val episodes: MutableMap<Int, MutableList<Episode>> = mutableMapOf()
	private val episodesMapMutableLiveData = MutableLiveData<Map<Int, List<Episode>>>()
	val episodesMapLiveData
		get() = episodesMapMutableLiveData

	fun addEpisode(showId: Int, newEpisode: Episode) {
		if(showId !in episodes) {
			episodes[ showId ] = mutableListOf()
		}
		episodes[ showId ]?.add(newEpisode)
		episodesMapMutableLiveData.value = episodes
	}

}