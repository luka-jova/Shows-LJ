package com.makina.shows_lukajovanovic.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makina.shows_lukajovanovic.data.model.Comment
import com.makina.shows_lukajovanovic.data.model.CommentsListResponse
import com.makina.shows_lukajovanovic.data.network.ResponseStatus

object CommentsRepository {
	private val commentsListResponseMutableLiveData = MutableLiveData<CommentsListResponse>()
	val commentsListResponseLiveData: LiveData<CommentsListResponse>
		get() = commentsListResponseMutableLiveData

	fun fetchDataFromWeb(showId: String, episodeId: String) {
		val buf = mutableListOf<Comment>()
		for(i in 1..5) {
			buf.add(Comment(userEmail = "bla@bla.bla", text = "Alo tamo jebo te led"))
			buf.add(Comment(userEmail = "neki user", text = "Svi pjevaju"))
			buf.add(Comment(userEmail = "luka j", text = "Ko tebe kamenom ti njega mitraljezom"))
			buf.add(Comment(userEmail = "ella d", text = "Nemrem izgubit kad doso sam s nicim"))
		}
		commentsListResponseMutableLiveData.value = CommentsListResponse(commentsList = buf, status = ResponseStatus.SUCCESS)
	}

	fun addComment(showId: String, episodeId: String, comment: Comment) {
		if(commentsListResponseMutableLiveData.value == null) commentsListResponseMutableLiveData.value = CommentsListResponse()
		commentsListResponseMutableLiveData.value?.commentsList?.add(comment)
		commentsListResponseMutableLiveData.value = commentsListResponseMutableLiveData.value
	}
}