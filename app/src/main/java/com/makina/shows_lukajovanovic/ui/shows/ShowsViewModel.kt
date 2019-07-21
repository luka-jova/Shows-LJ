package com.makina.shows_lukajovanovic.ui.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.makina.shows_lukajovanovic.data.model.Show
import com.makina.shows_lukajovanovic.data.repository.ShowsRepository

class ShowsViewModel : ViewModel() {
	private var shows: MutableLiveData<List<Show>> = ShowsRepository.showsLiveData

	val showsLiveData: LiveData<List<Show>>
		get() = shows

	fun addShow(newShow: Show) {
		ShowsRepository.addShow(newShow)
	}
}