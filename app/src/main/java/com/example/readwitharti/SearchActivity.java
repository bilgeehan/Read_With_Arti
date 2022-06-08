package com.example.readwitharti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private SearchView searchView;
    private DatabaseReference mDatabase;
    private ArrayList<String> titles;
    private ArrayAdapter<String> adapter;
    private ListView listTitles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = findViewById(R.id.searchView);
        titles = new ArrayList<>();
        listTitles = (ListView) findViewById(R.id.listTitles);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) SearchActivity.this);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_profile)
                .build();
       // NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
        navView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) this);



        mDatabase.child("Stories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    titles.add(String.valueOf(snapshot.getKey()));
                }
                adapter = new ArrayAdapter<String>(SearchActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, titles);
                listTitles.setAdapter(adapter);
                listTitles.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItem = String.valueOf(parent.getItemAtPosition(position));
                        Intent intent = new Intent(SearchActivity.this, SpeechToStoryActivity.class);
                        intent.putExtra("chosenTitle", selectedItem);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                for (int i = 0; i < titles.size(); i++) {
                    if (titles.get(i).contains(query)) {
                        adapter.getFilter().filter(query);
                    } else {
                        // Toast.makeText(SearchActivity.this, "There is no story with this title", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.navigation_home:{
                startActivity(new Intent(this, MainActivity.class));
                break;
            }
            case R.id.navigation_search:{
              //  startActivity(new Intent(this, SearchActivity.class));
                break;
            }
            case R.id.navigation_profile:{
                startActivity(new Intent(this, ProfileActivity.class));
                break;

            }
        }
        return false;
    }
}