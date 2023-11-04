package com.amazing.android.autopompomme.ranking;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.amazing.android.autopompomme.api.MemberInfo;
import com.amazing.android.autopompomme.databinding.ActivityRankingBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    ActivityRankingBinding binding;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRankingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        initToolbar();
        fetchRanking();
    }

    private void initToolbar() {
        Toolbar toolbar = binding.tbRanking;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void fetchRanking() {
        db.collection("users")
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<MemberInfo> rankingList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            MemberInfo memberInfo = document.toObject(MemberInfo.class);
                            rankingList.add(memberInfo);
                        }

                        if (rankingList.size() == 1) {
                            MemberInfo first = rankingList.get(0);

                            getUserData(first,binding.tvRankingFirstMan,binding.ivRankingFirst,binding.tvRankingFirstP);
                        } else if (rankingList.size() == 2) {
                            MemberInfo first = rankingList.get(0);
                            MemberInfo second = rankingList.get(1);

                            getUserData(first,binding.tvRankingFirstMan,binding.ivRankingFirst,binding.tvRankingFirstP);
                            getUserData(second,binding.tvRankingSecondMan,binding.ivRankingSecond,binding.tvRankingSecondP);
                        } else if (rankingList.size() == 3) {
                            MemberInfo first = rankingList.get(0);
                            MemberInfo second = rankingList.get(1);
                            MemberInfo third = rankingList.get(2);

                            getUserData(first,binding.tvRankingFirstMan,binding.ivRankingFirst,binding.tvRankingFirstP);
                            getUserData(second,binding.tvRankingSecondMan,binding.ivRankingSecond,binding.tvRankingSecondP);
                            getUserData(third,binding.tvRankingThirdMan,binding.ivRankingThird,binding.tvRankingThirdP);
                        }else {
                            List<MemberInfo> restUser = rankingList.subList(3, rankingList.size());
                            Log.d("TEST", "4위 부터 랭킹:" + restUser);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TEST", "랭킹 불러오기 실패");
                    }
                });
    }

    private void getUserData(MemberInfo user, TextView name, ImageView imgUri,TextView scope) {
        name.setText(user.getNickname());
        imgUri.setImageURI(Uri.parse(user.getUserUri()));
        scope.setText(String.valueOf(user.getScore())+ "P");
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