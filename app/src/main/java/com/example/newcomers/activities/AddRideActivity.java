package com.example.newcomers.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.newcomers.R;
import com.example.newcomers.beans.Trip;
import com.example.newcomers.beans.User;
import com.example.newcomers.databinding.ActivityAddRideBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Add Ride Activity
 * @Author: Meng
 * @Date: Aug 14 2023
 */
public class AddRideActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG="AddRideActivity";
    ActivityAddRideBinding addRideBinding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String fromId = "";
    double fromLat, fromLng;
    String destId = "";
    double destLat, destLng;
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
        addRideBinding.btnAddNewTrip.setOnClickListener(this);
        addRideBinding.edtFrom.setOnClickListener(this);
        addRideBinding.edtDest.setOnClickListener(this);
    }


    /**
     * create New Trip
     */
    private void createNewTrip() {
        final Activity that = this;
        Trip trip = new Trip();
        trip.fromId = fromId;
        trip.fromLat = fromLat;
        trip.fromLng = fromLng;
        trip.destinationId = destId;
        trip.destLat = destLat;
        trip.destLng = destLng;
        trip.from = addRideBinding.edtFrom.getText().toString().trim();
        trip.destination = addRideBinding.edtDest.getText().toString().trim();
        trip.carModel = addRideBinding.edtCarModel.getText().toString().trim();
        trip.carColor = addRideBinding.edtCarColor.getText().toString().trim();
        trip.licencePlate = addRideBinding.edtLicencePlate.getText().toString().trim();
        trip.seatTotal = Integer.parseInt(addRideBinding.edtSeatRemain.getText().toString().trim());
        trip.seatTaken = 0;
        trip.description = addRideBinding.edtDesc.getText().toString().trim();

        // Add a new document with a generated ID
        db.collection("Trips")
                .add(trip)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(AddRideActivity.this, "Trip Add Successfully", Toast.LENGTH_SHORT).show();
                        that.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(AddRideActivity.this, "Trip Add Error", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    // get the from location
    private final ActivityResultLauncher<Intent> fromAutocomplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Log.i(TAG, intent.toString());
                        Place place = Autocomplete.getPlaceFromIntent(intent);
                        Log.i(TAG, "From Place: {"+place.getName()+"}, {"+place.getId()+"}, {"+place.getAddress()+"}");
                        addRideBinding.edtFrom.setText(place.getAddress());
                        fromId = place.getId();
                        fromLat = place.getLatLng().latitude;
                        fromLng = place.getLatLng().longitude;
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    // The user canceled the operation.
                    Log.i(TAG, "User canceled autocomplete");
                }
            });

    // get the dest location
    private final ActivityResultLauncher<Intent> destAutocomplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Log.i(TAG, intent.toString());
                        Place place = Autocomplete.getPlaceFromIntent(intent);
                        Log.i(TAG, "Dest Place: {"+place.getName()+"}, {"+place.getId()+"}, {"+place.getAddress()+"}, {"+place.getLatLng()+"}");
                        addRideBinding.edtDest.setText(place.getAddress());
                        destId = place.getId();
                        destLat = place.getLatLng().latitude;
                        destLng = place.getLatLng().longitude;
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    // The user canceled the operation.
                    Log.i(TAG, "User canceled autocomplete");
                }
            });



    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == addRideBinding.btnAddNewTrip.getId()) {
            // submit new trip
           boolean canSubmit = checkInputValue();
           if(canSubmit) {
               createNewTrip();
           }
        } else if (v.getId() == addRideBinding.edtFrom.getId()) {
            addRideBinding.edtFrom.setError(null);
            Intent intent = getIntentForAdress();
            fromAutocomplete.launch(intent);
        } else if (v.getId() == addRideBinding.edtDest.getId()) {
            addRideBinding.edtDest.setError(null);
            Intent intent = getIntentForAdress();
            destAutocomplete.launch(intent);
        }
    }


    /**
     * checkInputValue
     */
    private boolean checkInputValue() {
        boolean canSubmit = true;
        if(addRideBinding.edtFrom.length()==0) {
            addRideBinding.edtFrom.setError("From required");
            canSubmit = false;
        }
        if(addRideBinding.edtDest.length()==0) {
            addRideBinding.edtDest.setError("Destination required");
            canSubmit = false;
        }
        if(addRideBinding.edtCarModel.length()==0) {
            addRideBinding.edtCarModel.setError("Car Model required");
            canSubmit = false;
        }
        if(addRideBinding.edtCarColor.length()==0) {
            addRideBinding.edtCarColor.setError("Car Color required");
            canSubmit = false;
        }
        if(addRideBinding.edtLicencePlate.length()==0) {
            addRideBinding.edtLicencePlate.setError("Licence Plate required");
            canSubmit = false;
        }
        if(addRideBinding.edtSeatRemain.length()==0) {
            addRideBinding.edtSeatRemain.setError("Seat Remain required");
            canSubmit = false;
        }else if(Integer.parseInt(addRideBinding.edtSeatRemain.getText().toString().trim())<1) {
            addRideBinding.edtSeatRemain.setError("Seat Remain must greater than 1");
            canSubmit = false;
        }
        return canSubmit;
    }

        /**
         * get Intent For Adress
         */
    private Intent getIntentForAdress() {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        ApplicationInfo appInfo = null;
        try {
            appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), appInfo.metaData.getString("com.google.android.geo.API_KEY"), Locale.US);
        }
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(this);
        return intent;
    }
}