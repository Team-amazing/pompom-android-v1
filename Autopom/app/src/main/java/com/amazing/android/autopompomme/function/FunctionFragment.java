package com.amazing.android.autopompomme.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

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

        dbRef = FirebaseDatabase.getInstance().getReference();
        initTemp();
        linkBlueTooth();
        initControl();

        return binding.getRoot();
    }

    private void linkBlueTooth() {
        binding.ivFunctionBlueTooth.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), LinkingActivity.class);
            startActivity(intent);
        });
    }

    private void initTemp() {
        dbRef.child("hardware/data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getValue();
                Log.d("TEST","dd"+snapshot);
                Data data = snapshot.getValue(Data.class);
                Log.d("TEST","a"+data.getHum());
                binding.tvFunctionTmp.setText(data.getTem()+"");
                binding.pbFunctionHum.setProgress(data.getHum());
                binding.tvFunctionHum.setText(data.getHum()+"%");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initControl() {
        //dbRef.child("hardware/control").setValue();

        binding.switchFunctionWater.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    dbRef.child("hardware/control").child("water").setValue(1);
                }else {
                    dbRef.child("hardware/control").child("water").setValue(0);
                }
            }
        });

        binding.switchFunctionLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    dbRef.child("hardware/control").child("light").setValue(1);
                }else {
                    dbRef.child("hardware/control").child("light").setValue(0);
                }
            }
        });
    }
}