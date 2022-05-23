package com.example.readwitharti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechToStoryActivity extends AppCompatActivity {
    private TextView title;
    private String strTitle;
    private TextView story;
    private String userTalk;
    private ImageView micButton;
    private SpeechRecognizer speechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_story);
        title = findViewById(R.id.textTitle);
        story = findViewById(R.id.textStory);
        Intent intent = getIntent();
        strTitle = intent.getStringExtra("chosenTitle");
        title.setText(strTitle);
        userTalk = "";
        micButton = findViewById(R.id.imageView20);
        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
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
            }

            @Override
            public void onResults(Bundle results) {
                userTalk = "";
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                for (int i = 0; i < data.size(); i++) {
                    userTalk += data.get(i);
                }
                story.setText(userTalk);
                System.out.println(userTalk);
                micButton.setImageResource(R.drawable.no_sound);
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
        speechRecognizer.startListening(intent);
        micButton.setImageResource(R.drawable.microphone);
    }
}