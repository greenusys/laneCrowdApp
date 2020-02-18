package com.example.lanecrowd.retrofit


import com.example.lancrowd.activity.modal.RegisterResModal
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiInterface {


    @FormUrlEncoded
    @POST("/signin")
    fun loginAPI(@Field("email") email: String, @Field("password") password: String): Call<RegisterResModal>


    @FormUrlEncoded
    @POST("/signup")
    fun registerUser(@Field("name") name: String, @Field("email_phone") email_phone: String, @Field("password") password: String): Call<RegisterResModal>


    @FormUrlEncoded
    @POST("/verify_otp")
    fun verifyOTP(@Field("verify_otp") verify_otp: String, @Field("otp") otp: String, @Field("email_phone") email_phone: String, @Field("password") password: String, @Field("name") name: String): Call<RegisterResModal>


    @Multipart
    @POST("/addPost")
    fun uploadPostAPI(@Part("android") android: RequestBody,
                      @Part("post_type") post_type: RequestBody,
                      @Part("post") post: RequestBody,
                      @Part("imgageData") imgageData: RequestBody,
                      @Part files: List<MultipartBody.Part>,
                      @Part("user_id") user_id: RequestBody): Call<JsonObject>


    @FormUrlEncoded
    @POST("/fetchpost")
    fun fetchpost(@Field("android") android: String, @Field("user_id") user_id: String, @Field("offset") offset: String): Call<JsonObject>



}
