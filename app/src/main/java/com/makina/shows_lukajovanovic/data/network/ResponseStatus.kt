package com.makina.shows_lukajovanovic.data.network

object ResponseStatus {
	///Response status
	const val SUCCESS = 0
	const val FAIL = 1
	const val DOWNLOADING = 2

	//Message info (for Toast-ing message on the screen only once)
	const val INFO_ERROR_INTERNET = -1
	const val INFO_ERROR_LOGIN = -2
	const val INFO_ERROR_REGISTER = -3
	const val INFO_ERROR_POST_COMMENT = -4
	const val INFO_ERROR_GET_COMMENTS = -5
}