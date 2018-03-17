package com.example.shashvatkedia.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by shashvatkedia on 17/03/18.
 */

public class MovementBroadcast extends BroadcastReceiver {

    private final String TAG = MovementBroadcast.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"Gyroscope reading changed");
    }
}
