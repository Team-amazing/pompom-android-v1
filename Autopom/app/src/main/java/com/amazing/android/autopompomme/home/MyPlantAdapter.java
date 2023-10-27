package com.amazing.android.autopompomme.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.amazing.android.autopompomme.home.HomeFragment;
import com.amazing.android.autopompomme.home.AddPlantFragment;
import com.amazing.android.autopompomme.home.MyPlantFragment;

import java.util.ArrayList;
import java.util.List;

public class MyPlantAdapter extends FragmentStateAdapter {

    //private final List<Fragment> fragments;
    private final ArrayList<MyPlantList> arrayList;

    public MyPlantAdapter(HomeFragment fa,ArrayList<MyPlantList> arrayList) {
        super(fa);

        this.arrayList = arrayList;

//        fragments = new ArrayList<>();
//        fragments.add(new AddPlantFragment());
//        fragments.add(new MyPlantFragment());
//        fragments.add(new MyPlantFragment());
        Log.d("TEST","al/"+arrayList);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0) {
            return new AddPlantFragment();
        }else {
            return new MyPlantFragment(arrayList.get(position - 1));
            //return MyPlantFragment.newInstance(position);
        }
        //return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size()+1;
    }
}
