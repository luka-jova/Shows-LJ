package com.makina.shows_lukajovanovic.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodesListResponse(
	@Json(name = "data")
	val episodesList: List<Episode>? = arrayListOf(),

	@Transient
	var isSuccessful: Boolean = true
)
