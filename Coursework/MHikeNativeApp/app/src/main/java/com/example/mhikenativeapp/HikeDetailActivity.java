package com.example.mhikenativeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhikenativeapp.adapters.ObservationAdapter;
import com.example.mhikenativeapp.helpers.DatabaseHelper;
import com.example.mhikenativeapp.models.Hike;
import com.example.mhikenativeapp.models.Observation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class HikeDetailActivity extends AppCompatActivity implements ObservationAdapter.OnObservationActionListener {


    private TextView tvDetailName, tvDetailLocation, tvDetailDate, tvDetailParking;
    private TextView tvDetailLength, tvDetailDifficulty, tvDetailWeather, tvDetailTrailCondition, tvDetailDescription;


    private RecyclerView rvObservations;
    private Button fabAddObservation;


    private DatabaseHelper dbHelper;
    private ArrayList<Observation> observationList;
    private ObservationAdapter observationAdapter;

    private long hikeId;
    private Hike currentHike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_detail);

        dbHelper = new DatabaseHelper(this);


        Intent intent = getIntent();
        hikeId = intent.getLongExtra("HIKE_ID", -1);

        if (hikeId == -1) {
            Toast.makeText(this, "Error: Hike ID not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        setupUI();

        setupRecyclerView();

        setupClickListeners();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadHikeData();
        loadObservations();
    }


    private void setupUI() {

        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailLocation = findViewById(R.id.tvDetailLocation);
        tvDetailDate = findViewById(R.id.tvDetailDate);
        tvDetailParking = findViewById(R.id.tvDetailParking);
        tvDetailLength = findViewById(R.id.tvDetailLength);
        tvDetailDifficulty = findViewById(R.id.tvDetailDifficulty);
        tvDetailWeather = findViewById(R.id.tvDetailWeather);
        tvDetailTrailCondition = findViewById(R.id.tvDetailTrailCondition);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);


        rvObservations = findViewById(R.id.rvObservations);
        fabAddObservation = findViewById(R.id.fabAddObservation);
    }


    private void setupRecyclerView() {
        observationList = new ArrayList<>();

        observationAdapter = new ObservationAdapter(observationList, this);

        rvObservations.setLayoutManager(new LinearLayoutManager(this));
        rvObservations.setAdapter(observationAdapter);
    }


    private void setupClickListeners() {
        fabAddObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HikeDetailActivity.this, AddObservationActivity.class);

                intent.putExtra("HIKE_ID", hikeId);

                startActivity(intent);
            }
        });
    }


    private void loadHikeData() {
        currentHike = dbHelper.getHikeById(hikeId);

        if (currentHike != null) {
            tvDetailName.setText(currentHike.getName());
            tvDetailLocation.setText("Location: " + currentHike.getLocation());
            tvDetailDate.setText("Date: " + currentHike.getDate());
            tvDetailParking.setText("Parking: " + currentHike.getParkingAvailable());
            tvDetailLength.setText("Length: " + currentHike.getLength() + " km");
            tvDetailDifficulty.setText("Difficulty: " + currentHike.getDifficulty());
            tvDetailWeather.setText("Weather: " + currentHike.getWeather());
            tvDetailTrailCondition.setText("Trail: " + currentHike.getTrailCondition());
            tvDetailDescription.setText("Description: " + currentHike.getDescription());
        } else {
            Toast.makeText(this, "Error: Hike data corrupted.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void loadObservations() {
        observationList.clear();
        observationList.addAll(dbHelper.getAllObservationsForHike(hikeId));
        observationAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDeleteObsClick(Observation observation) {

        new AlertDialog.Builder(this)
                .setTitle("Delete Observation")
                .setMessage("Are you sure you want to delete this observation?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteObservation(observation.getId());
                        loadObservations();
                        Toast.makeText(HikeDetailActivity.this, "Observation deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onEditObsClick(Observation observation) {
        // Mở màn hình EditObservationActivity
        Intent intent = new Intent(HikeDetailActivity.this, EditObservationActivity.class);

        // Gửi ID của quan sát cần sửa
        intent.putExtra("OBS_ID", observation.getId());

        startActivity(intent);
    }
}