package com.example.newcomers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE User (userId INTEGER PRIMARY KEY AUTOINCREMENT, userName TEXT NOT NULL, userEmail TEXT NOT NULL, userPhoneNumber TEXT NOT NULL, userPassword TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS User");
        onCreate(db);
    }

    public long insertUserData(String userName, String userEmail, String userPhone, String userPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("userName", userName);
        values.put("userEmail", userEmail);
        values.put("userPhoneNumber", userPhone);
        values.put("userPassword", userPassword);

        long newRowId = db.insert("User", null, values);

//        db.close();

        return newRowId;
    }

    public boolean validateCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = { "userId" };
        String selection = "userName = ? AND userPassword = ?";
        String[] selectionArgs = { username, password };

        Cursor cursor = db.query("User", projection, selection, selectionArgs, null, null, null);

        boolean isValid = cursor.getCount() > 0;

        cursor.close();
//        db.close();

        return isValid;
    }

    public boolean isUserEmailExists(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = { "userEmail" };
        String selection = "userEmail = ?";
        String[] selectionArgs = { userEmail };

        Cursor cursor = db.query("User", projection, selection, selectionArgs, null, null, null);

        boolean emailExists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return emailExists;
    }
    public boolean updatePassword(String userEmail, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("userPassword", newPassword);

        int rowsAffected = db.update("User", values, "userEmail = ?", new String[]{userEmail});

        db.close();

        return rowsAffected > 0;
    }
}