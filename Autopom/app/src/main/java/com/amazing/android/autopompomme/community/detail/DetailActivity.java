package com.amazing.android.autopompomme.community.detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazing.android.autopompomme.databinding.ActivityDetailBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;
    private DatabaseReference dbRef;
    private FirebaseFirestore db;
    private String postId;
    private String userId;
    private String myNickName;
    private Uri myProfileUri;
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

        dbRef = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();

        String title = intent.getExtras().getString("title");
        //Uri profileImg = Uri.parse(intent.getExtras().getString("profileImg"));
        String profileName = intent.getExtras().getString("profileName");
        String time = intent.getExtras().getString("time");
        String content = intent.getExtras().getString("content");
        List<String> postImg = intent.getStringArrayListExtra("postImg");
        postId = intent.getExtras().getString("postId");
        userId = intent.getExtras().getString("userId");

        readComment();
        getUserData();

        binding.tvDetailTitle.setText(title);
        //binding.ivDetailProfile.setImageURI(profileImg);
        binding.tvDetailName.setText(profileName);
        binding.tvDetailTime.setText(time);
        binding.tvDetailContent.setText(content);

        //binding.ivDetailComment.setImageURI(myProfileUri);
        Glide.with(getBaseContext())
                .load(myProfileUri)
                .into(binding.ivDetailComment);

        RecyclerView rvPostImg = binding.rvDetailImg;
        rvPostImg.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        DetailAdapter detailAdapter = new DetailAdapter(postImg);
        rvPostImg.setAdapter(detailAdapter);

        RecyclerView rvComment = binding.rvDetailComment;
        rvComment.setLayoutManager(new LinearLayoutManager(this));

        binding.ibDetailSend.setOnClickListener(v -> writeComment());

        commentList = new ArrayList<>();
        adapter = new CommentAdapter(commentList);
        rvComment.setAdapter(adapter);
    }

    private void getUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            myNickName = user.getDisplayName();
            myProfileUri = user.getPhotoUrl();
        }

        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    Log.d("TEST", "사용자 정보" + documentSnapshot);
                } else {
                    //사용자 정보 불러오기 실패
                }
            }
        });
    }

    private void writeComment() {
        String commentText = binding.etDetailComment.getText().toString();

        Comment comment = new Comment(myNickName, commentText, getTime(), myProfileUri.toString());

        dbRef.child("comments").child(postId).child(userId).setValue(comment);
        updateRankingScore();
        Log.d("TEST", "댓글 작성");

        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        binding.etDetailComment.setText(null);
    }

    private String getTime() {
        long now = System.currentTimeMillis();
        return "" + now;
    }

    private void readComment() {
        dbRef.child("comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot commentSnapshot : snapshot.getChildren()) {
                    Comment comment = commentSnapshot.getValue(Comment.class);
                    commentList.add(comment);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TEST", "데이터 불러오기 실패" + error);
            }
        });
    }

    private void updateRankingScore() {
        db.collection("users").document(userId)
                .update("score", FieldValue.increment(2))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TEST", "점수 업데이트");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TEST", "점수 업데이트 실패:", e);
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