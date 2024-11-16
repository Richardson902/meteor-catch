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

/**
 * The Coin class represents a coin falling object in the game.
 */
public class Coin extends FallingObject{

    private static final int drawableId = R.drawable.ic_coin;
    private static final int value = 5;

    /**
     * Constructor for the Coin class.
     *
     * @param context The context of the application
     * @param gameView The GameView object that the coin will be placed in
     */
    public Coin(Context context, GameView gameView) {
        super(context, drawableId, value, gameView);
    }
}
