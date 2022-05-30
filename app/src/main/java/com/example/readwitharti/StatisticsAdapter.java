package com.example.readwitharti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Statistics> statisticsArrayList;

    public StatisticsAdapter(Context mContext, ArrayList<Statistics> statisticsArrayList) {
        this.mContext = mContext;
        this.statisticsArrayList = statisticsArrayList;
    }

    @NonNull
    @Override
    public StatisticsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.statistics_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticsAdapter.ViewHolder holder, int position) {
        holder.textViewScore.setText(String.valueOf(statisticsArrayList.get(position).getScore()));
        holder.textViewTime.setText(String.valueOf(statisticsArrayList.get(position).getTime()));
        holder.textViewTitle.setText(statisticsArrayList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return statisticsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewScore;
        TextView textViewTime;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textView17);
            textViewScore = itemView.findViewById(R.id.textView19);
            textViewTime = itemView.findViewById(R.id.textView33);
        }
    }
}

