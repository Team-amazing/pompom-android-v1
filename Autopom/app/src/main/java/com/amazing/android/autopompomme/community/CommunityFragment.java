package com.amazing.android.autopompomme.community;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazing.android.autopompomme.activity.MainActivity;
import com.amazing.android.autopompomme.databinding.FragmentCommunityBinding;
import com.amazing.android.autopompomme.write.WriteActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CommunityFragment extends Fragment {

    FragmentCommunityBinding binding;

    FirebaseFirestore db;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
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

        binding.btnCommunity.setOnClickListener(v ->{
            Intent intent = new Intent(getActivity(), WriteActivity.class);
            startActivity(intent);
        });

        db = FirebaseFirestore.getInstance();
        arrayList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("posts");
        bringData();
        //data();

        recyclerView = binding.rvCommunity;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);




        return binding.getRoot();
    }

    private void data() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                arrayList.clear();
                Log.d("TEST","sus");
                for(DataSnapshot snapshot : datasnapshot.getChildren()) {

                }
                Log.d("TEST","list"+arrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                                //Log.d("TEST","ob"+document.toObject(CommunityList.class));
                                CommunityList data = document.toObject(CommunityList.class);
                                Log.d("TEST","da"+document);
                                data.setPostId(document.getId());
                                arrayList.add(data);
                            }

                            Log.d("TEST","list"+arrayList);
                            //List<RecyclerViewItem> items = arrayList;
                            //SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                            //SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs",0);
                            //String userId = sharedPreferences.getString("userId", "default value");

                            SharedPreferences data = MainActivity.context.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
                            String userId = data.getString("userId","default value");

                            CommunityAdapter communityAdapter = new CommunityAdapter(arrayList,userId);
                            recyclerView.setAdapter(communityAdapter);

                        } else {
                            Log.d("TEST", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}