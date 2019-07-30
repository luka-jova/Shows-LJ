package com.makina.shows_lukajovanovic.data.model

import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShowsListResponse(
	@Json(name = "data")
	val showsList: List<Show>? = arrayListOf(),

	@Transient
	var status: Int = ResponseStatus.SUCCESS
)