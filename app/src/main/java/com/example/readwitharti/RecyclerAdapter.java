package com.example.readwitharti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private static final String Tag = "RecyclerView";
    private Context mContext;
    private ArrayList<Story> storyList;
    private onClickStoryListener mOnClickStoryListener;

    public RecyclerAdapter(Context mContext, ArrayList<Story> storyList,onClickStoryListener onClickStoryListener) {
        this.mContext = mContext;
        this.storyList = storyList;
        this.mOnClickStoryListener=onClickStoryListener;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.story_item, parent, false);
        return new ViewHolder(view,mOnClickStoryListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(storyList.get(position).getTitle());

        Glide.with(mContext)
                .load(storyList.get(position).getCoverPhoto())
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView;
        onClickStoryListener onClickStoryListener;

        public ViewHolder(@NonNull View itemView, onClickStoryListener onClickStoryListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textViewy);
            itemView.setOnClickListener(this);
            this.onClickStoryListener = onClickStoryListener;
        }

        @Override
        public void onClick(View v) {
            onClickStoryListener.onClickStory(getAdapterPosition());
        }
    }

    public interface onClickStoryListener {
        void onClickStory(int position);
    }
}
