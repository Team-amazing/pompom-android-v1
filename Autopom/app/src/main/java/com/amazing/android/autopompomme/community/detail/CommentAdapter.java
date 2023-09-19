package com.amazing.android.autopompomme.community.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amazing.android.autopompomme.R;
import com.amazing.android.autopompomme.community.CommunityAdapter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> comments;
    static class ViewHolder extends RecyclerView.ViewHolder{

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
            //imageView.setImageURI(comment.);
        }

        public void setName(Comment comment) {
            name.setText(comment.getUserId());
        }

        public void setTime(Comment comment) {
            //time.setText(comment.getTimestamp());
        }

        public void setComment(Comment commentList) {
            comment.setText(commentList.getComment());
        }
    }

    public CommentAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.setName(comment);
        holder.setComment(comment);

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
