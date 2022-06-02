package com.example.readwitharti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> implements Filterable {
    Context context;
    ArrayList<Story> listt;
    ArrayList<Story> filteredlistt;



    public SearchAdapter(Context context, ArrayList<Story> listt, ArrayList<Story> filteredlistt) {
        this.context = context;
        this.listt = listt;
        this.filteredlistt=filteredlistt;
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString=charSequence.toString();
                if(charString.isEmpty()){
                    filteredlistt=listt;
                } else {
                    ArrayList<Story> filteredlist = new ArrayList<>();
                        for(int i=0; i<listt.size(); i++){
                            if(listt.get(i).getTitle().toLowerCase().contains(charString.toLowerCase())){
                                filteredlist.add(listt.get(i));
                            }
                        }
                    filteredlistt=filteredlist;
                }
                FilterResults filterResults= new FilterResults();
                filterResults.values=filteredlistt;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listt.clear();
                filteredlistt=(ArrayList<Story>) filterResults.values;
                notifyDataSetChanged();

            }
        };

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
