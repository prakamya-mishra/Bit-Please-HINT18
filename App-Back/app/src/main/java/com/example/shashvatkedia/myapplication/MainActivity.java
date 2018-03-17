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
        initPickedUpState();
        executeTask();
    }

    private void initPickedUpState(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference parentRef = database.getReference();
        DatabaseReference sensorInfoRef = parentRef.child("sensorinfo");
        DatabaseReference pickedUpRef = sensorInfoRef.child("pickedUp");
        pickedUpRef.setValue(new Integer(0));
    }

    private void executeTask(){
        SensorTask task = new SensorTask(getApplicationContext());
        task.execute();
    }

    @Override
    protected void onResume(){
        super.onResume();
        executeTask();
    }
}
