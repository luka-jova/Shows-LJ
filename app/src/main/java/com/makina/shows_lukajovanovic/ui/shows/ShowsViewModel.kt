package com.makina.shows_lukajovanovic.ui.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.makina.shows_lukajovanovic.data.model.Show
import com.makina.shows_lukajovanovic.data.repository.ShowsRepository

class ShowsViewModel : ViewModel(), Observer<Map<Int, Show>> {
	private val showsMapMutableLiveData = MutableLiveData<Map<Int, Show>>()

	val showsMapLiveData: LiveData<Map<Int, Show>>
		get() = showsMapMutableLiveData

	val showsList: List<Show>
		get() {
			val res: MutableList<Show> = mutableListOf()
			for(i in showsMapLiveData.value?.toList() ?: listOf()) {
				res.add(i.second)
			}
			return res
		}

	init {
		ShowsRepository.showsMapLiveData.observeForever(this)
	}

	override fun onChanged(shows: Map<Int, Show>?) {
		showsMapMutableLiveData.value = shows
	}

	fun addShow(newShow: Show) {
		ShowsRepository.addShow(newShow)
	}

	override fun onCleared() {
		ShowsRepository.showsMapLiveData.removeObserver(this)
	}
}