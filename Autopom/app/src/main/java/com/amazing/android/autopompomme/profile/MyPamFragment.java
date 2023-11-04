package com.amazing.android.autopompomme.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amazing.android.autopompomme.R;
import com.amazing.android.autopompomme.activity.MainActivity;
import com.amazing.android.autopompomme.databinding.FragmentMyPamBinding;
import com.amazing.android.autopompomme.home.MyPlantList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class MyPamFragment extends Fragment {

    FragmentMyPamBinding binding;
    FirebaseFirestore db;
    private ArrayList<MyPamList> arrayList;
    private FirebaseUser user;
    private String userId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyPamBinding.inflate(getLayoutInflater());

        db = FirebaseFirestore.getInstance();
        //user = FirebaseAuth.getInstance().getCurrentUser();

        //if(user != null) {
          //  userId = user.getUid();
        //}

        SharedPreferences data = MainActivity.context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        userId = data.getString("userId", "default value");

        arrayList = new ArrayList<>();

        RecyclerView recyclerView = binding.rvMyPam;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db.collection("register")
                .whereEqualTo("uid", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            arrayList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TEST", document.getId() + " => " + document.getData());
                                MyPamList data = document.toObject(MyPamList.class);
                                arrayList.add(data);
                            }
                            MyPamAdapter myPamAdapter = new MyPamAdapter(arrayList);
                            recyclerView.setAdapter(myPamAdapter);
                        } else {
                            Toast.makeText(getContext(), "정보를 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return binding.getRoot();
    }
}