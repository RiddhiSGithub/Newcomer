package com.example.newcomers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.newcomers.beans.User;
import com.example.newcomers.databinding.ActivityLoginPageBinding;
import com.example.newcomers.databinding.ActivityRegistrationPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistrationPage extends AppCompatActivity implements View.OnClickListener {

    ActivityRegistrationPageBinding registrationPageBinding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private final String API_KEY = "AIzaSyDDAZFRkn2stMGHE5b3uqdUgMAjT98y9xw"; // Replace with your actual API key


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_registration_page);

        registrationPageBinding = ActivityRegistrationPageBinding.inflate(getLayoutInflater());
        View view = registrationPageBinding.getRoot();
        setContentView(view);

        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        init();
    }

    private void init(){
        registrationPageBinding.btnRegistration.setOnClickListener(this);
    }

    private void registerUser() {
        String name = registrationPageBinding.edtUserName.getText().toString();
        String email = registrationPageBinding.edtEmail.getText().toString();
        String password = registrationPageBinding.edtPassword.getText().toString();
        String confirmPassword = registrationPageBinding.edtCPassword.getText().toString();

        if(name.isEmpty()){
            registrationPageBinding.txtLayUName.setError("Please Enter Name.");
        }else if(email.isEmpty()){
            registrationPageBinding.txtLayEmail.setError("Please Enter Email.");
        }else if(password.isEmpty()){
            registrationPageBinding.txtLayPassword.setError("Please Enter Password.");
        }else if(confirmPassword.isEmpty()){
            registrationPageBinding.txtLayCPassword.setError("Please Enter Confirm Password.");
        }

        if (!isValidEmail(email)) {
            registrationPageBinding.txtLayEmail.setError("Invalid email address");
            return;
        } else {
            registrationPageBinding.txtLayEmail.setError(null);
        }

//        if (!isValidPassword(password)) {
//            registrationPageBinding.txtLayPassword.setError("Password must be at least 6 characters");
//            return;
//        } else {
//            registrationPageBinding.txtLayPassword.setError(null);
//        }

        if (!password.equals(confirmPassword)) {
            registrationPageBinding.txtLayCPassword.setError("Passwords do not match");
            return;
        } else {
            registrationPageBinding.txtLayCPassword.setError(null);
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                // Create a User object and set user data
                                User user = new User();
                                user.setEmailId(email);
                                user.setPassword(password);

                                // Store user data in Firestore
                                mFirestore.collection("users")
                                        .document(firebaseUser.getUid())
                                        .set(user)
                                        .addOnSuccessListener(aVoid -> {
                                            // Registration successful, user data stored in Firestore
                                            showAlertDialog("Success", "User registered successfully");
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle Firestore write failure
                                            showAlertDialog("Error", "Error saving user data");
                                        });                            }
                        } else {
                            Exception exception = task.getException();
                            if (exception instanceof FirebaseAuthException) {
                                FirebaseAuthException authException = (FirebaseAuthException) exception;
                                String errorCode = authException.getErrorCode();

                                // Handle different FirebaseAuthException error codes
                                switch (errorCode) {
                                    case "ERROR_EMAIL_ALREADY_IN_USE":
                                        registrationPageBinding.txtLayEmail.setError("Email is already in use");
                                        break;
                                    case "ERROR_WEAK_PASSWORD":
                                        registrationPageBinding.txtLayPassword.setError("Password is too weak");
                                        break;
                                    // Handle other FirebaseAuthException error codes as needed
                                    default:
                                        showAlertDialog("Error", "Registration failed: " + authException.getMessage());
                                        break;
                                }
                            } else {
                                // Handle other types of exceptions
                                showAlertDialog("Error", "Registration failed: " + exception.getMessage());
                            }

                        }
                    }
    });
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == registrationPageBinding.btnRegistration.getId()){
            registerUser();
        }
    }

    private boolean isValidEmail(String email) {
        // Use a regular expression pattern to validate the email format
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
}
