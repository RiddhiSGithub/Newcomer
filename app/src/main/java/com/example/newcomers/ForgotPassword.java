package com.example.newcomers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.newcomers.databinding.ActivityForgotPasswordBinding;
import com.example.newcomers.databinding.ActivityLoginPageBinding;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    ActivityForgotPasswordBinding forgotPasswordBinding;
    DBHelper dbHelper;
    Intent intentForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forgotPasswordBinding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        View view = forgotPasswordBinding.getRoot();
        setContentView(view);

        initForgotPassword();
    }

    private void initForgotPassword() {
        forgotPasswordBinding.btnChangePassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == forgotPasswordBinding.btnChangePassword.getId()) {
            if (validateInput()) {
                String email = forgotPasswordBinding.edtEmail.getText().toString();
                String newPassword = forgotPasswordBinding.edtPassword.getText().toString();

                dbHelper = new DBHelper(this);if (dbHelper.isUserEmailExists(email)) {
                    boolean passwordChanged = dbHelper.updatePassword(email, newPassword);

                    if (passwordChanged) {
                        showAlertDialog("Success", "Password changed successfully");
                        intentForgotPassword = new Intent(this, LoginPage.class);
                        startActivity(intentForgotPassword);
                    } else {
                        showAlertDialog("Error", "Failed to change password. Please try again.");
                    }
                } else {
                    showAlertDialog("Error", "User email not found.");
                }
            }
        }
    }

    private boolean validateInput() {
        String email = forgotPasswordBinding.edtEmail.getText().toString();
        String password = forgotPasswordBinding.edtPassword.getText().toString();
        String confirmPassword = forgotPasswordBinding.edtCPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlertDialog("Error", "All fields are required.");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showAlertDialog("Error", "Passwords do not match.");
            return false;
        }

        // add if user email is exist in database in user table

        return true; // Input is valid
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
