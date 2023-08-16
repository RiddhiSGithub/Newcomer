package com.example.newcomers;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.util.Util;
import com.example.newcomers.databinding.ActivityForgotPasswordBinding;
import com.example.newcomers.utils.Utils;
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
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        showAlertDialog("Success", "Password reset email sent.");
                    } else {
                        showAlertDialog("Error", "Error sending password reset email.");
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
        Utils.showMaterialAlertDialog(this, getString(R.string.app_name), message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }
}