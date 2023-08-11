package com.example.newcomers;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.newcomers.databinding.ActivityPostAccommodationBinding;
import com.example.newcomers.utils.Utils;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.SelectionCreator;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class PostAccommodation extends AppCompatActivity implements View.OnClickListener {

    ActivityPostAccommodationBinding binding;
    Calendar startDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    final int REQUEST_CODE_IMAGES_PICKER = 3244;
    List<Uri> mSelectedPhotos = new ArrayList<>();
    AccommodationPhotoAdapter accommodationPhotoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostAccommodationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
    }

    // --- initialize views (eg. set spinner items, set onClickListeners, etc.)
    private void initViews() {
        // --- set property types spinner items
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, R.array.property_types);
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
        Integer noOfBathrooms = Utils.toInteger(binding.edtBathrooms.getText().toString());
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
            binding.edtStartDate.setError("Please select available from date");
            binding.edtStartDate.requestFocus();
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