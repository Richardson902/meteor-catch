package com.example.m03_bounce;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.util.Random;

public class FallingObject {

    double radius = 50;
    double x, y;
    double speedX, speedY;
    double speed_resistance = 0.50f;
    double acc_resistance = 0.50f;
    private RectF bounds;
    private int value;
    private Drawable drawable;
    private GameView gameview;

    private double ax, ay, az = 0; // acceration from different axis

    public void setAcc(double ax, double ay, double az){
        this.ax = ax;
        this.ay = ay;
        this.az = az;
    }

    Random r = new Random();

    public FallingObject(Context context, int drawableId, int value, GameView gameview) {
        bounds = new RectF();
        drawable = ContextCompat.getDrawable(context, drawableId);
        this.value = value;
        this.gameview = gameview;

        x = radius + r.nextInt(800);
        y = radius;

        speedX = r.nextInt(10) - 5;
        speedY = r.nextInt(10) - 5;

        if (drawable == null) {
            Log.e("FallingObject", "Error loading drawable resource.");
        }
        else {
            Log.d("FallingObject", "Successfully loaded drawable resource.");
        }
    }

    public int getValue() {
        return value;
    }

    public void moveWithCollisionDetection(Box box) {
        // Get new (x,y) position
        x += speedX;
        y += speedY;


        // Add acceleration to speed
        speedX = (speedX) * speed_resistance + ax * acc_resistance;
        speedY = (speedY) * speed_resistance + ay * acc_resistance;


        // Detect collision and react
        if (x + radius > box.xMax) {
            speedX = -speedX;
            x = box.xMax - radius;
        } else if (x - radius < box.xMin) {
            speedX = -speedX;
            x = box.xMin + radius;
        }
        if (y + radius > box.yMax) {
            speedY = 0;
            y = box.yMax - radius;
            gameview.onGameOver();
            Log.d("FallingObject", "Game over");
        } else if (y - radius < box.yMin) {
            speedY = 0;
            y = box.yMin + radius;
        }
    }

    public void draw(Canvas canvas) {
        if (drawable != null) {
            bounds.set((float) (x-radius),
                    (float) (y-radius),
                    (float) (x+radius),
                    (float) (y+radius));
            drawable.setBounds((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom);
            Log.d("FallingObject", "Drawing object at x=" + x + ", y=" + y);
            drawable.draw(canvas);
        }
    }

    public void resetPosition() {
        x = radius + r.nextInt(800);
        y = radius;
        speedX = r.nextInt(10) - 5;
        speedY = r.nextInt(10) - 5;
    }

    public void resetSpeed() {
        speed_resistance = 0.50f;
        acc_resistance = 0.50f;
    }
    public void increaseSpeed() {
        speed_resistance += 0.02f;
        acc_resistance += 0.02f;
    }

    public void setSpeedResistance(double speedResistance) {
        this.speed_resistance = speedResistance;
    }

    public void setAccResistance(double accResistance) {
        this.acc_resistance = accResistance;
    }

    public double getSpeedResistance(){
        return speed_resistance;
    }

    public double getAccResistance(){
        return acc_resistance;
    }
}
