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
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.core.content.ContextCompat;

/**
 * The Basket class represents a basket in the game that can collect falling objects.
 */
public class Basket {

    double x, y;
    private RectF bounds;
    private Drawable drawable;
    private MediaPlayer mediaPlayer;


    /**
     * Constructor for the Basket class.
     *
     * @param context The context of the application
     * @param box The box object that the basket will be placed in
     */
    public Basket(Context context, Box box) {
        bounds = new RectF();

        try {
            drawable = ContextCompat.getDrawable(context, R.drawable.ic_basket);
            initializeMediaPlayer(context);
        } catch (Exception e) {
            Log.e("Basket", "Error loading resources.", e);
        }

        x = (box.xMin + box.xMax) / 2;
        y = box.yMax - 150;

    }

    /**
     * Draws the basket on the canvas.
     *
     * @param canvas The canvas to draw the basket on
     */
    public void draw(Canvas canvas) {
        if (drawable != null) {
            Log.d("Basket", "Resource loaded successfully.");
            bounds.set((float) (x - 150), (float) (y - 150), (float) (x + 150), (float) (y + 150));

            drawable.setBounds ((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom);
            drawable.draw(canvas);
            Log.d("Basket", "Drawable drawn successfully at x=" + x + ", y=" + y);
        } else {
            Log.e("Basket", "Drawable is null.");
        }
    }

    /**
     * Checks if a falling object has been collected by the basket.
     *
     * @param fallingObject The falling object to check
     * @return true if the falling object has been collected, false otherwise
     */
    public boolean collectedObject(FallingObject fallingObject) {
        double objectX = fallingObject.getX();
        double objectY = fallingObject.getY();

        if (objectX > x - 150 && objectX < x + 150 && objectY > y - 150 && objectY < y + 150) {

            try {
                if (mediaPlayer!= null) {
                    mediaPlayer.start();
                }
            } catch (Exception e) {
                Log.e("Basket", "Error playing sound.", e);
            }

            return true;
        }
        return false;
    }

    /**
     * Releases the media resource used by the basket.
     */
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * Initializes the media player used by the basket.
     *
     * @param context The context of the application
     */
    public void initializeMediaPlayer(Context context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.collect_sound);
        }
    }

}
