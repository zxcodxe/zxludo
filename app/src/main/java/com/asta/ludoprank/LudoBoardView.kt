package com.asta.ludoprank

import android.content.Context
import android.graphics.*
import android.view.View

class LudoBoardView(context: Context) : View(context) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val colors = intArrayOf(
        Color.RED,
        Color.GREEN,
        Color.YELLOW,
        Color.BLUE
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val size = minOf(width, height).toFloat()
        val cell = size / 15f

        canvas.drawColor(Color.WHITE)

        for (i in 0..14) {
            for (j in 0..14) {

                val left = j * cell
                val top = i * cell

                paint.color = Color.LTGRAY
                paint.style = Paint.Style.STROKE

                canvas.drawRect(
                    left, top,
                    left + cell, top + cell,
                    paint
                )
            }
        }

        drawHome(canvas, cell, 0, 0, colors[0])
        drawHome(canvas, cell, 9, 0, colors[1])
        drawHome(canvas, cell, 0, 9, colors[2])
        drawHome(canvas, cell, 9, 9, colors[3])

        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL

        canvas.drawRect(
            cell * 6,
            cell * 6,
            cell * 9,
            cell * 9,
            paint
        )

        paint.color = Color.BLACK
        paint.textSize = cell

        canvas.drawText(
            "LUDO",
            cell * 6.2f,
            cell * 7.6f,
            paint
        )
    }

    private fun drawHome(
        canvas: Canvas,
        cell: Float,
        x: Int,
        y: Int,
        color: Int
    ) {

        paint.color = color
        paint.style = Paint.Style.FILL

        canvas.drawRect(
            x * cell,
            y * cell,
            (x + 6) * cell,
            (y + 6) * cell,
            paint
        )

        paint.color = Color.WHITE

        canvas.drawRoundRect(
            (x + 1) * cell,
            (y + 1) * cell,
            (x + 5) * cell,
            (y + 5) * cell,
            12f, 12f,
            paint
        )

        paint.color = color

        for (i in 0..3) {
            val cx = (x + 2 + i % 2 * 2) * cell
            val cy = (y + 2 + i / 2 * 2) * cell

            canvas.drawCircle(
                cx, cy,
                cell * 0.45f,
                paint
            )
        }
    }
}
