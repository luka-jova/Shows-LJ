package com.makina.shows_lukajovanovic.ui.shows

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.data.model.ShowsListResponse
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.makina.shows_lukajovanovic.ui.welcome.LoginActivity
import com.makina.shows_lukajovanovic.ui.welcome.LoginViewModel
import kotlinx.android.synthetic.main.fragment_shows.*


class ShowsFragment : Fragment() {
	companion object {
		const val SHOWS_FRAGMENT_TAG = "SHOWS_FRAGMENT_TAG"
		const val SHOW_POSITION_TAG = "SHOW_POSITION_TAG"
		const val SHOW_LAYOUT_STATE_TAG = "SHOW_LAYOUT_STATE_TAG"
		const val LAYOUT_LIST = 0
		const val LAYOUT_GRID = 1
	}

	private lateinit var adapter: ShowsRecyclerAdapter
	private lateinit var viewModel: ShowsViewModel
	private lateinit var viewModelAuthorization: LoginViewModel
	private var curLayout = LAYOUT_GRID

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_shows, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		viewModel = ViewModelProviders.of(this).get(ShowsViewModel::class.java)
		viewModel.showsListResponseLiveData.observe(this, Observer {
			updateUI()
		})

		viewModelAuthorization = ViewModelProviders.of(this).get(LoginViewModel::class.java)
		viewModelAuthorization.tokenResponseLiveData.observe(this, Observer { tokenResponse ->
			updateUI()
		})

		curLayout = savedInstanceState?.getInt(SHOW_LAYOUT_STATE_TAG) ?: LAYOUT_GRID
		generateRecyclerView(savedInstanceState?.getInt(SHOW_POSITION_TAG) ?: 0)

		progressBarDownloading.visibility = View.INVISIBLE
		viewModel.getShowsList()
		fabToggleLayout.setOnClickListener {
			curLayout =
				if (curLayout == LAYOUT_LIST) LAYOUT_GRID
				else LAYOUT_LIST
			generateRecyclerView(getScrollPosition())
		}
		buttonLogout.setOnClickListener {
			LogoutConfirmDialogFragment().show(requireFragmentManager(), "Confirm logout dialog")
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		outState.putInt(SHOW_POSITION_TAG, getScrollPosition())
		outState.putInt(SHOW_LAYOUT_STATE_TAG, curLayout)
	}

	private fun getScrollPosition(): Int {
		return when {
			recyclerViewShows.layoutManager is LinearLayoutManager -> (recyclerViewShows.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
			recyclerViewShows.layoutManager is GridLayoutManager -> (recyclerViewShows.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
			else -> 0
		}
	}

	interface ShowsFragmentContainer {
		fun startEpisodesFragment(showId: String, title: String, likesNumber: Int?)
	}

	private fun generateRecyclerView(scrollPosition: Int) {
		val viewId = if (curLayout == LAYOUT_LIST) R.layout.layout_show else R.layout.layout_show_card
		adapter = ShowsRecyclerAdapter(viewId) { showId, title, likesNumber ->
			try {
				(activity as ShowsFragmentContainer).startEpisodesFragment(showId, title, likesNumber)
			} catch (e: ClassCastException) {
				e.printStackTrace()
			}
		}
		recyclerViewShows.adapter = adapter
		//recyclerViewShows.addItemDecoration(MarginDecoration(this))
		recyclerViewShows.layoutManager =
			if (curLayout == LAYOUT_LIST) LinearLayoutManager(requireContext())
			else GridLayoutManager(requireContext(), 2)
		updateUI()
		recyclerViewShows.scrollToPosition(scrollPosition)
		fabToggleLayout.setImageDrawable(
			ContextCompat.getDrawable(
				requireContext(),
				if (curLayout == LAYOUT_LIST) R.drawable.ic_gridview_white
				else R.drawable.ic_listview
			)
		)
	}

	private fun updateUI() {
		buttonLogout.visibility =
			if ((viewModelAuthorization.tokenResponse?.token ?: "") != "") View.VISIBLE else View.INVISIBLE
		val response: ShowsListResponse? = viewModel.showsListResponseLiveData.value
		if (response?.showsList?.isEmpty() != false && response?.status != ResponseStatus.DOWNLOADING) {
			recyclerViewShows.visibility = View.INVISIBLE
			defaultLayout.visibility = View.VISIBLE
		} else {
			recyclerViewShows.visibility = View.VISIBLE
			defaultLayout.visibility = View.INVISIBLE
		}
		when (response?.status) {
			ResponseStatus.SUCCESS -> {
				adapter.setData(response.showsList ?: listOf())
				progressBarDownloading.visibility = View.INVISIBLE
			}
			ResponseStatus.DOWNLOADING -> {
				progressBarDownloading.visibility = View.VISIBLE
			}
			ResponseStatus.FAIL -> {
				progressBarDownloading.visibility = View.INVISIBLE
			}
		}
	}

	fun responseLogout() {
		viewModelAuthorization.logout()
		startActivity(LoginActivity.newInstance(requireContext()))
		activity?.finish()
	}

}


class LogoutConfirmDialogFragment : DialogFragment() {
	interface LogoutDialogListener {
		fun responseLogout()
	}

	private lateinit var listener: LogoutDialogListener

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return activity?.let {
			AlertDialog.Builder(it)
				.setMessage("Are you sure to logout?")
				.setPositiveButton("Logout", DialogInterface.OnClickListener { dialog, id ->
					listener.responseLogout()
				})
				.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
				})
				.create()
		} ?: throw IllegalStateException("Activity cannot be null")
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		try {
			listener = context as LogoutDialogListener
		} catch (e: ClassCastException) {
			e.printStackTrace()
		}
	}
}