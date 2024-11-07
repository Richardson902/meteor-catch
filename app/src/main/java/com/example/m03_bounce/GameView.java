package com.example.m03_bounce;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Russ on 08/04/2014.
 */
public class GameView extends View implements SensorEventListener {

    private FallingObject fallingObject;
    private Box box;
    private boolean isGameStarted = false;
    private int score;
    private int record;
    private Basket basket;
    private TextView scoreView;
    private TextView recordView;

    private HandlerThread gameThread;
    private Handler gameHandler;

    double ax = 0;   // Store here for logging to screen
    double ay = 0;   //
    double az = 0;   //

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        box = new Box(context, R.drawable.ic_background);

        Log.d("GameView", "Creating basket object");
        basket = new Basket(context, box);

        Log.d("GameView", "Creating falling object");
        fallingObject = createRandomFallingObject();

        gameThread = new HandlerThread("GameThread");
        gameThread.start();
        gameHandler = new Handler(gameThread.getLooper());

        this.setFocusable(true);
        this.requestFocus();
        this.setFocusableInTouchMode(true);
    }

    private FallingObject createRandomFallingObject() {
        Random r = new Random();
        int chance = r.nextInt(10) + 1;

        Log.w("GameView", "chance = " + chance);

        if (chance <= 3) {
            return new Coin(getContext(), this);
        } else {
            return new Meteor(getContext(), this);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Log.v("GameView", "onDraw");
        box.draw(canvas);
        basket.draw(canvas);

        if (isGameStarted) {
            fallingObject.draw(canvas);
        } else {
            Log.d("GameView", "Game not started");
        }
    }

    public void onNewGame() {
        Log.d("GameView  BUTTON", "User tapped the button...GAMEVIEW");

        score = 0;
        Log.w("GameView", "Score = " + score);
        scoreView.setText("Score: " + score);
        recordView.setText("");

        if (!isGameStarted) {
            isGameStarted = true;
            fallingObject.resetSpeed();
            fallingObject.resetPosition();
            Log.w("GameView", "Game started = " + isGameStarted);
            gameHandler.post(gameRunnable);
        }
    }

    public void setGameStarted(boolean isGameStarted) {
        this.isGameStarted = isGameStarted;
    }

    public void onGameOver() {
        post(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)getContext()).showNewGameButton();
                Log.d("GameView", "Game over");
                setGameStarted(false);
                recordView.setText("Record: " + record);
            }
        });
    }

    public void setTextViews(TextView stextView, TextView rTextView){
        scoreView = stextView;
        recordView = rTextView;
    }

    public void updateRecord(){
        if (score > record) {
            record = score;
        }
    }


    private Runnable gameRunnable = new Runnable() {
        @Override
        public void run() {
            if (isGameStarted) {
                fallingObject.moveWithCollisionDetection(box);
                if (basket.collectedObject(fallingObject)) {
                    score += fallingObject.getValue();
                    scoreView.post(() -> scoreView.setText("Score: " + score));
                    updateRecord();

                    double currentSpeedResistance = fallingObject.speed_resistance;
                    double currentAccResistance = fallingObject.acc_resistance;

                    fallingObject = createRandomFallingObject();

                    fallingObject.setSpeedResistance(currentSpeedResistance);
                    fallingObject.setAccResistance(currentAccResistance);

                    fallingObject.increaseSpeed();
                    Log.w("GameView", "speed = " + fallingObject.acc_resistance + " " + fallingObject.speed_resistance);
                }
                postInvalidate();
                gameHandler.postDelayed(this, 16); // approx 60fps
            }
        }
    };


    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        // Set the movement bounds for the ball
        box.set(0, 0, w, h);
        Log.w("BouncingBallLog", "onSizeChanged w=" + w + " h=" + h);

        basket = new Basket(getContext(), box);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // Lots of sensor types...get which one, unpack accordingly
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            ax = -event.values[0];  // turns out x is backwards...on my screen?
            ay = event.values[1];   // y component of Accelerometer
            az = event.values[2];   // z component of Accelerometer

            fallingObject.setAcc(ax, ay, az);

            Log.v("onSensorChanged", "ax=" + ax + " ay=" + ay + " az=" + az);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.v("onAccuracyChanged", "event=" + sensor.toString());
    }
    public void callMe() {
        Log.v("xxxxx", "yyyyy");
//        this.callMe();
    }
}
