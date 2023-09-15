package com.amazing.android.autopompomme.write.adapter;

import android.content.Context;
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

public class WriteAdapter extends RecyclerView.Adapter<WriteAdapter.ViewHolder> {

    private Context context;
    private List<Uri> imgUrls;

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_write_img);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    public WriteAdapter (Context context, List<Uri> imgUrls){
        this.context = context;
        this.imgUrls = imgUrls;
    }
    @NonNull
    @Override
    public WriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.write_img_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WriteAdapter.ViewHolder holder, int position) {
        Uri imgUrl = imgUrls.get(position);

        Glide.with(context)
                .load(imgUrl)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imgUrls.size();
    }
}
