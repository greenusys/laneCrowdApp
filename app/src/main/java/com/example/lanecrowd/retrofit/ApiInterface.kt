package com.example.lanecrowd.retrofit


import com.example.lancrowd.activity.modal.RegisterResModal
import com.google.gson.JsonObject
import okhttp3.*
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


    @FormUrlEncoded
    @POST("/fetchpost")
    fun fetchpost(@Field("android") android: String, @Field("user_id") user_id: String, @Field("offset") offset: String): Call<JsonObject>


    @FormUrlEncoded
    @POST("/fetchstories")
    fun fetchStory(@Field("android") android: String, @Field("user_id") user_id: String, @Field("offset") offset: String): Call<JsonObject>


    @FormUrlEncoded
    @POST("/deletePost")
    fun deletePost(@Field("post_id") post_id: String): Call<JsonObject>


    @FormUrlEncoded
    @POST("/likeOrdislike")
    fun likeDislikePost(
        @Field("android") android: String, @Field("post_id") post_id: String, @Field(
            "user_id"
        ) user_id: String
    ): Call<JsonObject>


    @FormUrlEncoded
    @POST("/getComments")
    fun getComments(@Field("post_id") post_id: String, @Field("offset") offset: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("/deleteComment")
    fun deleteComments(@Field("c_id") c_id: String): Call<JsonObject>


    @FormUrlEncoded
    @POST("/editcomment")
    fun editComments(
        @Field("user_id") user_id: String, @Field("comment") comment: String, @Field("c_id") c_id: String, @Field(
            "android"
        ) android: String
    ): Call<JsonObject>


    @FormUrlEncoded
    @POST("/addComment")
    fun addComment(
        @Field("android") android: String,
        @Field("post_id") post_id: String,
        @Field("comment") comment: String,
        @Field("commented_by_") user_id: String
    ): Call<JsonObject>


    @Multipart
    @POST("/changeprofilepic")
    fun changeProfilePic(@Part file: MultipartBody.Part?, @Part("user_id") user_id: RequestBody?, @Part("android") android: RequestBody?): Call<RegisterResModal?>?

    @Multipart
    @POST("/changecoverpic")
    fun changeCoverePic(@Part file: MultipartBody.Part?, @Part("user_id") user_id: RequestBody?, @Part("android") android: RequestBody?): Call<RegisterResModal?>?


}
