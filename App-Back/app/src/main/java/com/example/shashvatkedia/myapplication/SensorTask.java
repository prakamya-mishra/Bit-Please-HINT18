package com.example.shashvatkedia.myapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

/**
 * Created by shashvatkedia on 17/03/18.
 */

public class SensorTask extends AsyncTask<Void, Void, Void> implements SensorEventListener {

    private final String TAG = SensorTask.class.getName();
    private Context con;
    private int flag = 0,count = 0;
    private float X,Y,Z;

    private FirebaseDatabase database;
    private DatabaseReference parentRef;


    public SensorTask(Context context){
        con = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
            monitorGyroscope();
            database = FirebaseDatabase.getInstance();
        parentRef = database.getReference();
            DatabaseReference sensorInfoRef = parentRef.child("sensorinfo");
            DatabaseReference pickedUpRef = sensorInfoRef.child("pickedUp");
            pickedUpRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Integer pickedUp = dataSnapshot.getValue(Integer.class);
                    flag = pickedUp;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            while(true){
                if(flag == 1){
                    SensorManager manager = (SensorManager) con.getSystemService(Context.SENSOR_SERVICE);
                    manager.unregisterListener(this);
                    if(count == 0){
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference parentRef = database.getReference();
                        DatabaseReference sensorInforef = parentRef.child("sensorinfo");
                        DatabaseReference pickedUpFlag = sensorInforef.child("pickedUp");
                        pickedUpFlag.setValue(new Integer(1));
                        count = 1;
                    }
                    writeToFirebase();
                    break;
                }
                else{
                    writeToFirebase();
                }
            }
        return null;
    }


    private void monitorGyroscope(){
            Log.e(TAG, "starting function");
            SensorManager sensorManager = (SensorManager) con.getSystemService(Context.SENSOR_SERVICE);
            Sensor acce = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, acce, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER){
            X = event.values[0];
            Y = event.values[1];
            Z = event.values[2];
            Log.e(TAG,event.values[2] + "");
            float z = event.values[2];
            if(z > 11){
                flag = 1;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onPostExecute(Void result){
    }

    private void writeToFirebase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference parentRef = database.getReference();
        DatabaseReference sensorInfoRef = parentRef.child("sensorinfo");
        DatabaseReference gyroRefZ = sensorInfoRef.child("gyroZ");
        DatabaseReference gyroRefY = sensorInfoRef.child("gyroY");
        DatabaseReference gyroRefX = sensorInfoRef.child("gyroX");
        DecimalFormat twoDecimalFormat = new DecimalFormat("#.##");
        Log.e(TAG,Float.valueOf(twoDecimalFormat.format(X))+"");
        gyroRefX.setValue(Float.valueOf(twoDecimalFormat.format(X)));
        gyroRefY.setValue(Float.valueOf(twoDecimalFormat.format(Y)));
        gyroRefZ.setValue(Float.valueOf(twoDecimalFormat.format(Z)));
    }
}
