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
        ArrayList<String> images = new ArrayList<>();
        images.add("https://fastly.picsum.photos/id/114/200/300.jpg?hmac=RsaHLtW_cVJ2g7oCf2cW_kkIsaHv3QPZgv81ZYH5-aA");
        images.add("https://fastly.picsum.photos/id/114/200/300.jpg?hmac=RsaHLtW_cVJ2g7oCf2cW_kkIsaHv3QPZgv81ZYH5-aA");
        binding.vpPropImages.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.vpPropImages.setAdapter(new AccommodationPhotoAdapter(images));
        binding.vpPropImages.setOffscreenPageLimit(2);

        new TabLayoutMediator(binding.layoutTabs, binding.vpPropImages, true, (tab, position) -> {
        }).attach();
    }
}