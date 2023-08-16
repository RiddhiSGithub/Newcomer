package com.example.newcomers;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newcomers.databinding.ActivityForgotPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;
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

        mFirestore.collection("users")
                .whereEqualTo("emailId", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        mAuth.sendPasswordResetEmail(email)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        showAlertDialog("Success", "Password reset email sent.");
                                    } else {
                                        showAlertDialog("Error", "Error sending password reset email.");
                                    }
                                });
                    } else {
                        showAlertDialog("Error", "User with this email does not exist.");
                    }
                });
    }

    private boolean validateInput() {
        String email = forgotPasswordBinding.edtEmail.getText().toString();

        if (email.isEmpty()) {
            forgotPasswordBinding.txtLayEmail.setError("Please enter an email address.");
            return false;
        }

        return true;
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}