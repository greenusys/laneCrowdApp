package com.example.lanecrowd.view_modal


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lanecrowd.retrofit.AppController
import com.example.lanecrowd.util.URL
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.*


class AddPostVM : ViewModel() {
    private var request: Request?=null
    var addPostRes: MutableLiveData<JSONObject>? = null

    internal lateinit var appController: AppController
    var isOnlyText:Boolean=false

    var context: Context?=null

    //this will cancel network.xml call if the call already sent
     fun cancleNetworkCall() {


         //When you want to cancel:
         //A) go through the queued calls and cancel if the tag matches:
         for (call in appController.okHttpClient.dispatcher().queuedCalls()) {
             if (call.request().tag() == "requestKey")
                 call.cancel()
         }

         //B) go through the running calls and cancel if the tag matches:
         for (call in appController.okHttpClient.dispatcher().runningCalls()) {
             if (call.request().tag() == "requestKey")
                 call.cancel()
         }


     }


    fun addPostvm(from:String,imgageData:String,status:String,files:ArrayList<File>,isImage: Boolean?,context: Context): MutableLiveData<JSONObject> {

        addPostRes = MutableLiveData()
        this.context=context

        appController =  context as AppController


        if(status.length>0 && files.size<=0 || files==null)
            isOnlyText=true
        else
            isOnlyText=false



        addPost(from,isOnlyText,status,imgageData,URL.userId,files,isImage)


        return addPostRes as MutableLiveData<JSONObject>

    }

    private fun addPost(from:String,isOnlyText:Boolean,post: String?, imgageData: String, user_id: String,
                        files: ArrayList<File>, isImage: Boolean?) {



        var post_type:String=""
        var type_key:String=""
        var post_key:String=""
        var url:String=""


        //for add post
        if(from.equals("post")) {
            type_key = "post_type"
            post_key = "post"
            url = URL.add_post
        }
        //for add story
        else {
            type_key = "story_type"
            post_key = "story"
            url = URL.add_story
        }



        if(isOnlyText)
            post_type="0"
        else {

            if (isImage!!)
                post_type = "1"
            else
                post_type = "2"
        }


        println("add_post_called")
        println("from"+from)
        println("isImage"+isImage)
        println("isOnlytext"+isOnlyText)
        println("post_type"+post_type)


        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("android", "")
        builder.addFormDataPart(type_key, post_type)
        builder.addFormDataPart(post_key, post)
        builder.addFormDataPart("imgageData", imgageData)
        builder.addFormDataPart("user_id", user_id)




        if(files.size>0)
        for (f in files) {

            val filePath = f.absolutePath

          //  println("file_path"+filePath)
            val mediaType = MediaType.parse(if (isImage!!) "image/" else "video/" + filePath.substring(filePath.lastIndexOf(".") + 1))
            builder.addFormDataPart("files[]", filePath.substring(filePath.lastIndexOf("/") + 1),
                    RequestBody.create(mediaType, f))
        }

         request = Request.Builder().url(url).tag("requestKey").post(builder.build()).build()

        cancleNetworkCall()

        appController.getOkHttpClient().newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                addPostRes!!.postValue(null)
            }

            @Throws(Exception::class)
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {



               try {


                val myResponse = response.body()!!.string()

                println("my_response"+myResponse)


                val main:JSONObject= JSONObject(myResponse)

                   println("status"+main.getString("status"))


                   if(main.getString("status").equals("1"))
                   addPostRes!!.postValue(main)
                   else
                    addPostRes!!.postValue(null)



               }catch (e:Exception)
               {
                   addPostRes!!.postValue(null)
               }


            }
        })
    }



}
