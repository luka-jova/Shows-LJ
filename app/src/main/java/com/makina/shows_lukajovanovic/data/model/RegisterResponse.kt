package com.makina.shows_lukajovanovic.data.model

import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegistrationResponse(
	val token: String = "",
	var status: Int = ResponseStatus.SUCCESS
)


@JsonClass(generateAdapter = true)
data class CreateAccountResponse(
	@Transient
	var status: Int = ResponseStatus.SUCCESS
)
