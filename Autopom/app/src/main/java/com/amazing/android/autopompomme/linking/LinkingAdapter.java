package com.amazing.android.autopompomme.linking;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.amazing.android.autopompomme.R;

import java.util.ArrayList;

public class LinkingAdapter extends RecyclerView.Adapter<LinkingAdapter.ViewHolder> {
    private ArrayList<String> list;



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

    public LinkingAdapter (ArrayList<String> dataSet) {
        Log.d("TEST","t/"+dataSet);
        list = dataSet;
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
        String text = list.get(position);
        holder.deviceName.setText(text);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }




}
