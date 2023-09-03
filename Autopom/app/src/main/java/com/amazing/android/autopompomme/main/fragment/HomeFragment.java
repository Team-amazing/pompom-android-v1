package com.amazing.android.autopompomme.main.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazing.android.autopompomme.R;
import com.amazing.android.autopompomme.databinding.FragmentHomeBinding;
import com.amazing.android.autopompomme.main.home.MyPlantAdapter;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    private MyPlantAdapter plantAdapter;
    private ViewPager2 viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater);

        viewPager = binding.vpHome;
        plantAdapter = new MyPlantAdapter(this);

        int pagerWidth = getResources().getDimensionPixelOffset(R.dimen.pagerWidth);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int pagerPadding = (int) ((screenWidth - pagerWidth) * 0.5);
        int offsetPx = (int) ((screenWidth - pagerWidth)* 0.25);
        float scaleFactor = 0.8f;
        float alphaFactor = 0.5f;

        viewPager.setPadding(pagerPadding, 0, pagerPadding, 0);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);

        viewPager.setOffscreenPageLimit(1);
        viewPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                viewPager.setTranslationX(position * offsetPx);

                int currentPagePosition = viewPager.getCurrentItem();
                Log.d("TEST","pos"+currentPagePosition);
                Log.d("TEST","p"+position);

                float scaleFactorAbs = Math.abs(scaleFactor - Math.abs(position));
                float alpha = Math.max(alphaFactor, scaleFactorAbs);

                if (position == currentPagePosition - 1 || position == currentPagePosition + 1) {
                    //page.setVisibility(View.INVISIBLE);


                    page.setScaleX(1.0f);
                    page.setScaleY(1.0f);
                    page.setAlpha(1);

                } else {
                    page.setVisibility(View.VISIBLE);


                    page.setScaleX(scaleFactor);
                    page.setScaleY(scaleFactor);
                    page.setAlpha(alpha);


                }
            }
        });

        viewPager.setAdapter(plantAdapter);

        return binding.getRoot();
    }
}