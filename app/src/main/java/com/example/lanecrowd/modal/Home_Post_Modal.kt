package com.example.lancrowd.activity.modal

class Home_Post_Modal(val post_id:String,
                      val user_id:String,
                      val post:String,
                      val post_files:ArrayList<String>,
                      val posted_by:String,
                      val profile_pic:String,
                      val posted_on:String,
                      var total_likes:String,
                      val total_share:String,
                      val total_comments:String,
                      val isImage:Boolean,
                      var isMyLike:Boolean) {



}