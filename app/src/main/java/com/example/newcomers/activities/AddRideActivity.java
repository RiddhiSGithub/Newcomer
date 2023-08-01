package com.example.newcomers.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.newcomers.R;
import com.example.newcomers.databinding.ActivityAddRideBinding;

public class AddRideActivity extends AppCompatActivity {
    ActivityAddRideBinding addRideBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_ride);
        addRideBinding = ActivityAddRideBinding.inflate(getLayoutInflater());
        View view = addRideBinding.getRoot();
        initAddRide();
        setContentView(view);
    }

    /**
     * init Add Ride
     */
    private void initAddRide() {
    }
}