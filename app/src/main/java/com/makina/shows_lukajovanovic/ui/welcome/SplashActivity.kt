package com.makina.shows_lukajovanovic.ui.welcome

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.view.doOnLayout
import com.makina.shows_lukajovanovic.R
import kotlinx.android.synthetic.main.activity_splash.*



class SplashActivity: AppCompatActivity() {
	companion object {
		const val START_ACTIVITY_CODE = 1
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash)

		//TODO zasto se animacija nece pokrenuti ako je view na pocetku bio izvan ekrana?
		//TODO zasto animacija nece raditi ako je samo u onCreate odmah pokrenuta, a ne u layout.doOnLayout?
		//TODO podesi tocnu granicu
		textViewTitle.visibility = View.INVISIBLE
		layout.doOnLayout {
			imageViewLogo.animate()
				.y(layout.height.toFloat() / 2 - imageViewLogo.height)
				.setInterpolator(BounceInterpolator())
				.setDuration(1000)
				.setStartDelay(500)
				.setListener(object : Animator.AnimatorListener {
					override fun onAnimationEnd(p0: Animator?) {
						animateTitle()
					}

					override fun onAnimationRepeat(p0: Animator?) {}
					override fun onAnimationCancel(p0: Animator?) {}
					override fun onAnimationStart(p0: Animator?) {}

				})
				.start()
		}

	}

	private val handlerThread = Handler {
		if(it.what == START_ACTIVITY_CODE)
			startActivity(LoginActivity.newInstance(this@SplashActivity))
		this@SplashActivity.finish()
		true
	}

	fun animateTitle() {
		val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
		valueAnimator.addUpdateListener {
			val value = it.animatedValue as Float
			textViewTitle.visibility = View.VISIBLE
			textViewTitle.scaleX = value
			textViewTitle.scaleY = value
		}

		valueAnimator.interpolator = OvershootInterpolator()
		valueAnimator.duration = 500
		valueAnimator.doOnEnd {
			handlerThread.sendEmptyMessageDelayed(START_ACTIVITY_CODE, 2000)
		}
		valueAnimator.start()
	}

	override fun onStop() {
		//TODO ne radi remove
		handlerThread.removeCallbacksAndMessages(Any())
		handlerThread.removeMessages(START_ACTIVITY_CODE)
		super.onStop()
		finish()
	}

}