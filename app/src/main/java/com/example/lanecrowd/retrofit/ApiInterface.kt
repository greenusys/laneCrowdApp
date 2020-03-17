package com.example.lanecrowd.retrofit


import com.example.lancrowd.activity.modal.Profile_Detail_Modal
import com.example.lancrowd.activity.modal.RegisterResModal
import com.example.lanecrowd.util.NetworkConnectionInterceptor
import com.google.gson.JsonObject
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiInterface {




    companion object{
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ) : ApiInterface{

            val retrofit: Retrofit? = null


            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()


            if (retrofit == null){

                return Retrofit.Builder()
                    .client(okHttpClient)
                    .client(okHttpClient)
                    .baseUrl("http://www.lanecrowd.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

                    .build()
                    .create(ApiInterface::class.java)


            }
            return retrofit.create(ApiInterface::class.java)




        }
    }

    //for Login
    @FormUrlEncoded
    @POST("/signin")
    fun loginAPI(@Field("email") email: String, @Field("password") password: String): Single<RegisterResModal>


    @FormUrlEncoded
    @POST("/signup")
    fun registerUser(@Field("name") name: String, @Field("email_phone") email_phone: String, @Field("password") password: String, @Field("dob") dob: String, @Field("gender") gender: String): Single<RegisterResModal>


    @FormUrlEncoded
    @POST("/verify_otp")
    fun verifyOTP(
        @Field("verify_otp") verify_otp: String,
        @Field("otp") otp: String,
        @Field("email_phone") email_phone: String,
        @Field("password") password: String,
        @Field("name") name: String,
        @Field("dob") dob: String,
        @Field("gender") gender: String
    ): Single<RegisterResModal>


    //for HomePost
    @FormUrlEncoded
    @POST("/fetchpost")
    fun fetchpost(@Field("android") android: String, @Field("user_id") user_id: String, @Field("offset") offset: String): Single<JsonObject>


    @FormUrlEncoded
    @POST("/fetchstories")
    fun fetchStory(@Field("android") android: String, @Field("user_id") user_id: String, @Field("offset") offset: String): Single<JsonObject>


    @FormUrlEncoded
    @POST("/sharePost")
    fun sharePost(@Field("android") android: String, @Field("post_id") post_id: String, @Field("user_id") user_id: String): Single<JsonObject>

    @FormUrlEncoded
    @POST("/deletePost")
    fun deletePost(@Field("post_id") post_id: String): Single<JsonObject>


    @FormUrlEncoded
    @POST("/likeOrdislike")
    fun likeDislikePost(
        @Field("android") android: String, @Field("post_id") post_id: String, @Field(
            "user_id"
        ) user_id: String
    ): Single<JsonObject>


    //for Comments
    @FormUrlEncoded
    @POST("/getComments")
    fun getComments(@Field("post_id") post_id: String, @Field("offset") offset: String): Single<JsonObject>

    @FormUrlEncoded
    @POST("/deleteComment")
    fun deleteComments(@Field("c_id") c_id: String): Single<JsonObject>


    @FormUrlEncoded
    @POST("/editcomment")
    fun editComments(
        @Field("user_id") user_id: String, @Field("comment") comment: String, @Field("c_id") c_id: String, @Field(
            "android"
        ) android: String
    ): Single<JsonObject>


    @FormUrlEncoded
    @POST("/addComment")
    fun addComment(
        @Field("android") android: String,
        @Field("post_id") post_id: String,
        @Field("comment") comment: String,
        @Field("commented_by_") user_id: String
    ): Single<JsonObject>


    //for change profile and cover pic
    @Multipart
    @POST("/changeprofilepic")
    fun changeProfilePic(@Part file: MultipartBody.Part?, @Part("user_id") user_id: RequestBody?, @Part("android") android: RequestBody?): Single<RegisterResModal?>?

    @Multipart
    @POST("/changecoverpic")
    fun changeCoverePic(@Part file: MultipartBody.Part?, @Part("user_id") user_id: RequestBody?, @Part("android") android: RequestBody?): Single<RegisterResModal?>?



    //for Profile Section
    @FormUrlEncoded
    @POST("/fetchProfile")
    fun fetchProfile(@Field("myId") myId: String,@Field("userId") userId: String): Single<Profile_Detail_Modal>

    @FormUrlEncoded
    @POST("/fetchtimelinepost")
    fun fetchtimelinepost(@Field("android") android: String, @Field("user_id") user_id: String, @Field("offset") offset: String): Single<JsonObject>




}
