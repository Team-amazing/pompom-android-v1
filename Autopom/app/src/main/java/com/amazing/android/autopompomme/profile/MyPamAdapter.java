package com.amazing.android.autopompomme.profile;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amazing.android.autopompomme.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyPamAdapter extends RecyclerView.Adapter<MyPamAdapter.ViewHolder> {

    private ArrayList<MyPamList> arrayList;

    public MyPamAdapter (ArrayList<MyPamList> arrayList) {
        this.arrayList = arrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView species;
        TextView birth;
        TextView nick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_my_pam_plant);
            species = itemView.findViewById(R.id.tv_my_pam_species);
            birth = itemView.findViewById(R.id.tv_my_pam_birth);
            nick = itemView.findViewById(R.id.tv_my_pam_nick);
        }
    }
    @NonNull
    @Override
    public MyPamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_pam,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPamAdapter.ViewHolder holder, int position) {
        Glide.with(holder.imageView)
                        .load(arrayList.get(position).getPlantImgUri())
                        .into(holder.imageView);
        //holder.imageView.setImageURI(Uri.parse(arrayList.get(position).getPlantImgUri()));
        holder.species.setText("팜팜이 종 : "arrayList.get(position).getPlantSpecies());
        holder.nick.setText("팜팜이 별명 : "arrayList.get(position).getPlantNickName());
        holder.birth.setText("팜팜이 생일 : "arrayList.get(position).getPlantBirth());
    }

    @Override
    public int getItemCount() {
        return arrayList != null ? arrayList.size() : 0;
    }
}
