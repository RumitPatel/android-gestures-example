package com.rums.gestures_example.gestures

import android.view.MotionEvent
import com.rums.gestures_example.math.Vector2D
import com.rums.gestures_example.math.Vector2D.Companion.subtract

class TouchManager(private val maxNumberOfTouchPoints: Int) {
    private val points: Array<Vector2D?>
    private val previousPoints: Array<Vector2D?>

    init {
        points = arrayOfNulls(maxNumberOfTouchPoints)
        previousPoints = arrayOfNulls(maxNumberOfTouchPoints)
    }

    fun isPressed(index: Int): Boolean {
        return points[index] != null
    }

    val pressCount: Int
        get() {
            var count = 0
            for (point in points) {
                if (point != null) ++count
            }
            return count
        }

    fun moveDelta(index: Int): Vector2D {
        return if (isPressed(index)) {
            val previous =
                if (previousPoints[index] != null) previousPoints[index] else points[index]
            subtract(points[index]!!, previous!!)
        } else {
            Vector2D()
        }
    }

    fun getPoint(index: Int): Vector2D? {
        return if (points[index] != null) points[index] else Vector2D()
    }

    fun getPreviousPoint(index: Int): Vector2D? {
        return if (previousPoints[index] != null) previousPoints[index] else Vector2D()
    }

    fun getVector(indexA: Int, indexB: Int): Vector2D {
        return getVector(points[indexA], points[indexB])
    }

    fun getPreviousVector(indexA: Int, indexB: Int): Vector2D {
        return if (previousPoints[indexA] == null || previousPoints[indexB] == null) getVector(
            points[indexA],
            points[indexB]
        ) else getVector(previousPoints[indexA], previousPoints[indexB])
    }

    fun update(event: MotionEvent) {
        val actionCode = event.action and MotionEvent.ACTION_MASK
        if (actionCode == MotionEvent.ACTION_POINTER_UP || actionCode == MotionEvent.ACTION_UP) {
            val index = event.action shr MotionEvent.ACTION_POINTER_ID_SHIFT
            points[index] = null
            previousPoints[index] = points[index]
        } else {
            for (i in 0 until maxNumberOfTouchPoints) {
                if (i < event.pointerCount) {
                    val index = event.getPointerId(i)
                    val newPoint = Vector2D(event.getX(i), event.getY(i))
                    if (points[index] == null) points[index] = newPoint else {
                        if (previousPoints[index] != null) {
                            previousPoints[index]!!.set(points[index]!!)
                        } else {
                            previousPoints[index] = Vector2D(newPoint)
                        }
                        if (subtract(points[index]!!, newPoint).length < 64) points[index]!!
                            .set(newPoint)
                    }
                } else {
                    points[i] = null
                    previousPoints[i] = points[i]
                }
            }
        }
    }

    companion object {
        private fun getVector(a: Vector2D?, b: Vector2D?): Vector2D {
            if (a == null || b == null) throw RuntimeException("can't do this on nulls")
            return subtract(b, a)
        }
    }
}