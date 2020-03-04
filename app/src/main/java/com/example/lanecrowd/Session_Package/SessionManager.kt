package com.example.lanecrowd.Session_Package

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.lanecrowd.activity.HomeActivity
import com.example.lanecrowd.activity.Login_Activity
import java.util.*

class SessionManager(// Context
    var _context: Context
) {
    // Shared Preferences
    var pref: SharedPreferences
    // Editor for Shared preferences
    var editor: SharedPreferences.Editor
    // Shared pref mode
    var PRIVATE_MODE = 0

    /**
     * Create login session
     */
    fun createLoginSession(
        userId: String?,
        email: String?,
        phone: String?,
        fullName: String?,
        biography: String?,
        profilePicture: String?,
        coverPhoto: String?
    ) { // Storing login value as TRUE
        editor.putBoolean(
            IS_LOGIN,
            true
        )
        editor.putString(
            KEY_USERID,
            userId
        )
        editor.putString(
            KEY_EMAIL,
            email
        )
        editor.putString(
            KEY_PHONE,
            phone
        )
        editor.putString(
            KEY_FULL_NAME,
            fullName
        )
        editor.putString(
            KEY_BIOGRAPHY,
            biography
        )
        editor.putString(
            KEY_PROFILE_PICTURE,
            profilePicture
        )
        editor.putString(
            KEY_COVER_PHOTO,
            coverPhoto
        )
        // commit changes
        editor.commit()
    }

    fun updateProfilePic(profilePicture: String?) {
        editor.putString(
            KEY_PROFILE_PICTURE,
            profilePicture
        )
        // commit changes
        editor.commit()
    }

    fun updateCoverPic(coverPhoto: String?) {
        editor.putString(
            KEY_COVER_PHOTO,
            coverPhoto
        )
        // commit changes
        editor.commit()
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    fun checkLogin() { // Check login status
        if (isLoggedIn) {
            val i = Intent(_context, HomeActivity::class.java)
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            // Add new Flag to start new Activity
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            // Staring Login Activity
            _context.startActivity(i)
        }
    }

    // return user
    val userDetails: HashMap<String, String?>
        get() {
            val user =
                HashMap<String, String?>()
            user[KEY_USERID] = pref.getString(
                KEY_USERID,
                ""
            )
            user[KEY_EMAIL] = pref.getString(
                KEY_EMAIL,
                ""
            )
            user[KEY_PHONE] = pref.getString(
                KEY_PHONE,
                ""
            )
            user[KEY_FULL_NAME] = pref.getString(
                KEY_FULL_NAME,
                ""
            )
            user[KEY_BIOGRAPHY] = pref.getString(
                KEY_BIOGRAPHY,
                ""
            )
            user[KEY_PROFILE_PICTURE] = pref.getString(
                KEY_PROFILE_PICTURE,
                ""
            )
            user[KEY_COVER_PHOTO] = pref.getString(
                KEY_COVER_PHOTO,
                ""
            )
            // return user
            return user
        }

    /**
     * Clear session details
     */
    fun logoutUser() { // Clearing all data from Shared Preferences
        editor.clear()
        editor.commit()
        // After logout redirect user to Loing Activity
        val i = Intent(_context, Login_Activity::class.java)
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // Add new Flag to start new Activity
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        // Staring Login Activity
        _context.startActivity(i)
    }

    /**
     * Quick check for login
     */
// Get Login State
    val isLoggedIn: Boolean
        get() = pref.getBoolean(
            IS_LOGIN,
            false
        )

    companion object {
        // Sharedpref file name
        private const val PREF_NAME = "lane_crowd"
        // All Shared Preferences Keys
        private const val IS_LOGIN = "IsLoggedIn"
        const val KEY_USERID = "id"
        const val KEY_EMAIL = "email"
        const val KEY_PHONE = "phone"
        const val KEY_FULL_NAME = "full_name"
        const val KEY_BIOGRAPHY = "biography"
        const val KEY_PROFILE_PICTURE = "profile_pic"
        const val KEY_COVER_PHOTO = "cover_photo"
    }

    // Constructor
    init {
        pref = _context.getSharedPreferences(
            PREF_NAME,
            PRIVATE_MODE
        )
        editor = pref.edit()
    }
}