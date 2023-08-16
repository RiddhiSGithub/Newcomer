package com.example.newcomers;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newcomers.beans.User;
import com.example.newcomers.databinding.ActivityRegistrationPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistrationPage extends AppCompatActivity implements View.OnClickListener {

    private ActivityRegistrationPageBinding registrationPageBinding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        registrationPageBinding = ActivityRegistrationPageBinding.inflate(getLayoutInflater());
        View view = registrationPageBinding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        init();
    }

    private void init() {
        registrationPageBinding.btnRegistration.setOnClickListener(this);
    }

    private boolean registerUser() {
        String name = registrationPageBinding.edtUserName.getText().toString();
        String email = registrationPageBinding.edtEmail.getText().toString();
        String phone = registrationPageBinding.edtPhone.getText().toString();
        String password = registrationPageBinding.edtPassword.getText().toString();
        String confirmPassword = registrationPageBinding.edtCPassword.getText().toString();

        if (name.isEmpty()) {
            registrationPageBinding.edtUserName.setError("Please Enter Name.");
            registrationPageBinding.edtUserName.requestFocus();
            return false;
        }
        if (!isValidEmail(email)) {
            registrationPageBinding.edtEmail.setError("Invalid email address");
            return false;
        } else {
            registrationPageBinding.edtEmail.setError(null);
        }
        if (phone.isEmpty()) {
            registrationPageBinding.edtPhone.setError("Please Enter Phone Number.");
            registrationPageBinding.edtPhone.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            registrationPageBinding.edtPassword.setError("Please Enter Password.");
            registrationPageBinding.edtPassword.requestFocus();
            return false;
        }
        if (confirmPassword.isEmpty()) {
            registrationPageBinding.edtCPassword.setError("Please Enter Confirm Password.");
            registrationPageBinding.edtCPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            registrationPageBinding.edtCPassword.setError("Passwords do not match");
            return false;
        } else {
            registrationPageBinding.edtCPassword.setError(null);
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                User user = new User();
                                user.setUsername(name);
                                user.setPhoneNumber(phone);

                                mFirestore.collection("users")
                                        .document(firebaseUser.getUid())
                                        .set(user)
                                        .addOnSuccessListener(aVoid -> {
                                            showAlertDialog("Success", "User registered successfully");
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            showAlertDialog("Error", "Error saving user data");
                                        });
                            }
                        } else {
                            // Handle registration failure
                            handleRegistrationFailure(task.getException());
                        }
                    }
                });
        return false;
    }

    private void handleRegistrationFailure(Exception exception) {
        if (exception instanceof FirebaseAuthException) {
            FirebaseAuthException authException = (FirebaseAuthException) exception;
            String errorCode = authException.getErrorCode();


            switch (errorCode) {
                case "ERROR_EMAIL_ALREADY_IN_USE":
                    registrationPageBinding.edtEmail.setError("Email is already in use");
                    break;
                case "ERROR_WEAK_PASSWORD":
                    registrationPageBinding.edtPassword.setError("Password is too weak");
                    break;
                default:
                    showAlertDialog("Error", "Registration failed: " + authException.getMessage());
                    break;
            }
        } else {
            showAlertDialog("Error", "Registration failed: " + exception.getMessage());
        }
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
        if (v.getId() == registrationPageBinding.btnRegistration.getId()) {
            registerUser();
        }
    }

    private boolean isValidEmail(String email) {
        // Use a regular expression pattern to validate the email format
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
}
