package com.example.shashvatkedia.myapplication;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by shashvatkedia on 17/03/18.
 */

public class GyroscopeService extends IntentService implements SensorEventListener {

    private int flag = 0;
    public static final String MOVEMENT = "com.example.shashvatkedia.myapplication";

    public GyroscopeService(String name) {
        super(name);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        monitorGyroscope();
        if(flag == 1){
            Intent movementIntent = new Intent(MOVEMENT);
            sendBroadcast(movementIntent);
        }
        else{
            onHandleIntent(intent);
        }
    }

    private void monitorGyroscope(){
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor acce = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,acce,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float z = event.values[2];
            if(z > 9.8){
                flag = 1;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
