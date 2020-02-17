package com.example.lanecrowd.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lancrowd.activity.modal.Chatting_Modal
import com.example.lancrowd.activity.modal.Home_Post_Modal
import com.example.lancrowd.activity.modal.SelectedModal
import com.example.lanecrowd.R
import com.example.lanecrowd.activity.Add_Post_Activity

class Show_Selected_File_Adapter(val list: ArrayList<String>, val context: Add_Post_Activity) : RecyclerView.Adapter<Show_Selected_File_Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.selected_file_item, parent, false);
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context).load(list.get(position)).into(holder.iv_img)


        holder.iv_close.setOnClickListener(View.OnClickListener {

            context.removeItem(position)
        })
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val iv_img:ImageView=view.findViewById(R.id.iv_img)
        val iv_close:ImageView=view.findViewById(R.id.iv_close)


    }
}