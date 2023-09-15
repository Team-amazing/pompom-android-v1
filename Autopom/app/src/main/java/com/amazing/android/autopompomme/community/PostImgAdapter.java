package com.amazing.android.autopompomme.community;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.amazing.android.autopompomme.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class PostImgAdapter extends RecyclerView.Adapter<PostImgAdapter.ViewHolder> {
    private List<String> items;
    public PostImgAdapter(List<String> items) {
        this.items = items;
    }

    /*@Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.write_img_item, collection, false);

        ImageView imageView = layout.findViewById(R.id.iv_write_img);

        ViewPagerItem item = viewPagerItems.get(position);

        Glide.with(context).load(item.postUri).into(imageView);

        collection.addView(layout);

        return layout;
    }*/


    @NonNull
    @Override
    public PostImgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_img_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostImgAdapter.ViewHolder holder, int position) {
        //holder.img.setImageURI(Uri.parse(items.get(position)));
        Glide.with(holder.itemView.getContext())
                .load(items.get(position))
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.iv_community_post_img);
        }
    }
}
