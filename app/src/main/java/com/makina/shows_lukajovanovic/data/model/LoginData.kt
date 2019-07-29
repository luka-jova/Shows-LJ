package com.makina.shows_lukajovanovic.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

data class LoginData(
	@Json(name = "email")
	val email: String,

	@Json(name = "password")
	val password: String

) : Serializable