package com.example.readwitharti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ArrayList<Story> arrStories;
     private ImageView imagee;
    private RecyclerView recyclerView;
    private int chosenUser;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chosenUser = 0;
        arrStories = new ArrayList<>();
        imagee=findViewById(R.id.imageView17);
        recyclerView = findViewById(R.id.recyclerStoryList);
        myAdapter = new MyAdapter(MainActivity.this, arrStories);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        // myAdapter = new MyAdapter(MainActivity.this, arrStories);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.exists()) {
                    arrStories.clear();
                    recyclerView.setAdapter(myAdapter);
                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                        StorageReference imageRef = storageRef.child("storyImages/pamuk");
                        long MAXBYTES = 1024 * 1024;
                        imageRef.getBytes(MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Story temp = new Story();
                                temp.setTitle(String.valueOf(snapshot.getKey()));
                                //  temp.setStory(String.valueOf(snapshot.getValue()));
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                temp.setCoverPhoto(bitmap);
                                arrStories.add(temp);
                                 imagee.setImageBitmap(arrStories.get(0).getCoverPhoto());

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    myAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}