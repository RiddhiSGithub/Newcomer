package com.example.newcomers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.newcomers.databinding.ActivityAccommodationDetailsBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class AccommodationDetailsActivity extends AppCompatActivity {

    ActivityAccommodationDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAccommodationDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
    }

    // --- initialize views (eg. set spinner items, set onClickListeners, etc.)
    private void initViews() {
        // --- set property photos view pager
        binding.vpPropImages.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.vpPropImages.setAdapter(new AccommodationPhotoAdapter("detailsPage"));
        binding.vpPropImages.setOffscreenPageLimit(2);

        new TabLayoutMediator(binding.layoutTabs, binding.vpPropImages, true, (tab, position) -> {
        }).attach();
    }
}