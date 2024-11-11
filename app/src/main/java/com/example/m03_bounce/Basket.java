package com.example.m03_bounce;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.core.content.ContextCompat;

public class Basket {

    double x, y;
    private RectF bounds;
    private Drawable drawable;
    private MediaPlayer mediaPlayer;

    public Basket(Context context, Box box) {
        bounds = new RectF();
        drawable = ContextCompat.getDrawable(context, R.drawable.ic_basket);
        mediaPlayer = MediaPlayer.create(context, R.raw.collect_sound);

        x = (box.xMin + box.xMax) / 2;
        y = box.yMax - 150;

    }

    public void draw(Canvas canvas) {
        if (drawable != null) {
            Log.v("Basket", "Resource loaded successfully.");
            bounds.set((float) (x - 150), (float) (y - 150), (float) (x + 150), (float) (y + 150));

            drawable.setBounds ((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom);
            drawable.draw(canvas);
            Log.d("Basket", "Drawable drawn successfully at x=" + x + ", y=" + y);
        } else {
            Log.e("Basket", "Error loading drawable resource.");
        }
    }

    public boolean collectedObject(FallingObject fallingObject) {
        if (fallingObject.x > x - 150 && fallingObject.x < x + 150 && fallingObject.y > y - 150 && fallingObject.y < y + 150) {
            mediaPlayer.start();
            return true;
        }
        return false;
    }

}
