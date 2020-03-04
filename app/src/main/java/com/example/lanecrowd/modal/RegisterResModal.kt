package com.example.lancrowd.activity.modal


import java.io.Serializable


class RegisterResModal(val status:String,
                       val msg:String,
                       val pic:String,
                       val name:String,
                       val email_phone:String,
                       val password:String,
                       val otp:String,
                       val data:ArrayList<UserData>):Serializable{




    class UserData(val user_id:String,
                   val email:String,
                   val phone:String,
                   val full_name:String,
                   val bio_graphy:String,
                   val profile_picture:String,
                   val cover_photo:String):Serializable



}