package com.example.readwitharti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.NoCopySpan;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    SearchView searchView;
    RecyclerView recyclerView;
    DatabaseReference myRef;
    ArrayList<Story> stories;
    RecyclerAdapter recyclerAdapter;
private AutoCompleteTextView searchtext;
private ListView liststory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
       recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
       recyclerView.setLayoutManager(new LinearLayoutManager(this));
       searchView=findViewById(R.id.searchView);
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
        List<Story> storylist=new ArrayList<>();
        for (Story item: storylist){

        }
    }
    private void displayStories(){
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        stories=new ArrayList<>();


    }


}