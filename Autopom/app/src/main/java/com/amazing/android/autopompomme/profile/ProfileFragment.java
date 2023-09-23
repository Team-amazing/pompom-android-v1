package com.amazing.android.autopompomme.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.amazing.android.autopompomme.R;
import com.amazing.android.autopompomme.activity.SettingActivity;
import com.amazing.android.autopompomme.databinding.FragmentProfileBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    private TabLayout tableLayout;
    private ViewPager2 viewPager;
    private ProfileAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(getLayoutInflater());

        binding.ibProfileSetting.setOnClickListener( v-> {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        });


        //adapter.addFragment(new MyUploadFragment());
        //adapter.addFragment(new MyLikeFragment());

        setData();
        setPager();
        setTabLayout();

        return binding.getRoot();
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
                    tab.setText("내 좋아요");
                }
            }
        }).attach();
    }

    private void setPager() {
        viewPager = binding.vpProfile;

        //adapter= new ProfileAdapter(getActivity().getSupportFragmentManager(),getLifecycle());
        adapter = new ProfileAdapter(this);
        adapter.addFragment(new MyUploadFragment());
        adapter.addFragment(new MyLikeFragment());

        viewPager.setAdapter(adapter);
    }

    private void setData() {

    }
}