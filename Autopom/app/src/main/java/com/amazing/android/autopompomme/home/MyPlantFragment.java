package com.amazing.android.autopompomme.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.amazing.android.autopompomme.databinding.FragmentMyPlantBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


public class MyPlantFragment extends Fragment {

    FragmentMyPlantBinding binding;
    int plantState;

    MyPlantList arrayList;

    public MyPlantFragment() {}
    public MyPlantFragment(MyPlantList arrayList) {
        this.arrayList = arrayList;
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
        setProgress();
        setPlantImg();

        return binding.getRoot();
    }

    private void setPlantImg() {
        switch ((int) plantState / 10) {
            case 0:
            case 1:
                binding.ivMyPlantPot1.setVisibility(View.VISIBLE);
                break;
            case 2:
            case 3:
                binding.ivMyPlantPot2.setVisibility(View.VISIBLE);
                break;
            case 4:
            case 5:
                binding.ivMyPlantPot3.setVisibility(View.VISIBLE);
                break;
            case 6:
            case 7:
                binding.ivMyPlantPot4.setVisibility(View.VISIBLE);
                break;
            case 8:
            case 9:
                binding.ivMyPlantPot5.setVisibility(View.VISIBLE);
                break;
            case 10:
                binding.ivMyPlantPot6.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setProgress() {
        int plantLife = 30;
        plantState = 100 / plantLife * Integer.parseInt(getDate());
        binding.pbMyPlantState.setProgress(plantState);
        binding.tvMyPlantState.setText(plantState + "%");
    }

    private void setData() {
        binding.tvMyPlantName.setText(arrayList.getPlantNickName());
        binding.tvMyPlantDate.setText("+" + getDate());
    }

    private String getDate() {
        String plantBirth = arrayList.getPlantBirth();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate = LocalDate.parse(plantBirth, formatter);

        LocalDate today = LocalDate.now();

        return String.valueOf(ChronoUnit.DAYS.between(localDate, today) + 1);
    }
}