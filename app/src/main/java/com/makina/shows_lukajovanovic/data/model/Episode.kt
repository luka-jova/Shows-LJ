package com.makina.shows_lukajovanovic.data.model

import androidx.core.text.isDigitsOnly
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
class Episode(
	@Json(name = "title")
	var name: String = "",

	@Json(name = "season")
	var seasonNumString: String = "1",

	@Json(name = "episodeNumber")
	var episodeNumString: String = "1",

	@Json(name = "description")
	var episodeDescription:String = ""

) :Serializable {
	var seasonNum: Int
		get() = if(!seasonNumString.isDigitsOnly() || seasonNumString == "") 0 else seasonNumString.toInt()
		set(value) {
			seasonNumString = value.toString()
		}

	var episodeNum: Int
		get() =  if(!episodeNumString.isDigitsOnly() || episodeNumString == "") 0 else episodeNumString.toInt()
		set(value) {
			episodeNumString = value.toString()
		}
}