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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AdminEditStory extends AppCompatActivity {
    private TextView admintitle;
    private EditText adminstory;
    private DatabaseReference mDatabase;
    private String story;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_story);
        Intent intent = getIntent();
        title = intent.getStringExtra("Title");
        adminstory = findViewById(R.id.text9);
        admintitle = findViewById(R.id.textView11);
        admintitle.setText(title);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Stories").child(title).child("story").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    story = snapshot.getValue().toString();
                    adminstory.setText(story);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminEditStory.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onclickbutton(View view) {
        String str = adminstory.getText().toString();
        mDatabase.child("Stories").child(title).child("story").setValue(str).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AdminEditStory.this, "Edited", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AdminEditStory.this, AdminActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AdminEditStory.this, "Not Edited", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}



