package com.amazing.android.autopompomme.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amazing.android.autopompomme.R;
import com.amazing.android.autopompomme.api.MemberInfo;
import com.amazing.android.autopompomme.databinding.ActivitySetProfileBinding;
import com.amazing.android.autopompomme.linking.LinkingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class SetProfileActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 1;
    private ActivitySetProfileBinding binding;
    private boolean nickNameAble = false;
    private boolean imageChange = false;
    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySetProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivSetProfileSelect.setOnClickListener(v -> openGallery());
        binding.btnSetProfile.setOnClickListener(v -> nicknameCheck());
        nextBtnClick();
    }

    private void nextBtnClick() {
        binding.tvSetProfileNext.setOnClickListener(view -> {
            if(nickNameAble && imageChange) {
                binding.tvSetProfileNext.setClickable(true);
                binding.tvSetProfileNext.setTextColor(Color.BLACK);
                binding.tvSetProfileNext.setOnClickListener(v -> saveData());
            }else {
                binding.tvSetProfileNext.setClickable(false);
                binding.tvSetProfileNext.setTextColor(Color.GRAY);
            }
        });
    }

    private void openGallery() {
        checkSelfPermission();

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(resultCode, resultCode, data);

        ImageView imageView = binding.ivSetProfile;

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            String[] filePathColum = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(uri, filePathColum, null, null, null);
            cursor.moveToFirst();
            cursor.close();

            imageView.setImageURI(uri);

            imageChange = true;
        }
    }

    public void checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("TEST", "하용 안됨");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "외부 저장소 사용을 위해 읽기/쓰기 필요", Toast.LENGTH_SHORT).show();
            }
            Log.d("TEST", "요청");
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        } else {
            Log.d("TEST", "하용 됨");
        }
    }

    private void nicknameCheck() {
        String nickName = binding.etSetProfile.getText().toString();

        if (nickName.length() > 0) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .whereEqualTo("nickname", nickName)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().isEmpty()) {
                                    // 검색된 문서가 없으므로, 해당 닉네임은 사용 가능
                                    //Log.d(TAG, "Nickname is available.");
                                    binding.tvSetProfileNotAble.setVisibility(View.GONE);
                                    binding.tvSetProfileAble.setVisibility(View.VISIBLE);
                                    binding.tvSetProfileNoIn.setVisibility(View.GONE);
                                    nickNameAble = true;
                                } else {
                                    // 검색된 문서가 있으므로, 해당 닉네임은 이미 사용 중
                                    //Log.d(TAG, "Nickname is already in use.");
                                    binding.tvSetProfileAble.setVisibility(View.GONE);
                                    binding.tvSetProfileNotAble.setVisibility(View.VISIBLE);
                                    binding.tvSetProfileNoIn.setVisibility(View.GONE);
                                    nickNameAble = false;
                                }
                            } else {
                                Log.w("TEST", "Error getting documents.", task.getException());
                            }
                        }
                    });
        } else {
            //닉네임 입력 안할 시 처리
            binding.tvSetProfileNoIn.setVisibility(View.VISIBLE);
            binding.tvSetProfileAble.setVisibility(View.GONE);
            binding.tvSetProfileNotAble.setVisibility(View.GONE);
        }
    }

    private void saveData() {
        String nickName = binding.etSetProfile.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        UserProfileChangeRequest profileUpdates;

        MemberInfo memberInfo = new MemberInfo(nickName, uri.toString(), 0);

        if (user != null) {
            db.collection("users").document(user.getUid()).set(memberInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //닉네임 추가 성공 로직 구현
                            //Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //닉네임 추가 실패 로직 구현
                            //Log.w(TAG, "Error writing document", e);
                        }
                    });

            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nickName)
                    .setPhotoUri(uri)
                    .build();

            //profileUpdates = new UserProfileChangeRequest.Builder()
            //        .setDisplayName(nickName)
            //        .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //프로필 정보 업데이트 성공 로직
                                Intent intent = new Intent(SetProfileActivity.this, LinkingActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
        }
    }
}