package com.example.lanecrowd.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Home_Post_Modal
import com.example.lancrowd.activity.modal.My_Friends_Modal
import com.example.lancrowd.activity.modal.Story_Modal
import com.example.lanecrowd.R

class My_Friend_List_Adapter(val list: ArrayList<My_Friends_Modal>, val context: Context) : RecyclerView.Adapter<My_Friend_List_Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.my_friends_item, parent, false);
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