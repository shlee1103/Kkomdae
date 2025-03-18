package com.pizza.kkomdae.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CameraOverlayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val overlayPaint = Paint().apply {
        color = Color.parseColor("#80000000")  // 검은색 50% 투명 (#80 = 50% 투명도)
        style = Paint.Style.FILL
    }

    private val borderPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 5f
        isAntiAlias = true
    }

    private val rect = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val isLandscape = width > height  // ✅ 가로 모드인지 체크

        val overlayMargin = if (isLandscape) width * 0.2f else height * 0.1f  // ✅ 네모 틀 마진 조정

        // 16:9 비율로 크기 설정
        val rectWidth: Float
        val rectHeight: Float

        if (isLandscape) {
            rectWidth = width - overlayMargin * 2
            rectHeight = rectWidth / 356.6f * 229.1f   // 16:9 비율 유지
        } else {
            rectWidth = width - overlayMargin * 2
            rectHeight = rectWidth /  356.6f * 229.1f // 16:9 비율 유지
        }

        val left = (width - rectWidth) / 2
        val top = (height - rectHeight) / 2
        val right = left + rectWidth
        val bottom = top + rectHeight

        rect.set(left, top, right, bottom)

        // 바깥 영역을 반투명하게 채우기
        canvas.drawRect(0f, 0f, width, top, overlayPaint)
        canvas.drawRect(0f, bottom, width, height, overlayPaint)
        canvas.drawRect(0f, top, left, bottom, overlayPaint)
        canvas.drawRect(right, top, width, bottom, overlayPaint)

        // 네모 틀 그리기 (둥근 모서리 적용)
        canvas.drawRoundRect(rect, 20f, 20f, borderPaint)
    }
}
