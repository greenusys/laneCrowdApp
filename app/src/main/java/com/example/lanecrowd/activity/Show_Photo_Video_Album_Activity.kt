package com.example.lanecrowd.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Photo_Video_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.adapter.Show_Photos_Videos_Adapter
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class Show_Photo_Video_Album_Activity : AppCompatActivity(), View.OnClickListener {


    var all_photos: TextView? = null
    var pic_title: TextView? = null
    var tagged_photos: TextView? = null
    var album: TextView? = null
    var addTitle: TextView? = null
    var backFrom: String? = null

    var data_rv: RecyclerView? = null
    var adapter: Show_Photos_Videos_Adapter? = null

    var photos_video_list = ArrayList<Photo_Video_Modal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show__photo__video__album_)




        backFrom=intent.getStringExtra("from")
        println("from_photo_vide"+backFrom)

        initViews()
        setTopBartitles(backFrom)
        set_RV_Adapter("all_photos")

    }

    private fun setTopBartitles(backFrom: String?) {

        if(backFrom.equals("photo"))
        {
            all_photos!!.setText("All Photos")
            tagged_photos !!.setText("Tagged Photos")
            album!!.setText("Album")
            pic_title!!.setText("All Photos")
        }

         else
        {
            all_photos!!.setText("All Videos")
            tagged_photos !!.setText("Tagged Videos")
            album!!.setText("Album")
            pic_title!!.setText("All Videos")
        }



    }


    private fun initViews() {


        data_rv = findViewById(R.id.data_rv) as RecyclerView



        addTitle = findViewById(R.id.addTitle)
        pic_title = findViewById(R.id.pic_title)
        all_photos = findViewById(R.id.all_photos)
        tagged_photos = findViewById(R.id.tagged_photos)
        album = findViewById(R.id.album)


        all_photos!!.setOnClickListener(this)
        tagged_photos!!.setOnClickListener(this)
        album!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        if (v!!.id == R.id.all_photos) {

           if(backFrom.equals("photo")) pic_title!!.setText("All Photos") else pic_title!!.setText("All Videos")
            set_RV_Adapter("all_photos")
        } else if (v!!.id == R.id.tagged_photos) {
            if(backFrom.equals("photo")) pic_title!!.setText("Tagged Photos") else pic_title!!.setText("Tagged Videos")

            set_RV_Adapter("tagged_photos")
        } else if (v!!.id == R.id.album) {
            pic_title!!.setText("Album")
            set_RV_Adapter("album")
        }
    }

    private fun set_RV_Adapter(from: String) {

        if (from.equals("all_photos")) {
            all_photos!!.setTextColor(getResources().getColor(R.color.colorPrimary));
            tagged_photos!!.setTextColor(getResources().getColor(R.color.black));
            album!!.setTextColor(getResources().getColor(R.color.black));
            addTitle!!.setText("Add Photos")

        } else if (from.equals("tagged_photos")) {
            all_photos!!.setTextColor(getResources().getColor(R.color.black));
            tagged_photos!!.setTextColor(getResources().getColor(R.color.colorPrimary));
            album!!.setTextColor(getResources().getColor(R.color.black));
            addTitle!!.visibility=View.GONE
        } else if (from.equals("album")) {
            all_photos!!.setTextColor(getResources().getColor(R.color.black));
            tagged_photos!!.setTextColor(getResources().getColor(R.color.black));
            album!!.setTextColor(getResources().getColor(R.color.colorPrimary));
            addTitle!!.setText("Add Album")
        }


        data_rv!!.recycledViewPool.clear()
        photos_video_list.clear()

        photos_video_list.add(Photo_Video_Modal())
        photos_video_list.add(Photo_Video_Modal())
        photos_video_list.add(Photo_Video_Modal())
        photos_video_list.add(Photo_Video_Modal())
        photos_video_list.add(Photo_Video_Modal())
        photos_video_list.add(Photo_Video_Modal())
        photos_video_list.add(Photo_Video_Modal())
        photos_video_list.add(Photo_Video_Modal())
        photos_video_list.add(Photo_Video_Modal())
        photos_video_list.add(Photo_Video_Modal())
        photos_video_list.add(Photo_Video_Modal())
        photos_video_list.add(Photo_Video_Modal())
        photos_video_list.add(Photo_Video_Modal())
        photos_video_list.add(Photo_Video_Modal())
        photos_video_list.add(Photo_Video_Modal())



        if(!from.equals("album")) {
            // set a GridLayoutManager with 3 number of columns
            var gridLayoutManager = GridLayoutManager(getApplicationContext(), 3);
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
            adapter = Show_Photos_Videos_Adapter(photos_video_list, this,"photos")
            data_rv!!.layoutManager = gridLayoutManager
            data_rv!!.adapter = adapter
            adapter!!.notifyDataSetChanged()
        }
        else
        {
            var gridLayoutManager = GridLayoutManager(getApplicationContext(), 2);
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
            adapter = Show_Photos_Videos_Adapter(photos_video_list, this,"album")
            data_rv!!.layoutManager = gridLayoutManager
            data_rv!!.adapter = adapter
            adapter!!.notifyDataSetChanged()
        }


    }




}