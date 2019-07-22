package com.makina.shows_lukajovanovic.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.ShowsApp
import com.makina.shows_lukajovanovic.data.model.Show
import java.io.ObjectInputStream
import java.lang.Exception

private const val FILENAME = "Shows.data"
object ShowsRepository {

	private val showsListId: MutableList<Int> = mutableListOf()
	private val showsListIdMutableLiveData = MutableLiveData<List<Int>>()

	val showsListIdLiveData: LiveData<List<Int>>
		get() = showsListIdMutableLiveData


	private val shows: MutableMap<Int, Show> = mutableMapOf()
	private val showsMapMutableLiveData = MutableLiveData<Map<Int, Show>>()
	private val curShowMutableLiveData = MutableLiveData<Show>()
	private var observingShowId = -1

	val showsMapLiveData: LiveData<Map<Int, Show>>
		get() = showsMapMutableLiveData

	fun showLiveDataById(showId: Int): LiveData<Show>? {
		curShowMutableLiveData.value = shows[ showId ]
		observingShowId = showId
		return curShowMutableLiveData
	}

	init {
		getData()
	}

	fun addShow(newShow: Show) {
		if(newShow.showId in shows) return

		shows[ newShow.showId ] = newShow
		showsMapMutableLiveData.value = shows

		showsListId.add(newShow.showId)
		showsListIdMutableLiveData.value = showsListId
	}

	private fun getData() {
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
	}

	private fun readShowsFromFile(): MutableList<Show> {
		return try {
			ObjectInputStream(ShowsApp.instance.openFileInput(FILENAME)).use {
				it.readObject() as MutableList<Show>
			}
		} catch (e: Exception) {
			return mutableListOf()
		}
	}

}