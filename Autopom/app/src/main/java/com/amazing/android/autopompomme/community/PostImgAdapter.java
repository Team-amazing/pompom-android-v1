package com.amazing.android.autopompomme.community;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amazing.android.autopompomme.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class PostImgAdapter extends RecyclerView.Adapter<PostImgAdapter.ViewHolder> {
    private final List<String> items;

    public PostImgAdapter(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public PostImgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_img_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostImgAdapter.ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(items.get(position))
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.iv_community_post_img);
        }
    }
}
