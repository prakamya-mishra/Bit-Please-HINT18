package com.example.shashvatkedia.myapplication;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter filter = new IntentFilter(GyroscopeService.MOVEMENT);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        MovementBroadcast movementBroadcast = new MovementBroadcast();
        registerReceiver(movementBroadcast,filter);
        Intent intent = new Intent(getApplicationContext(),GyroscopeService.class);
        startService(intent);
    }
}
