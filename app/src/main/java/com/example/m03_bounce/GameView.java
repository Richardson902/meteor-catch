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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

/**
 * The GameView class represents the main view of the game, handling the game loop and rendering.
 */
public class GameView extends View implements SensorEventListener {

    private FallingObject fallingObject;
    private Box box;
    private boolean isGameStarted = false;
    private int score;
    private int record;
    private boolean isGamePaused = false;
    private Basket basket;
    private TextView scoreView;
    private TextView recordView;

    private HandlerThread gameThread;
    private Handler gameHandler;

    double ax = 0;   // Store here for logging to screen
    double ay = 0;   //
    double az = 0;   //

    /**
     * Constructor for the GameView class.
     *
     * @param context The context of the application
     * @param attrs The attribute set
     */
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        box = new Box(context, R.drawable.ic_background);

        Log.d("GameView", "Creating basket object");
        basket = new Basket(context, box);

        Log.d("GameView", "Creating falling object");
        fallingObject = createRandomFallingObject();

        try {
            gameThread = new HandlerThread("GameThread");
            gameThread.start();
            gameHandler = new Handler(gameThread.getLooper());
        } catch (Exception e) {
            Log.e("GameView", "Error creating game thread.", e);
        }

        this.setFocusable(true);
        this.requestFocus();
        this.setFocusableInTouchMode(true);
    }

    /**
     * Creates a random falling object (either a Coin or a Meteor).
     *
     * @return A new FallingObject instance
     */
    private FallingObject createRandomFallingObject() {
        Random r = new Random();
        int chance = r.nextInt(10) + 1;

        Log.d("GameView", "chance = " + chance);

        if (chance <= 3) {
            return new Coin(getContext(), this);
        } else {
            return new Meteor(getContext(), this);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        box.draw(canvas);
        basket.draw(canvas);

        if (isGameStarted) {
            fallingObject.draw(canvas);
        } else {
            Log.d("GameView", "Game not started");
        }
    }

    /**
     * Starts a new game.
     */
    public void onNewGame() {
        Log.d("GameView  BUTTON", "User tapped the button...GAMEVIEW");

        score = 0;
        Log.d("GameView", "Score = " + score);
        scoreView.setText("Score: " + score);
        recordView.setText("");

        if (!isGameStarted) {
            setGameStarted(true);
            fallingObject.resetSpeed();
            fallingObject.resetPosition();
            try {
                gameHandler.post(gameRunnable);
            } catch (Exception e) {
                Log.e("GameView", "Error starting game.", e);
            }
        }
    }

    /**
     * Sets the game started state.
     *
     * @param isGameStarted The game started state
     */
    public void setGameStarted(boolean isGameStarted) {
        this.isGameStarted = isGameStarted;
        Log.d("GameView", "Game started = " + isGameStarted);
    }

    /**
     * Handles the game over state.
     */
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

    /**
     * Sets the TextViews for displaying the score and record.
     *
     * @param stextView The TextView for the score
     * @param rTextView The TextView for the record
     */
    public void setTextViews(TextView stextView, TextView rTextView){
        scoreView = stextView;
        recordView = rTextView;
    }

    /**
     * Updates the record if the current score is higher.
     */
    public void updateRecord(){
        if (score > record) {
            record = score;
        }
    }

    /**
     * Releases the media player.
     */
    public void releaseMediaPlayer() {
        if (basket != null) {
            basket.release();
        }
    }

    /**
     * Initializes the media player.
     */
    public void initializeMediaPlayer() {
        if (basket != null) {
            basket.initializeMediaPlayer(getContext());
        }
    }

    public void pauseGame() {
        isGamePaused = true;
    }

    public boolean getIsGamePaused() {
        return isGamePaused; // Ugly, but needed for testing
    }

    public void resumeGame() {
        isGamePaused = false;
        gameHandler.post(gameRunnable);
    }

    /**
     * The game loop runnable.
     */
    private final Runnable gameRunnable = new Runnable() {
        @Override
        public void run() {
            if (isGameStarted && !isGamePaused) {
                fallingObject.moveWithCollisionDetection(box); // Move the falling object if the game is started
                if (basket.collectedObject(fallingObject)) {
                    score += fallingObject.getValue(); // Increase the score if the falling object is collected
                    scoreView.post(() -> scoreView.setText("Score: " + score)); // Update the score view
                    updateRecord();

                    double currentSpeedResistance = fallingObject.getSpeedResistance(); // Get the current speed resistance and acceleration resistance
                    double currentAccResistance = fallingObject.getAccResistance();

                    fallingObject = createRandomFallingObject(); // Create a new falling object

                    fallingObject.setSpeedResistance(currentSpeedResistance); // Set the speed resistance and acceleration resistance
                    fallingObject.setAccResistance(currentAccResistance);

                    fallingObject.increaseSpeed(); // Increase the speed of the falling object for the next iteration
                    Log.d("GameView", "speed = " + fallingObject.getAccResistance() + " " + fallingObject.getAccResistance());
                }
                postInvalidate();
                gameHandler.postDelayed(this, 16); // approx 60fps
            }
        }
    };


    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        // Set the movement bounds for the basket
        box.set(0, 0, w, h);
        Log.d("GameView", "onSizeChanged w=" + w + " h=" + h);

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
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("onAccuracyChanged", "event=" + sensor.toString());
    }
}
