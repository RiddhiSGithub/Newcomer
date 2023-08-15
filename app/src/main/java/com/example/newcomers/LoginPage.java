package com.example.newcomers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.newcomers.databinding.ActivityLoginPageBinding;


public class LoginPage extends AppCompatActivity implements View.OnClickListener {

    ActivityLoginPageBinding loginPageBinding;
    Intent logInIntent;

    Intent intentLog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginPageBinding = ActivityLoginPageBinding.inflate(getLayoutInflater());
        View view = loginPageBinding.getRoot();
        setContentView(view);

        init();
    }

    public void init() {
        loginPageBinding.btnLogIn.setOnClickListener(this);
        loginPageBinding.btnSignUp.setOnClickListener(this);
        loginPageBinding.txtForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == loginPageBinding.btnLogIn.getId()) {
            if(validation()){
                logInIntent = new Intent(this, HomeActivity.class);
                startActivity(logInIntent);
            }
        } else if (v.getId() == loginPageBinding.btnSignUp.getId()) {
            logInIntent = new Intent(this, RegistrationPage.class);
            startActivity(logInIntent);
        } else if (v.getId() == loginPageBinding.txtForgotPassword.getId()) {
            logInIntent = new Intent(this, ForgotPassword.class);
            startActivity(logInIntent);
        }
    }

    private boolean validation() {
        String username = loginPageBinding.edtUserName.getText().toString();
        String userPassword = loginPageBinding.edtPassword.getText().toString();

        if (username.isEmpty()) {
            loginPageBinding.txtLayUName.setError("Please Enter Name.");
            return false;
        }else if (!isValidUsername(username)) {
            loginPageBinding.txtLayUName.setError("Username format is invalid.");
            return false;
        }if (userPassword.isEmpty()) {
            loginPageBinding.txtLayPassword.setError("Please Enter Password.");
            return false;
        }


        return true; // Validation successful
    }


    private boolean isValidUsername(String username) {
        // Use a regular expression pattern to validate the username format
        String usernamePattern = "^[a-zA-Z0-9_]+$";
        return username.matches(usernamePattern);
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null) // You can add a listener if needed
                .show();
    }
}
