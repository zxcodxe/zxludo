package com.asta.ludoprank

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ModeSelectActivity : AppCompatActivity() {
    private var selectedPlayers = 4
    private var preSelectedWinner = "Player 1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode_select)

        val btn2P = findViewById<Button>(R.id.btn2P)
        val btn4P = findViewById<Button>(R.id.btn4P)
        val btnPlay = findViewById<Button>(R.id.btnPlay)

        btn2P.setOnClickListener { 
            selectedPlayers = 2 
            Toast.makeText(this, "2 Players Selected", Toast.LENGTH_SHORT).show() 
        }
        
        btn4P.setOnClickListener { 
            selectedPlayers = 4 
            Toast.makeText(this, "4 Players Selected", Toast.LENGTH_SHORT).show() 
        }

        btnPlay.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra("PLAYER_COUNT", selectedPlayers)
                putExtra("WINNER", preSelectedWinner)
            }
            startActivity(intent)
        }
    }
}
