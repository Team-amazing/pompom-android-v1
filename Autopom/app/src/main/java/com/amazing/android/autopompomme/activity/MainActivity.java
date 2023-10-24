package com.amazing.android.autopompomme.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.amazing.android.autopompomme.R;
import com.amazing.android.autopompomme.community.CommunityFragment;
import com.amazing.android.autopompomme.databinding.ActivityMainBinding;
import com.amazing.android.autopompomme.fragment.FunctionFragment;
import com.amazing.android.autopompomme.home.HomeFragment;
import com.amazing.android.autopompomme.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final HomeFragment homeFragment = new HomeFragment();
    private final FunctionFragment functionFragment = new FunctionFragment();
    private final CommunityFragment communityFragment = new CommunityFragment();
    private final ProfileFragment profileFragment = new ProfileFragment();
    public static Context context;
    ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private Map<Integer, Fragment> fragments;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = getApplicationContext();

        fragmentManager = getSupportFragmentManager();
        fragments = new HashMap<>();

        initializeFragments();
        initBottomNavigation();

        userData();
    }

    private void userData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userId", userId);
            editor.apply();
        }
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
                return true;
            }
            return false;
        });
    }
}