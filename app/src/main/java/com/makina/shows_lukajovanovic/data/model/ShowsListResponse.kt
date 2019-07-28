package com.makina.shows_lukajovanovic.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShowsListResponse(
	@Json(name = "data")
	val showsIdList: List<ShowId>? = arrayListOf(),

	@Transient
	var isSuccessful: Boolean = true
)

@JsonClass(generateAdapter = true)
data class ShowId(
	@Json(name = "_id")
	val showId: String
)