/*
 * Copyright (C) 2024 Nick Richardson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.m03_bounce;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

/**
 * The Box class represents the boundaries of the game area.
 */
public class Box {

    int xMin, xMax, yMin, yMax;
    private Drawable backgroundDrawable;
    private Rect bounds;

    /**
     * Constructor for the Box class.
     *
     * @param context The context of the application
     * @param drawableId The ID of the drawable resource to use as the background
     */
    public Box(Context context, int drawableId) {
        try {
            backgroundDrawable = ContextCompat.getDrawable(context, drawableId);
        } catch (Exception e) {
            Log.e("Box", "Error loading background drawable." + drawableId, e);
        }
        bounds = new Rect();
    }

    /**
     * Sets the bounds of the box.
     *
     * @param x The x-coordinate of the top-left corner of the box
     * @param y The y-coordinate of the top-left corner of the box
     * @param width The width of the box
     * @param height The height of the box
     */
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

    /**
     * Draws the box on the canvas.
     *
     * @param canvas The canvas to draw the box on
     */
    public void draw(Canvas canvas) {
        if(backgroundDrawable != null) {

        backgroundDrawable.draw(canvas);
        }
    }
}
