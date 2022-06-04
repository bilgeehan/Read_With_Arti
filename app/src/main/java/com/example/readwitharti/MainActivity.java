package com.example.readwitharti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.onClickStoryListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    private ArrayList<Story> stories;
    private RecyclerAdapter recyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        myRef = FirebaseDatabase.getInstance().getReference();
        stories = new ArrayList<>();
        clearEverything();
        getDataFromDatabase();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) MainActivity.this);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_profile)
                .build();
        // NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
        navView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) this);

    }

    private void getDataFromDatabase() {
        myRef.child("Stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearEverything();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Story story = new Story();
                    story.setCoverPhoto(snapshot.child("image").getValue().toString());
                    story.setTitle(snapshot.child("title").getValue().toString());
                    stories.add(story);
                }
                recyclerAdapter = new RecyclerAdapter(getApplicationContext(), stories, MainActivity.this);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "!Error! Try again later", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clearEverything() {
        if (stories != null) {
            stories.clear();
            if (recyclerAdapter != null) {
                recyclerAdapter.notifyDataSetChanged();
            }
        }
        stories = new ArrayList<>();
    }

    @Override
    public void onClickStory(int position) {
        System.out.println(stories.get(position).getTitle());
        Intent intent = new Intent(MainActivity.this, SpeechToStoryActivity.class);
        intent.putExtra("chosenTitle", stories.get(position).getTitle());
        startActivity(intent);
        //finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home: {
                // startActivity(new Intent(this, MainActivity.class));
                break;
            }
            case R.id.navigation_search: {
                startActivity(new Intent(this, SearchActivity.class));
                break;
            }
            case R.id.navigation_profile: {
                startActivity(new Intent(this, ProfileActivity.class));
                break;

            }
        }
        return false;
    }
}