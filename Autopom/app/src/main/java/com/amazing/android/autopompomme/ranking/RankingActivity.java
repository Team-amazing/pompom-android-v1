package com.amazing.android.autopompomme.ranking;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

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
                            binding.tvRankingFirstMan.setText(first.getNickname());
                            binding.ivRankingFirst.setImageURI(Uri.parse(first.getUserUri()));
                            binding.tvRankingFirstP.setText(String.valueOf(first.getScore()) + "P");
                        }
                        if (rankingList.size() == 2) {
                            MemberInfo second = rankingList.get(1);
                            binding.tvRankingSecondMan.setText(second.getNickname());
                            binding.ivRankingSecond.setImageURI(Uri.parse(second.getUserUri()));
                            binding.tvRankingSecondP.setText(String.valueOf(second.getScore()) + "P");
                        }
                        if (rankingList.size() == 3) {
                            MemberInfo third = rankingList.get(2);
                            binding.tvRankingThirdMan.setText(third.getNickname());
                            binding.ivRankingThird.setImageURI(Uri.parse(third.getUserUri()));
                            binding.tvRankingThirdP.setText(String.valueOf(third.getScore()) + "P");
                        }
                        if (rankingList.size() > 3) {
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