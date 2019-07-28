package com.makina.shows_lukajovanovic.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.ShowsApp
import com.makina.shows_lukajovanovic.data.model.Show
import com.makina.shows_lukajovanovic.data.model.ShowResponse
import com.makina.shows_lukajovanovic.data.model.ShowsListResponse
import com.makina.shows_lukajovanovic.data.network.Api
import com.makina.shows_lukajovanovic.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ObjectInputStream
import java.lang.Exception

private const val FILENAME = "Shows.data"
object ShowsRepository {
	private val shows: MutableMap<String, Show> = mutableMapOf()
	private val showsMapMutableLiveData = MutableLiveData<Map<String, Show>>()
	val showsMapLiveData: LiveData<Map<String, Show>>
		get() = showsMapMutableLiveData

	init {
		getData()
	}

	fun addShow(newShow: Show) {
		if(newShow.showId in shows) return

		shows[ newShow.showId ] = newShow
		showsMapMutableLiveData.value = shows
	}

	private fun getData() {
		/*
		addShow(
			Show(
				0,
				R.drawable.img_big_bang,
				"The Big Bang Theory",
				"(2007 - 2019)",
				showDescription = "A woman who moves into an apartment across the hall from two brilliant but socially awkward physicists shows them how little they know about life outside of the laboratory."
			)
		)
		addShow(
			Show(
				1,
				R.drawable.img_office,
				"The Office",
				"(2005 - 2013)",
				showDescription = "A mockumentary on a group of typical office workers, where the workday consists of ego clashes, inappropriate behavior, and tedium."
			)
		)
		addShow(
			Show(
				2,
				R.drawable.img_dr_house,
				"Dr House",
				"(2004 - 2012)",
				showDescription = "An antisocial maverick doctor who specializes in diagnostic medicine does whatever it takes to solve puzzling cases that come his way using his crack team of doctors and his wits."
			)
		)
		addShow(
			Show(
				3,
				R.drawable.img_jane_the_virgin,
				"Jane The Virgin",
				"(2014 - )",
				showDescription = " A young, devout Catholic woman discovers that she was accidentally artificially inseminated."
			)
		)
		addShow(
			Show(
				4,
				R.drawable.img_sherlock,
				"Sherlock",
				"(2010 - )",
				showDescription = "A modern update finds the famous sleuth and his doctor partner solving crime in 21st century London."
			)
		)
		addShow(
			Show(
				5,
				R.drawable.img_men,
				"Two and a half men",
				"(2003 - 2015)",
				showDescription = "A hedonistic jingle writer's free-wheeling life comes to an abrupt halt when his brother and 10-year-old nephew move into his beach-front house."
			)
		)
		addShow(
			Show(
				6,
				R.drawable.img_chernobyl,
				"Chernobyl",
				"(2019 - 2019)",
				showDescription = "In April 1986, an explosion at the Chernobyl nuclear power plant in the Union of Soviet Socialist Republics becomes one of the world's worst man-made catastrophes."
			)
		)
		for(i in 7..15) {
			addShow(
				Show(
					i,
					R.drawable.img_chernobyl,
					"Chernobyl",
					"(2019 - 2019)",
					showDescription = "In April 1986, an explosion at the Chernobyl nuclear power plant in the Union of Soviet Socialist Republics becomes one of the world's worst man-made catastrophes."
				)
			)
		}
		*/
	}

	private val apiService = RetrofitClient.retrofitInstance?.create(Api::class.java)

	private fun downloadShow(showId: String) {
		apiService?.getShowById(showId)?.enqueue(object : Callback<ShowResponse> {

			override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
				//do something when we get error, always notify the user
				Log.d("tigar", "failed in onFailure for $showId")
			}

			override fun onResponse(call: Call<ShowResponse>, response: Response<ShowResponse>) {
				with(response) {
					if (isSuccessful && body() != null) {
						Log.d("tigar", ShowResponse(
							show = body()?.show,
							isSuccessful = true
						).toString())
						val buf = body()?.show
						if(buf != null) {addShow(buf)} else {}
					} else {
						Log.d("tigar", "failed in onResponse for $showId")
					}
				}
			}
		})
	}

	fun fetchWebData() {
		apiService?.getShowsIdList()?.enqueue(object : Callback<ShowsListResponse> {

			override fun onFailure(call: Call<ShowsListResponse>, t: Throwable) {
				//do something when we get error, always notify the user
				Log.d("tigar", "failed in onFailure")
			}

			override fun onResponse(call: Call<ShowsListResponse>, response: Response<ShowsListResponse>) {
				with(response) {
					if (isSuccessful && body() != null) {
							Log.d("tigar", ShowsListResponse(
								showsIdList = body()?.showsIdList,
								isSuccessful = true
							).toString())
						for(i in body()?.showsIdList ?: listOf()) {
							downloadShow(i.showId)
						}
					} else {
						Log.d("tigar", "failed in onResponse")
					}
				}
			}
		})
	}

}