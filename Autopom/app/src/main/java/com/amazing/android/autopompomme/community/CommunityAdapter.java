package com.amazing.android.autopompomme.community;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.amazing.android.autopompomme.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {

    private ArrayList<CommunityList> arrayList;
    private List<RecyclerViewItem> recyclerViewItems;
    private Context context;

    public CommunityAdapter(ArrayList<CommunityList> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
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
            btnLike = itemView.findViewById(R.id.btn_community_like);
        }
    }


    @NonNull
    @Override
    public CommunityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item,parent,false);
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

        List<String> imgUri = arrayList.get(position).getPostUri();
        //RecyclerViewItem item = recyclerViewItems.get(position);
        Log.d("TEST","s"+imgUri);
        PostImgAdapter adapter = new PostImgAdapter(context, imgUri);
        holder.pager.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return arrayList != null ? arrayList.size() : 0;
    }
}
