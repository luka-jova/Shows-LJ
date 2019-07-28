package com.makina.shows_lukajovanovic.data.network

import com.makina.shows_lukajovanovic.data.model.EpisodesListResponse
import com.makina.shows_lukajovanovic.data.model.Show
import com.makina.shows_lukajovanovic.data.model.ShowResponse
import com.makina.shows_lukajovanovic.data.model.ShowsListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface Api {
	@GET("/api/shows/")
	fun getShowsIdList(): Call<ShowsListResponse>

	@GET("/api/shows/{showId}")
	fun getShowById(@Path("showId") showId: String): Call<ShowResponse>

	@GET("/api/shows/{showId}/episodes")
	fun getEpisodesListById(@Path("showId") showId: String): Call<EpisodesListResponse>
}