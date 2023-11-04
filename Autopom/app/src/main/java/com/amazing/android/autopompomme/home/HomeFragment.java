package com.amazing.android.autopompomme.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.amazing.android.autopompomme.R;
import com.amazing.android.autopompomme.activity.MainActivity;
import com.amazing.android.autopompomme.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    FirebaseFirestore db;
    private MyPlantAdapter plantAdapter;
    private ViewPager2 viewPager;
    private ArrayList<MyPlantList> arrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        arrayList = new ArrayList<>();

        viewPager = binding.vpHome;

        int pagerWidth = getResources().getDimensionPixelOffset(R.dimen.pagerWidth);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int pagerPadding = (int) ((screenWidth - pagerWidth) * 0.5);
        float scaleFactor = 0.8f;

        viewPager.setPadding(pagerPadding, 0, pagerPadding, 0);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);

        viewPager.setSaveEnabled(false);

        viewPager.setOffscreenPageLimit(3); //1

        CompositePageTransformer transform = new CompositePageTransformer();
        transform.addTransformer(new MarginPageTransformer(8));

        transform.addTransformer((vieww, fl) -> {
            float v = 1 - Math.abs(fl);
            vieww.setScaleY(scaleFactor);
        });

        viewPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                if (position == 0) {
                    page.setScaleY(1.0f);
                    page.setScaleX(1.0f);
                } else {
                    page.setScaleX(scaleFactor);
                    page.setScaleY(scaleFactor);
                }
            }

        });

        SharedPreferences data = MainActivity.context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userId = data.getString("userId", "default value");

        plantAdapter = new MyPlantAdapter(getChildFragmentManager(), getLifecycle(), new ArrayList<>());
        viewPager.setAdapter(plantAdapter);

        db.collection("register")
                .whereEqualTo("uid", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            arrayList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TEST", document.getId() + " => " + document.getData());
                                MyPlantList data = document.toObject(MyPlantList.class);
                                arrayList.add(data);
                            }
                            plantAdapter.setMyPlantList(arrayList);
                            plantAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "정보를 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}