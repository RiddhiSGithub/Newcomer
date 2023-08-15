package com.example.newcomers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.newcomers.databinding.ActivityForgotPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    ActivityForgotPasswordBinding forgotPasswordBinding;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forgotPasswordBinding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        View view = forgotPasswordBinding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        initForgotPassword();
    }

    private void initForgotPassword() {
        forgotPasswordBinding.btnChangePassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == forgotPasswordBinding.btnChangePassword.getId()) {
            if (validateInput()) {
                resetPassword();
            }
        }
    }

    private void resetPassword() {
        String email = forgotPasswordBinding.edtEmail.getText().toString();

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showAlertDialog("Success", "Password reset email sent.");
                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                            showAlertDialog("Error", "Invalid email address.");
                        } else if (exception instanceof FirebaseAuthException) {
                            showAlertDialog("Error", "Error sending password reset email.");
                        } else {
                            showAlertDialog("Error", "Password reset failed.");
                        }
                    }
                });
    }

    private boolean validateInput() {
        String email = forgotPasswordBinding.edtEmail.getText().toString();
        String password = forgotPasswordBinding.edtPassword.getText().toString();
        String confirmPassword = forgotPasswordBinding.edtCPassword.getText().toString();

        if (email.isEmpty()) {
            forgotPasswordBinding.txtLayEmail.setError("Please enter an email address.");
            return false;
        } else if (password.isEmpty()) {
            forgotPasswordBinding.txtLayPassword.setError("Please enter a password.");
            return false;
        } else if (confirmPassword.isEmpty()) {
            forgotPasswordBinding.txtLayCPassword.setError("Please enter confirm password.");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showAlertDialog("Error", "Passwords do not match.");
            return false;
        }

        // Check if user email exists in Firestore
        mFirestore.collection("users")
                .whereEqualTo("emailId", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            // Email exists in Firestore
                            resetPassword();
                        } else {
                            showAlertDialog("Error", "User with this email does not exist.");
                        }
                    } else {
                        showAlertDialog("Error", "Error checking email.");
                    }
                });

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
