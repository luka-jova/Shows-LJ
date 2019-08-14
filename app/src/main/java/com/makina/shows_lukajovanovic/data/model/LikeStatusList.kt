package com.makina.shows_lukajovanovic.data.model

import com.makina.shows_lukajovanovic.data.network.ResponseStatus

class LikeStatusList(
	var likeStatus: MutableMap<String, Int> = mutableMapOf(),
	var status: Int = ResponseStatus.SUCCESS
)