package com.makina.shows_lukajovanovic.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makina.shows_lukajovanovic.data.model.ShowsListResponse
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.makina.shows_lukajovanovic.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object ShowsRepository {
	private var dataUpToDate = false
	private val showsListResponseMutableLiveData = MutableLiveData<ShowsListResponse>()
	val showsListResponseLiveData: LiveData<ShowsListResponse>
		get() = showsListResponseMutableLiveData

	fun fetchShowsListWebData() {
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
	}
}