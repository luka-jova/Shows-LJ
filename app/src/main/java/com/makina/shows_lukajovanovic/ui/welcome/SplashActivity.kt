package com.makina.shows_lukajovanovic.ui.welcome

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.renderscript.Sampler
import android.util.Log
import android.view.View
import android.view.animation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.view.doOnLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.makina.shows_lukajovanovic.ui.MainContainerActivity
import kotlinx.android.synthetic.main.activity_splash.*



class SplashActivity: AppCompatActivity() {
	companion object {
		const val START_ACTIVITY_CODE = 1
	}
	private var active = true
	private var killedAnimation = false

	private lateinit var viewModel: LoginViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		active = true
		setContentView(R.layout.activity_splash)
		textViewTitle.visibility = View.INVISIBLE
		layout.doOnLayout {
			animateLogo()
		}

	}

	override fun onResume() {
		viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
		viewModel.tokenResponseLiveData.observe(this, Observer {})
		viewModel.resetToken()
		if(killedAnimation) animateLogo()
		active = true
		super.onResume()
	}

	private val handlerThread = Handler()
	private val startActivityRunnable = Runnable {
		if(viewModel.tokenResponse?.status == ResponseStatus.SUCCESS && (viewModel.tokenResponse?.token ?: "") != "") {
			startActivity(MainContainerActivity.newInstance(this))
		}
		else {
			startActivity(LoginActivity.newInstance(this))
		}
		finish()
	}

	private lateinit var valueAnimatorLogo: ValueAnimator
	private fun animateLogo() {
		valueAnimatorLogo = ValueAnimator.ofFloat(imageViewLogo.y, guidelineCenter.y - imageViewLogo.height)
		with(valueAnimatorLogo) {
			addUpdateListener {
				imageViewLogo.y = it.animatedValue as Float
			}
			interpolator = BounceInterpolator()
			duration = 1000
			doOnEnd {
				if(active) animateTitle()
			}
			startDelay = 500
			start()
		}
	}


	private lateinit var valueAnimatorTitle: ValueAnimator
	private fun animateTitle() {
		valueAnimatorTitle = ValueAnimator.ofFloat(0f, 1f)
		with(valueAnimatorTitle) {
			addUpdateListener {
				val value = it.animatedValue as Float
				textViewTitle.visibility = View.VISIBLE
				textViewTitle.scaleX = value
				textViewTitle.scaleY = value
			}

			interpolator = OvershootInterpolator()
			duration = 500
			doOnEnd {
				if (active)
					handlerThread.postDelayed(startActivityRunnable, 2000)
			}
			start()
		}
	}

	override fun onStop() {
		active = false
		killedAnimation = true
		handlerThread.removeCallbacks(startActivityRunnable)
		if(::valueAnimatorLogo.isInitialized) valueAnimatorLogo.cancel()
		if(::valueAnimatorTitle.isInitialized) valueAnimatorTitle.cancel()
		super.onStop()
	}
}