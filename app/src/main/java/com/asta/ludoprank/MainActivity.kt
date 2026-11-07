package com.asta.ludoprank

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*

class MainActivity : Activity() {

    private lateinit var root: LinearLayout

    var selectedMode = "Friends"
    var selectedPlayers = 4
    var selectedWinner = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showSplash()
    }

    fun createScreen(): LinearLayout {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(24, 24, 24, 24)
            setBackgroundColor(Color.rgb(12, 35, 75))
        }
        setContentView(root)
        return root
    }

    fun addTitle(text: String) {
        root.addView(TextView(this).apply {
            this.text = text
            textSize = 30f
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            setPadding(10, 20, 10, 30)
        })
    }

    fun addButton(text: String, action: () -> Unit) {
        root.addView(Button(this).apply {
            this.text = text
            textSize = 17f
            setOnClickListener { action() }
            layoutParams = LinearLayout.LayoutParams(
                -1, 60
            ).apply {
                setMargins(0, 8, 0, 8)
            }
        })
    }

    fun showSplash() {
        SplashScreen.show(this)
    }

    fun showHome() {
        LudoHomeScreen.show(this)
    }

    fun showGameSelection() {
        GameSelectionScreen.show(this)
    }

    fun showPlayerSelection() {
        PlayerSelectionScreen.show(this)
    }

    fun showPrankSetup() {
        PrankSetupScreen.show(this)
    }

    fun startLudo() {
        createScreen()
        addTitle("LUDO GAME")

        val board = LudoBoardView(this)
        root.addView(board,
            LinearLayout.LayoutParams(-1, 0, 1f))

        addButton("ROLL DICE") {
            board.invalidate()
        }

        addButton("HOME") {
            showHome()
        }
    }
}
