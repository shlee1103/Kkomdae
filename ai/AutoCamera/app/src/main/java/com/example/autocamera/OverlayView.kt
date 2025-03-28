package com.example.autocamera

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.camera.view.PreviewView

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

    fun setBoxes(newBoxes: List<BBox>, bitmapWidth: Int, bitmapHeight: Int) {
        boxes.clear()

        // previewView의 크기를 가져옴
        val viewWidth = width.toFloat()
        val viewHeight = height.toFloat()

        // bitmap 크기 → previewView 크기로 변환하는 scale 비율
        val scaleX = viewWidth / bitmapWidth
        val scaleY = viewHeight / bitmapHeight

        // 각 박스를 previewView 좌표계로 변환
        val scaledBoxes = newBoxes.map { box ->
            val rect = box.rect
            val scaledRect = RectF(
                rect.left * scaleX,
                rect.top * scaleY,
                rect.right * scaleX,
                rect.bottom * scaleY
            )
            BBox(scaledRect, box.label, box.confidence)
        }

        boxes.addAll(scaledBoxes)
        invalidate()
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
