package com.example.aimentor.repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.aimentor.databases.SqliteDbHelper;
import com.example.aimentor.models.UserModel;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class UserRepository extends SqliteDbHelper {
    public UserRepository(@Nullable Context context) {
        super(context);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getCurrentDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZonedDateTime zoneDt = ZonedDateTime.now();
        return dtf.format(zoneDt); // lay ra duoc ngay thang hien tai va gio phut giay
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public long saveUserAccount(String username, String password, String email, String phone){
        String currentDate = getCurrentDate();
        ContentValues values = new ContentValues();
        values.put(USERNAME_USER, username);
        values.put(PASSWORD_USER, password);
        values.put(EMAIL_USER, email);
        values.put(PHONE_USER, phone);
        values.put(ROLE_USER, 1);
        values.put(CREATED_AT, currentDate);
        SQLiteDatabase db = this.getWritableDatabase();
        long insert = db.insert(TABLE_USERS, null, values);
        db.close();
        return  insert;
    }

    @SuppressLint("Range")
    public UserModel loginUser(String username, String password){
        UserModel user = new UserModel();
        // tao 1 mot mang chua thong tin du lieu cua tai khoan
        String[] cols = {ID_USER, USERNAME_USER, EMAIL_USER, PHONE_USER, ROLE_USER};
        // Select id, username, email, phone, role from Users where username = ? and password = ?
        String condition = USERNAME_USER + " =? AND " + PASSWORD_USER + " =? ";
        String[] params = { username, password };
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.query(TABLE_USERS, cols, condition, params, null, null, null);
        if (data.getCount() > 0){
            data.moveToFirst();
            user.setId(data.getInt(data.getColumnIndex(ID_USER)));
            user.setUsername(data.getString(data.getColumnIndex(USERNAME_USER)));
            user.setEmail(data.getString(data.getColumnIndex(EMAIL_USER)));
            user.setPhone(data.getString(data.getColumnIndex(PHONE_USER)));
            user.setRole(data.getInt(data.getColumnIndex(ROLE_USER)));
        }
        data.close();
        db.close();
        return user;
    }

    @SuppressLint("Range")
    public UserModel getUserById(int id){
        UserModel user = new UserModel();
        String[] cols = {ID_USER, USERNAME_USER, EMAIL_USER, PHONE_USER, ROLE_USER, PASSWORD_USER};
        String condition = ID_USER + " =? ";
        String[] params = { String.valueOf(id) };
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.query(TABLE_USERS, cols, condition, params, null, null, null);
        if (data.getCount() > 0){
            data.moveToFirst();
            user.setId(data.getInt(data.getColumnIndex(ID_USER)));
            user.setUsername(data.getString(data.getColumnIndex(USERNAME_USER)));
            user.setEmail(data.getString(data.getColumnIndex(EMAIL_USER)));
            user.setPhone(data.getString(data.getColumnIndex(PHONE_USER)));
            user.setRole(data.getInt(data.getColumnIndex(ROLE_USER)));
            user.setPassword(data.getString(data.getColumnIndex(PASSWORD_USER)));
        }
        data.close();
        db.close();
        return user;
    }

    public boolean updatePassword(int userId, String newPassword){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PASSWORD_USER, newPassword);
        int rows = db.update(TABLE_USERS, values, ID_USER + " =? ", new String[]{String.valueOf(userId)});
        db.close();
        return rows > 0;
    }
}
