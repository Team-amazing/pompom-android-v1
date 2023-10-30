package com.amazing.android.autopompomme.community;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazing.android.autopompomme.activity.MainActivity;
import com.amazing.android.autopompomme.databinding.FragmentCommunityBinding;
import com.amazing.android.autopompomme.write.WriteActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class CommunityFragment extends Fragment {

    FragmentCommunityBinding binding;

    FirebaseFirestore db;

    private ArrayList<CommunityList> arrayList;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCommunityBinding.inflate(inflater);

        binding.btnCommunity.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WriteActivity.class);
            startActivity(intent);
        });

        db = FirebaseFirestore.getInstance();
        arrayList = new ArrayList<>();
        bringData();

        recyclerView = binding.rvCommunity;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        return binding.getRoot();
    }

    private void bringData() {
        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            arrayList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TEST", document.getId() + " => " + document.getData());
                                CommunityList data = document.toObject(CommunityList.class);
                                data.setProfileUri(String.valueOf(document.get("profileUri")));
                                data.setPostId(document.getId());
                                arrayList.add(data);
                            }
                            SharedPreferences data = MainActivity.context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            String userId = data.getString("userId", "default value");

                            CommunityAdapter communityAdapter = new CommunityAdapter(arrayList, userId);
                            recyclerView.setAdapter(communityAdapter);
                        } else {
                            Log.d("TEST", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}