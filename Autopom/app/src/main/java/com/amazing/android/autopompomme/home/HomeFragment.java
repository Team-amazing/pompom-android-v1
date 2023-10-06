package com.amazing.android.autopompomme.home;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazing.android.autopompomme.R;
import com.amazing.android.autopompomme.databinding.FragmentHomeBinding;
import com.amazing.android.autopompomme.adapter.MyPlantAdapter;


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

        viewPager.setSaveEnabled(false);


        //viewPager.setPadding(100,0,100,0);


        viewPager.setOffscreenPageLimit(3); //1

        CompositePageTransformer transform = new CompositePageTransformer();
        transform.addTransformer(new MarginPageTransformer(8));

        transform.addTransformer((view, fl) -> {
            float v = 1 - Math.abs(fl);
            Log.d("TEST","fl"+fl);
            //view.setScaleY(0.8f + v * 0.2f);
            view.setScaleY(0.8f);
        });

        viewPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                int currentPagePosition = viewPager.getCurrentItem();

                //page.setTranslationX(position * offsetPx);              사이조절




//                if(position % 1 == 0.0){
//
//                    page.setScaleY(1.0f);
//                    page.setScaleX(1.0f);
//                }else {
//                    page.setScaleY(scaleFactor);
//                    page.setScaleX(scaleFactor);
//                }
//
//                Log.d("TEST","lePos"+position);
//                Log.d("TEST","p"+currentPagePosition);
//
//                if( position == currentPagePosition-1 || position == currentPagePosition +1){
//
//                    page.setScaleX(0.9f);
//                    page.setScaleY(0.9f);
//                }

//                if((position + (float) currentPagePosition) == 2.0) {
//                    page.setScaleY(1.0f);
//                    page.setScaleX(1.0f);
//                    Log.d("TEST","in");
//                }else {
//                    page.setScaleX(0.8f);
//                    page.setScaleY(0.8f);
//                }

                Log.d("TEST","lePos"+position);
                Log.d("TEST","p"+currentPagePosition);
                Log.d("TEST","sum"+(position + (float) currentPagePosition));


                if(position == 0) {
                    page.setScaleY(1.0f);
                    page.setScaleX(1.0f);
                }else {
                    page.setScaleX(0.8f);
                    page.setScaleY(0.8f);
                }
            }

        });

//        viewPager.setPageTransformer(new ViewPager2.PageTransformer() {
//            @Override
//            public void transformPage(@NonNull View page, float position) {
//                //viewPager.setTranslationX(position * offsetPx);
//
//                int currentPagePosition = viewPager.getCurrentItem();
//                //Log.d("TEST","pos"+currentPagePosition);
//                //Log.d("TEST","p"+position);
//
//                float scaleFactorAbs = Math.abs(scaleFactor - Math.abs(position));
//                float alpha = Math.max(alphaFactor, scaleFactorAbs);
//
//                if(position == 1.0){
//                    page.setVisibility(View.VISIBLE);
//
//
//                    page.setScaleX(scaleFactor);
//                    page.setScaleY(scaleFactor);
//                    page.setAlpha(alpha);
//
//
//                }else {
//                    page.setVisibility(View.VISIBLE);
//
//                    page.setScaleX(1.0f);
//                    page.setScaleY(1.0f);
//                    page.setAlpha(1);
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        int animationDurationMillis = viewPager.getResources().getInteger(android.R.integer.config_mediumAnimTime); // 애니메이션 지속 시간 설정 (여기서는 중간 정도로 설정)
//                        page.animate()
//                                .scaleX(1f)
//                                .scaleY(1f)
//                                .alpha(1f)
//                                .setDuration(animationDurationMillis)
//                                .start();
//                    }
//                }
//                if (position == currentPagePosition - 1 || position == currentPagePosition + 1) {
//                    //page.setVisibility(View.INVISIBLE);
//
//
//
//
//                } else {
//
//
//
//                }
//            }
//        });

        viewPager.setAdapter(plantAdapter);

        return binding.getRoot();
    }
}