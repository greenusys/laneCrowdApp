package com.example.lanecrowd.view_modal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lanecrowd.Session_Package.SessionManager
import java.util.*

class MySessionVM private constructor() : ViewModel() {
    private fun RequestInformation() {}
    private var livedata: MutableLiveData<HashMap<String, String?>>? = null
    val user = HashMap<String, String?>()

    fun getName(): MutableLiveData<HashMap<String, String?>>? {
        if (livedata == null) {
            livedata = MutableLiveData()
        }
        return livedata!!
    }




    fun StoreValueTOLiveData(
        userId: String?,
        email: String?,
        phone: String?,
        fullName: String?,
        biography: String?,
        profilePicture: String?,
        coverPhoto: String?,
        dob: String?,
        gender: String?
    ): MutableLiveData<HashMap<String, String?>> {

        if (livedata == null) {
            livedata = MutableLiveData()

        }



        user.put(SessionManager.KEY_USERID, userId!!)
        user.put(SessionManager.KEY_EMAIL, email)
        user.put(SessionManager.KEY_PHONE, phone)
        user.put(SessionManager.KEY_FULL_NAME, fullName)
        user.put(SessionManager.KEY_BIOGRAPHY, biography)
        user.put(SessionManager.KEY_PROFILE_PICTURE, profilePicture)
        user.put(SessionManager.KEY_COVER_PHOTO, coverPhoto)
        user.put(SessionManager.KEY_GENDER, gender)
        user.put(SessionManager.KEY_DOB, dob)

        livedata!!.value = user



        return livedata!!
    }




    companion object {
        private var mySessionVM: MySessionVM? = null
        @JvmStatic
        @get:Synchronized
        val instance: MySessionVM?
            get() {
                if (mySessionVM == null) {
                    mySessionVM = MySessionVM()
                    return mySessionVM
                }
                return mySessionVM
            }
    }

    init {
        RequestInformation()
    }
}