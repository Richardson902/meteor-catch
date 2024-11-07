package com.example.m03_bounce;

import android.content.Context;

public class Meteor extends FallingObject{

    private static final int drawableId = R.drawable.meteor;
    private static final int value = 1;

    public Meteor(Context context, GameView gameView) {
        super(context, drawableId, value, gameView);
    }
}
