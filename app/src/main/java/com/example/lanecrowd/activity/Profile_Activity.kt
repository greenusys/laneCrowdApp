package com.example.lanecrowd.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Home_Post_Modal
import com.example.lancrowd.activity.modal.Photo_Video_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.adapter.Photos_Video_Adapter
import com.example.lanecrowd.adapter.Profile_Post_Adapter

class Profile_Activity : AppCompatActivity(), View.OnClickListener {


    var timeline: TextView? = null
    var photos: TextView? = null
    var videos: TextView? = null


    var profile_rv: RecyclerView? = null
    var profilePostAdapter: Profile_Post_Adapter? = null
    var photosVideoAdapter: Photos_Video_Adapter? = null
    var home_post_list = ArrayList<Home_Post_Modal>()

    var photos_video_list = ArrayList<Photo_Video_Modal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initViews()

        set_Time_Post_Adapter()
    }

    @SuppressLint("WrongConstant")
    private fun set_Time_Post_Adapter() {


        timeline!!.setTextColor(getResources().getColor(R.color.colorPrimary));
        photos!!.setTextColor(getResources().getColor(R.color.black));
        videos!!.setTextColor(getResources().getColor(R.color.black));




        profile_rv!!.recycledViewPool.clear()
       /* home_post_list.add(Home_Post_Modal())
        home_post_list.add(Home_Post_Modal())
        home_post_list.add(Home_Post_Modal())
        home_post_list.add(Home_Post_Modal())
        home_post_list.add(Home_Post_Modal())
        home_post_list.add(Home_Post_Modal())*/


        profilePostAdapter = Profile_Post_Adapter(home_post_list, applicationContext)
        profile_rv!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        profile_rv!!.adapter = profilePostAdapter
        profilePostAdapter!!.notifyDataSetChanged()


    }

    private fun set_Photos_Videos_Adapter(from: String) {

        if (from.equals("photo")) {
            timeline!!.setTextColor(getResources().getColor(R.color.black));
            photos!!.setTextColor(getResources().getColor(R.color.colorPrimary));
            videos!!.setTextColor(getResources().getColor(R.color.black));
        } else {
            timeline!!.setTextColor(getResources().getColor(R.color.black));
            photos!!.setTextColor(getResources().getColor(R.color.black));
            videos!!.setTextColor(getResources().getColor(R.color.colorPrimary));
        }


        profile_rv!!.recycledViewPool.clear()

        photos_video_list.clear()

        photos_video_list.add(Photo_Video_Modal())
        photos_video_list.add(Photo_Video_Modal())
        photos_video_list.add(Photo_Video_Modal())


        photosVideoAdapter = Photos_Video_Adapter(from,photos_video_list, applicationContext)
        profile_rv!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        profile_rv!!.adapter = photosVideoAdapter
        photosVideoAdapter!!.notifyDataSetChanged()


    }

    private fun initViews() {



        timeline = findViewById(R.id.timeline)
        photos = findViewById(R.id.photos)
        videos = findViewById(R.id.videos)

        profile_rv = findViewById(R.id.profile_rv) as RecyclerView


        timeline!!.setOnClickListener(this)
        photos!!.setOnClickListener(this)
        videos!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v!!.id == R.id.timeline) {
            set_Time_Post_Adapter()
        } else if (v!!.id == R.id.photos) {
            set_Photos_Videos_Adapter("photo")
        } else if (v!!.id == R.id.videos) {
            set_Photos_Videos_Adapter("video")
        }
    }

    fun Back(view: View) {
        onBackPressed()
    }
}
