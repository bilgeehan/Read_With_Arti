package com.example.readwitharti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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

public class DeleteStoryActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_delete_story);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        arrKeys = new ArrayList<>();
        arrValues = new ArrayList<>();
        chosenPosition = 0;
        mAuth = FirebaseAuth.getInstance();
        list = (ListView) findViewById(R.id.listActivities);
        mDatabase.child("Stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.exists()) {
                    arrValues.clear();
                    arrKeys.clear();
                    adapter = new ArrayAdapter<String>(DeleteStoryActivity.this,
                            android.R.layout.simple_list_item_1, android.R.id.text1, arrKeys);
                    list.setAdapter(adapter);
                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                        arrKeys.add(String.valueOf(snapshot.getKey()));
                        arrValues.add(String.valueOf(snapshot.getValue()));
                    }
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
                Toast.makeText(DeleteStoryActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickDeleteActivityButton(View view) {
        mDatabase.child("Stories").child(arrKeys.get(chosenPosition)).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(DeleteStoryActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        if (arrKeys.size() <= 1 && arrValues.size() <= 1) {
            adapter.clear();
        }
    }
}