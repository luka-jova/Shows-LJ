package com.makina.shows_lukajovanovic.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenResponse(
	@Json(name = "data")
	val token: Token,

	@Transient
	var isSuccessful: Boolean = true
)

@JsonClass(generateAdapter = true)
data class Token(
	@Json(name = "token")
	val token: String
)