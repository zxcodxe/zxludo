package com.asta.ludoprank

import kotlin.random.Random

class LudoGameEngine(
    val playerCount: Int,
    val selectedWinner: Int = -1
) {

    var currentPlayer = 0
        private set

    var diceValue = 1
        private set

    private val positions = Array(playerCount) {
        IntArray(4) { -1 }
    }

    fun rollDice(): Int {
        diceValue = Random.nextInt(1, 7)
        return diceValue
    }

    fun moveToken(token: Int): Boolean {

        if (token !in 0..3) return false

        val oldPosition = positions[currentPlayer][token]

        if (oldPosition == -1 && diceValue != 6) {
            return false
        }

        positions[currentPlayer][token] =
            if (oldPosition == -1) 0
            else oldPosition + diceValue

        return true
    }

    fun nextTurn() {
        if (diceValue != 6) {
            currentPlayer =
                (currentPlayer + 1) % playerCount
        }
    }

    fun getPosition(player: Int, token: Int): Int {
        return positions[player][token]
    }

    fun getWinner(): Int {
        for (player in 0 until playerCount) {
            if (positions[player].all { it >= 57 }) {
                return player
            }
        }
        return -1
    }

    fun getPrankWinner(): Int {
        return if (selectedWinner in 0 until playerCount) {
            selectedWinner
        } else {
            -1
        }
    }
}
