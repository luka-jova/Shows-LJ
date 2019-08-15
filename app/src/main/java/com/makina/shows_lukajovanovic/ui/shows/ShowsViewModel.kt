package com.makina.shows_lukajovanovic.ui.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.makina.shows_lukajovanovic.data.model.Show
import com.makina.shows_lukajovanovic.data.model.ShowsListResponse
import com.makina.shows_lukajovanovic.data.repository.ShowsRepository

class ShowsViewModel : ViewModel(), Observer<ShowsListResponse> {
	private val showsListResponseMutableLiveData = MutableLiveData<ShowsListResponse>()
	val showsListResponseLiveData: LiveData<ShowsListResponse>
		get() = showsListResponseMutableLiveData

	init {
		ShowsRepository.showsListResponseLiveData.observeForever(this)
	}

	override fun onChanged(response: ShowsListResponse?) {
		showsListResponseMutableLiveData.value = response
	}

	fun getShowsList() {
		ShowsRepository.fetchShowsListWebData()
	}

	override fun onCleared() {
		ShowsRepository.cancelCalls()
		ShowsRepository.showsListResponseLiveData.removeObserver(this)
	}

}