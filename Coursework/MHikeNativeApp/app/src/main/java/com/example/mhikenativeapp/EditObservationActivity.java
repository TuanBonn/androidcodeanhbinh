package com.example.mhikenativeapp; // Thay bằng package của bạn

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mhikenativeapp.helpers.DatabaseHelper;
import com.example.mhikenativeapp.models.Observation;
import com.google.android.material.textfield.TextInputEditText;

public class EditObservationActivity extends AppCompatActivity {

    private TextInputEditText etObservationText, etObservationTime, etObservationComments;
    private Button btnUpdateObservation; // Dùng lại ID btnSaveObservation từ layout copy

    private DatabaseHelper dbHelper;
    private long obsId;
    private Observation currentObs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_observation);

        dbHelper = new DatabaseHelper(this);

        // Nhận ID quan sát cần sửa
        Intent intent = getIntent();
        obsId = intent.getLongExtra("OBS_ID", -1);

        if (obsId == -1) {
            Toast.makeText(this, "Error: Observation ID missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupUI();
        loadData(); // Tải dữ liệu cũ lên

        btnUpdateObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateObservation();
            }
        });
    }

    private void setupUI() {
        etObservationText = findViewById(R.id.etObservationText);
        etObservationTime = findViewById(R.id.etObservationTime);
        etObservationComments = findViewById(R.id.etObservationComments);
        btnUpdateObservation = findViewById(R.id.btnSaveObservation); // ID bên layout copy vẫn là btnSaveObservation
        btnUpdateObservation.setText("Update Observation");
    }

    private void loadData() {
        currentObs = dbHelper.getObservationById(obsId);
        if (currentObs != null) {
            etObservationText.setText(currentObs.getObservation());
            etObservationTime.setText(currentObs.getTime());
            etObservationComments.setText(currentObs.getComments());
        }
    }

    private void updateObservation() {
        if (etObservationText.getText().toString().trim().isEmpty() ||
                etObservationTime.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Observation updatedObs = new Observation();
        updatedObs.setId(obsId); // Quan trọng: Giữ nguyên ID
        updatedObs.setObservation(etObservationText.getText().toString().trim());
        updatedObs.setTime(etObservationTime.getText().toString().trim());
        updatedObs.setComments(etObservationComments.getText().toString().trim());
        updatedObs.setHikeId(currentObs.getHikeId()); // Giữ nguyên Hike ID cũ

        dbHelper.updateObservation(updatedObs);
        Toast.makeText(this, "Observation updated!", Toast.LENGTH_SHORT).show();
        finish(); // Quay về màn hình chi tiết
    }
}