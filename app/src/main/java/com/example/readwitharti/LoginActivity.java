package com.example.readwitharti;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private EditText mail;
    private EditText password;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private ArrayList<String> admins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        password = (EditText) findViewById(R.id.editTextPassword);
        mail = (EditText) findViewById(R.id.editTextMail);
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        getDataFromDatabase();
        admins = new ArrayList<>();
    }

    private void getDataFromDatabase() {
        myRef.child("Admins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    admins.add(String.valueOf(snapshot.getKey()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "!Error! Try again later", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onClickLogin(View view) {
        String stringMail = mail.getText().toString();
        String stringPassword = password.getText().toString();
        mAuth.signInWithEmailAndPassword(stringMail, stringPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (admins.contains(mAuth.getUid())) {
                        Toast.makeText(LoginActivity.this, "You are successfully logged in", Toast.LENGTH_SHORT).show();
                        Intent loginIntent = new Intent(LoginActivity.this, AdminActivity.class);
                        startActivity(loginIntent);
                    } else {
                        Toast.makeText(LoginActivity.this, "You are successfully logged in", Toast.LENGTH_SHORT).show();
                        Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(loginIntent);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, " Please check your informations and try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onClickSignup(View view) {
        Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(signUpIntent);
    }

    public void onClickForgot(View view) {
        Intent signUpIntent = new Intent(LoginActivity.this, ForgotPassword.class);
        startActivity(signUpIntent);
    }
}