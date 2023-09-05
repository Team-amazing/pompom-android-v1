package com.amazing.android.autopompomme.main.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.amazing.android.autopompomme.main.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class MyPlantAdapter extends FragmentStateAdapter {

    private final List<Fragment> fragments;

    public MyPlantAdapter(HomeFragment fa) {
        super(fa);

        fragments = new ArrayList<>();
        fragments.add(new AddPlantFragment());
        fragments.add(new MyPlantFragment());
        fragments.add(new MyPlantFragment());
        Log.d("TEST","in");
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
