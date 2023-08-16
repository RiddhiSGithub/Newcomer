package com.example.newcomers;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.newcomers.databinding.ActivityHomeBinding;
import com.example.newcomers.fragments.RideListFragment;

public class HomeActivity extends AppCompatActivity {

     ActivityHomeBinding homeBinding;
     Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_home);
        homeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(homeBinding.getRoot());



        final int acco = R.id.acco;

        homeBinding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.home)
            {
//                intent = new Intent(this, HomeActivity.class);
//                startActivity(intent);
                replacefragment(new HomeFragment());
            }

            if(item.getItemId() == R.id.ride)
            {
                replacefragment(new RideListFragment());
            }

            if(item.getItemId() == R.id.acco)
            {
                replacefragment(new AccommodationsList());
            }
            if(item.getItemId() == R.id.profile)
            {
                replacefragment(new ProfilePage());
            }

            return true;
        });

        replacefragment(new HomeFragment());
    }




    private void replacefragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}