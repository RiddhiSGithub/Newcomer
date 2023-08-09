package com.example.newcomers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.newcomers.databinding.ActivityPostAccommodationBinding;
import com.example.newcomers.utils.Utils;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.Calendar;
import java.util.TimeZone;

public class PostAccommodation extends AppCompatActivity implements View.OnClickListener {

    ActivityPostAccommodationBinding binding;
    Calendar startDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

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

        // --- set click listeners
        binding.edtStartDate.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
    }

    // --- validate inputs
    boolean validateInputs() {
        String propType = binding.edtPropertyType.getText().toString();
        String address = binding.edtAddress.getText().toString();
        String city = binding.edtCity.getText().toString();
        String province = binding.edtProvince.getText().toString();
        String postalCode = binding.edtPostalCode.getText().toString();
        Double rent = Utils.toDouble(binding.edtRent.getText().toString());

        return true;
    }

    @Override
    public void onClick(View view) {
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

        if (view == binding.btnSubmit) {
            if (!validateInputs()) return;
            return;
        }
    }
}