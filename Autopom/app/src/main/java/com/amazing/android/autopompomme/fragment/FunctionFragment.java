package com.amazing.android.autopompomme.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazing.android.autopompomme.R;
import com.amazing.android.autopompomme.databinding.FragmentFunctionBinding;
import com.amazing.android.autopompomme.linking.LinkingActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FunctionFragment extends Fragment {

    FragmentFunctionBinding binding;
    DatabaseReference dbRef;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFunctionBinding.inflate(getLayoutInflater());

        dbRef = FirebaseDatabase.getInstance().getReference("Temp/hum");
        initTemp();
        linkBlueTooth();

        return binding.getRoot();
    }

    private void linkBlueTooth() {
        binding.ivFunctionBlueTooth.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), LinkingActivity.class);
            startActivity(intent);
        });
    }

    private void initTemp() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}