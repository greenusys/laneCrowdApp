package com.example.lanecrowd.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Chatting_Modal
import com.example.lancrowd.activity.modal.Home_Post_Modal
import com.example.lanecrowd.R

class Chatting_Adapter(val list: ArrayList<Chatting_Modal>, val context: Context) : RecyclerView.Adapter<Chatting_Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chat_message_item, parent, false);
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}