package com.makina.shows_lukajovanovic.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShowResponse(
	@Json(name = "data")
	val show: Show?,

	@Transient
	var isSuccessful: Boolean = true
)