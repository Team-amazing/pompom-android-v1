package com.amazing.android.autopompomme.search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.amazing.android.autopompomme.databinding.ActivitySearchBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    static final int REQUEST_CODE = 1;
    ActivitySearchBinding binding;
    Bitmap bitmap;
    InputImage image;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initToolbar();
        ObjectDetectorOptions options =
                new ObjectDetectorOptions.Builder()
                        .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE) //STREAM_MODE
                        .enableClassification()  // Optional
                        .build();

        ObjectDetector objectDetector = ObjectDetection.getClient(options);

        binding.ibSearchFold.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent, REQUEST_CODE);
        });

        binding.fbSearch.setOnClickListener(view -> {

            // 이미지에서 객체를 인식합니다.
            objectDetector.process(image)
                    .addOnSuccessListener(
                            new OnSuccessListener<List<DetectedObject>>() {
                                @Override
                                public void onSuccess(List<DetectedObject> detectedObjects) {
                                    // 인식된 객체를 로그로 출력합니다.
                                    List<Rect> boxes = new ArrayList<>();
                                    for (DetectedObject detectedObject : detectedObjects) {
                                        Rect rect = detectedObject.getBoundingBox();
                                        Integer id = detectedObject.getTrackingId();
                                        boxes.add(detectedObject.getBoundingBox());

                                        if (detectedObject.getLabels().isEmpty()) {
                                            binding.tvSearch.setText("검색 결과 없음");
                                        }
                                        BoundingBoxImageView imageView = binding.bbivSearch;
                                        imageView.setBoxes(boxes);

                                        for (DetectedObject.Label label : detectedObject.getLabels()) {
                                            String text = label.getText();
                                            int index = label.getIndex();
                                            float confidence = label.getConfidence();
                                            binding.tvSearch.setText(text);
                                        }

                                    }
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Task failed with an exception
                                    // ...
                                    Log.d("TEST", "e" + e);
                                }
                            });
        });
    }

    private void initToolbar() {
        Toolbar toolbar = binding.tbSearch;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            // 갤러리에서 선택한 사진에 대한 uri를 가져온다.
            uri = data.getData();
            setImage(uri);
        }
    }

    // uri를 비트맵으로 변환시킨후 이미지뷰에 띄워주고 InputImage를 생성하는 메서드
    private void setImage(Uri uri) {
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(in);
            binding.ivSearch.setImageBitmap(bitmap);

            image = InputImage.fromBitmap(bitmap, 0);
            Log.e("setImage", "이미지 to 비트맵");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}