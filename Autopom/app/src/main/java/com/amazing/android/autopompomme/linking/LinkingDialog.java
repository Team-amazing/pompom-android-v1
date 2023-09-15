package com.amazing.android.autopompomme.linking;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.amazing.android.autopompomme.databinding.LinkingDialogBinding;

import java.io.IOException;

public class LinkingDialog extends Dialog {

    LinkingDialogBinding binding;

    private TextView deviceName;
    private TextView connect;
    private EditText wifiName;
    private EditText wifiPw;

    private String title;
    private String device;

    Listener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = LinkingDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
  //      getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        deviceName = binding.tvLinkingDialogDevice;
        connect = binding.tvLinkingDialogConnect;
        wifiName = binding.etLinkingDialogWifiName;
        wifiPw = binding.etLinkingDialogWifiPW;

        deviceName.setText(title);

        binding.tvLinkingDialogConnect.setOnClickListener(v -> {
            if(listener != null) {
                try {
                    listener.wifi(wifiName.getText().toString(),wifiPw.getText().toString(),device);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    public LinkingDialog(@NonNull Context context, String title, String device, Listener listener) {
        super(context);

        this.title = title;
        this.listener = listener;
        this.device = device;
    }



}
