package com.example.readwitharti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Story> list;

    public MyAdapter(Context context, ArrayList<Story> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Story story = list.get(position);
        holder.storyNamee.setText(story.getTitle());
        holder.storyImage.setImageBitmap(story.getCoverPhoto());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView storyNamee;
        ImageView storyImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            storyImage = (ImageView) itemView.findViewById(R.id.imageView15);
            storyNamee = (TextView) itemView.findViewById(R.id.storyName);
        }
    }
}
