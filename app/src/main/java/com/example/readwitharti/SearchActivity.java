package com.example.readwitharti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
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
        mDatabase.child("Stories").addValueEventListener(new ValueEventListener() {
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
                        String selecteditem = String.valueOf(parent.getItemAtPosition(position));
                        Intent intent = new Intent(SearchActivity.this, SpeechToStoryActivity.class);
                        intent.putExtra("chosenTitle", selecteditem);
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


}