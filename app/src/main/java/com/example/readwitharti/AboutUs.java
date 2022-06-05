package com.example.readwitharti;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
    }

    public void onClickBack(View v) {
        Intent intent = new Intent(AboutUs.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}