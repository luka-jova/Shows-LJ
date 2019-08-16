package com.makina.shows_lukajovanovic.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.data.model.Show
import com.makina.shows_lukajovanovic.data.model.ShowResponse
import com.makina.shows_lukajovanovic.data.model.ShowsListResponse
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.makina.shows_lukajovanovic.data.network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object ShowsRepository {
	private var dataUpToDate = false
	private val showsListResponseMutableLiveData = MutableLiveData<ShowsListResponse>()
	val showsListResponseLiveData: LiveData<ShowsListResponse>
		get() = showsListResponseMutableLiveData

	var listener: RepositoryInfoHandler? = null

	private var callShows: Call<ShowsListResponse>? = null

	fun fetchShowsListWebData() {
		if(dataUpToDate && showsListResponseLiveData.value?.status == ResponseStatus.SUCCESS
			|| showsListResponseLiveData.value?.status == ResponseStatus.DOWNLOADING) {
			//Already downloaded or downloading
			return
		}
		//if(showsListResponseLiveData.value?.status == ResponseStatus.DOWNLOADING) return
		showsListResponseMutableLiveData.value = ShowsListResponse(status = ResponseStatus.DOWNLOADING)
		callShows = RetrofitClient.apiService?.getShowsList()
		callShows?.enqueue(object : Callback<ShowsListResponse> {
			override fun onFailure(call: Call<ShowsListResponse>, t: Throwable) {
				showsListResponseMutableLiveData.value = ShowsListResponse(status = ResponseStatus.FAIL)
				if(!call.isCanceled) listener?.displayMessage("Error", "Connection error")
			}

			override fun onResponse(call: Call<ShowsListResponse>, response: Response<ShowsListResponse>) {
				if(!response.isSuccessful || response.body() == null) {
					listener?.displayMessage("Error", "Error while downloading shows")
					showsListResponseMutableLiveData.value = ShowsListResponse(status = ResponseStatus.FAIL)
				}
				else {
					showsListResponseMutableLiveData.value =
						ShowsListResponse(
							showsList = response.body()?.showsList ?: listOf(),
							status = ResponseStatus.SUCCESS
						)
					dataUpToDate = true
				}
			}
		})
	}

	fun fetchMockData() {
		val buff:MutableList<Show> = mutableListOf()
		for(i in 1..10) {
			buff.add(Show(showId = "prvi$i", name = "Chernobyl", imageId = R.drawable.img_chernobyl, likeNumber = 10))
			buff.add(Show(showId = "drugi$i", name = "Two and a half men", imageId = R.drawable.img_men, likeNumber = 2))
			buff.add(Show(showId = "treci$i", name = "Sherlock", imageId = R.drawable.img_sherlock))
			buff.add(Show(showId = "cetvri$i", name = "Dr House", imageId = R.drawable.img_dr_house))
		}
		showsListResponseMutableLiveData.value = ShowsListResponse(status = ResponseStatus.SUCCESS, showsList = buff)
	}

	fun updateLikeCount(showId: String, newLikeCnt: Int?) {
		val showListResponse = showsListResponseLiveData.value
		if(newLikeCnt == null || showListResponse == null) return
		val showList = showListResponse.showsList ?: listOf()
		for(i in showList) {
			if(i.showId == showId) {
				i.likeNumber = newLikeCnt
			}
		}
		showsListResponseMutableLiveData.value = ShowsListResponse(showsList = showList, status = showListResponse.status)
	}

	fun getShowResponse(showId: String): ShowResponse {
		for(i in showsListResponseLiveData.value?.showsList ?: listOf()) {
			if(i.showId == showId) {
				return ShowResponse(show = i, status = showsListResponseLiveData.value?.status ?: ResponseStatus.FAIL)
			}
		}

		return ShowResponse(status = ResponseStatus.FAIL)
	}

	fun cancelCalls() {
		callShows?.cancel()
	}
}