package com.example.readwitharti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechToStoryActivity extends AppCompatActivity {
    private TextView title;
    ArrayList<String> userTalks;
    ArrayList<String> arrStory;
    private String strTitle;
    private TextView story;
    private DatabaseReference myRef;
    private String userTalk;
    private ImageView micButton;
    private Chronometer timer;
    private SpeechRecognizer speechRecognizer;
    private long totalTime;
    private boolean isRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_story);
        title = findViewById(R.id.textTitle);
        story = findViewById(R.id.textStory);
        micButton = findViewById(R.id.imageView20);
        myRef = FirebaseDatabase.getInstance().getReference();
        userTalk = "";
        arrStory = new ArrayList<>();
        userTalks = new ArrayList<>();

        timer = findViewById(R.id.chronometer);
        timer.setFormat("Time: %s");
        timer.setBase(SystemClock.elapsedRealtime());

        Intent intent = getIntent();
        strTitle = intent.getStringExtra("chosenTitle");
        title.setText(strTitle);
        getStoryFromDatabase();

        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
    }

    private void getStoryFromDatabase() {
        myRef.child("Stories").child(strTitle).child("story").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                story.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SpeechToStoryActivity.this, "!Error! Try again later", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start to Read");
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(SpeechToStoryActivity.this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Toast.makeText(SpeechToStoryActivity.this, "Listening...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {
                Toast.makeText(SpeechToStoryActivity.this, "Speech Not Detected", Toast.LENGTH_LONG).show();
                micButton.setImageResource(R.drawable.no_sound);
                pauseTimer();
                resetTimer();
                totalTime = 0;
            }

            @Override
            public void onResults(Bundle results) {
                userTalk = "";
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                for (int i = 0; i < data.size(); i++) {
                    userTalk += data.get(i);
                }
                //  story.setText(userTalk);
                System.out.println(userTalk);
                micButton.setImageResource(R.drawable.no_sound);
               // speechRecognizer.stopListening();
                pauseTimer();
                System.out.println(totalTime);
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
        speechRecognizer.startListening(intent);
        resetTimer();
        startTimer();
        micButton.setImageResource(R.drawable.microphone);
    }

    public void startTimer() {
        if (!isRunning) {
            timer.setBase(SystemClock.elapsedRealtime() - totalTime);
            timer.start();
            isRunning = true;
        }
    }

    public void pauseTimer() {
        if (isRunning) {
            timer.stop();
            totalTime = SystemClock.elapsedRealtime() - timer.getBase();
            isRunning = false;
        }
    }

    public void resetTimer() {
        timer.setBase(SystemClock.elapsedRealtime());
        totalTime = 0;
    }
}