package com.example.lanecrowd.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lanecrowd.R
import com.example.lanecrowd.util.URL
import com.mzelzoghbi.zgallery.ZGallery
import com.mzelzoghbi.zgallery.ZGrid
import com.mzelzoghbi.zgallery.entities.ZColor
import kotlin.collections.ArrayList


class ShowPhotoActivity : AppCompatActivity() {


     var files:ArrayList<String>?=null
     var isImage:String?=null
     var position:String?=null
     var name:String?=null


    var backCount:Int=0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_photo)



         files = intent.extras!!.getStringArrayList("files")
        isImage = intent.extras!!.getString("isImage")
        position = intent.extras!!.getString("position")
        name = intent.extras!!.getString("name")


        println("files_size"+files!!.size)
        println("isImage"+isImage)
        println("position"+position)
        println("name"+name)



            showSingleImages()


    }

    fun showSingleImages() {


        //open single image
        ZGallery.with(this, getImageList())
            .setToolbarTitleColor(ZColor.WHITE)
            .setGalleryBackgroundColor(ZColor.WHITE)
            .setToolbarColorResId(R.color.colorPrimary)
            .setTitle(name!!.capitalize())
            .setSelectedImgPosition(position!!.toInt())
            .show(isImage)



    }

    private fun getImageList(): ArrayList<String>? {

        val imagesList = ArrayList<String>()
        var url:String?=null

        if(isImage.equals("true"))
            url=URL.imagePath
        else
            url=URL.videoPath


        for (i in 0 until files!!.size)
            imagesList.add(url+files!!.get(i))



        return imagesList

    }







    override fun onResume() {
        super.onResume()

        ++backCount
        if(backCount>=2)
            onBackPressed()

    }




}
