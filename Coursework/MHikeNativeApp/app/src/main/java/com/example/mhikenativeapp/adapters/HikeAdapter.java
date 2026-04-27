package com.example.mhikenativeapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhikenativeapp.R;
import com.example.mhikenativeapp.models.Hike;

import java.util.ArrayList;


public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.HikeViewHolder> {

    private ArrayList<Hike> hikeList;
    private OnHikeActionListener listener;


    public static class HikeViewHolder extends RecyclerView.ViewHolder {
        public TextView tvHikeName, tvHikeDate;
        public Button btnEdit, btnDelete;

        public HikeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHikeName = itemView.findViewById(R.id.tvHikeName);
            tvHikeDate = itemView.findViewById(R.id.tvHikeDate);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


    public interface OnHikeActionListener {
        void onEditClick(Hike hike);
        void onDeleteClick(Hike hike);
        void onHikeClick(Hike hike);
    }

    public HikeAdapter(ArrayList<Hike> hikeList, OnHikeActionListener listener) {
        this.hikeList = hikeList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public HikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hike, parent, false);
        return new HikeViewHolder(v);

    }


    @Override
    public void onBindViewHolder(@NonNull HikeViewHolder holder, int position) {
        Hike currentHike = hikeList.get(position);

        holder.tvHikeName.setText(currentHike.getName());
        holder.tvHikeDate.setText("Date: " + currentHike.getDate());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onEditClick(currentHike);
                }
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDeleteClick(currentHike);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onHikeClick(currentHike);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return hikeList.size();
    }
}