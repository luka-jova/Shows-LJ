package com.makina.shows_lukajovanovic.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class EpisodesFragmentResponse(
	val show: Show?,
	val episodesList: MutableList<Episode> = mutableListOf(),

	@Transient
	var isSuccessful: Boolean = true
)

@JsonClass(generateAdapter = true)
data class ShowResponse(
	@Json(name = "data")
	val show: Show? = null,

	@Transient
	var isSuccessful: Boolean = true
)

@JsonClass(generateAdapter = true)
data class EpisodesListResponse(
	@Json(name = "data")
	val episodesList: List<Episode>? = arrayListOf(),

	@Transient
	var isSuccessful: Boolean = true
)