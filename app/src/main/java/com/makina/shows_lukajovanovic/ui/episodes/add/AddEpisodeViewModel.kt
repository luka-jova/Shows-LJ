package com.makina.shows_lukajovanovic.ui.episodes.add

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.makina.shows_lukajovanovic.data.model.Episode
import com.makina.shows_lukajovanovic.data.model.EpisodePostResponse
import com.makina.shows_lukajovanovic.data.repository.AddEpisodeRepository

class AddEpisodeViewModel : ViewModel(), Observer<EpisodePostResponse> {
	private val episodePostResponseMutableLiveData = MutableLiveData<EpisodePostResponse>()
	val episodePostResponseLiveData: LiveData<EpisodePostResponse>
		get() = episodePostResponseMutableLiveData
	val episodePostResponse: EpisodePostResponse?
		get() = episodePostResponseLiveData.value

	init {
		AddEpisodeRepository.episodePostResponseLiveData.observeForever(this)
	}

	override fun onChanged(t: EpisodePostResponse?) {
		episodePostResponseMutableLiveData.value = t
	}

	fun addEpisode(showId: String, newEpisode: Episode, photoUri: Uri?) {
		AddEpisodeRepository.addEpisode(showId, newEpisode, photoUri)
	}

	fun addListener(listener: AddEpisodeRepository.AddEpisodeFragmentListener) {
		AddEpisodeRepository.listenerFragment = listener
	}

	override fun onCleared() {
		AddEpisodeRepository.episodePostResponseLiveData.removeObserver(this)
		AddEpisodeRepository.cancelCalls()
	}
}