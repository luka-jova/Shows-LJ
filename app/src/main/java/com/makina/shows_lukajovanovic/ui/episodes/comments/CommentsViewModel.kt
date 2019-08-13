package com.makina.shows_lukajovanovic.ui.episodes.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.makina.shows_lukajovanovic.data.model.Comment
import com.makina.shows_lukajovanovic.data.model.CommentsListResponse
import com.makina.shows_lukajovanovic.data.repository.CommentsRepository

class CommentsViewModel: ViewModel(), Observer<CommentsListResponse> {
	private val commentsListResponseMutableLiveData = MutableLiveData<CommentsListResponse>()
	val commentsListResponseLiveData: LiveData<CommentsListResponse>
		get() = commentsListResponseMutableLiveData

	init {
		CommentsRepository.commentsListResponseLiveData.observeForever(this)
	}

	override fun onChanged(response: CommentsListResponse?) {
		commentsListResponseMutableLiveData.value = response
	}

	fun getComments(showId: String, episodeId: String) {
		CommentsRepository.fetchDataFromWeb(showId, episodeId)
	}

	fun addComment(showId: String, episodeId: String, comment: Comment) {
		CommentsRepository.addComment(showId, episodeId, comment)
	}

	override fun onCleared() {
		CommentsRepository.commentsListResponseLiveData.removeObserver(this)
	}
}