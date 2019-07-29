package com.makina.shows_lukajovanovic.ui.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.makina.shows_lukajovanovic.data.model.Show
import com.makina.shows_lukajovanovic.data.repository.ShowsRepository

class ShowsViewModel : ViewModel(), Observer<Map<String, Show>> {
	private val showsListMutableLiveData = MutableLiveData<List<Show>>()
	val showsListLiveData: LiveData<List<Show>>
		get() = showsListMutableLiveData

	val showsList: List<Show>
		get() = showsListLiveData.value ?: listOf()

	init {
		ShowsRepository.showsMapLiveData.observeForever(this)
	}

	private fun showsToList(showsMap: Map<String, Show>?): List<Show> {
		val res: MutableList<Show> = mutableListOf()
		for(i in showsMap?.toList() ?: listOf()) {
			res.add(i.second)
		}
		return res
	}

	override fun onChanged(shows: Map<String, Show>?) {
		showsListMutableLiveData.value = showsToList(shows)
	}

	fun addShow(newShow: Show) {
		ShowsRepository.addShow(newShow)
	}

	override fun onCleared() {
		ShowsRepository.showsMapLiveData.removeObserver(this)
	}

	fun getData() {
		ShowsRepository.fetchWebData()
	}
}