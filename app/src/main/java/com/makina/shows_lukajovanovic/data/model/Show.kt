package com.makina.shows_lukajovanovic.data.model

import com.makina.shows_lukajovanovic.data.model.Episode
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Show(
	@Json(name = "_id")
	val showId: String = "",

	@Json(name = "imageUrl")
	val imageUrl: String = "",

	@Json(name = "title")
	val name: String = "",

	@Json(name = "description")
	val showDescription:String = "Default Description",

	@Json(name = "likesCount")
	val likeNumber: Int = 0,

	@Transient
	val imageId: Int = -1,

	@Transient
	val airDate: String = "? - ?"

) :Serializable