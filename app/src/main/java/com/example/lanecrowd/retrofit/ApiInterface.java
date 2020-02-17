package com.example.lanecrowd.retrofit;


import com.example.lancrowd.activity.modal.RegisterResModal;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {


    @FormUrlEncoded
    @POST("/signin")
    Call<RegisterResModal> loginAPI(@Field("email") String email, @Field("password") String password);


    @FormUrlEncoded
    @POST("/signup")
    Call<RegisterResModal> registerUser(@Field("name") String name, @Field("email_phone") String email_phone, @Field("password") String password);


    @FormUrlEncoded
    @POST("/verify_otp")
    Call<RegisterResModal> verifyOTP(@Field("verify_otp") String verify_otp, @Field("otp") String otp, @Field("email_phone") String email_phone, @Field("password") String password, @Field("name") String name);


    @Multipart
    @POST("/addPost")
    Call<JsonObject> uploadPostAPI(@Part("android") RequestBody android,
                                      @Part("post_type") RequestBody post_type,
                                      @Part("post") RequestBody post,
                                      @Part("imgageData") RequestBody imgageData,
                                    @Part List<MultipartBody.Part> files,
                                      @Part("user_id") RequestBody user_id);



    /*@FormUrlEncoded
    @POST("/addPost")
    Call<Object> uploadImage(@Field("android") String android,
                               @Field("post_type") String post_type,
                               @Field("post") String post,
                               @Field("imgageData") String imgageData,
                               @Field("user_id") String user_id);

*/
}
