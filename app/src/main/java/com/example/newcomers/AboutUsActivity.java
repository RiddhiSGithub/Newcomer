package com.example.newcomers;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newcomers.databinding.ActivityAboutUsBinding;

public class AboutUsActivity extends AppCompatActivity {

    ActivityAboutUsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }
    private void init(){
        // --- set back button on toolbar
        binding.actionBar.materialToolbar.setNavigationIcon(R.drawable.baseline_keyboard_backspace_24);
        binding.actionBar.materialToolbar.setNavigationOnClickListener(view -> finish());
        binding.actionBar.materialToolbar.setTitle(R.string.about_us_title);
    }
}