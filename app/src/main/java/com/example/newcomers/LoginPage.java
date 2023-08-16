package com.example.newcomers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.util.Util;
import com.example.newcomers.databinding.ActivityLoginPageBinding;
import com.example.newcomers.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginPage extends AppCompatActivity implements View.OnClickListener {

    private ActivityLoginPageBinding loginPageBinding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginPageBinding = ActivityLoginPageBinding.inflate(getLayoutInflater());
        View view = loginPageBinding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();

        if (Utils.getCurrentUserID() != null) {
            navigateToHome();
        }

        init();
    }

    private void init() {
        loginPageBinding.btnLogIn.setOnClickListener(this);
        loginPageBinding.btnSignUp.setOnClickListener(this);
        loginPageBinding.txtForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == loginPageBinding.btnLogIn.getId()) {
            validateAndLogin();
        } else if (v.getId() == loginPageBinding.btnSignUp.getId()) {
            Intent signUpIntent = new Intent(this, RegistrationPage.class);
            startActivity(signUpIntent);
        } else if (v.getId() == loginPageBinding.txtForgotPassword.getId()) {
            Intent forgotPasswordIntent = new Intent(this, ForgotPassword.class);
            startActivity(forgotPasswordIntent);
        }
    }

    private void validateAndLogin() {
        String email = loginPageBinding.edtUserEmail.getText().toString();
        String password = loginPageBinding.edtPassword.getText().toString();

        if (email.isEmpty()) {
            loginPageBinding.txtLayUEmail.setError("Please Enter Email.");
            return;
        }
        if (password.isEmpty()) {
            loginPageBinding.txtLayPassword.setError("Please Enter Password.");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            navigateToHome();
                        } else {
                            Exception exception = task.getException();
                            if (exception instanceof FirebaseAuthInvalidUserException
                                    || exception instanceof FirebaseAuthInvalidCredentialsException) {
                                showAlertDialog("Login Error", "Invalid email or password.");
                            } else {
                                showAlertDialog("Login Error", "An error occurred during login.");
                            }
                        }
                    }
                });
    }

    private void navigateToHome() {
        Intent homeIntent = new Intent(LoginPage.this, HomeActivity.class);
        startActivity(homeIntent);
        finish();
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
