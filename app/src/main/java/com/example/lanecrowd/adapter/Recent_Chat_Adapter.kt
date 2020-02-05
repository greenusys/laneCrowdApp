package com.example.lanecrowd.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Home_Post_Modal
import com.example.lancrowd.activity.modal.Recent_Chat_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.activity.Chat_Activity

class Recent_Chat_Adapter(val list: ArrayList<Recent_Chat_Modal>, val context: Context) : RecyclerView.Adapter<Recent_Chat_Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recent_chat_item, parent, false);
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.main_layout.setOnClickListener(View.OnClickListener {

            context.startActivity(Intent(context,Chat_Activity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        })
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var main_layout=view.findViewById<CardView>(R.id.main_layoutphoto)

    }
}