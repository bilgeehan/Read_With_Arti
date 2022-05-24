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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.widget.CheckBox;


public class SignUpActivity extends AppCompatActivity {
    private EditText name;
    private EditText date;
    private EditText email;
    private EditText password;
    private EditText repeatedpassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        name = (EditText) findViewById(R.id.inputName);
        date = (EditText) findViewById(R.id.inputDate);
        email = (EditText) findViewById(R.id.inputMail);
        password = (EditText) findViewById(R.id.inputPassword);
        repeatedpassword=(EditText) findViewById(R.id.inputRepeatPassword);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

    }


    public void onClickButton(View view) {
        String stringName = name.getText().toString();
        String stringDate = date.getText().toString();
        String stringEmail = email.getText().toString();
        String stringPassword = password.getText().toString();
        String stringrepeatPassword= repeatedpassword.getText().toString();


        if (stringName.isEmpty() || stringDate.isEmpty() || stringEmail.isEmpty() || stringPassword.isEmpty()|| stringrepeatPassword.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please fill in the blank spaces and if not, Agree to the Terms of Policy", Toast.LENGTH_LONG).show();
        } else {
            if(stringPassword.length() >= 6 ){
            if (stringrepeatPassword.equals(stringPassword)) {
                if (checkBox.isChecked()) {

                    mAuth.createUserWithEmailAndPassword(stringEmail, stringPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mDatabase.child("Users").child(mAuth.getUid()).child("name").setValue(stringName);
                                mDatabase.child("Users").child(mAuth.getUid()).child("date").setValue(stringDate);
                                mDatabase.child("Users").child(mAuth.getUid()).child("mail").setValue(stringEmail);

                                Toast.makeText(SignUpActivity.this, "User successfully created", Toast.LENGTH_SHORT).show();
                                Intent registerIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(registerIntent);
                            } else {
                                Toast.makeText(SignUpActivity.this, "User cannot be created. Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUpActivity.this, "You have to agree to the terms of policy to sign up.", Toast.LENGTH_LONG).show();

                }
            } else {
                Toast.makeText(SignUpActivity.this, "Please create a password of at least 6 characters", Toast.LENGTH_SHORT).show();
            }
        } else{
                Toast.makeText(SignUpActivity.this, "Please enter the same string as password in repeated password blank", Toast.LENGTH_SHORT).show();
            }

        }


        }
    }

