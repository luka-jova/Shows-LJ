package com.makina.shows_lukajovanovic.data.model

import com.makina.shows_lukajovanovic.data.network.ResponseStatus

data class EpisodeDetailsResponse(
	var episode: Episode = Episode(),

	@Transient
	var status: Int = ResponseStatus.SUCCESS
)