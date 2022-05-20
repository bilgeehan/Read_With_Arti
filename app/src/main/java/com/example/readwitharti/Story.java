package com.example.readwitharti;

import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ImageView;

import java.io.File;

public class Story {
    private Bitmap coverPhoto;
    private String title;
    private String story;

    public Story() {
    }

    public Story(String title, String story) {
        this.title = title;
        this.story = story;
    }

    public Bitmap getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(Bitmap coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }
}
