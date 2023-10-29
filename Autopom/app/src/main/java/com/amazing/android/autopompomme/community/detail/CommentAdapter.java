package com.amazing.android.autopompomme.community.detail;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amazing.android.autopompomme.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private final List<Comment> comments;

    public CommentAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.setName(comment);
        holder.setComment(comment);
        holder.setImageView(comment);
        holder.setTime(comment);

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView name;
        TextView time;
        TextView comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_comment_profile);
            name = itemView.findViewById(R.id.tv_comment_name);
            time = itemView.findViewById(R.id.tv_comment_time);
            comment = itemView.findViewById(R.id.tv_comment_comment);
        }

        public void setImageView(Comment comment) {
            imageView.setImageURI(Uri.parse(comment.getProfileUri()));
        }

        public void setName(Comment comment) {
            name.setText(comment.getUserId());
        }

        public void setTime(Comment comment) {
            time.setText(formatTimeString(comment.getTimestamp()));
        }

        public void setComment(Comment commentList) {
            comment.setText(commentList.getComment());
        }
    }

    private static String formatTimeString(String regTime) {

        int SEC = 60;
        int MIN = 60;
        int HOUR = 24;
        int DAY = 30;
        int MONTH = 12;

        long curTime = System.currentTimeMillis();
        long diffTime = (curTime - Long.parseLong(regTime)) / 1000;
        String msg = null;
        if (diffTime < SEC) {
            msg = "방금 전";
        } else if ((diffTime /= SEC) < MIN) {
            msg = diffTime + "분 전";
        } else if ((diffTime /= MIN) < HOUR) {
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= HOUR) < DAY) {
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= DAY) < MONTH) {
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }
        return msg;
    }
}
