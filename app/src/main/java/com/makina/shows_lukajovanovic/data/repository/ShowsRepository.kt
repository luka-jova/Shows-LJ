package com.makina.shows_lukajovanovic.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.data.model.Show
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

	fun fetchShowsListWebData() {
		/*val buff:MutableList<Show> = mutableListOf()
		for(i in 1..10) {
			buff.add(Show(showId = "prvi$i", name = "Chernobyl", imageId = R.drawable.img_chernobyl, likeNumber = 10))
			buff.add(Show(showId = "drugi$i", name = "Two and a half men", imageId = R.drawable.img_men, likeNumber = 2))
			buff.add(Show(showId = "treci$i", name = "Sherlock", imageId = R.drawable.img_sherlock))
			buff.add(Show(showId = "cetvri$i", name = "Dr House", imageId = R.drawable.img_dr_house))
		}
		showsListResponseMutableLiveData.value = ShowsListResponse(status = ResponseStatus.SUCCESS, showsList = buff)
		return*/
		if(dataUpToDate && showsListResponseLiveData.value?.status == ResponseStatus.SUCCESS) {
			//Already downloaded
			return
		}
		showsListResponseMutableLiveData.value = ShowsListResponse(status = ResponseStatus.DOWNLOADING)
		RetrofitClient.apiService?.getShowsList()?.enqueue(object : Callback<ShowsListResponse> {
			override fun onFailure(call: Call<ShowsListResponse>, t: Throwable) {
				showsListResponseMutableLiveData.value = ShowsListResponse(status = ResponseStatus.FAIL)
			}

			override fun onResponse(call: Call<ShowsListResponse>, response: Response<ShowsListResponse>) {
				with(response) {
					if(isSuccessful && body() != null) {
						showsListResponseMutableLiveData.value = ShowsListResponse(
							showsList = body()?.showsList ?: listOf(),
							status = ResponseStatus.SUCCESS
						)
						dataUpToDate = true
					} else {
						showsListResponseMutableLiveData.value = ShowsListResponse(status = ResponseStatus.FAIL)
					}
				}
			}
		})
		val buf = mutableListOf<Show>()
	}
}