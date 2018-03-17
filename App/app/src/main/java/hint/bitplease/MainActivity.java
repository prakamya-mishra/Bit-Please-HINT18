package hint.bitplease;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getName();

    private TextView txtSpeechInput;
    private EditText Distance;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    public String Command;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference sensorDataRef;
    private DatabaseReference parentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtSpeechInput = (TextView) findViewById(R.id.txtView);
        Distance = (EditText) findViewById(R.id.dist);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        writeValuesToFirebase();
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
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
                }
                break;
            }

        }
    }

    private void writeValuesToFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        parentRef = firebaseDatabase.getReference();
        sensorDataRef = parentRef.child("sensorinfo");
        DatabaseReference distanceRef = sensorDataRef.child("distance");
        DatabaseReference gyroXRef = sensorDataRef.child("gyroX");
        DatabaseReference gyroYRef = sensorDataRef.child("gyroY");
        DatabaseReference gyroZRef = sensorDataRef.child("gyroZ");
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
        });
        distanceRef.setValue(new Float(1.0));
        gyroXRef.setValue(new Float(1.0));
        gyroYRef.setValue(new Float(1.0));
        gyroZRef.setValue(new Float(1.0));
        //sensorDataRef.setValue(new SensorData(1.0f,1.0f,1.0f,1.0f,1.0f,1.0f));
    }
}