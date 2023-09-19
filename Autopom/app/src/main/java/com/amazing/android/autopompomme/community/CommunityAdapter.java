package com.amazing.android.autopompomme.community;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.amazing.android.autopompomme.R;
import com.amazing.android.autopompomme.activity.MainActivity;
import com.amazing.android.autopompomme.community.detail.DetailActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {

    private ArrayList<CommunityList> arrayList;
    private ViewGroup parent;
    private int likeNum;
    private Timer timer;
    private boolean isLikePending = false;
    private boolean isLike = false;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String postId;
    private String userId;
    private String documentId;

    public CommunityAdapter(ArrayList<CommunityList> arrayList,String userId) {
        this.arrayList = arrayList;
        this.userId = userId;
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
        CardView cardView;
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
            cardView = itemView.findViewById(R.id.cv_community);
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
        postId = arrayList.get(position).getPostId();

        initLike(holder);
        holder.btnNoLike.setOnClickListener( v -> {
            noLike(holder);
        });

        holder.btnLike.setOnClickListener( v -> {
            yseLike(holder);
        });

        List<String> imgUri = arrayList.get(position).getPostUri();
        //RecyclerViewItem item = recyclerViewItems.get(position);
        Log.d("TEST","s"+imgUri);
        PostImgAdapter adapter = new PostImgAdapter(imgUri);
        holder.pager.setAdapter(adapter);

        holder.cardView.setOnClickListener( v -> {

            Intent detailActivity = new Intent(v.getContext(), DetailActivity.class);

            detailActivity.putExtra("title", arrayList.get(position).getTitle()); //제목
            detailActivity.putExtra("profileImg", arrayList.get(position).getProfile());
            detailActivity.putExtra("profileName", arrayList.get(position).getProfileName());
            detailActivity.putExtra("time",arrayList.get(position).getDate());
            detailActivity.putExtra("content",arrayList.get(position).getContent());
            detailActivity.putExtra("likeNum",arrayList.get(position).getLikeNum());
            detailActivity.putExtra("commentNum", arrayList.get(position).getCommentNum());
            detailActivity.putStringArrayListExtra("postImg", (ArrayList<String>) arrayList.get(position).getPostUri());
            detailActivity.putExtra("postId",documentId);

            (v.getContext()).startActivity(detailActivity);
        });
    }

    private void yseLike(ViewHolder holder) {
        holder.btnNoLike.setVisibility(View.VISIBLE);
        holder.btnLike.setVisibility(View.GONE);
        likeNum -= 1;
        holder.tvLike.setText(likeNum+"명이 하트를 보냈어요");
        clickBtn();
        isLike = false;
    }

    private void noLike(ViewHolder holder) {
        holder.btnNoLike.setVisibility(View.GONE);
        holder.btnLike.setVisibility(View.VISIBLE);
        likeNum += 1;
        holder.tvLike.setText(likeNum +"명이 하트를 보냈어요");
        clickBtn();
        isLike = true;
    }

    private void initLike(ViewHolder holder) {
        db.collection("likes")
                .whereEqualTo("userId",userId)
                .whereEqualTo("postId", postId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult().isEmpty()) {
                                //문서가 없음
                                makeSever();
                                Log.d("TEST","문서 없음");
                            }else {
                                for(QueryDocumentSnapshot document : task.getResult()) {
                                    documentId = document.getId();
                                    isLike = Boolean.TRUE.equals(document.getBoolean("liked"));
                                }
                            }
                        }else {
                            Log.d("TEST","문서 불러오기 에러");
                            Log.d("TEST", "Error getting documents: ", task.getException());
                        }
                    }
                });

        if(isLike) {
            //누른거
            holder.btnNoLike.setVisibility(View.GONE);
            holder.btnLike.setVisibility(View.VISIBLE);
            holder.tvLike.setText(likeNum +"명이 하트를 보냈어요");
        }
        else {
            //안누른거
            holder.btnNoLike.setVisibility(View.VISIBLE);
            holder.btnLike.setVisibility(View.GONE);
            holder.tvLike.setText(likeNum+"명이 하트를 보냈어요");
        }
    }

    private void clickBtn() {
        if(isLikePending) {
            return;
        }

        if(timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("TEST","ss");
                upDateLikesToSever();
                isLikePending = false;
            }
        },5000);
        isLikePending = true;
    }

    private void upDateLikesToSever() {

        DocumentReference likeRef = db.collection("likes").document(documentId);

        Log.d("TEST","is"+isLike);
        likeRef
                .update("liked", isLike)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TEST", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TEST","업데이트 에러");
                        Log.w("TEST", "Error updating document", e);
                    }
                });
    }

    private void makeSever(){
        Map<String, Object> like = new HashMap<>();
        like.put("userId",userId);
        like.put("postId",postId);
        like.put("liked",isLike);

        db.collection("likes")
                .add(like)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TEST", "DocumentSnapshot added with ID: " + documentReference.getId());
                        documentId = documentReference.getId();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TEST","문서 생성 에러");
                        Log.w("TEST", "Error adding document", e);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return arrayList != null ? arrayList.size() : 0;
    }
}
