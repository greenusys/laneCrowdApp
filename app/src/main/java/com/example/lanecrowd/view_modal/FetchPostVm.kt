package com.example.lanecrowd.view_modal


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lanecrowd.retrofit.ApiClient
import com.example.lanecrowd.retrofit.ApiInterface
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FetchPostVm : ViewModel() {
    var fetchPostRes: MutableLiveData<JsonObject>? = null

    lateinit var apiInterface: ApiInterface





    fun fetchPostvm(userId:String,offset:String): MutableLiveData<JsonObject> {




        apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        fetchPostRes = MutableLiveData()

        fetchPost(userId,offset)


        return fetchPostRes as MutableLiveData<JsonObject>

    }




    fun fetchPost(userId:String,offset:String) {

        val call = apiInterface.fetchpost("",userId,offset)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                try {

                    println("fetch_post_res"+response.body())


                    if (response.isSuccessful) {

                        fetchPostRes!!.value = response.body()

                    }
                    else
                        fetchPostRes?.value = null


                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                println("Failed" + t.message)
                fetchPostRes?.value = null
                call.cancel()

            }
        })


    }

}
