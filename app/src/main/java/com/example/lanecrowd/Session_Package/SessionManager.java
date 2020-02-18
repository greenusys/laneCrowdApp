package com.example.lanecrowd.Session_Package;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.lanecrowd.activity.HomeActivity;
import com.example.lanecrowd.activity.Login_Activity;

import java.util.HashMap;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "lane_crowd";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";


    public static final String KEY_USERID = "id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_FULL_NAME = "full_name";
    public static final String KEY_BIOGRAPHY= "biography";
    public static final String KEY_PROFILE_PICTURE = "profile_pic";

    public static final String KEY_COVER_PHOTO = "cover_photo";


    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    /**
     * Create login session
     */
    public void createLoginSession(String userId,String email,String phone,String fullName,String biography,String profilePicture,String coverPhoto) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);


        editor.putString(KEY_USERID, userId);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_FULL_NAME, fullName);
        editor.putString(KEY_BIOGRAPHY, biography);
        editor.putString(KEY_PROFILE_PICTURE, profilePicture);
        editor.putString(KEY_COVER_PHOTO, coverPhoto);


        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (this.isLoggedIn()) {
            Intent i = new Intent(_context, HomeActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }


    }


    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();




        user.put(KEY_USERID, pref.getString(KEY_USERID, ""));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, ""));
        user.put(KEY_PHONE, pref.getString(KEY_PHONE, ""));
        user.put(KEY_FULL_NAME, pref.getString(KEY_FULL_NAME, ""));
        user.put(KEY_BIOGRAPHY, pref.getString(KEY_BIOGRAPHY, ""));
        user.put(KEY_PROFILE_PICTURE, pref.getString(KEY_PROFILE_PICTURE, ""));
        user.put(KEY_COVER_PHOTO, pref.getString(KEY_COVER_PHOTO, ""));

        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Login_Activity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}