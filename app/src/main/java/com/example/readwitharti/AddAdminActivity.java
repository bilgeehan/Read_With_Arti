package com.example.readwitharti;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddAdminActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ArrayList<User> arrUser;
    private ArrayList<String> arrKeys;
    private ArrayList<String> userMails;
    private Spinner spinner;
    private int chosenUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((R.layout.activity_add_admin));
        // Map<String, User> emptyMap = Collections.<String, User>emptyMap();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        arrUser = new ArrayList<>();
        arrKeys = new ArrayList<>();
        userMails = new ArrayList<>();
        spinner = (Spinner) findViewById(R.id.spinner);
        mDatabase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrUser.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        arrUser.add((User) dataSnapshot.getValue(User.class));
                        // emptyMap.put(dataSnapshot.getKey(), (User) dataSnapshot.getValue(User.class));
                        arrKeys.add((String) dataSnapshot.getKey());
                    }
                    for (int i = 0; i < arrUser.size(); i++) {
                        userMails.add(arrUser.get(i).getMail());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, userMails);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            chosenUser = i;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            Toast.makeText(AddAdminActivity.this, "Please Choose a User", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddAdminActivity.this, "!!ERROR!!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void addsAdmin(View view) {
        mDatabase.child("Admins").child(arrKeys.get(chosenUser)).setValue(arrUser.get(chosenUser).getMail()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(AddAdminActivity.this, AdminActivity.class);
                    Toast.makeText(AddAdminActivity.this, "Admin Added", Toast.LENGTH_SHORT).show();
                    // Snackbar.make(findViewById(android.R.id.content), "Admin Added", Snackbar.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}