package com.example.newcomers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.newcomers.databinding.ActivityPostAccommodationBinding;
import com.google.android.material.datepicker.MaterialDatePicker;

public class PostAccommodation extends AppCompatActivity {

    ActivityPostAccommodationBinding binding;

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

        // --- set available from date picker
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select a date");
        MaterialDatePicker datePicker = builder.build();
        binding.edtStartDate.setOnClickListener(v -> datePicker.show(getSupportFragmentManager(), "START_DATE_PICKER"));
    }


}