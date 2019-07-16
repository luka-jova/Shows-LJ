package com.makina.shows_lukajovanovic

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_shows.*

class ShowsActivity : AppCompatActivity() {
	companion object {
		var showsList = mutableListOf<Show>()

		init {
			showsList.add(Show(
				0,
				R.drawable.img_big_bang,
				"The Big Bang Theory",
				"(2007 - 2019)",
				showDescription = "A woman who moves into an apartment across the hall from two brilliant but socially awkward physicists shows them how little they know about life outside of the laboratory."
			))
			showsList.add(Show(
				1,
				R.drawable.img_office,
				"The Office",
				"(2005 - 2013)",
				showDescription = "A mockumentary on a group of typical office workers, where the workday consists of ego clashes, inappropriate behavior, and tedium."
			))
			showsList.add(Show(
				2,
				R.drawable.img_dr_house,
				"Dr House",
				"(2004 - 2012)",
				showDescription = "An antisocial maverick doctor who specializes in diagnostic medicine does whatever it takes to solve puzzling cases that come his way using his crack team of doctors and his wits."
			))
			showsList.add(Show(
				3,
				R.drawable.img_jane_the_virgin,
				"Jane The Virgin",
				"(2014 - )",
				showDescription = " A young, devout Catholic woman discovers that she was accidentally artificially inseminated."
			))
			showsList.add(Show(
				4,
				R.drawable.img_sherlock,
				"Sherlock",
				"(2010 - )",
				showDescription = "A modern update finds the famous sleuth and his doctor partner solving crime in 21st century London."
			))
			showsList.add(Show(
				5,
				R.drawable.img_men,
				"Two and a half men",
				"(2003 - 2015)",
				showDescription =  "A hedonistic jingle writer's free-wheeling life comes to an abrupt halt when his brother and 10-year-old nephew move into his beach-front house."
			))
			showsList.add(Show(
				6,
				R.drawable.img_chernobyl,
				"Chernobyl",
				"(2019 - 2019)",
				showDescription =  "In April 1986, an explosion at the Chernobyl nuclear power plant in the Union of Soviet Socialist Republics becomes one of the world's worst man-made catastrophes."
			))

		}
		fun newInstance(context: Context) : Intent {
			val intent = Intent(context, ShowsActivity::class.java)
			return intent
		}
	}

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_shows)

	  val recyclerView: RecyclerView = recyclerViewShows
	  recyclerView.layoutManager = LinearLayoutManager(this)
	  recyclerView.adapter = ShowsRecyclerAdapter(showsList)
  }
}
