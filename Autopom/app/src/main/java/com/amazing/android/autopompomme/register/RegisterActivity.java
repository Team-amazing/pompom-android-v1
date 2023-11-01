package com.amazing.android.autopompomme.register;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.amazing.android.autopompomme.R;
import com.amazing.android.autopompomme.activity.MainActivity;
import com.amazing.android.autopompomme.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    ActivityRegisterBinding binding;
    private FirebaseFirestore db;
    private Uri plantImgUri;
    private String plantSpecies;
    private String plantNickName;
    private String plantBirth;
    private String uid;
    private Boolean isFull = false;
    private int year;
    private int month;
    private int day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        setToolbar();
        getUid();
        setCameraImg();
        setCalendar();
        checkTextInput();
        sever();
    }

    private void setCalendar() {
        binding.tvRegisterPlantDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String formattedMonth = String.format("%02d", month + 1);
                    String formattedDay = String.format("%02d", dayOfMonth);

                    plantBirth = year + "" + formattedMonth + "" + formattedDay;
                    binding.tvRegisterPlantDate.setText(plantBirth);
                    checkInEssential();
                }
            }, year, month, day);

            Calendar maxDate = Calendar.getInstance();
            maxDate.set(year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

            datePickerDialog.show();

            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.light_black, getTheme()));
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.light_black, getTheme()));
        });
    }


    private void getUid() {
        SharedPreferences data = MainActivity.context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        uid = data.getString("userId", "default value");
    }

    private void sever() {
        binding.tvRegisterComplete.setOnClickListener(v -> {
            if (isFull) {
                Map<String, Object> register = new HashMap<>();
                register.put("plantImgUri", plantImgUri);
                register.put("plantSpecies", plantSpecies);
                register.put("plantNickName", plantNickName);
                register.put("plantBirth", plantBirth);
                register.put("uid", uid);

                db.collection("register")
                        .add(register)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                updateRankingScore();
                                Toast.makeText(getBaseContext(), "팜팜이가 추가되었습니다", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getBaseContext(), "에러", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    private void checkTextInput() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInEssential();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        binding.etRegisterPlantName.addTextChangedListener(textWatcher);
        binding.etRegisterPlantNick.addTextChangedListener(textWatcher);
    }

    private void checkInEssential() {
        plantSpecies = binding.etRegisterPlantName.getText().toString();
        plantNickName = binding.etRegisterPlantNick.getText().toString();
        plantBirth = binding.tvRegisterPlantDate.getText().toString();

        if (plantSpecies.length() == 0 || plantNickName.length() == 0 || plantBirth.length() == 0 || plantImgUri == null) {
            binding.tvRegisterComplete.setClickable(false);
            binding.tvRegisterComplete.setTextColor(Color.parseColor("#D9D9D9"));
            isFull = false;
        } else {
            binding.tvRegisterComplete.setClickable(true);
            binding.tvRegisterComplete.setTextColor(Color.parseColor("#28DF99"));
            isFull = true;
        }
    }

    private void setCameraImg() {
        binding.ivRegisterSelect.setOnClickListener(v -> showPhotoSelectionDialog());
    }

    private void showPhotoSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("사진 선택");
        builder.setItems(new CharSequence[]{"사진 찍기", "갤러리에서 가져오기"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // 사진 찍기를 선택한 경우
                        dispatchTakePictureIntent();
                        break;
                    case 1:
                        // 갤러리에서 가져오기를 선택한 경우
                        openGallery();
                        break;
                }
            }
        });
        builder.show();
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("TEST","xxx"+resultCode);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            plantImgUri = data.getData();
            String[] filePathColum = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(plantImgUri, filePathColum, null, null, null);
            cursor.moveToFirst();

            cursor.close();

            binding.ivRegisterSelect.setImageURI(plantImgUri);
            checkInEssential();
        }

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if(extras != null) {
                Uri u = (Uri) extras.get("data");
                Log.d("TEST","gg"+u);
            }
        }else {
            
        }
    }

    private void setToolbar() {
        Toolbar toolbar = binding.tbRegister;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void updateRankingScore() {
        db.collection("users").document(uid)
                .update("score", FieldValue.increment(5))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TEST", "점수 업데이트");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TEST", "점수 업데이트 실패:", e);
                    }
                });
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