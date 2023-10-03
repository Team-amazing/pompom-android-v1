package com.amazing.android.autopompomme.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazing.android.autopompomme.R;
import com.amazing.android.autopompomme.community.CommunityList;
import com.amazing.android.autopompomme.databinding.FragmentMyUploadBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class MyUploadFragment extends Fragment {

    FragmentMyUploadBinding binding;
    private FirebaseUser user;
    private ArrayList<MyUploadList> arrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMyUploadBinding.inflate(getLayoutInflater());

        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        int numberOfColumns = 3;
        RecyclerView recyclerView = binding.rvMyUpload;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),numberOfColumns));

        arrayList = new ArrayList<>();

        if(user != null) {
            String uid = user.getUid();

            db.collection("posts")
                    .whereEqualTo("uid",uid)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                arrayList.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TEST", document.getId() + " => " + document.getData());
                                    MyUploadList data = document.toObject(MyUploadList.class);
                                    data.setPostId(document.getId());
                                    arrayList.add(data);
                                }

                                MyUploadAdapter myUploadAdapter = new MyUploadAdapter(arrayList,uid);
                                recyclerView.setAdapter(myUploadAdapter);
                            } else {
                                //에러
                            }
                        }
                    });

        }

        return binding.getRoot();
    }
}