package com.example.readwitharti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

public class SearchStoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MainAdapter mainAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_story);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Story> options= new FirebaseRecyclerOptions.Builder<Story>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Stories"),)
    }
}