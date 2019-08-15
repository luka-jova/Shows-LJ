package com.makina.shows_lukajovanovic.data.model

import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MediaResponseData(
	@Json(name="path")
	val path: String = "",

	@Json(name = "_id")
	val imageId: String = ""
)

@JsonClass(generateAdapter = true)
data class MediaResponse(
	@Json(name="data")
	val mediaResponseData: MediaResponseData,

	@Transient
	val status: Int = ResponseStatus.SUCCESS
)

@JsonClass(generateAdapter = true)
data class EpisodePostData(
	@Json(name="showId")
	val showId: String,
	@Json(name="mediaId")
	val photoId: String,
	@Json(name="title")
	val title: String,
	@Json(name="description")
	val description: String,
	@Json(name="episodeNumber")
	val episodeNumber: String,
	@Json(name="season")
	val seasonNumber: String
)

@JsonClass(generateAdapter = true)
data class EpisodePostResponse(
	@Json(name = "data")
	val episode: Episode = Episode(),

	@Transient
	val status: Int = ResponseStatus.SUCCESS
)