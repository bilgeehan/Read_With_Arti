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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class SpeechToStoryActivity extends AppCompatActivity {
    private TextView title;
    private String[] userTalks;
    private String[] splitStory;
    private ArrayList<String> wrongWords;
    private String strStory;
    private String strTitle;
    private TextView story;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private double userScore;
    private ImageView saveButton;
    private ImageView micButton;
    private Boolean isClicked;
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
        saveButton = findViewById(R.id.imageView32);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        wrongWords = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        isClicked = false;
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
                isClicked = true;
                speechToTextMethod();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClicked) {
                    mDatabase.child("Users").child(mAuth.getUid()).child("Stories").child(strTitle).child("time").setValue(totalTime);
                    mDatabase.child("Users").child(mAuth.getUid()).child("Stories").child(strTitle).child("score").setValue(userScore);
                    Toast.makeText(SpeechToStoryActivity.this, "Story Saved", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(SpeechToStoryActivity.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                } else {
                    Toast.makeText(SpeechToStoryActivity.this, "Please Read Story First", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getStoryFromDatabase() {
        mDatabase.child("Stories").child(strTitle).child("story").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                strStory = dataSnapshot.getValue().toString();
                story.setText(strStory);
                splitStory = strStory.split(" ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SpeechToStoryActivity.this, "!Error! Try again later", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void speechToTextMethod() {
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
                if (error == 3 || error == 9) {
                    Toast.makeText(SpeechToStoryActivity.this, "Please Give Record Permission to Google and ReadWithArti",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SpeechToStoryActivity.this, "Speech Not Detected", Toast.LENGTH_SHORT).show();
                }
                System.out.println("Error" + error);
                micButton.setImageResource(R.drawable.no_sound);
                pauseTimer();
                resetTimer();
                totalTime = 0;
            }

            @Override
            public void onResults(Bundle results) {
                String userTalk = "";
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                for (int i = 0; i < data.size(); i++) {
                    userTalk += data.get(i);
                }
                System.out.println(userTalk);
                micButton.setImageResource(R.drawable.no_sound);
                userTalks = userTalk.split(" ");
                pauseTimer();
                compareTalks();
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

    private void compareTalks() {
        wrongWords.clear();
        for (int i = 0; i < splitStory.length; i++) {
            try {
                if (!Objects.equals(userTalks[i], splitStory[i])) {
                    wrongWords.add(splitStory[i]);
                }
            } catch (ArrayIndexOutOfBoundsException exception) {
                break;
            }
        }
        for (int i = 0; i < wrongWords.size(); i++) {
            System.out.println(wrongWords.get(i));
        }
        final DecimalFormat df = new DecimalFormat("0.00");
        userScore = (((double) splitStory.length - (double) wrongWords.size()) / ((double) splitStory.length));
        userScore = Double.valueOf(df.format(userScore));
    }

    private void startTimer() {
        if (!isRunning) {
            timer.setBase(SystemClock.elapsedRealtime() - totalTime);
            timer.start();
            isRunning = true;
        }
    }

    private void pauseTimer() {
        if (isRunning) {
            timer.stop();
            totalTime = SystemClock.elapsedRealtime() - timer.getBase();
            isRunning = false;
        }
    }

    private void resetTimer() {
        timer.setBase(SystemClock.elapsedRealtime());
        totalTime = 0;
    }
}