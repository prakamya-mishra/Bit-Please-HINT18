package com.example.shashvatkedia.myapplication;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getName();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference sensorDataRef;
    private DatabaseReference parentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SensorTask task = new SensorTask(getApplicationContext());
        task.execute();
        /*IntentFilter filter = new IntentFilter(GyroscopeService.MOVEMENT);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        MovementBroadcast movementBroadcast = new MovementBroadcast();
        registerReceiver(movementBroadcast,filter);
        Intent intent = new Intent(getApplicationContext(),GyroscopeService.class);
        Log.e(TAG,"Starting service");
        startService(intent);*/
    }

    private void writeValuesToFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        parentRef = firebaseDatabase.getReference();
        sensorDataRef = parentRef.child("sensorinfo");
      //  DatabaseReference distanceRef = sensorDataRef.child("distance");
        DatabaseReference gyroXRef = sensorDataRef.child("gyroX");
        DatabaseReference gyroYRef = sensorDataRef.child("gyroY");
        DatabaseReference gyroZRef = sensorDataRef.child("gyroZ");
       // final DatabaseReference distanceLeft = sensorDataRef.child("distanceLeft");
       // final DatabaseReference distanceRight = sensorDataRef.child("distanceRight");
        /* distanceLeft.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Float>> genericTypeIndicator = new GenericTypeIndicator<List<Float>>(){};
                List<Float> distanceLeftList = (List<Float>) dataSnapshot.getValue(genericTypeIndicator);
                if(distanceLeftList == null){
                    Log.d(TAG,"Data error");
                }
                else{
                    if(distanceLeftList.size() == 10){
                        distanceLeftList.remove(0);
                        distanceLeftList.add(new Float(1.0));
                    }
                    else{
                        distanceLeftList.add(new Float(1.0));
                    }
                    distanceLeft.setValue(distanceLeftList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,databaseError.getCode() + databaseError.getMessage());
            }
        });
        distanceRight.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Float>> genericTypeIndicator = new GenericTypeIndicator<List<Float>>(){};
                List<Float> distanceRightList = (List<Float>) dataSnapshot.getValue(genericTypeIndicator);
                if(distanceRightList == null){
                    Log.d(TAG,"Data error");
                }
                else{
                    if(distanceRightList.size() == 10){
                        distanceRightList.remove(0);
                        distanceRightList.add(new Float(1.0));
                    }
                    else{
                        distanceRightList.add(new Float(1.0));
                    }
                    distanceRight.setValue(distanceRightList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,databaseError.getCode() + databaseError.getMessage());
            }
        });*/
       // distanceRef.setValue(new Float(1.0));
        gyroXRef.setValue(new Float(1.0));
        gyroYRef.setValue(new Float(1.0));
        gyroZRef.setValue(new Float(1.0));
    }
}
