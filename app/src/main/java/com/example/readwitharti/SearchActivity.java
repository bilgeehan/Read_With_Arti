package com.example.readwitharti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    SearchView searchView;
    RecyclerView recyclerView;
    DatabaseReference mDatabase;
    ArrayList<String> titles;
    RecyclerAdapter recyclerAdapter;
    private AutoCompleteTextView searchtext;
    private ListView liststory;
    private ArrayAdapter<String> adapter;
    private ListView listTitles;
    private int chosenPosition;

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
                        System.out.println(position);
                        chosenPosition = position;
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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });


    }

    private void filter(String newText) {
        List<Story> storylist = new ArrayList<>();
        for (Story item : storylist) {
        }
    }
}

