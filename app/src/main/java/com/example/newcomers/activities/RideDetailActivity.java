package com.example.newcomers.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.newcomers.R;
import com.example.newcomers.databinding.ActivityRideDetailBinding;

public class RideDetailActivity extends AppCompatActivity {
    ActivityRideDetailBinding rideDetailBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_ride_detail);
        rideDetailBinding = ActivityRideDetailBinding.inflate(getLayoutInflater());
        View view = rideDetailBinding.getRoot();
        initRideDetail();
        setContentView(view);
    }

    /**
     * init Ride Detail
     */
    private void initRideDetail() {
    }
}