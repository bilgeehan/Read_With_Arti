package com.example.readwitharti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void onClickAddStory(View view) {
        Intent addStoryIntent = new Intent(AdminActivity.this, AddStoryActivity.class);
        startActivity(addStoryIntent);
    }

    public void onClickDeleteStory(View view) {
        Intent deleteStoryIntent = new Intent(AdminActivity.this, DeleteStoryActivity.class);
        startActivity(deleteStoryIntent);
    }
}