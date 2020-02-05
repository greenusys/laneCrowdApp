package com.example.lanecrowd.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Home_Post_Modal
import com.example.lancrowd.activity.modal.Story_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.activity.View_Story_Activity

class Story_Adapter(val list: ArrayList<Story_Modal>, val context: Context) : RecyclerView.Adapter<Story_Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.story_item, parent, false);
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.main_layout.setOnClickListener(View.OnClickListener {
            context.startActivity(Intent(context,View_Story_Activity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        })
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val main_layout=view.findViewById<LinearLayout>(R.id.main_layout)

    }
}