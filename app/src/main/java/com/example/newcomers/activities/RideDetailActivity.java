package com.example.newcomers.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.example.newcomers.R;
import com.example.newcomers.beans.Order;
import com.example.newcomers.beans.Trip;
import com.example.newcomers.databinding.ActivityRideDetailBinding;
import com.example.newcomers.databinding.EdittextOrderBinding;
import com.example.newcomers.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Ride Detail Activity
 * Author: Meng
 */
public class RideDetailActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    ActivityRideDetailBinding rideDetailBinding;
    EdittextOrderBinding orderBinding;
    Trip trip;
    private GoogleMap mMap;
    Calendar startDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_ride_detail);
        rideDetailBinding = ActivityRideDetailBinding.inflate(getLayoutInflater());
        orderBinding = EdittextOrderBinding.inflate(getLayoutInflater());
        View view = rideDetailBinding.getRoot();
        initRideDetail();
        setContentView(view);

    }

    /**
     * init Ride Detail
     */
    private void initRideDetail() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // --- set back button on toolbar
        rideDetailBinding.actionBar.materialToolbar.setNavigationIcon(R.drawable.baseline_keyboard_backspace_24);
        rideDetailBinding.actionBar.materialToolbar.setNavigationOnClickListener(view -> finish());
        rideDetailBinding.actionBar.materialToolbar.setTitle(R.string.ride_details);

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
        rideDetailBinding.txtSeatRemain.setText(trip.getSeatTotal()-trip.getSeatTaken()+"/"+trip.getSeatTotal()+"");
//                        trip.getUserID()

        rideDetailBinding.btnRideOrder.setOnClickListener(this);
        rideDetailBinding.btnChatOwner.setOnClickListener(this);
        orderBinding.edtOrderDate.setOnClickListener(this);

        // if user didn't have phone, then remove Chat with Owner button
        rideDetailBinding.btnChatOwner.setVisibility(View.GONE);
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
            if(orderBinding.getRoot().getParent()!=null) {
                ((ViewGroup)orderBinding.getRoot().getParent()).removeView(orderBinding.getRoot());
            }
            String msg = "Make Order";
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle(msg)
                    .setMessage(msg)
                    .setView(orderBinding.getRoot())
                    .setPositiveButton("Make Order", null)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create();
            alertDialog.show();
            Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Log.d("TAG", "onClick: "+btn);

            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(checkOrderForm()) {
                                        submitOrderToFirebase();
                                        alertDialog.dismiss();
                                    }
                                }
                            });

        } else if (v.getId() == orderBinding.edtOrderDate.getId()) { // click Date Select
            Calendar mCalendar = Calendar.getInstance();
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH);
            int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);

            Utils.showMaterialDatePickerDialog(getSupportFragmentManager(), System.currentTimeMillis(), startDate.getTimeInMillis(), new MaterialPickerOnPositiveButtonClickListener() {
                @Override
                public void onPositiveButtonClick(Object selection) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeZone(TimeZone.getTimeZone("utc"));
                    calendar.setTimeInMillis((Long) selection);
                    startDate = calendar;

                    orderBinding.edtOrderDate.setText(Utils.formatDate(startDate));
                    orderBinding.edtOrderDate.setError(null);
                }
            });
        }
    }

    /**
     * submit Order To Firebase
     */
    private void submitOrderToFirebase() {
        if(this.trip.seatTaken+1>=this.trip.seatTotal) {
            String msg = "Cann't Book the Trip, Seat not available";
            showAlertMsg(msg);
            return;
        }

        Order order = new Order();
        this.trip.seatTaken += 1;
        order.orderUserID = Utils.getCurrentUserID();
        order.setOrderTrip(this.trip);
        order.orderDate = orderBinding.edtOrderDate.getText().toString().trim();
        order.flightNumber = orderBinding.edtFlightNo.getText().toString().trim();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Orders")
                .add(order)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("RideDetailActivity", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(RideDetailActivity.this, "Order Add Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("RideDetailActivity", "Error adding document", e);
                        Toast.makeText(RideDetailActivity.this, "Order Add Error", Toast.LENGTH_SHORT).show();
                    }
                });
        db.collection("Trips")
                .document(this.trip.id)
                .update("seatTaken",trip.getSeatTaken())
                //.set(this.trip)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("RideDetailActivity", "DocumentSnapshot Updated with ID: " + trip.getId());
                        Toast.makeText(RideDetailActivity.this, "Trip Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("RideDetailActivity", "Error Updated document", e);
                        Toast.makeText(RideDetailActivity.this, "Trip Updated Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * check Order Form
     * @return
     */
    private boolean checkOrderForm() {
        boolean passCheck = true;
        if(orderBinding.edtOrderDate.length()==0) {
            orderBinding.edtOrderDate.setError(getString(R.string.order_date_is_required));
            orderBinding.edtOrderDate.requestFocus();
            passCheck = false;
        }
        return passCheck;
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

    /**
     * google Map
     * @param googleMap
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng from = new LatLng(trip.fromLat, trip.fromLng);
        MarkerOptions startMarker = new MarkerOptions().position(from);
        startMarker.title(trip.from);
        startMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mMap.addMarker(startMarker);

        LatLng dest = new LatLng(trip.destLat, trip.destLng);
        MarkerOptions endMarker = new MarkerOptions().position(dest);
        startMarker.title(trip.destination);
        mMap.addMarker(endMarker);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(from, 8));

        // TODO: 2023/8/14 Add Routes for real project
//        RoutesSettings routesSettings = null;
//        try {
//            routesSettings = RoutesSettings.newBuilder()
//                    .setHeaderProvider(() -> {
//                        Map headers = new HashMap<>();
//                        headers.put("X-Goog-FieldMask", "*");
//                        return headers;
//                    }).build();
//            RoutesClient routesClient = RoutesClient.create(routesSettings);
//
//            ComputeRoutesResponse response = routesClient.computeRoutes(ComputeRoutesRequest.newBuilder()
//                    .setOrigin(Waypoint.newBuilder().setPlaceId("ChIJtwVr559GK4gR22ZZ175sFAM").build())
//                    .setDestination(Waypoint.newBuilder().setPlaceId("ChIJK2f-X1bxK4gRkB0jxyh7AwU").build())
//                    .setRoutingPreference(RoutingPreference.TRAFFIC_AWARE)
//                    .setTravelMode(RouteTravelMode.DRIVE).build());
//            System.out.println("Response: " + response.toString());
//            Log.e("TAG", "onMapReady: "+response.toString() );
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


//        PolylineOptions polyLines= ;
//        mMap.addPolyline(polyLines);
    }
}