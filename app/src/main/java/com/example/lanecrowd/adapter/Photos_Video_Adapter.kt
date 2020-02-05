package com.example.lanecrowd.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Photo_Video_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.activity.Show_Photo_Video_Album_Activity

class Photos_Video_Adapter(val from:String,val list: ArrayList<Photo_Video_Modal>, val context: Context) : RecyclerView.Adapter<Photos_Video_Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recent_chat_item, parent, false);
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    holder.main_layout.setOnClickListener(View.OnClickListener {
        context.startActivity(Intent(context,Show_Photo_Video_Album_Activity::class.java)
                .putExtra("from",from)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

    })
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val main_layout=view.findViewById<CardView>(R.id.main_layoutphoto)

    }
}