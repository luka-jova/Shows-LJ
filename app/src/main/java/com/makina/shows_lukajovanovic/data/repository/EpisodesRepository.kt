package com.makina.shows_lukajovanovic.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makina.shows_lukajovanovic.data.model.Episode

object EpisodesRepository {

	private var episodes: MutableMap<Int, MutableList<Episode>> = mutableMapOf()
	private var episodesMutableLiveData: MutableMap<Int, MutableLiveData< List<Episode> > > = mutableMapOf()

	fun episodesLiveDataById(showId: Int): LiveData<List <Episode> >? {
		return episodesMutableLiveData[ showId ]
	}

	fun addEmptyShow(showId: Int) {
		if(showId in episodes) return
		episodes[ showId ] = mutableListOf()
		episodesMutableLiveData[ showId ] = MutableLiveData()
		episodesMutableLiveData[ showId ]?.value = episodes[ showId ]
	}

	fun addEpisode(showId: Int, newEpisode: Episode) {
		if(showId !in episodesMutableLiveData) {
			Log.d("tigar", "krivi showId($showId) u EpisodesRepository, stvaram prazan")
			addEmptyShow(showId)
		}
		episodes[ showId ]?.add(newEpisode)
		episodesMutableLiveData[ showId ]?.value = episodes[ showId ]
	}

}