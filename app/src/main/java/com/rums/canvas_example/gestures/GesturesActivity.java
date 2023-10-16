package com.rums.canvas_example.gestures;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.rums.canvas_example.R;

public class GesturesActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.advert);
        View view = new SandboxView(this, bitmap);

        setContentView(view);
    }
}