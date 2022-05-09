package com.example.readwitharti;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddStoryActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ImageView storyImage;
    private EditText title;
    private EditText story;
    private Uri imageUrl;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private boolean isPhotoAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        title = (EditText) findViewById(R.id.inputTitleStory);
        story = (EditText) findViewById(R.id.inputStory);
        storyImage = (ImageView) findViewById(R.id.storyImage);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        isPhotoAdded = false;
    }

    public void onClickAdd(View view) {
        String strTitle = title.getText().toString();
        String strStory = story.getText().toString();
        if (strTitle.equals("") || strStory.equals("")) {
            Toast.makeText(AddStoryActivity.this, "Please Fill The Blanks", Toast.LENGTH_SHORT).show();
        } else {
            mDatabase.child("Stories").child(strTitle).setValue(strStory).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful() && isPhotoAdded) {
                        uploadPicture();
                    } else {
                        Toast.makeText(AddStoryActivity.this, "!!ERROR!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void onClickAddPhoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUrl = data.getData();
            //storyImage.setImageURI(imageUrl);
          //  storyImage.setImageDrawable(R.drawable.tick);
            storyImage.setImageResource(getResources().getIdentifier("com.example.readwitharti:drawable/tick", null, null));
            isPhotoAdded = true;
        }
    }

    private void uploadPicture() {
        final ProgressDialog progress = new ProgressDialog(AddStoryActivity.this);
        progress.setTitle("Image Uploading...");
        progress.show();

        //   String random = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("storyImages/" + title.getText().toString());
        riversRef.putFile(imageUrl).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    progress.dismiss();
                    Snackbar.make(findViewById(android.R.id.content), "Story Uploaded", Snackbar.LENGTH_LONG).show();
                } else {
                    progress.dismiss();
                    Toast.makeText(AddStoryActivity.this, "Failed to Upload Picture.", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressNum = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progress.setMessage("Progress :" + (int) progressNum + "%");
            }
        });
    }
}