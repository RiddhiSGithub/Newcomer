package com.example.newcomers.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.newcomers.R;
import com.example.newcomers.beans.Trip;
import com.example.newcomers.databinding.ActivityRideDetailBinding;
import com.example.newcomers.databinding.EdittextOrderBinding;

import java.util.Locale;

/**
 * Ride Detail Activity
 * Author: Meng
 */
public class RideDetailActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityRideDetailBinding rideDetailBinding;
    Trip trip;
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
        Intent intent = getIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.trip = intent.getSerializableExtra("TRIP_DETAIL",Trip.class);
        }else {
            this.trip = (Trip)intent.getSerializableExtra("TRIP_DETAIL");
        }

        rideDetailBinding.txtDetailFrom.setText(trip.getFrom());
        rideDetailBinding.txtDetailDest.setText(trip.getDestination());
        rideDetailBinding.txtDetailCarModel.setText(trip.getCarModel());
        rideDetailBinding.txtDetailCarColor.setText(trip.getCarColor());
        rideDetailBinding.txtDetailDescription.setText(trip.getDescription());
        rideDetailBinding.txtLicencePlate.setText(trip.getLicencePlate().toUpperCase(Locale.ROOT));
        rideDetailBinding.txtSeatRemain.setText(String.valueOf(trip.getSeatRemain()));
//                        trip.getUserID()

        rideDetailBinding.btnRideOrder.setOnClickListener(this);
        rideDetailBinding.btnChatOwner.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == rideDetailBinding.btnChatOwner.getId()) {
            String msg = "Chat with the Car Owner";
            showAlertMsg(msg);
        } else if (v.getId() == rideDetailBinding.btnRideOrder.getId()) {
            String msg = "Make Order";
            EdittextOrderBinding orderBinding = EdittextOrderBinding.inflate(getLayoutInflater());
            new AlertDialog.Builder(this)
                    .setTitle(msg)
                    .setMessage(msg)
                    .setView(orderBinding.getRoot())
                    .setPositiveButton("Make Order", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
    }

    /**
     * show Alert Msg
     * @param msg the msg will be display
     */
    private void showAlertMsg(String msg) {
        new AlertDialog.Builder(this)
                .setTitle(msg)
                .setMessage(msg)
                .setPositiveButton("Got It", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        
                    }
                }).show();
    }
}