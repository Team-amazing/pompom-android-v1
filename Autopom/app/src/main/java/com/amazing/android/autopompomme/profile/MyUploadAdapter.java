package com.amazing.android.autopompomme.profile;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amazing.android.autopompomme.R;
import com.amazing.android.autopompomme.community.detail.DetailActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyUploadAdapter extends RecyclerView.Adapter<MyUploadAdapter.ViewHolder> {

    private ArrayList<MyUploadList> arrayList;
    private String userId;

    public MyUploadAdapter(ArrayList<MyUploadList> arrayList,String userId) {
        this.arrayList = arrayList;
        this.userId = userId;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_my_upload);
        }
    }

    @NonNull
    @Override
    public MyUploadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_upload_item,parent,false);
        view.setOnClickListener(v -> {

        });

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyUploadAdapter.ViewHolder holder, int position) {
        //holder.imageView.setImageURI(Uri.parse(arrayList.get(position).getPostUri().get(0)));
        Glide.with(holder.itemView)
                .load(Uri.parse(arrayList.get(position).getPostUri().get(0)))
                .into(holder.imageView);

        String postId = arrayList.get(position).getPostId();
        holder.imageView.setOnClickListener(v -> {

            Intent detailActivity = new Intent(v.getContext(), DetailActivity.class);

            detailActivity.putExtra("title", arrayList.get(position).getTitle()); //제목
            detailActivity.putExtra("profileImg", arrayList.get(position).getProfile());
            detailActivity.putExtra("profileName", arrayList.get(position).getProfileName());
            detailActivity.putExtra("time",arrayList.get(position).getDate());
            detailActivity.putExtra("content",arrayList.get(position).getContent());
            detailActivity.putExtra("likeNum",arrayList.get(position).getLikeNum());
            detailActivity.putExtra("commentNum", arrayList.get(position).getCommentNum());
            detailActivity.putStringArrayListExtra("postImg", (ArrayList<String>) arrayList.get(position).getPostUri());
            detailActivity.putExtra("postId",postId);
            detailActivity.putExtra("userId",userId);

            (v.getContext()).startActivity(detailActivity);
        });
    }

    @Override
    public int getItemCount() {
        return arrayList != null ? arrayList.size() : 0;
    }
}
