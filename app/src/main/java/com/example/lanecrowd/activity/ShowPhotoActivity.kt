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



            showSingleImages()


    }

    fun showSingleImages() {


        //open single image
        ZGallery.with(this, getImageList())
            .setToolbarTitleColor(ZColor.WHITE)
            .setGalleryBackgroundColor(ZColor.WHITE)
            .setToolbarColorResId(R.color.colorPrimary)
            .setTitle(name)
            .setSelectedImgPosition(position!!.toInt())
            .show(isImage)



    }

    fun showMultipleImages() {
        ZGrid.with(this, getImageList())
                .setToolbarColorResId(R.color.colorPrimary)
                .setTitle("Zak Gallery")
                .setToolbarTitleColor(ZColor.WHITE)
                .setSpanCount(3)
                .setGridImgPlaceHolder(R.color.colorPrimary)
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

  /*  private fun getDummyImageList(): ArrayList<String> {
        val imagesList = ArrayList<String>()
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/08/05110349/20160731-igor-trepeshchenok-barnimages-08-768x509.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/08/05095154/tumblr_oawfisUmZo1u7ns0go1_500.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/08/05095153/tumblr_obbkeo3lZW1ted1sho1_500.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/08/05095153/tumblr_obaxpnJbKg1sfie3io1_500.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/08/05095153/tumblr_obdehwWneK1slhhf0o1_500.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/08/05095152/2016-08-01-roman-drits-barnimages-005-768x512.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/08/05095151/2016-08-01-roman-drits-barnimages-003-768x512.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/08/05095149/tumblr_obbjwp1bDz1ted1sho1_500.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/08/05095151/tumblr_oawfhnxNjL1u7ns0go1_500.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/08/05095150/tumblr_ob6xvqXLoB1tlwzgvo1_500.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/08/05095148/20160731-igor-trepeshchenok-barnimages-10-768x512.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/08/03092421/tumblr_oawfgd2G941u7ns0go1_500.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/08/03092423/tumblr_ob6xutS8N21tlwzgvo1_500.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/08/03092421/tumblr_o86sgm6F7a1ted1sho1_500.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/08/03092420/tumblr_ob6xudqW4U1tlwzgvo1_500.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/08/03092420/2016-08-01-roman-drits-barnimages-002-768x512.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/08/03092418/tumblr_o97fatuGnd1ted1sho1_500.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/08/03092420/tumblr_oawff12j9L1u7ns0go1_500.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/08/03092420/2016-08-01-roman-drits-barnimages-001-768x512.jpg")
        imagesList.add("http://1x402i15i5vh15yage3fafmg.wpengine.netdna-cdn.com/wp-content/uploads/2016/08/tumblr_oawfdsEx2w1u7ns0go1_500-1.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/08/03092417/tumblr_o97gyqSK3k1ted1sho1_500.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/07/03092417/20160731-igor-trepeshchenok-barnimages-07-768x512.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/07/03092605/tumblr_ob6wjiCBUh1tlwzgvo1_500.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/07/03092604/tumblr_ob6wkn58cJ1tlwzgvo1_500.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/07/03092416/tumblr_ob6wk7mns81tlwzgvo1_500.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/07/03092413/tumblr_ob6vv04yIP1tlwzgvo1_500.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/07/03092414/tumblr_ob6vk1bBPa1tlwzgvo1_500.jpg")
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/07/03092404/tumblr_o97ipvkger1ted1sho1_500.jpg")
        return imagesList
    }
*/


}
