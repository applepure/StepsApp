package com.simpler.myapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.simpler.myapplication.R;
import com.simpler.myapplication.model.JsonCommentsModel;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder>  {
    private final List<JsonCommentsModel> feedItemList;
    private Context mContext;


    public RecyclerViewAdapter(Context context, List<JsonCommentsModel> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.raw_recycler_comments, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, final int i) {
        final JsonCommentsModel feedItem = feedItemList.get(i);
        customViewHolder.rawHolder.setTag(i);
        customViewHolder.name.setText(feedItem.getName());
        customViewHolder.commentId.setText(feedItem.getId().toString());
        customViewHolder.body.setText(feedItem.getBody());
        customViewHolder.email.setText(feedItem.getEmail());


//        customViewHolder.name.setOnClickListener(v -> {
//            int position = (int)v.getTag();
//
//        });
//        customViewHolder.body.setOnClickListener(v -> {
//            int position = (int)v.getTag();
//
//        });
//
//        customViewHolder.rawHolder.setOnClickListener(v -> {
//            int position = (int)v.getTag();
//
//        });

    }


    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView commentId;
        protected TextView name;
        protected TextView body;
        protected TextView email;
        protected CardView cardView1;

        LinearLayout rawHolder;


        public CustomViewHolder(View view) {
            super(view);
            this.rawHolder = view.findViewById(R.id.raw_holder);
            this.commentId = view.findViewById(R.id.comment_id);
            this.body = view.findViewById(R.id.body);
            this.name = view.findViewById(R.id.name);
            this.email = view.findViewById(R.id.email);

            this.cardView1 = view.findViewById(R.id.cardView1);


        }
    }

}