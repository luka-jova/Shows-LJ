package com.makina.shows_lukajovanovic.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makina.shows_lukajovanovic.data.model.Comment
import com.makina.shows_lukajovanovic.data.model.CommentPostData
import com.makina.shows_lukajovanovic.data.model.CommentPostResponse
import com.makina.shows_lukajovanovic.data.model.CommentsListResponse
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.makina.shows_lukajovanovic.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object CommentsRepository {
	private val commentsListResponseMutableLiveData = MutableLiveData<CommentsListResponse>()
	val commentsListResponseLiveData: LiveData<CommentsListResponse>
		get() = commentsListResponseMutableLiveData
	val commentsListResponse: CommentsListResponse
		get() = commentsListResponseLiveData.value ?: CommentsListResponse()

	var listener: RepositoryInfoHandler? = null

	init {
		commentsListResponseMutableLiveData.value = CommentsListResponse(mutableListOf(), ResponseStatus.SUCCESS)
	}

	fun fetchDataFromWeb(showId: String, episodeId: String) {
		/*val buf = mutableListOf<Comment>()
		for(i in 1..5) {
			buf.add(Comment(userEmail = "bla@bla.bla", text = "Alo tamo jebo te led"))
			buf.add(Comment(userEmail = "neki user", text = "Svi pjevaju"))
			buf.add(Comment(userEmail = "luka j", text = "Ko tebe kamenom ti njega mitraljezom"))
			buf.add(Comment(userEmail = "ella d", text = "Nemrem izgubit kad doso sam s nicim"))
		}
		commentsListResponseMutableLiveData.value = CommentsListResponse(commentsList = buf, status = ResponseStatus.SUCCESS)
		*/
		if(commentsListResponseMutableLiveData.value?.status == ResponseStatus.DOWNLOADING) return
		commentsListResponseMutableLiveData.value = CommentsListResponse(status = ResponseStatus.DOWNLOADING)
		RetrofitClient.apiService?.getCommentsList(episodeId)?.enqueue(object: Callback <CommentsListResponse> {
			override fun onFailure(call: Call<CommentsListResponse>, t: Throwable) {
				listener?.displayMessage("Error", "No internet while downloading comments")
				commentsListResponseMutableLiveData.value = CommentsListResponse(status = ResponseStatus.FAIL)
			}

			override fun onResponse(call: Call<CommentsListResponse>, response: Response<CommentsListResponse>) {
				if(!response.isSuccessful || response.body() == null) {
					commentsListResponseMutableLiveData.value = CommentsListResponse(status = ResponseStatus.FAIL)
					listener?.displayMessage("Error", "Failed downloading comments.")
					return
				}
				commentsListResponseMutableLiveData.value = CommentsListResponse(
					commentsList = response.body()?.commentsList ?: mutableListOf(),
					status = ResponseStatus.SUCCESS
				)
			}
		})

	}

	fun addComment(showId: String, episodeId: String, text: String) {
		commentsListResponseMutableLiveData.value?.status = ResponseStatus.DOWNLOADING
		refresh()

		//TODO necu to iz repo-a cupat
		RetrofitClient.apiService?.postComment(
			AuthorizationRepository.tokenResponseLiveData.value?.token ?: "",
			CommentPostData(text = text, episodeId = episodeId)
		)?.enqueue(object: Callback<CommentPostResponse> {
			override fun onFailure(call: Call<CommentPostResponse>, t: Throwable) {
				listener?.displayMessage("Error", "No internet while posting comment.")
				commentsListResponseMutableLiveData.value?.status = ResponseStatus.FAIL
				refresh()
			}

			override fun onResponse(call: Call<CommentPostResponse>, response: Response<CommentPostResponse>) {
				if(!response.isSuccessful || response.body() == null) {
					listener?.displayMessage("Error", "Error while posting comment.")
					commentsListResponseMutableLiveData.value?.status = ResponseStatus.FAIL
					refresh()
					return
				}
				commentsListResponseMutableLiveData.value?.status = ResponseStatus.SUCCESS
				commentsListResponseMutableLiveData.value?.commentsList?.add(0, response.body()?.comment ?: Comment())
				refresh()
			}
		})
	}

	private fun refresh() {
		commentsListResponseMutableLiveData.value = commentsListResponseMutableLiveData.value
	}
}