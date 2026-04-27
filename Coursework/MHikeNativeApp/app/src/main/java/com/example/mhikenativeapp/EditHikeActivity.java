package com.example.mhikenativeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mhikenativeapp.helpers.DatabaseHelper;
import com.example.mhikenativeapp.models.Hike;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class EditHikeActivity extends AppCompatActivity {

    private TextInputEditText etHikeName, etHikeLocation, etHikeDate, etHikeLength, etHikeDescription, etWeather, etTrailCondition;
    private RadioGroup rgParking;
    private Spinner spinnerDifficulty;
    private Button btnUpdateHike;

    private DatabaseHelper dbHelper;
    private long hikeId;
    private Hike currentHike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hike);

        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        hikeId = intent.getLongExtra("HIKE_ID", -1);

        if (hikeId == -1) {
            Toast.makeText(this, "Error: Hike ID not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupUI();
        setupSpinner();
        loadHikeData();
        setupClickListeners();
    }

    private void setupUI() {
        etHikeName = findViewById(R.id.etHikeName);
        etHikeLocation = findViewById(R.id.etHikeLocation);
        etHikeDate = findViewById(R.id.etHikeDate);
        etHikeLength = findViewById(R.id.etHikeLength);
        etHikeDescription = findViewById(R.id.etHikeDescription);
        etWeather = findViewById(R.id.etWeather);
        etTrailCondition = findViewById(R.id.etTrailCondition);
        rgParking = findViewById(R.id.rgParking);
        spinnerDifficulty = findViewById(R.id.spinnerDifficulty);

        btnUpdateHike = findViewById(R.id.btnSaveHike);
        btnUpdateHike.setText("Update Hike");

    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.difficulty_levels, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapter);
    }

    private void loadHikeData() {
        currentHike = dbHelper.getHikeById(hikeId);

        if (currentHike == null) {
            Toast.makeText(this, "Error: Could not load hike data.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etHikeName.setText(currentHike.getName());
        etHikeLocation.setText(currentHike.getLocation());
        etHikeDate.setText(currentHike.getDate());
        etHikeLength.setText(currentHike.getLength());
        etHikeDescription.setText(currentHike.getDescription());
        etWeather.setText(currentHike.getWeather());
        etTrailCondition.setText(currentHike.getTrailCondition());

        if ("Yes".equals(currentHike.getParkingAvailable())) {
            rgParking.check(R.id.radioButton3);
        } else {
            rgParking.check(R.id.radioButton4);
        }

        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerDifficulty.getAdapter();
        int spinnerPosition = adapter.getPosition(currentHike.getDifficulty());
        spinnerDifficulty.setSelection(spinnerPosition);
    }

    private void setupClickListeners() {
        btnUpdateHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateHike();
            }
        });

        etHikeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void updateHike() {
        if (!validateInput()) {
            Toast.makeText(this, "Please fix the errors", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = etHikeName.getText().toString().trim();
        String location = etHikeLocation.getText().toString().trim();
        String date = etHikeDate.getText().toString().trim();
        String parking = getParkingValue();
        String length = etHikeLength.getText().toString().trim();
        String difficulty = spinnerDifficulty.getSelectedItem().toString();
        String description = etHikeDescription.getText().toString().trim();
        String weather = etWeather.getText().toString().trim();
        String trailCondition = etTrailCondition.getText().toString().trim();

        Hike updatedHike = new Hike();
        updatedHike.setId(hikeId);
        updatedHike.setName(name);
        updatedHike.setLocation(location);
        updatedHike.setDate(date);
        updatedHike.setParkingAvailable(parking);
        updatedHike.setLength(length);
        updatedHike.setDifficulty(difficulty);
        updatedHike.setDescription(description);
        updatedHike.setWeather(weather);
        updatedHike.setTrailCondition(trailCondition);

        int rowsAffected = dbHelper.updateHike(updatedHike);

        if (rowsAffected > 0) {
            Toast.makeText(this, "Hike updated successfully!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update hike", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        etHikeDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    private String getParkingValue() {
        int selectedId = rgParking.getCheckedRadioButtonId();
        if (selectedId == R.id.radioButton3) {
            return "Yes";
        } else if (selectedId == R.id.radioButton4) {
            return "No";
        }
        return "";
    }

    private boolean validateInput() {
        if (etHikeName.getText().toString().trim().isEmpty()) {
            etHikeName.setError("Name is required");
            return false;
        }
        if (etHikeLocation.getText().toString().trim().isEmpty()) {
            etHikeLocation.setError("Location is required");
            return false;
        }
        if (etHikeDate.getText().toString().trim().isEmpty()) {
            etHikeDate.setError("Date is required");
            return false;
        }
        if (etHikeLength.getText().toString().trim().isEmpty()) {
            etHikeLength.setError("Length is required");
            return false;
        }
        if (getParkingValue().isEmpty()) {
            Toast.makeText(this, "Please select parking availability", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}