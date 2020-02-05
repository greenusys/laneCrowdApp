package com.example.lanecrowd.adapter

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Photo_Video_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.activity.ShowPhotoActivity
import com.example.lanecrowd.activity.Show_Photo_Video_Album_Activity
import com.mzelzoghbi.zgallery.ZGallery
import com.mzelzoghbi.zgallery.entities.ZColor

class Show_Photos_Videos_Adapter(val list: ArrayList<Photo_Video_Modal>,
                                 val context: Context,
                                 val from: String) : RecyclerView.Adapter<Show_Photos_Videos_Adapter.ViewHolder>() {

    var activity:Show_Photo_Video_Album_Activity?=null
    init {
        activity=context as Show_Photo_Video_Album_Activity
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if (viewType == 1) {

            val view = LayoutInflater.from(context).inflate(R.layout.photos_videos_item, parent, false)
            return ViewHolder(view)
        } else if (viewType == 2) {

            val view = LayoutInflater.from(context).inflate(R.layout.album_item, parent, false)
            return ViewHolder(view)
        } else
            return throw RuntimeException("Invalid view type")
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (!from.equals("photos"))
            holder.albummain_layout.setOnClickListener(View.OnClickListener {
                context.startActivity(Intent(context, ShowPhotoActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

            })

        else {
           holder.albummain_layout.setOnClickListener(View.OnClickListener {
               ZGallery.with(activity, arrayListOf("http://static0.passel.co/wp-content/uploads/2016/08/05110349/20160731-igor-trepeshchenok-barnimages-08-768x509.jpg"))
                       .setToolbarTitleColor(ZColor.WHITE)
                       .setGalleryBackgroundColor(ZColor.WHITE)
                       .setToolbarColorResId(R.color.colorPrimary)
                       .setTitle("Gallery")
                       .show()
           })
       }


    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        val albummain_layout = view.findViewById<LinearLayout>(R.id.main_layout)

    }

    override fun getItemViewType(position: Int): Int {

        if (from.equals("photos"))
            return 1

        return 2
    }

}