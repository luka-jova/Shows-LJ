package com.makina.shows_lukajovanovic.data.network

import com.makina.shows_lukajovanovic.data.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST



interface Api {
	@GET("/api/shows/")
	fun getShowsList(): Call<ShowsListResponse>

	@GET("/api/shows/{showId}")
	fun getShowById(@Path("showId") showId: String): Call<ShowResponse>

	@GET("/api/shows/{showId}/episodes")
	fun getEpisodesListById(@Path("showId") showId: String): Call<EpisodesListResponse>

	@POST("/api/users/sessions")
	fun loginUser(@Body user: LoginData): Call<TokenResponseFromWeb>

	@POST("/api/users/")
	fun registerUser(@Body user: LoginData): Call<CreateAccountResponse>

	@POST("/api/comments")
	fun postComment(@Header("Authorization") tokenHeader: String, @Body comment: CommentPostData): Call<CommentPostResponse>

	@GET("/api/episodes/{episodeId}/comments")
	fun getCommentsList(@Path("episodeId") episodeId: String): Call<CommentsListResponse>

	@POST("/api/shows/{showId}/like")
	fun postLike(@Header("Authorization") tokenHeader: String, @Path("showId") showId: String): Call<Show>

	@POST("/api/shows/{showId}/dislike")
	fun postDislike(@Header("Authorization") tokenHeader: String, @Path("showId") showId: String): Call<Show>

	@POST("/api/media")
	@Multipart
	fun uploadMedia(@Header("Authorization") tokenHeader: String, @Part("file\"; filename=\"image1212123.jpg\"") request: RequestBody): Call<MediaResponse>

	@POST("/api/episodes")
	fun uploadEpisode(@Header("Authorization") tokenHeader: String, @Body episode: EpisodePostData): Call<EpisodePostResponse>
}