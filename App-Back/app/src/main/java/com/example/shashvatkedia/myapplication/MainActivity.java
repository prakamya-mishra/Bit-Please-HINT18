package com.example.shashvatkedia.myapplication;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aitorvs.android.fingerlock.FingerLock;
import com.aitorvs.android.fingerlock.FingerLockManager;
import com.aitorvs.android.fingerlock.FingerLockResultCallback;
import com.aitorvs.android.fingerlock.FingerprintDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements FingerLockResultCallback,FingerprintDialog.Callback {

    private final String TAG = MainActivity.class.getName();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference sensorDataRef;
    private DatabaseReference parentRef;
    private FingerLockManager fingerLockManager;

    private TextView mStatus;

    private static final int USE_FINGERPRINT_REQUEST_CODE = 1;
    private static final int REQUEST_FINGERPRINT_PERMISSION = 2;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    private static final String KEY_NAME = "FingerprintLockKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStatus = (TextView) findViewById(R.id.status);
        Button useDialog = (Button) findViewById(R.id.useDialog);
        fingerLockManager = FingerLock.initialize(this,KEY_NAME);
        if(useDialog != null){
            useDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FingerprintDialog.Builder().with(MainActivity.this).setKeyName(KEY_NAME).setRequestCode(69).show();
                }
            });
        }
        initPickedUpState();
        executeTask();
    }

    @Override
    public void onFingerLockError(@FingerLock.FingerLockErrorState int errorType,Exception e){
        switch(errorType){
            case FingerLock.FINGERPRINT_PERMISSION_DENIED :
                handleFingerPrintPermission();
                break;

            case FingerLock.FINGERPRINT_ERROR_HELP :
                Log.d(TAG,"Fingerprint error");
                break;

            case FingerLock.FINGERPRINT_NOT_RECOGNIZED :

                break;

            case FingerLock.FINGERPRINT_NOT_SUPPORTED :
                Log.d(TAG,"Fingerprint lock not supported by this device");
                break;

            case FingerLock.FINGERPRINT_REGISTRATION_NEEDED :
                Toast.makeText(getApplicationContext(),"There are no fingerprints registered in this device.",Toast.LENGTH_LONG).show();
                break;

            case FingerLock.FINGERPRINT_UNRECOVERABLE_ERROR :
                mStatus.setText(getString(R.string.status_error, e.getMessage()));
                break;
        }
    }

    @Override
    public void onFingerLockAuthenticationSucceeded(){
        mStatus.setText(R.string.status_authenticated);
    }

    @Override
    public void onFingerLockReady(){
        mStatus.setText(R.string.status_ready);
    }

    @Override
    public void onFingerLockScanning(boolean invalidKey){
        if(invalidKey){
            mStatus.setText(R.string.status_scanning_new);
        }
        else {
            mStatus.setText(R.string.status_scanning);
        }
    }

    @Override
    public void onFingerprintDialogStageUpdated(FingerprintDialog dialog, FingerprintDialog.Stage stage) {
        Log.d(TAG, "Dialog stage: " + stage.name());
    }

    @Override
    public void onFingerprintDialogCancelled() {
        Toast.makeText(this, R.string.dialog_cancelled, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFingerprintDialogAuthenticated() {
        Toast.makeText(this, R.string.dialog_authenticated, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFingerprintDialogVerifyPassword(final FingerprintDialog dialog, final String password) {
        // Simulate exchange with backend
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.notifyPasswordValidation(password.equals("aitorvs"));
            }
        }, 1000);
    }


    private void initPickedUpState(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference parentRef = database.getReference();
        DatabaseReference sensorInfoRef = parentRef.child("sensorinfo");
        DatabaseReference pickedUpRef = sensorInfoRef.child("pickedUp");
        pickedUpRef.setValue(new Integer(0));
    }

    private void handleFingerPrintPermission(){
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,android.Manifest.permission.USE_FINGERPRINT)){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("This app requires fingerprint permission");
            builder.setMessage("This permission is essential for secure working of the app");
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.USE_FINGERPRINT},USE_FINGERPRINT_REQUEST_CODE);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    sentToSettings = true;
                    Intent settingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri settingsUri = Uri.fromParts("package", getPackageName(), null);
                    settingsIntent.setData(settingsUri);
                    startActivityForResult(settingsIntent,REQUEST_FINGERPRINT_PERMISSION);
                    Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                }
            });
            builder.show();
        }
        else if(permissionStatus.getBoolean(Manifest.permission.USE_FINGERPRINT,false)){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("This app requires fingerprint permission");
            builder.setMessage("This permission is essential for secure working of the app");
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
        else{
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.USE_FINGERPRINT},USE_FINGERPRINT_REQUEST_CODE);
        }
        SharedPreferences.Editor editor = permissionStatus.edit();
        editor.putBoolean(Manifest.permission.USE_FINGERPRINT,true);
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FINGERPRINT_PERMISSION) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG,"USE_FINGERPRINT permission granted");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode == USE_FINGERPRINT_REQUEST_CODE){
            if(grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d(TAG,"USE_FINGERPRINT permission granted");
            }
            else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.USE_FINGERPRINT)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("This app requires fingerprint permission");
                    builder.setMessage("This permission is essential for secure working of the app");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.USE_FINGERPRINT},USE_FINGERPRINT_REQUEST_CODE);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Unable to get permission",Toast.LENGTH_LONG).show();
                }
            }
        }
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

    @Override
    protected void onPause(){
        super.onPause();

    }
}
