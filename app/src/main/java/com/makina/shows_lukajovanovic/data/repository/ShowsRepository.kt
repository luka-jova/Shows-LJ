package com.makina.shows_lukajovanovic.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makina.shows_lukajovanovic.data.model.ShowsListResponse
import com.makina.shows_lukajovanovic.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object ShowsRepository {
	private val showsListResponseMutableLiveData = MutableLiveData<ShowsListResponse>()
	val showsListResponseLiveData: LiveData<ShowsListResponse>
		get() = showsListResponseMutableLiveData

	fun fetchShowsListWebData() {
		RetrofitClient.apiService?.getShowsList()?.enqueue(object : Callback<ShowsListResponse> {
			override fun onFailure(call: Call<ShowsListResponse>, t: Throwable) {
				showsListResponseMutableLiveData.value = ShowsListResponse(isSuccessful = false)
			}

			override fun onResponse(call: Call<ShowsListResponse>, response: Response<ShowsListResponse>) {
				with(response) {
					if(isSuccessful && body() != null) {
						showsListResponseMutableLiveData.value = ShowsListResponse(
							showsList = body()?.showsList ?: listOf(),
							isSuccessful = true
						)
					} else {
						showsListResponseMutableLiveData.value = ShowsListResponse(isSuccessful = false)
					}
				}
			}
		})
	}
}