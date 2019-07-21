package com.makina.shows_lukajovanovic.ui.episodes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.makina.shows_lukajovanovic.data.model.Show
import com.makina.shows_lukajovanovic.data.repository.ShowsRepository

class EpisodesViewModelShow : ViewModel(), Observer<Show> {
	var showId = -1
	private val showMutableLiveData = MutableLiveData<Show>()

	val showLiveData: LiveData<Show>
		get() = showMutableLiveData

	val show: Show?
		get() = showLiveData.value

	fun initialize() {
		if(ShowsRepository.showLiveDataById(showId) == null) {Log.d("tigar", "wrong showId($showId) in EpisodesViewModelShow ") ; return}
		ShowsRepository.showLiveDataById(showId)?.observeForever(this)
	}

	override fun onChanged(t: Show?) {
		showMutableLiveData.value = t
	}


	override fun onCleared() {
		ShowsRepository.showLiveDataById(showId)?.removeObserver(this)
	}
}