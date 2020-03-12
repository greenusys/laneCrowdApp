package com.example.lanecrowd.modal.repository

import com.example.lancrowd.activity.modal.RegisterResModal
import com.example.lanecrowd.retrofit.ApiInterface
import com.google.gson.JsonObject
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

class UserRepository(private val api: ApiInterface) {



    //for Login
    fun loginRepo(email:String,password:String): Single<RegisterResModal> {

        return api.loginAPI(email,password)
    }

    fun registerRepo(name: String, email: String, password: String, dob: String, gender: String): Single<RegisterResModal> {
        return api.registerUser(name,email,password,dob,gender)
    }


    fun verfifyOTPRepo(otp_input: String, otp: String, email_phone: String, password: String, name: String, dob: String, gender: String): Single<RegisterResModal> {
        return api.verifyOTP(otp_input,otp,email_phone,password,name,dob, gender)
    }



    //for HomePost
    fun fetchPostRepo(android: String, userId: String, offset: String): Single<JsonObject> {

        return api.fetchpost(android, userId, offset)
    }


    fun fetchStoryRepo(android: String, userId: String, offset: String): Single<JsonObject> {

        return api.fetchStory(android, userId, offset)
    }

    fun deletePostRepo(post_id: String): Single<JsonObject> {

        return api.deletePost(post_id)
    }

    fun likeDislikePostRepo(post_id:String,userId:String): Single<JsonObject> {

        return api.likeDislikePost("",post_id,userId)
    }



    //for Comments
    fun fetchCommentRepo(post_id: String, offset: String): Single<JsonObject> {

        return api.getComments(post_id, offset)
    }

    fun addCommentRepo(post_id: String, commented_by_: String, comment: String): Single<JsonObject> {

        return api.addComment("", post_id, comment, commented_by_)
    }

    fun editCommentRepo(userId:String,comment:String,c_id: String): Single<JsonObject> {

        return api.editComments(userId,comment,c_id,"")
    }

    fun deleteCommentRepo(c_id: String): Single<JsonObject> {

        return api.deleteComments(c_id)
    }

    //for change profile and cover pic
    fun changePic(from: String,files: MultipartBody.Part, userId: RequestBody, android: RequestBody): Single<RegisterResModal?>? {

        if (from.equals("profile"))
        return api.changeProfilePic(files, userId, android)

        return api.changeCoverePic(files, userId, android)

    }




}