package com.example.newcomers.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AddRideActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG="AddRideActivity";
    ActivityAddRideBinding addRideBinding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
    }

    private void createUSer() {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        // Create a new user with a first, middle, and last name
        Map<String, Object> user2 = new HashMap<>();
        user2.put("first", "Alan");
        user2.put("middle", "Mathison");
        user2.put("last", "Turing");
        user2.put("born", 1912);

// Add a new document with a generated ID
        db.collection("users")
                .add(user2)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    /**
     * create New Trip
     */
    private void createNewTrip() {
        final Activity that = this;
        Trip trip = new Trip();
        trip.from = addRideBinding.edtFrom.getText().toString().trim();
        trip.destination = addRideBinding.edtDest.getText().toString().trim();
        trip.carModel = addRideBinding.edtCarModel.getText().toString().trim();
        trip.carColor = addRideBinding.edtCarColor.getText().toString().trim();
        trip.licencePlate = addRideBinding.edtLicencePlate.getText().toString().trim();
        trip.seatRemain = Integer.parseInt(addRideBinding.edtSeatRemain.getText().toString().trim());
        trip.description = addRideBinding.edtDesc.getText().toString().trim();

        // Add a new document with a generated ID
        db.collection("trips")
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



//        db.collection("users").whereEqualTo().whereEqualTo()
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });

    }

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
}