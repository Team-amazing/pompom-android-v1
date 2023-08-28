package com.amazing.android.autopompomme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amazing.android.autopompomme.databinding.ActivitySiginupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    ActivitySiginupBinding binding;
    private FirebaseAuth mAuth;

    private EditText email, password, passwordCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySiginupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        email = binding.etSignupEmail;
        password = binding.etSignupPassword;
        passwordCheck = binding.etSignupPasswordCheck;

        checkTextInput();
    }

    private void checkTextInput() {
        TextWatcher textWatcher =  new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = binding.etSignupEmail.getText().toString().trim();
                String password = binding.etSignupPassword.getText().toString().trim();
                String passwordCheck = binding.etSignupPasswordCheck.getText().toString().trim();

                binding.btnSignupSignup.setEnabled(!email.isEmpty()&&!password.isEmpty()&&!passwordCheck.isEmpty());

                if(password.equals(passwordCheck)) {
                    binding.btnSignupSignup.setOnClickListener(v -> signup(email, password));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        };
        email.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        passwordCheck.addTextChangedListener(textWatcher);
    }

    private void signup(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            startToast("회원가입이 성공적으로 되었습니다.");
                            myStartActivity(SetProfileActivity.class);


                        } else {
                            if(task.getException() != null){
                                startToast(task.getException().toString());
                                //예외 처리 필요
                            }
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg , Toast.LENGTH_SHORT).show();
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
    }



}