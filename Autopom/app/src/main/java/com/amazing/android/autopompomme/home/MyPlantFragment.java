package com.amazing.android.autopompomme.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazing.android.autopompomme.R;
import com.amazing.android.autopompomme.databinding.FragmentMyPlantBinding;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;


public class MyPlantFragment extends Fragment {

    FragmentMyPlantBinding binding;
    int page;

    MyPlantList arrayList;
    public MyPlantFragment(MyPlantList arrayList) {
        this.arrayList = arrayList;
        Log.d("TEST","myPlant1/"+arrayList);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMyPlantBinding.inflate(getLayoutInflater());

        getDate();
        setData();
        return binding.getRoot();
    }

    private void setData() {
        binding.tvMyPlantName.setText(arrayList.getPlantNickName());
        binding.tvMyPlantDate.setText("+"+getDate());
    }

    private String getDate() {
        String plantBirth = arrayList.getPlantBirth();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate = LocalDate.parse(plantBirth, formatter);

        LocalDate today = LocalDate.now();

        return String.valueOf(ChronoUnit.DAYS.between(localDate, today)+1);
    }
}