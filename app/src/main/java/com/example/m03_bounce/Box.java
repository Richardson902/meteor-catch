package com.example.m03_bounce;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

/**
 * Created by Russ on 08/04/2014.
 */
public class Box {

    int xMin, xMax, yMin, yMax;
    private Drawable backgroundDrawable;
    private Rect bounds;

    public Box(Context context, int drawableId) {
        backgroundDrawable = ContextCompat.getDrawable(context, drawableId);
        bounds = new Rect();
    }

    public void set(int x, int y, int width, int height) {
        xMin = x;
        xMax = x + width - 1;
        yMin = y;
        yMax = y + height - 1;
        // The box's bounds do not change unless the view's size changes
        bounds.set(xMin, yMin, xMax, yMax);
        if (backgroundDrawable != null) {
            backgroundDrawable.setBounds(bounds);
        }
    }

    public void draw(Canvas canvas) {
        if(backgroundDrawable != null) {

        backgroundDrawable.draw(canvas);
        }
    }
}
