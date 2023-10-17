package com.amazing.android.autopompomme.activity;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.tvLoginGoSignup.setOnClickListener(v -> gotoSignup());

        checkTextInput();
    }

    private void gotoSignup() {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    private void checkTextInput() {
        EditText email = binding.etLoginEmail;
        EditText password = binding.etLoginPassword;

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = binding.etLoginEmail.getText().toString().trim();
                String password = binding.etLoginPassword.getText().toString().trim();

                binding.btnLoginLogin.setEnabled(!email.isEmpty() && !password.isEmpty());

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
                            startToast("로그인에 성공했습니다.");
                            myStartActivity();
                        } else {
                            if (task.getException() != null) {
                                startToast(task.getException().toString());
                                Log.d("TEST", "예외" + task.getException().toString());
                            }
                        }
                    }
                });
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void myStartActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}