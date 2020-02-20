package com.example.lanecrowd.view_modalimport androidx.lifecycle.MutableLiveDataimport androidx.lifecycle.ViewModelimport com.example.lanecrowd.retrofit.ApiClientimport com.example.lanecrowd.retrofit.ApiInterfaceimport com.google.gson.JsonObjectimport retrofit2.Callimport retrofit2.Callbackimport retrofit2.Responseclass CommentVM : ViewModel() {    var fetchCommentRes: MutableLiveData<JsonObject>? = null    lateinit var apiInterface: ApiInterface    fun addCommentVM(post_id:String,commented_by_:String,comment:String): MutableLiveData<JsonObject> {        apiInterface = ApiClient.getClient().create(ApiInterface::class.java)        fetchCommentRes = MutableLiveData()        fechComment(post_id,commented_by_,comment)        return fetchCommentRes as MutableLiveData<JsonObject>    }    fun fechComment(post_id:String,commented_by_:String,comment:String) {        println("fechComment"+post_id+"  "+commented_by_+" "+comment)        val call = apiInterface.addComment("",post_id,comment,commented_by_)        call.enqueue(object : Callback<JsonObject> {            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {                try {                    println("fechComment"+response.body())                    if (response.isSuccessful) {                        fetchCommentRes!!.value = response.body()                    }                    else                        fetchCommentRes?.value = null                } catch (e: Exception) {                    e.printStackTrace()                }            }            override fun onFailure(call: Call<JsonObject>, t: Throwable) {                println("Failed" + t.message)                fetchCommentRes?.value = null                call.cancel()            }        })    }}