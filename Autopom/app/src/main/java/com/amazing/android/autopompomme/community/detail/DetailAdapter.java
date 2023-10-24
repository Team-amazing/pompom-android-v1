package com.amazing.android.autopompomme.community.detail;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amazing.android.autopompomme.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private final List<String> imgUrls;

    public DetailAdapter(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    @NonNull
    @Override
    public DetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_img_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailAdapter.ViewHolder holder, int position) {
        Uri imgUrl = Uri.parse(imgUrls.get(position));

        Glide.with(holder.imageView.getContext())
                .load(imgUrl)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imgUrls.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_detail_img);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
