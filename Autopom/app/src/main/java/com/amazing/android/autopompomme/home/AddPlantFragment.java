package com.amazing.android.autopompomme.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazing.android.autopompomme.R;
import com.amazing.android.autopompomme.databinding.FragmentAddPlantBinding;
import com.amazing.android.autopompomme.register.RegisterActivity;


public class AddPlantFragment extends Fragment {

    FragmentAddPlantBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding =  FragmentAddPlantBinding.inflate(inflater);

        binding.ibAddPlant.setOnClickListener(v -> moveRegister());
        return binding.getRoot();
    }

    private void moveRegister() {
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        startActivity(intent);
    }
}