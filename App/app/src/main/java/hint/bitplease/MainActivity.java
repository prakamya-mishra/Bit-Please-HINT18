package hint.bitplease;

import android.app.Notification;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import br.com.goncalves.pugnotification.notification.PugNotification;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getName();

    private TextView txtSpeechInput;
    private TextView Distance;
    private ImageButton btnSpeak;
    private Button refreshButton;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    public String Command;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference sensorDataRef;
    private DatabaseReference parentRef;

    private Thread firebaseWriteThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtSpeechInput = (TextView) findViewById(R.id.txtView);
        Distance = (TextView) findViewById(R.id.dist);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        refreshButton = (Button) findViewById(R.id.refresh);
        final Random random = new Random();
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Distance.setText((random.nextInt(100) + 1)+"");
            }
        });
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        firebaseWriteThread = new Thread(new Runnable() {
            @Override
            public void run() {
                writeValuesToFirebase();
            }
        });
        firebaseWriteThread.run();
        addPickedUpListener();
        addMaliciousUserListener();
    }

    private void addPickedUpListener(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        parentRef = firebaseDatabase.getReference();
        sensorDataRef = parentRef.child("sensorinfo");
        DatabaseReference pickedUpRef = sensorDataRef.child("pickedUp");
        pickedUpRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 Integer pickdeUp = dataSnapshot.getValue(Integer.class);
                 Log.e(TAG,pickdeUp+"");
                if(pickdeUp == 1){
                    PugNotification.with(getApplicationContext()).load().title("Warning")
                            .bigTextStyle("Your bag was lifted was this you?").smallIcon(R.drawable.pugnotification_ic_launcher)
                            .largeIcon(R.drawable.pugnotification_ic_launcher).flags(Notification.DEFAULT_ALL).simple().build();
                    initPickedUpState();
                }
                else{
                    Log.d(TAG,"pickedUp value reset i.e Server app relaunched");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,databaseError.getCode() + databaseError.getMessage());
            }
        });
    }

    private void addMaliciousUserListener(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        parentRef = firebaseDatabase.getReference();
        sensorDataRef = parentRef.child("sensorinfo");
        DatabaseReference lockedBitRef = sensorDataRef.child("lockedBit");
        final DatabaseReference maliciousUserRef = sensorDataRef.child("maliciousUserBit");
        lockedBitRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer lockedBit = dataSnapshot.getValue(Integer.class);
                if(lockedBit == 1){
                    maliciousUserRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Integer maliciousUserBit = dataSnapshot.getValue(Integer.class);
                            if(maliciousUserBit == 1){
                                PugNotification.with(getApplicationContext()).load().title("Warning")
                                        .bigTextStyle("Unknown fingerprint detected").smallIcon(R.drawable.pugnotification_ic_launcher)
                                        .largeIcon(R.drawable.pugnotification_ic_launcher).flags(Notification.DEFAULT_ALL).simple().build();
                                initMaliciousUserBitState();
                            }
                            else{
                                Log.d(TAG,"maliciousUserBit reset");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void initMaliciousUserBitState(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference parentRef = database.getReference();
        DatabaseReference sensorInfoRef = parentRef.child("sensorinfo");
        DatabaseReference maliciousUserBitRef = sensorInfoRef.child("maliciousUserBit");
        maliciousUserBitRef.setValue(new Integer(0));
    }

    private void initPickedUpState(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference parentRef = database.getReference();
        DatabaseReference sensorInfoRef = parentRef.child("sensorinfo");
        DatabaseReference pickedUpRef = sensorInfoRef.child("pickedUp");
        pickedUpRef.setValue(new Integer(0));
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    Command = result.get(0);
                    if(Command.equals("Stop")){
                        Toast.makeText(getApplicationContext(),"Luggage stopped",Toast.LENGTH_LONG).show();
                    }
                    else if(Command.equals("Hello")){
                        Toast.makeText(getApplicationContext(),"Hey!!!!",Toast.LENGTH_LONG).show();
                    }
                    else if(Command.equals("follow me"))
                    {
                        Random random = new Random();
                        Toast.makeText(getApplicationContext(),"Following started!",Toast.LENGTH_LONG).show();
                        Distance.setText((random.nextInt(50) + 1) + "");
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Invalid command.",Toast.LENGTH_LONG).show();

                    }

                }
                break;
            }

        }
    }

    private void writeValuesToFirebase(){
        final Random random = new Random();
        firebaseDatabase = FirebaseDatabase.getInstance();
        parentRef = firebaseDatabase.getReference();
        sensorDataRef = parentRef.child("sensorinfo");
        DatabaseReference distanceRef = sensorDataRef.child("distance");
        final DatabaseReference distanceLeft = sensorDataRef.child("distanceLeft");
        final DatabaseReference distanceRight = sensorDataRef.child("distanceRight");
        distanceLeft.addListenerForSingleValueEvent(new ValueEventListener() {
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
                        Log.e(TAG,random.nextInt(300) + 1 + "");
                        distanceLeftList.add(new Float(random.nextInt(300) + 1));
                    }
                    else{
                        distanceLeftList.add(new Float(random.nextInt(300) + 1));
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
                        distanceRightList.add(new Float(random.nextInt(300) + 1));
                    }
                    else{
                        distanceRightList.add(new Float(random.nextInt(300) + 1));
                    }
                    distanceRight.setValue(distanceRightList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,databaseError.getCode() + databaseError.getMessage());
            }
        });
        Float dist = new Float(random.nextInt(300) + 1);
        distanceRef.setValue(dist);
        Distance.setText(dist+"");
    }
}
