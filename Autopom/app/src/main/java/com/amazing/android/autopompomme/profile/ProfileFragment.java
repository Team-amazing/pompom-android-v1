package com.amazing.android.autopompomme.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazing.android.autopompomme.activity.SettingActivity;
import com.amazing.android.autopompomme.databinding.FragmentProfileBinding;
import com.amazing.android.autopompomme.ranking.RankingActivity;
import com.amazing.android.autopompomme.search.SearchActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    private TabLayout tableLayout;
    private ViewPager2 viewPager;
    private ProfileAdapter adapter;
    private FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(getLayoutInflater());




        //adapter.addFragment(new MyUploadFragment());
        //adapter.addFragment(new MyLikeFragment());

        setData();
        setPager();
        setTabLayout();
        setBtn();

        return binding.getRoot();
    }

    private void setBtn() {
        binding.ibProfileSetting.setOnClickListener( v-> {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        });

        binding.btnProfileRanking.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RankingActivity.class);
            startActivity(intent);
        });

        binding.btnProfilePlantBook.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });
    }

    private void setTabLayout() {
        tableLayout = binding.tlProfile;

        new TabLayoutMediator(tableLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                Log.d("TEST","p"+position);
                if(position == 0) {
                    tab.setText("내 업로드");
                }else {
                    tab.setText("내 팜팜이");
                }
            }
        }).attach();
    }

    private void setPager() {
        viewPager = binding.vpProfile;

        //adapter= new ProfileAdapter(getActivity().getSupportFragmentManager(),getLifecycle());
        adapter = new ProfileAdapter(this);
        adapter.addFragment(new MyUploadFragment());
        adapter.addFragment(new MyPamFragment());

        viewPager.setSaveEnabled(false);
        viewPager.setAdapter(adapter);
    }

    private void setData() {
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            String nickName =user.getDisplayName();
            String email =user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            binding.tvProfileUserName.setText(nickName);
            binding.tvProfileEmail.setText(email);
            binding.ivProfileProfile.setImageURI(photoUrl);

        }

    }
}