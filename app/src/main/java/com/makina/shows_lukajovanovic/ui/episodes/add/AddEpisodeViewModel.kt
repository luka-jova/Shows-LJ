package com.makina.shows_lukajovanovic.ui.episodes.add

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.makina.shows_lukajovanovic.data.model.Episode
import com.makina.shows_lukajovanovic.data.repository.EpisodesRepository

class AddEpisodeViewModel : ViewModel() {
	fun addEpisode(showId: Int, newEpisode: Episode) {
		EpisodesRepository.addEpisode(showId, newEpisode)
	}
}