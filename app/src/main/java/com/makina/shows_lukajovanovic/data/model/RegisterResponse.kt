package com.makina.shows_lukajovanovic.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterResponse(
	@Transient
	var isSuccessful: Boolean = true
)
