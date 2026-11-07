package com.asta.ludoprank

import android.content.Context
import android.graphics.*
import android.view.View
import kotlin.random.Random

class LudoDiceView(context: Context) : View(context) {

    var diceNumber = 1
        private set

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    fun rollDice(): Int {
        diceNumber = Random.nextInt(1, 7)
        invalidate()
        return diceNumber
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val size = minOf(width, height) * 0.8f
        val left = (width - size) / 2
        val top = (height - size) / 2

        paint.color = Color.WHITE
        canvas.drawRoundRect(
            left, top,
            left + size, top + size,
            18f, 18f, paint
        )

        paint.color = Color.BLACK

        val positions = arrayOf(
            Pair(0.25f, 0.25f),
            Pair(0.50f, 0.25f),
            Pair(0.75f, 0.25f),
            Pair(0.25f, 0.50f),
            Pair(0.50f, 0.50f),
            Pair(0.75f, 0.50f),
            Pair(0.25f, 0.75f),
            Pair(0.50f, 0.75f),
            Pair(0.75f, 0.75f)
        )

        val patterns = mapOf(
            1 to listOf(4),
            2 to listOf(0, 8),
            3 to listOf(0, 4, 8),
            4 to listOf(0, 2, 6, 8),
            5 to listOf(0, 2, 4, 6, 8),
            6 to listOf(0, 2, 3, 5, 6, 8)
        )

        patterns[diceNumber]?.forEach {
            val p = positions[it]

            canvas.drawCircle(
                left + size * p.first,
                top + size * p.second,
                size * 0.07f,
                paint
            )
        }
    }
}
