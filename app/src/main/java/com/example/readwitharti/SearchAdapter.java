package com.example.readwitharti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    Context context;
    ArrayList<Story> listt;

    public SearchAdapter(Context context, ArrayList<Story> listt) {
        this.context = context;
        this.listt = listt;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.story_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Story story = listt.get(position);
        holder.titlee.setText(story.getTitle());
    }

    @Override
    public int getItemCount() {
        return listt.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView  titlee;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titlee = itemView.findViewById(R.id.inputTitleStory);
        }
    }
}
