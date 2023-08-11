package com.example.newcomers;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.newcomers.adapters.AccommodationPhotoAdapter;
import com.example.newcomers.beans.Accommodation;
import com.example.newcomers.databinding.ActivityPostAccommodationBinding;
import com.example.newcomers.utils.Utils;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;

public class PostAccommodation extends AppCompatActivity implements View.OnClickListener {

    ActivityPostAccommodationBinding binding;
    Calendar startDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    final int REQUEST_CODE_IMAGES_PICKER = 3244;
    List<Uri> mSelectedPhotos = new ArrayList<>();
    AccommodationPhotoAdapter accommodationPhotoAdapter;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostAccommodationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();

//        fillWithTestInputs();
    }

    // --- initialize views (eg. set spinner items, set onClickListeners, etc.)
    private void initViews() {
        // --- set property types spinner items
        String[] propTypes = new String[]{"House", "Apartment", "Condo", "Other"};
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, propTypes);
        binding.edtPropertyType.setAdapter(adapter);

        // --- set adapter for photos recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.rvPropertyPhotos.setLayoutManager(layoutManager);
        accommodationPhotoAdapter = new AccommodationPhotoAdapter("postPage");
        binding.rvPropertyPhotos.setAdapter(accommodationPhotoAdapter);

        // --- set click listeners
        binding.edtStartDate.setOnClickListener(this);
        binding.btnUploadPhotos.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);

        // --- set back button on toolbar
        binding.actionBar.materialToolbar.setNavigationIcon(R.drawable.baseline_keyboard_backspace_24);
        binding.actionBar.materialToolbar.setNavigationOnClickListener(view -> finish());
    }

    // --- validate inputs
    boolean validateInputs() {
        String propType = binding.edtPropertyType.getText().toString();
        String address = binding.edtAddress.getText().toString();
        String city = binding.edtCity.getText().toString();
        String province = binding.edtProvince.getText().toString();
        String postalCode = binding.edtPostalCode.getText().toString();
        Double rent = Utils.toDouble(binding.edtRent.getText().toString());
        String startDate = binding.edtStartDate.getText().toString();
        Integer noOfBedrooms = Utils.toInteger(binding.edtBedrooms.getText().toString());
        Double noOfBathrooms = Utils.toDouble(binding.edtBathrooms.getText().toString());
        String contactNumber = binding.edtContactNumber.getText().toString();

        if (propType.isEmpty()) {
            binding.edtPropertyType.setError("Please select property type");
            binding.edtPropertyType.requestFocus();
            return false;
        }

        if (address.isEmpty()) {
            binding.edtAddress.setError("Please enter address");
            binding.edtAddress.requestFocus();
            return false;
        }

        if (city.isEmpty()) {
            binding.edtCity.setError("Please enter city");
            binding.edtCity.requestFocus();
            return false;
        }

        if (province.isEmpty()) {
            binding.edtProvince.setError("Please enter province");
            binding.edtProvince.requestFocus();
            return false;
        }

        if (postalCode.isEmpty()) {
            binding.edtPostalCode.setError("Please enter postal code");
            binding.edtPostalCode.requestFocus();
            return false;
        }

        if (rent == null || rent <= 0) {
            binding.edtRent.setError("Please enter valid rent");
            binding.edtRent.requestFocus();
            return false;
        }

        if (startDate.isEmpty()) {
            Utils.showToast(this, "Please select available from date");
            return false;
        }

        if (noOfBedrooms == null || noOfBedrooms <= 0) {
            binding.edtBedrooms.setError("Please enter valid number of bedrooms");
            binding.edtBedrooms.requestFocus();
            return false;
        }

        if (noOfBathrooms == null || noOfBathrooms <= 0) {
            binding.edtBathrooms.setError("Please enter valid number of bathrooms");
            binding.edtBathrooms.requestFocus();
            return false;
        }

        if (contactNumber.isEmpty()) {
            binding.edtContactNumber.setError("Please enter contact number");
            binding.edtContactNumber.requestFocus();
            return false;
        }

        if (mSelectedPhotos.size() == 0) {
            Utils.showToast(this, "Please upload at least one property photo");
            return false;
        }

        return true;
    }

    // --- upload photo from URI to firebase storage and get download URL
    CompletableFuture<Uri> uploadPhoto(Uri uri) {
        StorageReference storageRef = storage.getReference().child("accommodationPhotos");
        StorageReference fileRef = storageRef.child(System.currentTimeMillis() + ".jpg");

        UploadTask uploadTask = fileRef.putFile(uri);

        CompletableFuture<Uri> future = new CompletableFuture<>();

        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
            Log.e("Error uploading photo", exception.getMessage());
            future.completeExceptionally(exception);
        }).addOnSuccessListener(taskSnapshot -> {
            // Get download URL
            Task<Uri> urlTask = fileRef.getDownloadUrl();
            urlTask.addOnSuccessListener(downloadUri -> {
                future.complete(downloadUri);
            }).addOnFailureListener(exception -> {
                future.completeExceptionally(exception);
            });
        });

        return future;
    }

    // --- post accommodation (save it on database)
    void postAccommodation() {
        // --- show loader
        ProgressDialog progressDialog = Utils.showProgressDialog(this, "Posting accommodation", null);

        String propType = binding.edtPropertyType.getText().toString();
        String address = binding.edtAddress.getText().toString();
        String city = binding.edtCity.getText().toString();
        String province = binding.edtProvince.getText().toString();
        String postalCode = binding.edtPostalCode.getText().toString();
        Double rent = Utils.toDouble(binding.edtRent.getText().toString());
        Integer noOfBedrooms = Utils.toInteger(binding.edtBedrooms.getText().toString());
        Double noOfBathrooms = Utils.toDouble(binding.edtBathrooms.getText().toString());
        String contactNumber = binding.edtContactNumber.getText().toString();
        String otherDetails = binding.edtOtherDetails.getText().toString();

        ArrayList<String> amenities = new ArrayList<>();
        if (binding.cbHeater.isChecked()) amenities.add("heater");
        if (binding.cbWifi.isChecked()) amenities.add("Wifi");
        if (binding.cbLaundry.isChecked()) amenities.add("laundry");
        if (binding.cbMicrowave.isChecked()) amenities.add("microwave");
        if (binding.cbFridge.isChecked()) amenities.add("fridge");
        if (binding.cbOven.isChecked()) amenities.add("oven");
        if (binding.cbWater.isChecked()) amenities.add("water");
        if (binding.cbElectricity.isChecked()) amenities.add("electricity");

        // --- upload property photos to storage and prepare list of URLs
        ArrayList<String> propertyPhotosURLs = new ArrayList<>();
        ArrayList<CompletableFuture<Uri>> uploadPhotoFutures = new ArrayList<>();
        for (Uri uri : mSelectedPhotos) {
            CompletableFuture<Uri> future = uploadPhoto(uri);
            uploadPhotoFutures.add(future);
        }

        Utils.all(uploadPhotoFutures).thenAccept(downloadUrls -> {
            for (Uri uri : downloadUrls) {
                propertyPhotosURLs.add(uri.toString());
            }

            // --- create accommodation object
            Accommodation accommodation = new Accommodation(propType, address, city, province, postalCode, rent, startDate.getTimeInMillis(), noOfBedrooms, noOfBathrooms, amenities, contactNumber, otherDetails, propertyPhotosURLs);
            db.collection("accommodations").add(accommodation).addOnSuccessListener(documentReference -> {
                // --- trigger accommodation added listener


                Utils.showToast(this, "Accommodation posted successfully");
                progressDialog.dismiss();
                setResult(RESULT_OK);
                finish();
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Utils.showMaterialAlertDialog(this, "Error", e.getMessage());
            });
        }).exceptionally(e -> {
            progressDialog.dismiss();
            Utils.showMaterialAlertDialog(this, "Error", e.getMessage());
            return null;
        });
    }

    // --- method for make testing quick by filling in inputs by some values
    void fillWithTestInputs() {
        binding.edtPropertyType.setText("House");
        binding.edtAddress.setText("123 Main Street");
        binding.edtCity.setText("Toronto");
        binding.edtProvince.setText("Ontario");
        binding.edtPostalCode.setText("M1M 1M1");
        binding.edtRent.setText("1000");
        binding.edtBedrooms.setText("2");
        binding.edtBathrooms.setText("1");
        binding.edtContactNumber.setText("1234567890");
        binding.edtOtherDetails.setText("This is a test accommodation");
        binding.cbElectricity.setChecked(true);
        binding.cbFridge.setChecked(true);
    }

    @Override
    public void onClick(View view) {
        // --- start date picker
        if (view == binding.edtStartDate) {
            Utils.showMaterialDatePickerDialog(getSupportFragmentManager(), System.currentTimeMillis(), startDate.getTimeInMillis(), new MaterialPickerOnPositiveButtonClickListener() {
                @Override
                public void onPositiveButtonClick(Object selection) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeZone(TimeZone.getTimeZone("utc"));
                    calendar.setTimeInMillis((Long) selection);
                    startDate = calendar;

                    binding.edtStartDate.setText(Utils.formatDate(startDate));
                }
            });
            return;
        }

        // --- upload photos
        if (view == binding.btnUploadPhotos) {
            Matisse.from(PostAccommodation.this).choose(MimeType.ofImage()).countable(true).maxSelectable(10).imageEngine(new GlideEngine()).showPreview(false).forResult(REQUEST_CODE_IMAGES_PICKER);
            return;
        }

        if (view == binding.btnSubmit) {
            if (!validateInputs()) return;

            postAccommodation();
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGES_PICKER && resultCode == RESULT_OK) {
            mSelectedPhotos = Matisse.obtainResult(data);

            accommodationPhotoAdapter.setList(mSelectedPhotos);
        }
    }
}