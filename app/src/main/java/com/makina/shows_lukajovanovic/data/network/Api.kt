package com.makina.shows_lukajovanovic.data.network

import com.makina.shows_lukajovanovic.data.model.*
import retrofit2.Call
import retrofit2.http.*

interface Api {
	@GET("/api/shows/")
	fun getShowsList(): Call<ShowsListResponse>

	@GET("/api/shows/{showId}")
	fun getShowById(@Path("showId") showId: String): Call<ShowResponse>

	@GET("/api/shows/{showId}/episodes")
	fun getEpisodesListById(@Path("showId") showId: String): Call<EpisodesListResponse>

	@POST("/api/users/sessions")
	fun loginUser(@Body user: LoginData): Call<TokenResponse>

	@POST("/api/users/")
	fun registerUser(@Body user: LoginData): Call<RegisterResponse>
}