package com.amazing.android.autopompomme.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.amazing.android.autopompomme.R;
import com.amazing.android.autopompomme.databinding.ActivityMainBinding;
import com.amazing.android.autopompomme.community.CommunityFragment;
import com.amazing.android.autopompomme.fragment.FunctionFragment;
import com.amazing.android.autopompomme.home.HomeFragment;
import com.amazing.android.autopompomme.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final HomeFragment homeFragment = new HomeFragment();
    private final FunctionFragment functionFragment = new FunctionFragment();
    private final CommunityFragment communityFragment = new CommunityFragment();
    private final ProfileFragment profileFragment = new ProfileFragment();
    ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private Map<Integer, Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();
        fragments = new HashMap<>();

        initializeFragments();

        initBottomNavigation();

    }



    private void initializeFragments() {
        fragments.put(R.id.home, homeFragment);
        fragments.put(R.id.function, functionFragment);
        fragments.put(R.id.community, communityFragment);
        fragments.put(R.id.profile, profileFragment);
    }

    private void initBottomNavigation() {
        BottomNavigationView bottomNavigationView = binding.bottomNav;

        fragmentManager.beginTransaction().replace(R.id.containers, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = fragments.get(item.getItemId());

            if (fragment != null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();


                transaction.replace(R.id.containers, fragment)
                        .commit();
            /*if (fragment != null) {
                FragmentManager childFragmentManager = fragment.getChildFragmentManager();

                if(fragment.isAdded()){
                    FragmentTransaction transaction = childFragmentManager.beginTransaction();
                    transaction.replace(R.id.containers, fragment)
                            .commit();
                }


                /*if (fragment instanceof HomeFragment) {
                    fragment.getChildFragmentManager().beginTransaction().replace(R.id.containers, fragment)
                            //.addToBackStack(null)
                            .commit();
                } else {
                    transaction.replace(R.id.containers, fragment)
                            .commit();
                }*/
                return true;
            }
            return false;
        });
    }
}