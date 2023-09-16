package com.amazing.android.autopompomme.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amazing.android.autopompomme.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    private EditText email, password;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        email = binding.etLoginEmail;
        password = binding.etLoginPassword;

        binding.tvLoginGoSignup.setOnClickListener(v -> gotoSignup());

        checkTextInput();
    }

    private void gotoSignup() {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent );
    }

    private void checkTextInput() {
        TextWatcher textWatcher =  new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = binding.etLoginEmail.getText().toString().trim();
                String password = binding.etLoginPassword.getText().toString().trim();

                binding.btnLoginLogin.setEnabled(!email.isEmpty()&&!password.isEmpty());

                binding.btnLoginLogin.setOnClickListener(v -> logIn(email, password));
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        };
        email.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);

    }

    private void logIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();

                            sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userId", uid);
                            editor.apply();

                            startToast("로그인에 성공했습니다.");
                            myStartActivity(MainActivity.class);
                        } else {
                            if (task.getException() != null) {
                                startToast(task.getException().toString());
                                Log.d("TEST","예외"+task.getException().toString());
                            }
                        }
                    }
                });
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void myStartActivity(Class<?> cls) {
        Intent intent = new Intent(LoginActivity.this, cls);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}