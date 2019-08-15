package com.makina.shows_lukajovanovic.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {
	private var retrofit: Retrofit? = null

	const val BASE_URL = "https://api.infinum.academy/"

	val retrofitInstance: Retrofit?
		get() {
			if (retrofit == null) {
				retrofit = Retrofit.Builder()
					.baseUrl(BASE_URL)
					.addConverterFactory(MoshiConverterFactory.create())
					.build()
			}
			return retrofit
		}

	val apiService = retrofitInstance?.create(Api::class.java)
}