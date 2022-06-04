package com.example.readwitharti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.widget.CheckBox;

import java.util.Calendar;


public class SignUpActivity extends AppCompatActivity {
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText repeatedpassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private CheckBox checkBox;
    private Button selectDate;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int year, month, day;
    private String stringDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        name = (EditText) findViewById(R.id.inputName);
        email = (EditText) findViewById(R.id.inputMail);
        password = (EditText) findViewById(R.id.inputPassword);
        repeatedpassword = (EditText) findViewById(R.id.inputRepeatPassword);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        selectDate = findViewById(R.id.button10);
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(SignUpActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                stringDate = day + "/" + (month + 1) + "/" + year;
                                selectDate.setText(stringDate);
                                System.out.println(stringDate);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    public void onClickButton(View view) {
        String stringName = name.getText().toString();
        String stringEmail = email.getText().toString();
        String stringPassword = password.getText().toString();
        String stringRepeatPassword = repeatedpassword.getText().toString();

        if (stringName.isEmpty() || stringDate.isEmpty() || stringEmail.isEmpty() || stringPassword.isEmpty() || stringRepeatPassword.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please fill in the blank spaces and if not, Agree to the Terms of Policy", Toast.LENGTH_LONG).show();
        } else {
            if (stringPassword.length() >= 6) {
                if (stringRepeatPassword.equals(stringPassword)) {
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
            } else {
                Toast.makeText(SignUpActivity.this, "Please enter the same string as password in repeated password blank", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

