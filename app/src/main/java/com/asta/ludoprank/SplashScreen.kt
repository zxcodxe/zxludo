package com.asta.ludoprank

import android.graphics.Color
import android.view.Gravity
import android.widget.*

object SplashScreen {

    fun show(activity: MainActivity) {

        val layout = activity.createScreen()

        layout.gravity = Gravity.CENTER

        val title = TextView(activity).apply {
            text = "GAMEMOTION"
            textSize = 36f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        layout.addView(title)

        layout.postDelayed({
            activity.showHome()
        }, 2000)
    }
}
