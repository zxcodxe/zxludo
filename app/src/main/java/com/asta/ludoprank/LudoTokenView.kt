package com.asta.ludoprank

import android.content.Context
import android.graphics.*
import android.view.View

class LudoTokenView(
    context: Context,
    var tokenColor: Int
) : View(context) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val radius = minOf(width, height) * 0.35f

        paint.color = tokenColor
        canvas.drawCircle(
            width / 2f,
            height / 2f,
            radius,
            paint
        )

        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4f

        canvas.drawCircle(
            width / 2f,
            height / 2f,
            radius * 0.7f,
            paint
        )

        paint.style = Paint.Style.FILL
    }
}
