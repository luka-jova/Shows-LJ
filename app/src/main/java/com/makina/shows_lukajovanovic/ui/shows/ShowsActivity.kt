package com.makina.shows_lukajovanovic.ui.shows

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.data.model.Episode
import com.makina.shows_lukajovanovic.data.model.Show
import kotlinx.android.synthetic.main.activity_shows.*

class ShowsActivity : AppCompatActivity() {
	companion object {
		val showsList = listOf<Show>()
		fun newInstance(context: Context) : Intent {
			val intent = Intent(context, ShowsActivity::class.java)
			return intent
		}
	}

	private lateinit var adapter: ShowsRecyclerAdapter
	private lateinit var viewModel: ShowsViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_shows)

	  adapter = ShowsRecyclerAdapter()
	  recyclerViewShows.layoutManager = LinearLayoutManager(this)
	  recyclerViewShows.adapter = adapter

	  viewModel = ViewModelProviders.of(this).get(ShowsViewModel::class.java)
	  viewModel.showsMapLiveData.observe(this, Observer {
		  Log.d("tigar", "trigger u ShowsActivity")
		  adapter.setData(viewModel.showsList)
	  })

	  buttonAdd.setOnClickListener {
		  viewModel.addShow(Show(viewModel.showsList.size, viewModel.showsList[ 0 ].imageId, viewModel.showsList[ 0 ].name, viewModel.showsList[ 0 ].airDate))
	  }
  }


}
