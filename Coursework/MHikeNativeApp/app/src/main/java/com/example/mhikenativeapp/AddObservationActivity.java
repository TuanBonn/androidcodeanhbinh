package com.example.mhikenativeapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mhikenativeapp.helpers.DatabaseHelper;
import com.example.mhikenativeapp.models.Observation;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AddObservationActivity extends AppCompatActivity {

    private TextInputEditText etObservationText, etObservationTime, etObservationComments;
    private Button btnSaveObservation;

    private DatabaseHelper dbHelper;
    private long hikeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);

        dbHelper = new DatabaseHelper(this);


        Intent intent = getIntent();
        hikeId = intent.getLongExtra("HIKE_ID", -1);

        if (hikeId == -1) {
            Toast.makeText(this, "Error: Hike ID not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupUI();

        setupClickListeners();

        setDefaultTime();
    }


    private void setupUI() {
        etObservationText = findViewById(R.id.etObservationText);
        etObservationTime = findViewById(R.id.etObservationTime);
        etObservationComments = findViewById(R.id.etObservationComments);
        btnSaveObservation = findViewById(R.id.btnSaveObservation);
    }


    private void setDefaultTime() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String currentTime = sdf.format(calendar.getTime());

        etObservationTime.setText(currentTime);
    }


    private void setupClickListeners() {
        btnSaveObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveObservation();
            }
        });
    }


    private boolean validateInput() {
        if (etObservationText.getText().toString().trim().isEmpty()) {
            etObservationText.setError("Observation text is required");
            return false;
        }
        if (etObservationTime.getText().toString().trim().isEmpty()) {
            etObservationTime.setError("Time is required");
            return false;
        }
        return true;
    }


    private void saveObservation() {

        if (!validateInput()) {
            Toast.makeText(this, "Please fix the errors", Toast.LENGTH_SHORT).show();
            return;
        }


        String obsText = etObservationText.getText().toString().trim();
        String obsTime = etObservationTime.getText().toString().trim();
        String obsComments = etObservationComments.getText().toString().trim();


        Observation newObservation = new Observation();
        newObservation.setObservation(obsText);
        newObservation.setTime(obsTime);
        newObservation.setComments(obsComments);
        newObservation.setHikeId(hikeId);


        long id = dbHelper.addObservation(newObservation);


        if (id != -1) {
            Toast.makeText(this, "Observation added successfully!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add observation", Toast.LENGTH_SHORT).show();
        }
    }
}