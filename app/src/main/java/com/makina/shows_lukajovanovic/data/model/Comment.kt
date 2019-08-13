package com.makina.shows_lukajovanovic.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Comment(
	@Json(name="episodeId")
	var episodeId: String = "",

	@Json(name="userEmail")
	var userEmail: String,

	@Json(name="text")
	var text: String = "",

	@Json(name="_id")
	var commentId: String = ""
)