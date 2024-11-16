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

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * The MainActivity class is the entry point of the application, handling the main game setup and sensor management.
 */
public class MainActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the view object so we can reference it later
        gameView = (GameView) findViewById(R.id.custView);

        initializeTextViews();

        //Check sensors
        setupSensors();
    }

    private void initializeTextViews() {
        TextView scoreView = findViewById(R.id.scoreView);
        TextView recordView = findViewById(R.id.recordView);
        gameView.setTextViews(scoreView, recordView);
    }


    // Sensors
    private SensorManager mSensorManager;
    private Sensor mySensor;


    /**
     * Sets up the sensors for the game.
     */
    private void setupSensors() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Use the accelerometer.
        if (mySensor != null){
            Log.d("SENSORS", "Accelerometer found." );
        }
        else{
            Log.w("SENSORS", "No accelerometer found. You can't play this game." );
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (gameView !=null) {
            if (mySensor !=null) {
                mSensorManager.registerListener((SensorEventListener) gameView, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
            gameView.initializeMediaPlayer(); // initialize the media player on resume
            gameView.resumeGame(); // change flag to resume the game
            Log.d( "MainActivity", "Game paused = " + gameView.getIsGamePaused());
        } else {
            Log.d("SENSORS", "onResume gameView is null" );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener((SensorEventListener) gameView);
        if (gameView !=null) {
            gameView.pauseGame(); // change flag to pause the game
            gameView.releaseMediaPlayer(); // release the media player on pause to free resources
            Log.d( "MainActivity", "Game paused = " + gameView.getIsGamePaused());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameView !=null) {
            gameView.releaseMediaPlayer(); // release the media player on destroy to free resources
        }
    }


    /**
     * Handles the new game button click event.
     *
     * @param v The view that was clicked
     */
    public void onNewGameClicked(View v) {

        Log.d("MainActivity  BUTTON", "User tapped the button ... MAIN");

        // let the view do something
        gameView.onNewGame();

        v.setVisibility(View.GONE);
    }

    /**
     * Shows the new game button.
     */
    public void showNewGameButton() {
        findViewById(R.id.buttonNewGame).setVisibility(View.VISIBLE);
    }

}