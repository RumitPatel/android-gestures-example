package com.rums.gestures_example.gestures

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.rums.gestures_example.R
import com.rums.gestures_example.math.Vector2D
import com.rums.gestures_example.math.Vector2D.Companion.getSignedAngleBetween

class SandboxView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
//    private val bitmap: Bitmap? = null,
) : View(context, attrs, defStyleAttr), OnTouchListener {
    val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ruler_2)

    private val width: Int = bitmap?.width ?: 0
    private val height: Int = bitmap?.height ?: 0
    private val transform = Matrix()
    private val position = Vector2D()
    private var scale = 1f
    private var angle = 0f
    private val touchManager = TouchManager(2)
    private var isInitialized = false

    // Debug helpers to draw lines between the two touch points
    private var vca: Vector2D? = null
    private var vcb: Vector2D? = null
    private var vpa: Vector2D? = null
    private var vpb: Vector2D? = null

    private var paint: Paint

    init {
        setOnTouchListener(this)

        paint = Paint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!isInitialized) {
            val w = getWidth()
            val h = getHeight()
            position[(w / 2).toFloat()] = (h / 2).toFloat()
            isInitialized = true
        }

        transform.reset()
        transform.postTranslate(-width / 2.0f, -height / 2.0f)
        transform.postRotate(getDegreesFromRadians(angle))
        transform.postScale(scale, scale)
        transform.postTranslate(position.x, position.y)
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, transform, paint)
        }
        try {
            paint.color = -0xff8100
            canvas.drawCircle(vca!!.x, vca!!.y, 64f, paint)
            paint.color = -0x810000
            canvas.drawCircle(vcb!!.x, vcb!!.y, 64f, paint)
            paint.color = -0x10000
            canvas.drawLine(vpa!!.x, vpa!!.y, vpb!!.x, vpb!!.y, paint)
            paint.color = -0xff0100
            canvas.drawLine(vca!!.x, vca!!.y, vcb!!.x, vcb!!.y, paint)
        } catch (e: NullPointerException) {
            // Just being lazy here...
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        vca = null
        vcb = null
        vpa = null
        vpb = null
        try {
            touchManager.update(event)
            if (touchManager.pressCount == 1) {
                vca = touchManager.getPoint(0)
                vpa = touchManager.getPreviousPoint(0)
                position.add(touchManager.moveDelta(0))
            } else {
                if (touchManager.pressCount == 2) {
                    vca = touchManager.getPoint(0)
                    vpa = touchManager.getPreviousPoint(0)
                    vcb = touchManager.getPoint(1)
                    vpb = touchManager.getPreviousPoint(1)
                    val current = touchManager.getVector(0, 1)
                    val previous = touchManager.getPreviousVector(0, 1)
                    val currentDistance = current.length
                    val previousDistance = previous.length
                    if (previousDistance != 0f) {
                        scale *= currentDistance / previousDistance
                    }
                    angle -= getSignedAngleBetween(current, previous)
                }
            }
            invalidate()
        } catch (t: Throwable) {
            // So lazy...
        }
        return true
    }

    companion object {
        private fun getDegreesFromRadians(angle: Float): Float {
            return (angle * 180.0 / Math.PI).toFloat()
        }
    }
}