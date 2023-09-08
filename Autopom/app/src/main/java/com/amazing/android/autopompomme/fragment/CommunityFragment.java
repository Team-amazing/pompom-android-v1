package com.amazing.android.autopompomme.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazing.android.autopompomme.databinding.FragmentCommunityBinding;
import com.amazing.android.autopompomme.write.WriteActivity;


public class CommunityFragment extends Fragment {

    FragmentCommunityBinding binding;


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

        return binding.getRoot();
    }
}