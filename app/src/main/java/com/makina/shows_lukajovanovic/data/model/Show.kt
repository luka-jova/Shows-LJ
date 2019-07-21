package com.makina.shows_lukajovanovic.data.model

import com.makina.shows_lukajovanovic.data.model.Episode
import java.io.Serializable

class Show(val showId: Int, val imageId: Int, val name: String, val airDate: String, val episodeList: MutableList<Episode> = mutableListOf<Episode>(), val showDescription:String = "Default Description") :Serializable {
}