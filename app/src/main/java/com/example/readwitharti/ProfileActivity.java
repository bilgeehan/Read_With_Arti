package com.example.readwitharti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerVieww;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ArrayList<Statistics> statisticsArrayList;
    private StatisticsAdapter statisticsAdapter;
    private String name;
    private String date;
    private TextView textName;
    private TextView textDate;
    private final int currentYear = 2022;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerVieww = findViewById(R.id.recyclervieww);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerVieww.setLayoutManager(linearLayoutManager);
        textName = findViewById(R.id.textView22);
        textDate = findViewById(R.id.textView24);
        recyclerVieww.setHasFixedSize(true);
        statisticsArrayList = new ArrayList<>();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) ProfileActivity.this);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_profile)
                .build();
        // NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
        navView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) this);

        takeStatisticsFromDatabase();
        takeUserInformation();

    }

    private void takeUserInformation() {
        mDatabase.child("Users").child(mAuth.getUid()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue().toString();
                textName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, error.getCode(), Toast.LENGTH_LONG).show();
            }
        });
        mDatabase.child("Users").child(mAuth.getUid()).child("date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                date = dataSnapshot.getValue().toString();
                String arr[] = date.split("/");
                //System.out.println(arr[2]);
                int yearr = Integer.parseInt(arr[2]);
                textDate.setText(String.valueOf(currentYear - yearr));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, error.getCode(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void takeStatisticsFromDatabase() {
        mDatabase.child("Users").child(mAuth.getUid()).child("Stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Statistics statistics = new Statistics();
                    statistics.setScore(Double.parseDouble(snapshot.child("score").getValue().toString()));
                    statistics.setTime(Integer.parseInt(snapshot.child("time").getValue().toString()));
                    statistics.setTitle(snapshot.getKey());
                    statisticsArrayList.add(statistics);
                }
//                System.out.println(statisticsArrayList.get(1).time);
                statisticsAdapter = new StatisticsAdapter(getApplicationContext(), statisticsArrayList);
                recyclerVieww.setAdapter(statisticsAdapter);
                statisticsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, error.getCode(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home: {
                startActivity(new Intent(this, MainActivity.class));
                break;
            }
            case R.id.navigation_search: {
                startActivity(new Intent(this, SearchActivity.class));
                break;
            }
            case R.id.navigation_profile: {
                //startActivity(new Intent(this, ProfileActivity.class));
                break;

            }
        }
        return false;
    }

    public void logout(View view) {
        mAuth.signOut();
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}