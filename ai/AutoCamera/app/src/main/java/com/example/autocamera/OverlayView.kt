package com.example.autocamera

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

data class BBox(val rect: RectF, val label: String, val confidence: Float)

class OverlayView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val boxPaint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private val textPaint = Paint().apply {
        color = Color.GREEN
        textSize = 50f
        typeface = Typeface.DEFAULT_BOLD
    }

    private val boxes = mutableListOf<BBox>()

    fun setBoxes(newBoxes: List<BBox>) {
        boxes.clear()
        boxes.addAll(newBoxes)
        invalidate()  // 뷰 다시 그리기
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas ?: return

        for (box in boxes) {
            canvas.drawRect(box.rect, boxPaint)
            canvas.drawText("${box.label} ${(box.confidence * 100).toInt()}%", box.rect.left, box.rect.top - 10, textPaint)
        }
    }
}
