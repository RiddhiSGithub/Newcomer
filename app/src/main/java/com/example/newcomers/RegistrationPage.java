package com.example.newcomers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.newcomers.databinding.ActivityRegistrationPageBinding;


public class RegistrationPage extends AppCompatActivity implements View.OnClickListener {

    ActivityRegistrationPageBinding registrationPageBinding;
    Intent intentSignIn;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_registration_page);
        registrationPageBinding = ActivityRegistrationPageBinding.inflate(getLayoutInflater());
        View view = registrationPageBinding.getRoot();
        setContentView(view);

        init();
    }

    public void init() {
        registrationPageBinding.btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == registrationPageBinding.btnSubmit.getId()) {
            if (validateInput()) {
                saveUserData();
                intentSignIn = new Intent(this, LoginPage.class);
                startActivity(intentSignIn);

            }
        }
    }

    private boolean validateInput() {
        String userName = registrationPageBinding.edtUserName.getText().toString();
        String userEmail = registrationPageBinding.edtEmail.getText().toString();
        String userPhone = registrationPageBinding.edtPhone.getText().toString();
        String userPassword = registrationPageBinding.edtPassword.getText().toString();
        String confirmPassword = registrationPageBinding.edtCPassword.getText().toString();

        if (userName.isEmpty() || userEmail.isEmpty() || userPhone.isEmpty() ||
                userPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlertDialog("Error", "All fields are required.");
            return false;
        }

        if (!userPassword.equals(confirmPassword)) {
            showAlertDialog("Error", "Passwords do not match.");
            return false;
        }

        // You can add more validation checks here if needed

        return true; // Input is valid
    }

    private void saveUserData() {
        String userName = registrationPageBinding.edtUserName.getText().toString();
        String userEmail = registrationPageBinding.edtEmail.getText().toString();
        String userPhone = registrationPageBinding.edtPhone.getText().toString();
        String userPassword = registrationPageBinding.edtPassword.getText().toString();

        dbHelper = new DBHelper(this);
        long newRowId = dbHelper.insertUserData(userName, userEmail, userPhone, userPassword);

        if (newRowId != -1) {
            showAlertDialog("Success", "User registered successfully");
        } else {
            showAlertDialog("Error", "Error registering user");
        }
    }


    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }


}