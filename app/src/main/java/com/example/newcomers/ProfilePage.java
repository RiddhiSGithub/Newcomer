package com.example.newcomers;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.newcomers.beans.User;
import com.example.newcomers.databinding.FragmentProfilePageBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfilePage extends Fragment implements View.OnClickListener {

    FragmentProfilePageBinding profilePageBinding;
    private Intent intentProfilePage;
    private static final int CAMERA_PERMISSION_REQUEST = 5;
    private static final int GALLERY_PICK = 1;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private StorageReference storageReference;
    private FirebaseUser currentUser;

    public ProfilePage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        profilePageBinding = FragmentProfilePageBinding.inflate(inflater, container, false);
        View view = profilePageBinding.getRoot();

        initProfilePage();

        return view;
    }

    MaterialToolbar getActionBar() {
        MaterialToolbar actionBar = null;
        ViewGroup viewGroup = getActivity().findViewById(R.id.actionBar);
        if (viewGroup != null)
            actionBar = (MaterialToolbar) viewGroup.findViewById(R.id.materialToolbar);
        return actionBar;
    }

    private void initProfilePage() {

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        profilePageBinding.imgProfilePic.setOnClickListener(v -> selectImageFromGallery());
        profilePageBinding.btnSaveChanges.setOnClickListener(this);
        profilePageBinding.btnLogOut.setOnClickListener(this);

        MaterialToolbar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.getMenu().clear();
            actionBar.setTitle(R.string.profile);
        }
    }

    private void selectImageFromGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), GALLERY_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            StorageReference filePath = storageReference.child("profile_images").child(currentUser.getUid() + ".jpg");

            filePath.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(getContext(), "Profile picture uploaded", Toast.LENGTH_SHORT).show();
                loadUserProfileImage(currentUser.getUid());
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Failed to upload profile picture", Toast.LENGTH_SHORT).show();
            });
        }
    }
    private void loadUserProfileImage(String userId) {
        StorageReference profileImageRef = storageReference.child("profile_images").child(userId + ".jpg");
        profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.account_icon)
                    .error(R.drawable.account_icon);

            Glide.with(requireContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(uri)
                    .into(profilePageBinding.imgProfilePic);
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Failed to load profile picture", Toast.LENGTH_SHORT).show();
        });
    }
        @Override
    public void onClick(View v) {
        if (v.getId() == profilePageBinding.btnSaveChanges.getId()) {
            saveUserData();
        } else if (v.getId() == profilePageBinding.imgProfilePic.getId()) {
            checkPermissionAndOpenCamera();
        }
        if(v.getId()==profilePageBinding.btnLogOut.getId()){
            logoutUser();
        }
    }

    private void captureImage() {
        intentProfilePage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startForResult.launch(intentProfilePage);
    }

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result != null && result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    Bitmap photo = (Bitmap) extras.get("data");
                    profilePageBinding.imgProfilePic.setImageBitmap(photo);
                }
            }
        }
    });


    private void checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        } else {
            captureImage();
        }
    }

    private boolean validateUserData() {
        String newUsername = profilePageBinding.edtUserName.getText().toString();
        String newEmail = profilePageBinding.edtEmail.getText().toString();
        String newPhoneNumber = profilePageBinding.edtPhone.getText().toString();
        String newAddress = profilePageBinding.edtAddress.getText().toString();
        String newCity = profilePageBinding.edtCity.getText().toString();
        String newProvince = profilePageBinding.edtProvince.getText().toString();
        String newPostalCode = profilePageBinding.edtPcode.getText().toString();

        if (TextUtils.isEmpty(newUsername)) {
            profilePageBinding.edtUserName.setError("Username is required");
            return false;
        }

        if (TextUtils.isEmpty(newEmail)) {
            profilePageBinding.edtEmail.setError("Email is required");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            profilePageBinding.edtEmail.setError("Invalid email format");
            return false;
        }

        if (TextUtils.isEmpty(newPhoneNumber)) {
            profilePageBinding.edtPhone.setError("Phone number is required");
            return false;
        }

        if (!Patterns.PHONE.matcher(newPhoneNumber).matches()) {
            profilePageBinding.edtPhone.setError("Invalid phone number");
            return false;
        }

        if (TextUtils.isEmpty(newAddress)) {
            profilePageBinding.edtAddress.setError("Address is required");
            return false;
        }

        if (TextUtils.isEmpty(newCity)) {
            profilePageBinding.edtCity.setError("City is required");
            return false;
        }

        if (TextUtils.isEmpty(newProvince)) {
            profilePageBinding.edtProvince.setError("Province is required");
            return false;
        }

        if (TextUtils.isEmpty(newPostalCode)) {
            profilePageBinding.edtPcode.setError("Postal code is required");
            return false;
        }

        return true;
    }

    private void saveUserData() {
        if (validateUserData()) {

            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                mFirestore = FirebaseFirestore.getInstance();

                String newUsername = profilePageBinding.edtUserName.getText().toString();
                String newPhoneNumber = profilePageBinding.edtPhone.getText().toString();
                String newAddress = profilePageBinding.edtAddress.getText().toString();
                String newCity = profilePageBinding.edtCity.getText().toString();
                String newProvince = profilePageBinding.edtProvince.getText().toString();
                String newPostalCode = profilePageBinding.edtPcode.getText().toString();

                User updatedUser = new User();
                updatedUser.setUsername(newUsername);
                updatedUser.setPhoneNumber(newPhoneNumber);
                updatedUser.setStreetAddress(newAddress);
                updatedUser.setCity(newCity);
                updatedUser.setProvince(newProvince);
                updatedUser.setPostalCode(newPostalCode);

                // Update the user data in Firestore
                mFirestore.collection("users")
                        .document(currentUser.getUid())
                        .set(updatedUser)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "User data updated successfully", Toast.LENGTH_SHORT).show();
                            emptyEditField();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Failed to update user data", Toast.LENGTH_SHORT).show();
                        });
            }
        }
    }


    private void emptyEditField() {
        profilePageBinding.edtUserName.setText("");
        profilePageBinding.edtPhone.setText("");
        profilePageBinding.edtEmail.setText("");
        profilePageBinding.edtAddress.setText("");
        profilePageBinding.edtCity.setText("");
        profilePageBinding.edtProvince.setText("");
        profilePageBinding.edtPcode.setText("");
    }

    private void logoutUser() {
        // Clear shared preferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(requireContext(), LoginPage.class);
        startActivity(intent);
        requireActivity().finish();
    }
}
