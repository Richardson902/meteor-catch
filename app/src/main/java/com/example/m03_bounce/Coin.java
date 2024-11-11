package com.example.m03_bounce;

import android.content.Context;

public class Coin extends FallingObject{

    private static final int drawableId = R.drawable.ic_coin;
    private static final int value = 5;

    public Coin(Context context, GameView gameView) {
        super(context, drawableId, value, gameView);
    }
}
