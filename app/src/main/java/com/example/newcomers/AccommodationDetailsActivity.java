package com.example.newcomers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.example.newcomers.adapters.AccommodationPhotoAdapter;
import com.example.newcomers.beans.Accommodation;
import com.example.newcomers.databinding.ActivityAccommodationDetailsBinding;
import com.example.newcomers.utils.Utils;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;

public class AccommodationDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityAccommodationDetailsBinding binding;
    Accommodation accommodation;
    AccommodationPhotoAdapter accommodationPhotoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAccommodationDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
        initData();
    }

    @Override
    public void onClick(View view) {
        // --- handle call button click
        if (view == binding.btnCall) {
            Utils.makeCall(this, accommodation.getContactNo());
            return;
        }
    }

    // --- initialize views (eg. set spinner items, set onClickListeners, etc.)
    private void initViews() {
        // --- set property photos view pager
        binding.vpPropImages.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        accommodationPhotoAdapter = new AccommodationPhotoAdapter("detailsPage");
        binding.vpPropImages.setAdapter(accommodationPhotoAdapter);
        binding.vpPropImages.setOffscreenPageLimit(2);

        new TabLayoutMediator(binding.layoutTabs, binding.vpPropImages, true, (tab, position) -> {
        }).attach();

        // --- set click listeners
        binding.btnCall.setOnClickListener(this);

        // --- set back button on toolbar
        binding.actionBar.materialToolbar.setNavigationIcon(R.drawable.baseline_keyboard_backspace_24);
        binding.actionBar.materialToolbar.setNavigationOnClickListener(view -> finish());
    }

    // --- initialize data (eg. load data from server on UI)
    void initData() {
        Serializable accommodationData = getIntent().getSerializableExtra("accommodation");
        if (accommodationData == null) finish(); // --- if no data, close activity
        accommodation = (Accommodation) accommodationData;

        // --- attach data to UI views
        binding.txtPropType.setText(accommodation.getPropertyType());
        binding.txtAddress.setText(accommodation.getAddress() + "\n" + accommodation.getCity() + ", " + accommodation.getProvince() + "\n" + accommodation.getPostalCode());
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(accommodation.getStartDate());
        binding.txtAvailableFrom.setText(Utils.formatDate(calendar));
        binding.txtBedrooms.setText(accommodation.getNoOfBedrooms() + " bedrooms");
        binding.txtBathrooms.setText(accommodation.getNoOfBathrooms() + " bathrooms");
        binding.txtRent.setText("$" + accommodation.getRent());
        binding.txtAmenities.setText(String.join(", ", accommodation.getAmenities()));
        binding.txtOtherDetails.setText(accommodation.getOtherDetails());
        binding.txtContact.setText(accommodation.getContactNo());
        accommodationPhotoAdapter.setList(accommodation.getImageURLs());
    }
}