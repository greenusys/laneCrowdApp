package com.example.lanecrowd.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Photo_Video_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.activity.Show_Photo_Video_Album_Activity

class Photos_Video_Adapter(val from:String, val context: Context) : RecyclerView.Adapter<Photos_Video_Adapter.ViewHolder>() {

    var status=""
    init {
        if(from.equals("photo"))
            status="Photo"
        else
            status="Video"


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.all_photo_video_item, parent, false);
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if(position==0)
        holder.title.setText("All "+status)
        if(position==1)
        holder.title.setText("Tagged "+status)
        if(position==2)
        holder.title.setText("Album ")




    holder.main_layout.setOnClickListener(View.OnClickListener {
        context.startActivity(Intent(context,Show_Photo_Video_Album_Activity::class.java)
                .putExtra("from",from)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

    })
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val main_layout=view.findViewById<CardView>(R.id.main_layoutphoto)
        val title=view.findViewById<TextView>(R.id.title)

    }
}