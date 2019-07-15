package com.makina.shows_lukajovanovic

class Show(val showId: Int, val imageId: Int, val name: String, val airDate: String, val episodeList: MutableList<String> = mutableListOf<String>()) {
}