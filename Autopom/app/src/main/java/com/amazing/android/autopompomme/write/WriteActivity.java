package com.amazing.android.autopompomme.write;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.amazing.android.autopompomme.databinding.ActivityWriteBinding;
import com.amazing.android.autopompomme.write.adapter.WriteAdapter;
import com.amazing.android.autopompomme.write.presenter.WriteContract;
import com.amazing.android.autopompomme.write.presenter.WritePresenter;

import java.util.ArrayList;
import java.util.List;

public class WriteActivity extends AppCompatActivity implements WriteContract.View {

    ActivityWriteBinding binding;
    private static final int GALLERY_REQUEST_CODE = 1;
    private RecyclerView recyclerView;
    private List<String> imgUrls = new ArrayList<>();

    private int imgCount =0;

    private Uri uri;

    EditText title,detail;

    private WriteContract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.tbWrite;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        presenter = new WritePresenter(this);
        String etTitle = binding.etWriteTitle.getText().toString();
        String etDetail = binding.etWriteDetail.getText().toString();

        title = binding.etWriteTitle;
        detail = binding.etWriteDetail;

        recyclerView = binding.rvWrite;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        Log.d("TEST","img"+imgUrls);


        binding.btnWritePicture.setOnClickListener(v -> {
            if(imgCount <5) {
                openGallery();
            }
        });

        binding.tvWriteComplete.setOnClickListener( v -> presenter.write(title.getText().toString(),detail.getText().toString(),uri));



        checkTextInput(etTitle, etDetail);
    }



    private void checkTextInput(String titles, String details) {
        TextWatcher textWatcher =  new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("TEST","before");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("TEST","uri/"+uri);

                checkInside();
            }

            @Override
            public void afterTextChanged(Editable s) {
                int leng = binding.tvWriteCount.length();
                binding.tvWriteCount.setText(detail.length()+"/500");
            }


        };
        title.addTextChangedListener(textWatcher);
        detail.addTextChangedListener(textWatcher);

    }

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("TEST","e"+data.getData());


        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            Log.d("TEST","uri"+uri);
            imgUrls.add(data.getData().toString());
            WriteAdapter writeAdapter= new WriteAdapter(this,imgUrls);
            recyclerView.setAdapter(writeAdapter);
            //ImageView itemImageView = findViewById(R.id.iv_write_img);
            //itemImageView.setClipToOutline(true);

            imgCount++;
            binding.btnWritePicture.setText(imgCount+"/5");
            String[] filePathColum = {MediaStore.Images.Media.DATA};

            checkInside();

            Cursor cursor = getContentResolver().query(uri,filePathColum,null,null,null);
            cursor.moveToFirst();

            //int columnIndex = cursor.getColumnIndex(filePathColum[0]);
            cursor.close();


        }
    }

    private void checkInside() {
        if(title.length() == 0 || detail.length() == 0 || uri == null){
            binding.tvWriteComplete.setClickable(false);
            binding.tvWriteComplete.setTextColor(Color.parseColor("#D9D9D9"));
        }else {
            binding.tvWriteComplete.setClickable(true);
            binding.tvWriteComplete.setTextColor(Color.parseColor("#28DF99"));
            //binding.tvWriteComplete.setOnClickListener(v -> presenter.write(title, detail));
        }
    }
}