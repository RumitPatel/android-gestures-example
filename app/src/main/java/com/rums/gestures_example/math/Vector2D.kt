package com.rums.gestures_example.math

class Vector2D {
    var x = 0f
        private set
    var y = 0f
        private set

    constructor()
    constructor(v: Vector2D) {
        x = v.x
        y = v.y
    }

    constructor(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    val length: Float
        get() = Math.sqrt((x * x + y * y).toDouble()).toFloat()

    fun set(other: Vector2D): Vector2D {
        x = other.x
        y = other.y
        return this
    }

    operator fun set(x: Float, y: Float): Vector2D {
        this.x = x
        this.y = y
        return this
    }

    fun add(value: Vector2D): Vector2D {
        x += value.x
        y += value.y
        return this
    }

    override fun toString(): String {
        return String.format("(%.4f, %.4f)", x, y)
    }

    companion object {
        @JvmStatic
        fun subtract(lhs: Vector2D, rhs: Vector2D): Vector2D {
            return Vector2D(lhs.x - rhs.x, lhs.y - rhs.y)
        }

        fun getDistance(lhs: Vector2D, rhs: Vector2D): Float {
            val delta = subtract(lhs, rhs)
            return delta.length
        }

        @JvmStatic
        fun getSignedAngleBetween(a: Vector2D, b: Vector2D): Float {
            val na = getNormalized(a)
            val nb = getNormalized(b)
            return (Math.atan2(nb.y.toDouble(), nb.x.toDouble()) - Math.atan2(
                na.y.toDouble(),
                na.x.toDouble()
            )).toFloat()
        }

        fun getNormalized(v: Vector2D): Vector2D {
            val l = v.length
            return if (l == 0f) Vector2D() else Vector2D(v.x / l, v.y / l)
        }
    }
}