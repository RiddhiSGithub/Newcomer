package com.example.newcomers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.newcomers.databinding.ActivityLoginPageBinding;

public class LoginPage extends AppCompatActivity implements View.OnClickListener{

    ActivityLoginPageBinding loginPageBinding;

    Intent intentLog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login_page);
        loginPageBinding = ActivityLoginPageBinding.inflate(getLayoutInflater());
        View view = loginPageBinding.getRoot();
        setContentView(view);
        init();
    }

    public void init(){
        loginPageBinding.btnSignIn.setOnClickListener(this);
        loginPageBinding.btnForgotPassword.setOnClickListener(this);
        loginPageBinding.btnLogIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==loginPageBinding.btnLogIn.getId()){
            Validation();
        } else if (v.getId()==loginPageBinding.btnForgotPassword.getId()) {
            launchForgotPasswordScreen();
        } else if (v.getId()==loginPageBinding.btnSignIn.getId()) {
            launchSignInScreen();
        }
    }

//edit test validation
    private void Validation() {
        String username = loginPageBinding.edtUserName.getText().toString().trim();
        String password = loginPageBinding.edtPassword.getText().toString().trim();

        if (username.isEmpty()) {
            loginPageBinding.edtUserName.setError("Username is required");
            return;
        }

        if (password.isEmpty()) {
            loginPageBinding.edtPassword.setError("Password is required");
            return;
        }

//        // Assuming you have a more advanced validation logic, like checking against a database.
//        if (!isValidCredentials(username, password)) {
//            loginPageBinding.editTextPassword.setError("Invalid username or password");
//            return;
//        }
//
//        // Validation passed, proceed with login logic.
//        performLogin(username);
    }
//    private boolean isValidCredentials(String username, String password) {
//        // You would perform your authentication logic here, e.g., checking against a database.
//        // For demonstration purposes, let's consider a simple example.
//        return username.equals("validUsername") && password.equals("validPassword");
//    }
//
//    private void performLogin(String username) {
//        // Here, you can handle the successful login, such as saving user data to preferences,
//        // navigating to the main activity, etc.
//        // For now, let's just show a toast.
//        Toast.makeText(this, "Logged in as " + username, Toast.LENGTH_SHORT).show();
//    }


//    launching Forgot page.
    private void launchForgotPasswordScreen(){
    // we will write the code to switch to ForgotPassword page
        intentLog = new Intent(this,ForgotPassword.class);
        startActivity(intentLog);
    }
    private void launchSignInScreen(){
    // we will write the code to switch to ForgotPassword page
        intentLog = new Intent(this,RegistrationPage.class);
        startActivity(intentLog);
    }
}