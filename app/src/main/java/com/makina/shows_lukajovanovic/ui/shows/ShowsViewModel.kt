package com.makina.shows_lukajovanovic.ui.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.makina.shows_lukajovanovic.data.model.Show
import com.makina.shows_lukajovanovic.data.repository.ShowsRepository

class ShowsViewModel : ViewModel(), Observer<List<Show>> {

	private val showsMutableLiveData = MutableLiveData<List<Show>>()

	val showsLiveData: LiveData<List<Show>>
		get() = showsMutableLiveData

	val showsList: List<Show>
		get() = showsMutableLiveData.value ?: listOf()

	init {
		ShowsRepository.showsLiveData.observeForever(this)
	}

	override fun onChanged(showsList: List<Show>?) {
		showsMutableLiveData.value = showsList
	}

	fun addShow(newShow: Show) {
		ShowsRepository.addShow(newShow)
	}
}