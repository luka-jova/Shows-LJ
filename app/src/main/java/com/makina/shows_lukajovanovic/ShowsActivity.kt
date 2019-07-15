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
			showsList.add(Show(0, R.drawable.img_big_bang, "The Big Bang Theory", "2007 - 2019"))
			showsList.add(Show(1, R.drawable.img_office, "The Office", "2005 - 2013"))
			showsList.add(Show(2, R.drawable.img_dr_house, "Dr House", "2004 - 2012"))
			showsList.add(Show(3, R.drawable.img_jane_the_virgin, "Jane The Virgin", "2014 - "))
			showsList.add(Show(4, R.drawable.img_sherlock, "Sherlock", "2010 - "))
			showsList.add(Show(5, R.drawable.img_men, "Two and a half men", "? - ?"))

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
