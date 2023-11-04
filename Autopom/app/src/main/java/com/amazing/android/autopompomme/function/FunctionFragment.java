package com.amazing.android.autopompomme.function;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
        initAlarm();

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
                Log.d("TEST", "dd" + snapshot);
                Data data = snapshot.getValue(Data.class);
                Log.d("TEST", "a" + data.getHum());
                binding.tvFunctionTmp.setText(data.getTem() + "");
                binding.pbFunctionHum.setProgress(data.getHum());
                binding.tvFunctionHum.setText(data.getHum() + "%");
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
                if (isChecked) {
                    dbRef.child("hardware/control").child("water").setValue(1);
                    binding.switchFunctionWater.setChecked(true);
                } else {
                    dbRef.child("hardware/control").child("water").setValue(0);
                    binding.switchFunctionWater.setChecked(false);
                }
            }
        });

        binding.switchFunctionLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    dbRef.child("hardware/control").child("light").setValue(1);
                    binding.switchFunctionLight.setChecked(true);
                } else {
                    dbRef.child("hardware/control").child("light").setValue(0);
                    binding.switchFunctionLight.setChecked(false);
                }
            }
        });

        binding.switchFunctionWaterTankAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    enableNotification();
                } else {
                    disableNotification();
                }
            }
        });
    }

    private void enableNotification() {
        Intent intent = new Intent(getContext(), WaterTankService.class);
        requireActivity().startService(intent);
        binding.switchFunctionWaterTankAlarm.setChecked(true);
    }

    private void disableNotification() {
        Intent intent = new Intent(getContext(), WaterTankService.class);
        requireActivity().stopService(intent);
        binding.switchFunctionWaterTankAlarm.setChecked(false);
    }

    private void initAlarm() {
        binding.switchFunctionWaterTankAlarm.setChecked(isNotificationEnabled());
    }

    private boolean isNotificationEnabled() {
        ActivityManager manager = (ActivityManager) requireActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (WaterTankService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}