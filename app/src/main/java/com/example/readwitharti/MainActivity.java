package com.example.readwitharti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference myRef;
    ArrayList<Story> stories;
    RecyclerAdapter recyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        myRef = FirebaseDatabase.getInstance().getReference();
        stories = new ArrayList<>();
        clearAll();
        getDataFromFirebase();

    }

    private void getDataFromFirebase() {
        Query query = myRef.child("Stories");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearAll();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Story story = new Story();
                    story.setCoverPhoto(snapshot.child("image").getValue().toString());
                    story.setTitle(snapshot.child("title").getValue().toString());
                    stories.add(story);
                }
                System.out.println(stories.get(0).getTitle());
                System.out.println(stories.get(0).getCoverPhoto());
                System.out.println(stories.get(1).getTitle());
                System.out.println(stories.get(1).getCoverPhoto());
                recyclerAdapter = new RecyclerAdapter(getApplicationContext(), stories);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clearAll() {
        if (stories != null) {
            stories.clear();
            if (recyclerAdapter != null) {
                recyclerAdapter.notifyDataSetChanged();
            }
        }
        stories = new ArrayList<>();
    }
}