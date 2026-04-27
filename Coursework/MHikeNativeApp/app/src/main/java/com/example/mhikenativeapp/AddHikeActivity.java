package com.example.mhikenativeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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

public class AddHikeActivity extends AppCompatActivity {

    private TextInputEditText etHikeName, etHikeLocation, etHikeDate, etHikeLength, etHikeDescription, etWeather, etTrailCondition;
    private RadioGroup rgParking;
    private Spinner spinnerDifficulty;
    private Button btnSaveHike;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hike);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add New Hike");
        }

        dbHelper = new DatabaseHelper(this);
        setupUI();
        setupSpinner();
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
        btnSaveHike = findViewById(R.id.btnSaveHike);

    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.difficulty_levels,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnSaveHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHike();
            }
        });

        etHikeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


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
        if (selectedId == -1) {
            return "";
        }
        RadioButton selectedRadioButton = findViewById(selectedId);
        return selectedRadioButton.getText().toString();
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

    private void saveHike() {
        if (!validateInput()) {
            Toast.makeText(this, "Please fill in all information", Toast.LENGTH_SHORT).show();
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

        Hike newHike = new Hike();
        newHike.setName(name);
        newHike.setLocation(location);
        newHike.setDate(date);
        newHike.setParkingAvailable(parking);
        newHike.setLength(length);
        newHike.setDifficulty(difficulty);
        newHike.setDescription(description);
        newHike.setWeather(weather);
        newHike.setTrailCondition(trailCondition);

        long id = dbHelper.addHike(newHike);

        if (id != -1) {
            Toast.makeText(this, "Hike saved successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save hike", Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        finish();
//        return true;
//    }
}