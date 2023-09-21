package com.amazing.android.autopompomme.community.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.amazing.android.autopompomme.R;
import com.amazing.android.autopompomme.community.CommunityAdapter;
import com.amazing.android.autopompomme.databinding.ActivityDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;
    private RecyclerView rvPostImg;
    private RecyclerView rvComment;
    private DatabaseReference db;
    private String postId;
    private String profileName;
    private List<Comment> commentList;
    private CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.tbDetail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        db = FirebaseDatabase.getInstance().getReference();


        Intent intent = getIntent();

        String title = intent.getExtras().getString("title");
        //Uri profileImg = Uri.parse(intent.getExtras().getString("profileImg"));
        profileName = intent.getExtras().getString("profileName");
        String time = intent.getExtras().getString("time");
        String content = intent.getExtras().getString("content");
        //int likeNum = Integer.parseInt(intent.getExtras().getString("likeNum"));
        //int commentNum = Integer.parseInt(intent.getExtras().getString("commentNum"));
        List<String> postImg = intent.getStringArrayListExtra("postImg");
        postId = intent.getExtras().getString("postId");

        binding.tvDetailTitle.setText(title);
        //binding.ivDetailProfile.setImageURI(profileImg);
        binding.tvDetailName.setText(profileName);
        binding.tvDetailTime.setText(time);
        binding.tvDetailContent.setText(content);

        rvPostImg = binding.rvDetailImg;
        rvPostImg.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        DetailAdapter detailAdapter = new DetailAdapter(postImg);
        rvPostImg.setAdapter(detailAdapter);

        rvComment = binding.rvDetailComment;
        rvComment.setLayoutManager(new LinearLayoutManager(this));


        readComment();
        //writeComment();
        binding.ibDetailSend.setOnClickListener( v -> writeComment());

        commentList = new ArrayList<>();
        adapter = new CommentAdapter(commentList);
        rvComment.setAdapter(adapter);

    }

    private void writeComment() {
        String commentId = db.child("comments").child(postId).push().getKey();

        String commentText = binding.etDetailComment.getText().toString();

        //Comment comment = new Comment(profileName,commentText,getTime());
        Comment comment = new Comment();
        db.child("comments").child(postId).child(commentId).setValue(comment);

        Log.d("TEST","댓글 작성");
    }
    private String getTime(){
        long now = System.currentTimeMillis();
        Date date= new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return sdf.format(date);
    }

    private void readComment() {
        Log.d("TEST","postId"+postId);

        db.child("comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot commentSnapshot: snapshot.getChildren()) {
                    Comment comment = commentSnapshot.getValue(Comment.class);
                    Log.d("TEST","c"+comment);

                    commentList.add(comment);
                }
                Log.d("TEST","list"+commentList);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}