package com.amazing.android.autopompomme.activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.amazing.android.autopompomme.alarm.AlarmService;
import com.amazing.android.autopompomme.databinding.ActivitySettingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        setToolbar();

        editInfo();
        logOut();
        deleteUser();
        initAlarm();
        alarmSwitch();
        binding.tvSettingVersionNumber.setText(getVersionInfo(getBaseContext()));
    }

    private void alarmSwitch() {
        binding.switchSettingAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Log.d("TEST", "aa");
                    enableNotification();
                } else {
                    disableNotification();
                }
            }
        });
    }

    private void enableNotification() {
        Intent intent = new Intent(this, AlarmService.class);
        startService(intent);
    }

    private void disableNotification() {
        Intent intent = new Intent(this, AlarmService.class);
        stopService(intent);
    }

    private void initAlarm() {
        binding.switchSettingAlarm.setChecked(isNotificationEnabled());
    }

    private boolean isNotificationEnabled() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (AlarmService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void editInfo() {
        binding.ibSettingInfoEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditInfoActivity.class);
            startActivity(intent);
        });
    }

    private void deleteUser() {
        binding.tvSettingGetOut.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            assert user != null;
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getBaseContext(), "탈퇴 되었습니다", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getBaseContext(), StartActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
        });
    }

    private void logOut() {
        binding.tvSettingLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
        });
    }

    public String getVersionInfo(Context context) {
        String version = "0.0.0";
        try {
            PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = i.versionName;
        } catch (PackageManager.NameNotFoundException ignored) {

        }
        return version;
    }

    private void setToolbar() {
        Toolbar toolbar = binding.tbSetting;
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