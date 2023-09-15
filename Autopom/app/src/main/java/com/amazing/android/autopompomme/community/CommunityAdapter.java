package com.amazing.android.autopompomme.community;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.amazing.android.autopompomme.R;
import com.amazing.android.autopompomme.community.detail.DetailActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {

    private ArrayList<CommunityList> arrayList;
    private ViewGroup parent;
    private int likeNum;
    private int likeN;


    public CommunityAdapter(ArrayList<CommunityList> arrayList) {
        this.arrayList = arrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profileImg;
        TextView profileName;
        TextView date;
        TextView title;
        ViewPager2 pager;
        TextView detail;
        TextView tvLike;
        TextView comment;
        AppCompatButton btnNoLike;
        AppCompatButton btnLike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImg = itemView.findViewById(R.id.iv_community);
            profileName = itemView.findViewById(R.id.tv_community_userId);
            date = itemView.findViewById(R.id.tv_community_date);
            title = itemView.findViewById(R.id.tv_community_title);
            pager = itemView.findViewById(R.id.vp_community);
            detail = itemView.findViewById(R.id.tv_community_detail);
            tvLike = itemView.findViewById(R.id.tv_community_like);
            comment = itemView.findViewById(R.id.tv_community_comment);
            btnNoLike = itemView.findViewById(R.id.btn_community_defaultLike);
            btnLike = itemView.findViewById(R.id.btn_community_like);
        }
    }


    @NonNull
    @Override
    public CommunityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item,parent,false);
        view.setOnClickListener(v -> {
            //상세 화면 가기
        });
        this.parent = parent;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityAdapter.ViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProfile())
                .into(holder.profileImg);
        holder.profileName.setText(arrayList.get(position).getProfileName());
        holder.date.setText(arrayList.get(position).getDate());
        holder.title.setText(arrayList.get(position).getTitle());
        holder.detail.setText(arrayList.get(position).getContent());
        holder.tvLike.setText(arrayList.get(position).getLikeNum()+"명이 하트를 보냈어요");
        holder.comment.setText("댓글 "+arrayList.get(position).getCommentNum()+"개");
        likeNum = arrayList.get(position).getLikeNum();

        holder.btnNoLike.setOnClickListener( v -> {
            holder.btnNoLike.setVisibility(View.GONE);
            holder.btnLike.setVisibility(View.VISIBLE);
            likeNum += 1;
            holder.tvLike.setText(likeNum +"이 하트를 보냈어요");
        });

        holder.btnLike.setOnClickListener( v -> {
            holder.btnNoLike.setVisibility(View.VISIBLE);
            holder.btnLike.setVisibility(View.GONE);
            likeNum -= 1;
            holder.tvLike.setText(likeNum+"이 하트를 보냈어요");

        });

        List<String> imgUri = arrayList.get(position).getPostUri();
        //RecyclerViewItem item = recyclerViewItems.get(position);
        Log.d("TEST","s"+imgUri);
        PostImgAdapter adapter = new PostImgAdapter(imgUri);
        holder.pager.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return arrayList != null ? arrayList.size() : 0;
    }
}
