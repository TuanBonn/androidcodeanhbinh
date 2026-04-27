package com.example.mhikenativeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.widget.SearchView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Button;

import com.example.mhikenativeapp.adapters.HikeAdapter;
import com.example.mhikenativeapp.helpers.DatabaseHelper;
import com.example.mhikenativeapp.models.Hike;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements HikeAdapter.OnHikeActionListener, SearchView.OnQueryTextListener {


    private RecyclerView rvHikes;
    private Button fabAddHike;
    private SearchView searchView;


    private DatabaseHelper dbHelper;
    private ArrayList<Hike> hikeList;
    private HikeAdapter hikeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);


        rvHikes = findViewById(R.id.rvHikes);
        fabAddHike = findViewById(R.id.fabAddHike);
        searchView = findViewById(R.id.searchView);

        setupRecyclerView();


        fabAddHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddHikeActivity.class);
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(this);
    }

    private void setupRecyclerView() {
        hikeList = new ArrayList<>();
        hikeAdapter = new HikeAdapter(hikeList, this);
        rvHikes.setLayoutManager(new LinearLayoutManager(this));
        rvHikes.setAdapter(hikeAdapter);
    }


    private void loadAllHikes() {
        hikeList.clear();
        hikeList.addAll(dbHelper.getAllHikes());
        hikeAdapter.notifyDataSetChanged();
    }

    private void searchHikes(String query) {
        hikeList.clear();
        hikeList.addAll(dbHelper.searchHikes(query));
        hikeAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllHikes();
    }



    @Override
    public void onEditClick(Hike hike) {
        Intent intent = new Intent(MainActivity.this, EditHikeActivity.class);
        intent.putExtra("HIKE_ID", hike.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Hike hike) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Hike")
                .setMessage("Are you sure you want to delete '" + hike.getName() + "'?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteHike(hike.getId());
                        loadAllHikes();
                        Toast.makeText(MainActivity.this, "Hike deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onHikeClick(Hike hike) {
        Intent intent = new Intent(MainActivity.this, HikeDetailActivity.class);
        intent.putExtra("HIKE_ID", hike.getId());
        startActivity(intent);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        searchHikes(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()) {
            loadAllHikes();
        } else {
            searchHikes(newText);
        }
        return true;
    }
}