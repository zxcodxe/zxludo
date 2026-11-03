package com.asta.ludoprank

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val playerCount = intent.getIntExtra("PLAYER_COUNT", 4)
        val winner = intent.getStringExtra("WINNER")

        val tvInfo = findViewById<TextView>(R.id.tvGameInfo)
        tvInfo.text = "Players: $playerCount | Prank Winner: $winner"
    }
}
