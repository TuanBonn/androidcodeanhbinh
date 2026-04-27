package com.example.mhikenativeapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhikenativeapp.R;
import com.example.mhikenativeapp.models.Observation;

import java.util.ArrayList;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder> {

    private ArrayList<Observation> observationList;
    private OnObservationActionListener listener;

    public static class ObservationViewHolder extends RecyclerView.ViewHolder {
        public TextView tvObsText, tvObsTime, tvObsComments;
        public Button btnEditObs, btnDeleteObs;

        public ObservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvObsText = itemView.findViewById(R.id.tvObsText);
            tvObsTime = itemView.findViewById(R.id.tvObsTime);
            tvObsComments = itemView.findViewById(R.id.tvObsComments);
            btnEditObs = itemView.findViewById(R.id.btnEditObs);
            btnDeleteObs = itemView.findViewById(R.id.btnDeleteObs);
        }
    }

    public interface OnObservationActionListener {
        void onEditObsClick(Observation observation);
        void onDeleteObsClick(Observation observation);
    }

    public ObservationAdapter(ArrayList<Observation> observationList, OnObservationActionListener listener) {
        this.observationList = observationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ObservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_observation, parent, false);
        return new ObservationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationViewHolder holder, int position) {
        Observation currentObs = observationList.get(position);

        holder.tvObsText.setText(currentObs.getObservation());
        holder.tvObsTime.setText("Time: " + currentObs.getTime());
        holder.tvObsComments.setText("Comments: " + currentObs.getComments());

        holder.btnEditObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onEditObsClick(currentObs);
                }
            }
        });

        holder.btnDeleteObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDeleteObsClick(currentObs);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return observationList.size();
    }
}