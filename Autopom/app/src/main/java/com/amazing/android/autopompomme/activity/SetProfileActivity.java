package com.amazing.android.autopompomme.activity;

import android.content.Intent;
import android.os.Bundle;

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

public class SetProfileActivity extends AppCompatActivity {

    private ActivitySetProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySetProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvSetProfileNext.setOnClickListener(v -> checkInput());
    }

    private void checkInput() {

        String nickName = binding.etSetProfile.getText().toString();

        if (nickName.length() > 0) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

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

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nickName)
                        .build();

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

        } else {
            //닉네임 입력 안할 시 처리
        }
    }
}