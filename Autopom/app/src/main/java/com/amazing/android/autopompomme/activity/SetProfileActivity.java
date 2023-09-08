package com.amazing.android.autopompomme.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
        binding.tvSetProfileNext.setOnClickListener(v -> {
            if (nickNameAble) {
                saveData();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(resultCode, resultCode, data);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //Uri photoUrl = user.getPhotoUrl();

        ImageView imageView = binding.ivSetProfile;

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            String[] filePathColum = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, filePathColum, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColum[0]);
            String imagePath = cursor.getString(columnIndex);
            cursor.close();

            imageView.setImageURI(uri);
            //imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));

            imageChange = true;

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
                                    nickNameAble = true;
                                    //saveData(db, nickName);
                                } else {
                                    // 검색된 문서가 있으므로, 해당 닉네임은 이미 사용 중
                                    //Log.d(TAG, "Nickname is already in use.");
                                    binding.tvSetProfileAble.setVisibility(View.GONE);
                                    binding.tvSetProfileNotAble.setVisibility(View.VISIBLE);
                                    nickNameAble = false;
                                }
                            } else {
                                //Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });


        } else {
            //닉네임 입력 안할 시 처리
        }

    }


    private void saveData() {
        String nickName = binding.etSetProfile.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        UserProfileChangeRequest profileUpdates;

        MemberInfo memberInfo = new MemberInfo(nickName);

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

            if(imageChange) {
                profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nickName)
                        .setPhotoUri(Uri.parse(uri.toString()))
                        .build();

            }else {
                profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nickName)
                        .build();
            }

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(SetProfileActivity.this, LinkingActivity.class);
                                startActivity(intent);
                                //프로필 정보 업데이트 성공 로직
                                //Log.d(TAG, "User profile updated.");
                            }
                        }
                    });
        }
    }
}