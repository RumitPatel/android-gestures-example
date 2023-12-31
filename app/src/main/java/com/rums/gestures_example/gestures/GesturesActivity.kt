package com.rums.gestures_example.gestures

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import com.rums.gestures_example.R

class GesturesActivity : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.advert)
        val view: View = SandboxView(this, bitmap)
        setContentView(view)
    }
}