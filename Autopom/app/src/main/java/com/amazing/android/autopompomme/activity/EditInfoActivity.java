package com.amazing.android.autopompomme.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.amazing.android.autopompomme.R;
import com.amazing.android.autopompomme.databinding.ActivityEditInfoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class EditInfoActivity extends AppCompatActivity {

    ActivityEditInfoBinding binding;
    private FirebaseUser user;
    private static final int GALLERY_REQUEST_CODE = 1;

    private String baseNickName;
    private String baseEmail;
    private Uri baseProfileUri;
    private Uri profileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setToolbar();
        setBaseData();
        editImg();
        upDateProfile();

        Log.d("TEST",binding.etEditInfoNickName.getText().toString());
    }

    private void upDateProfile() {
        binding.tvEditInfoComplete.setOnClickListener(v -> {
            if(!Objects.equals(baseNickName, binding.etEditInfoNickName.getText().toString()) ||
                    !Objects.equals(baseEmail, binding.etEditInfoEmail.getText().toString()) || baseProfileUri != profileUri) {

                //변경사항 있음
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(binding.etEditInfoNickName.getText().toString())
                        .setPhotoUri(profileUri)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("TEST","닉네임 사진 변경 성공");
                                }
                            }
                        });

                user.updateEmail(binding.etEditInfoEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("TEST","이메일 변경 성공");
                                }
                            }
                        });

                Toast.makeText(getBaseContext(),"정보가 수정되었습니다",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getBaseContext(),"변경 사항이 없습니다",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBaseData() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            baseNickName = user.getDisplayName();
            baseEmail = user.getEmail();
            baseProfileUri = user.getPhotoUrl();
            profileUri = user.getPhotoUrl();

            binding.etEditInfoNickName.setText(baseNickName);
            binding.etEditInfoEmail.setText(baseEmail);
            binding.ivEditInfoProfile.setImageURI(baseProfileUri);
        }
    }

    private void editImg() {
        binding.ibEditInfoEdit.setOnClickListener( v -> openGallery());
    }

    public void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            profileUri = data.getData();
            String[] filePathColum = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(profileUri,filePathColum,null,null,null);
            cursor.moveToFirst();

            cursor.close();

            binding.ivEditInfoProfile.setImageURI(profileUri);
        }
    }

    private void setToolbar() {
        Toolbar toolbar = binding.tbEditInfo;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
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