package com.amazing.android.autopompomme.linking.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.amazing.android.autopompomme.R;
import com.amazing.android.autopompomme.linking.LinkingDialog;
import com.amazing.android.autopompomme.linking.Listener;

import java.util.ArrayList;

public class LinkingAdapter extends RecyclerView.Adapter<LinkingAdapter.ViewHolder> {
    private ArrayAdapter<String> nameAdapter;
    private ArrayList<String> deviceAddress;
    private Context context;
    private Listener listener;


    public interface OnItemClickListener {
        void onItemClicked(int position, String data);
    }

    private OnItemClickListener itemClickListener;

    public void setItemClickListener (OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView deviceName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            deviceName = itemView.findViewById(R.id.tv_linking_device);
        }
        public TextView getTextView() {
            return deviceName;
        }

    }

    public LinkingAdapter (Context context, ArrayAdapter<String> dataSet, ArrayList<String> deviceAddressArray, Listener listener) {
        Log.d("TEST","t/"+dataSet);
        nameAdapter = dataSet;
        this.context = context;
        this.listener = listener;
        this.deviceAddress = deviceAddressArray;
    }

    @NonNull
    @Override
    public LinkingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.linking_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LinkingAdapter.ViewHolder holder, int position) {
        String name = nameAdapter.getItem(position);
        String device = deviceAddress.get(position);
        holder.deviceName.setText(name);

        LinkingDialog linkingDialog = new LinkingDialog(context, name,device,listener);
        holder.deviceName.setOnClickListener(v -> {
            linkingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            linkingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            linkingDialog.show();
        });
    }


    @Override
    public int getItemCount() {
        return nameAdapter.getCount();
    }




}
