package com.example.readwitharti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AdminEditStory extends AppCompatActivity {
private TextView admintitle;
private EditText adminstory;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_story);
        Intent intent = getIntent();
        String title = intent.getStringExtra("Title");
        String story=intent.getStringExtra("Stories");
        adminstory=findViewById(R.id.text9);
        admintitle=findViewById(R.id.textView11);
        adminstory.setText(story);
        admintitle.setText(title);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

                }

            //mDatabase.child("Stories").child(admintitle).setValue(adminstory);


            }



