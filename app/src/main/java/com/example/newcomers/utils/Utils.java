package com.example.newcomers.utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.newcomers.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class Utils {
    // --- show material alert dialog
    public static void showMaterialAlertDialog(Context context, String title, String message) {
        new MaterialAlertDialogBuilder(context).setTitle(title).setMessage(message).setPositiveButton("OK", null).show();
    }

    // -- override method to show material alert dialog with positive button click listener
    public static void showMaterialAlertDialog(Context context, String title, String message, DialogInterface.OnClickListener positiveButtonClickListener) {
        new MaterialAlertDialogBuilder(context).setTitle(title).setMessage(message).setPositiveButton("OK", positiveButtonClickListener).show();
    }

    // --- get shared preference
    private static SharedPreferences getSharedPref(Context context) {
        return context.getSharedPreferences("NewComers", Context.MODE_PRIVATE);
    }

    // --- save data to shared preferences
    public static void savePref(Context context, String key, String value) {
        getSharedPref(context).edit().putString(key, value).apply();
    }

    // --- save data to shared preferences
    public static void savePref(Context context, String key, Boolean value) {
        getSharedPref(context).edit().putBoolean(key, value).apply();
    }

    // --- get data from shared preferences
    public static <T> T getPref(Context context, String key, Class<T> type) {
        SharedPreferences preferences = getSharedPref(context);

        try {
            if (type.getSimpleName().equals(String.class.getSimpleName())) {
                return type.cast(preferences.getString(key, null));
            } else if (type.getSimpleName().equals(int.class.getSimpleName()))
                return type.cast(preferences.getInt(key, -1));
            else if (type.getSimpleName().equals(Boolean.class.getSimpleName()))
                return type.cast(preferences.getBoolean(key, false));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // --- remove data from shared preferences
    public static void removePref(Context context, String key) {
        getSharedPref(context).edit().remove(key).apply();
    }

    // --- clear all preferences
    public static void clearPref(Context context) {
        getSharedPref(context).edit().clear().apply();
    }

    // --- utility method to convert string to integer, returns null if string is not a valid integer
    public static Integer toInteger(String string) {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
            return null;
        }
    }

    // --- utility method to convert string to double, returns null if string is not a valid double
    public static Double toDouble(String string) {
        try {
            return Double.parseDouble(string);
        } catch (Exception e) {
            return null;
        }
    }

    // --- show toast
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // --- show material date picker dialog
    public static MaterialDatePicker showMaterialDatePickerDialog(FragmentManager fragmentManager, long minMillis, long selectedMillis, MaterialPickerOnPositiveButtonClickListener positiveButtonClickListener) {
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();

        // --- set constraints (eg. min date)
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        if (minMillis > 0) {
            CalendarConstraints.DateValidator dateValidator = DateValidatorPointForward.from(minMillis);
            constraintsBuilder.setValidator(dateValidator);
        }
        builder.setCalendarConstraints(constraintsBuilder.build());

        // --- set default selection
        builder.setSelection(selectedMillis > 0 ? selectedMillis : System.currentTimeMillis());

        MaterialDatePicker datePicker = builder.build();

        // --- add positive button click listener
        if (positiveButtonClickListener != null)
            datePicker.addOnPositiveButtonClickListener(positiveButtonClickListener);

        datePicker.show(fragmentManager, null);

        return datePicker;
    }

    // --- formate date into dd/MM/yyyy
    public static String formatDate(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setTimeZone(calendar.getTimeZone());
        return sdf.format(calendar.getTime());
    }

    // --- wait for all completable futures to complete
    public static <T> CompletableFuture<List<T>> all(List<CompletableFuture<T>> futures) {
        CompletableFuture[] cfs = futures.toArray(new CompletableFuture[futures.size()]);

        return CompletableFuture.allOf(cfs).thenApply(ignored -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
    }

    // --- show progress dialog
    public static ProgressDialog showProgressDialog(Context context, String title, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title != null ? title : context.getString(R.string.app_name));
        progressDialog.setMessage(message != null ? message : "Please wait...");
        progressDialog.show();
        return progressDialog;
    }

    // --- make call to the given number
    public static void makeCall(Context context, String number) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number)));
    }

    // --- get current user id
    public static String getCurrentUserID() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null) return firebaseUser.getUid();
        else return null;
    }

    // --- get currently logged in user
    public static FirebaseUser getCurrentUser() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.getCurrentUser();
    }
}
