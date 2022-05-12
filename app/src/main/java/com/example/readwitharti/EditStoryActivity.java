package com.example.readwitharti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditStoryActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ArrayList<String> arrKeys;
    private ArrayList<String> arrValues;
    private ArrayAdapter<String> adapter;
    private int chosenPosition;
    private ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_story);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        arrKeys = new ArrayList<>();
        arrValues = new ArrayList<>();
        chosenPosition = 0;
        mAuth = FirebaseAuth.getInstance();
        list = (ListView) findViewById(R.id.listview3);

        mDatabase.child("Stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    adapter = new ArrayAdapter<String>(EditStoryActivity.this,
                            android.R.layout.simple_list_item_1, android.R.id.text1, arrKeys);
                    list.setAdapter(adapter);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        arrKeys.add(String.valueOf(dataSnapshot.getKey()));
                        arrValues.add(String.valueOf(dataSnapshot.getValue()));

                    }
                    System.out.println(arrKeys.get(1));
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            System.out.println(position);
                            chosenPosition = position;
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditStoryActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void onClickEditActivityButton(View view) {
        mDatabase.child("Stories").child(arrKeys.get(chosenPosition)).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(EditStoryActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        if (arrKeys.size() <= 1 && arrValues.size() <= 1) {
            adapter.clear();
        }
    }



}