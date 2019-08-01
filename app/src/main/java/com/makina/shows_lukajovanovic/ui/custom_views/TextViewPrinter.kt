package com.makina.shows_lukajovanovic.ui.custom_views

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

private const val TIME_DELTA = 50L

class TextViewPrinter @JvmOverloads constructor(
	context: Context,
	attributes: AttributeSet? = null,
	defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle
) :AppCompatTextView(context, attributes, defStyleAttr) {
	private var textContainer = getText().toString()
	var text: String
		get() = textContainer
		set(value) {
			textContainer = value
			handler.removeCallbacks(animationText)
			startAnimation(TIME_DELTA)
		}

	private var curLength = 0
	private val handlerThread = Handler()
	private val animationText = object: Runnable {
		override fun run() {
			if(curLength >= text.length) return
			curLength++
			setText(text.subSequence(0, curLength))
			handlerThread.postDelayed(this, TIME_DELTA)
		}
	}

	private fun startAnimation(timeDelta: Long) {
		curLength = 0
		handlerThread.postDelayed(animationText, timeDelta)
	}

	init {
		//TODO kako da ubijem handler ako view nestane?
		startAnimation(TIME_DELTA)
	}

}