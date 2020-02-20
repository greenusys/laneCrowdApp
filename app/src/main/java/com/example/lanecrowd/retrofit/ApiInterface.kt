package com.example.lanecrowd.retrofit


import com.example.lancrowd.activity.modal.RegisterResModal
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {


    @FormUrlEncoded
    @POST("/signin")
    fun loginAPI(@Field("email") email: String, @Field("password") password: String): Call<RegisterResModal>


    @FormUrlEncoded
    @POST("/signup")
    fun registerUser(
        @Field("name") name: String, @Field("email_phone") email_phone: String, @Field(
            "password"
        ) password: String
    ): Call<RegisterResModal>


    @FormUrlEncoded
    @POST("/verify_otp")
    fun verifyOTP(
        @Field("verify_otp") verify_otp: String, @Field("otp") otp: String, @Field("email_phone") email_phone: String, @Field(
            "password"
        ) password: String, @Field("name") name: String
    ): Call<RegisterResModal>


    @Multipart
    @POST("/addPost")
    fun uploadPostAPI(
        @Part("android") android: RequestBody,
        @Part("post_type") post_type: RequestBody,
        @Part("post") post: RequestBody,
        @Part("imgageData") imgageData: RequestBody,
        @Part files: List<MultipartBody.Part>,
        @Part("user_id") user_id: RequestBody
    ): Call<JsonObject>


    @FormUrlEncoded
    @POST("/fetchpost")
    fun fetchpost(@Field("android") android: String, @Field("user_id") user_id: String, @Field("offset") offset: String): Call<JsonObject>


    @FormUrlEncoded
    @POST("/deletePost")
    fun deletePost(@Field("post_id") post_id: String): Call<JsonObject>



    @FormUrlEncoded
    @POST("/likeOrdislike")
    fun likeDislikePost(@Field("android") android: String, @Field("post_id") post_id: String, @Field("user_id") user_id: String): Call<JsonObject>



 @FormUrlEncoded
    @POST("/addComment")
    fun addComment(@Field("android") android: String,
                   @Field("post_id") post_id: String,
                   @Field("comment") comment: String,
                   @Field("commented_by_") user_id: String): Call<JsonObject>




}
