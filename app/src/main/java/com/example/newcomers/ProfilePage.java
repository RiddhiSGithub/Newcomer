package com.example.newcomers;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newcomers.databinding.FragmentProfilePageBinding;


public class ProfilePage extends Fragment implements View.OnClickListener{

    FragmentProfilePageBinding profilePageBinding;
    Intent intentProfilePage;
    public ProfilePage() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profile_page, container, false);
        profilePageBinding = FragmentProfilePageBinding.inflate(inflater, container, false);
        View view = profilePageBinding.getRoot();


        initProfilePage(); // Call the init method here
        return view;
    }

    private void initProfilePage() {
        profilePageBinding.imgProfilePic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == profilePageBinding.btnSaveChanges.getId()){
            //add validation
            if (v.getId() == profilePageBinding.imgProfilePic.getId()) {
                checkPermissionAndOpenCamera(); // Check permissions and open the camera
            } else {
                captureImage(); // Capture an image using the camera
            }
        }
    }

    private void captureImage() {
        intentProfilePage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startForResult.launch(intentProfilePage);
    }

    // Activity result launcher for camera intent
    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result != null && result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    Bitmap photo = (Bitmap) extras.get("data");
                    profilePageBinding.imgProfilePic.setImageBitmap(photo); // Set the captured image to the ImageView
                }
            }
        }
    });

    // Check permission and open the camera
    private void checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 5); // Request camera permission
        }
    }
}