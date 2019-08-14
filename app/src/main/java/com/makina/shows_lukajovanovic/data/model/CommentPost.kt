package com.makina.shows_lukajovanovic.data.model

import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class CommentPostData(
	@Json(name="text")
	val text: String,

	@Json(name="episodeId")
	val episodeId: String
)

@JsonClass(generateAdapter = true)
data class CommentPostResponse(
	@Json(name = "data")
	var comment: Comment,

	@Transient
	var status: Int = ResponseStatus.SUCCESS
)