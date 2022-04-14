package com.example.readwitharti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText eMail;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        eMail = findViewById(R.id.editTextMail2);
        mAuth = FirebaseAuth.getInstance();
    }

    public void onClickSendMailButton(View view) {
        String strEmail = eMail.getText().toString();
        if (validateEmail(strEmail)) {
            mAuth.sendPasswordResetEmail(strEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPassword.this, "Password reset email has been sent", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ForgotPassword.this, "!Error! Try again later", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(ForgotPassword.this, " Your email address is invalid", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateEmail(String valEmail) {
 /*       if (!valEmail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(valEmail).matches()) {
            return true;
        } else {
            return false;
        }*/
        return !valEmail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(valEmail).matches();

    }
}