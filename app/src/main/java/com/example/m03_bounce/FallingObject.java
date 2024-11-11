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
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.util.Random;

/**
 * The FallingObject class represents an object that falls within the game.
 */
public class FallingObject {

    private static final double RADIUS = 50;
    private static final double INITIAL_SPEED_RESISTANCE = 0.50f;
    private static final double INITIAL_ACC_RESISTANCE = 0.50f;

    private double x, y;
    private double speedX, speedY;
    private double speedResistance = INITIAL_SPEED_RESISTANCE;
    private double accResistance = INITIAL_ACC_RESISTANCE;
    private RectF bounds;
    private int value;
    private Drawable drawable;
    private GameView gameview;

    private double ax, ay, az = 0; // acceleration from different axis

    Random r = new Random();

    /**
     * Constructor for the FallingObject class.
     *
     * @param context The context of the application
     * @param drawableId The ID of the drawable resource to use for the falling object
     * @param value The value of the object
     * @param gameview The GameView object that the object will be placed in
     */
    public FallingObject(Context context, int drawableId, int value, GameView gameview) {
        bounds = new RectF();
        try {
            drawable = ContextCompat.getDrawable(context, drawableId);
        } catch (Exception e) {
            Log.e("FallingObject", "Error loading drawable resource.", e);
        }
        this.value = value;
        this.gameview = gameview;

        x = RADIUS + r.nextInt(800);
        y = RADIUS;

        speedX = r.nextInt(10) - 5;
        speedY = r.nextInt(10) - 5;

        if (drawable == null) {
            Log.d("FallingObject", "Drawable is null.");
        }
        else {
            Log.d("FallingObject", "Successfully loaded drawable resource.");
        }
    }

    /**
     * Sets the acceleration of the object.
     *
     * @param ax The acceleration in the x-axis
     * @param ay The acceleration in the y-axis
     * @param az The acceleration in the z-axis
     */
    public void setAcc(double ax, double ay, double az){
        this.ax = ax;
        this.ay = ay;
        this.az = az;
    }

    public int getValue() {
        return value;
    }

    /**
     * Moves the object with collision detection.
     *
     * @param box The box representing the boundaries of the game area
     */
    public void moveWithCollisionDetection(Box box) {
        // Get new (x,y) position
        x += speedX;
        y += speedY;


        // Add acceleration to speed
        speedX = (speedX) * speedResistance + ax * accResistance;
        speedY = (speedY) * speedResistance + ay * accResistance;


        // Detect collision and react
        if (x + RADIUS > box.xMax) {
            speedX = -speedX;
            x = box.xMax - RADIUS;
        } else if (x - RADIUS < box.xMin) {
            speedX = -speedX;
            x = box.xMin + RADIUS;
        }
        if (y + RADIUS > box.yMax) {
            speedY = 0;
            y = box.yMax - RADIUS;
            gameview.onGameOver();
            Log.d("FallingObject", "Game over");
        } else if (y - RADIUS < box.yMin) {
            speedY = 0;
            y = box.yMin + RADIUS;
        }
    }

    /**
     * Draws the falling object on the canvas.
     *
     * @param canvas The canvas to draw the falling object on
     */
    public void draw(Canvas canvas) {
        if (drawable != null) {
            bounds.set((float) (x- RADIUS),
                    (float) (y- RADIUS),
                    (float) (x+ RADIUS),
                    (float) (y+ RADIUS));
            drawable.setBounds((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom);
            Log.d("FallingObject", "Drawing object at x=" + x + ", y=" + y);
            drawable.draw(canvas);
        }
    }

    /**
     * Resets the position of the falling object.
     */
    public void resetPosition() {
        x = RADIUS + r.nextInt(800);
        y = RADIUS;
        speedX = r.nextInt(10) - 5;
        speedY = r.nextInt(10) - 5;
    }

    /**
     * Resets the speed of the falling object.
     */
    public void resetSpeed() {
        speedResistance = INITIAL_SPEED_RESISTANCE;
        accResistance = INITIAL_ACC_RESISTANCE;
    }

    /**
     * Increases the speed of the falling object.
     */
    public void increaseSpeed() {
        speedResistance += 0.02f;
        accResistance += 0.02f;
    }


    public void setSpeedResistance(double speedResistance) {
        this.speedResistance = speedResistance;
    }


    public void setAccResistance(double accResistance) {
        this.accResistance = accResistance;
    }


    public double getSpeedResistance(){
        return speedResistance;
    }

    public double getAccResistance(){
        return accResistance;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }
}
