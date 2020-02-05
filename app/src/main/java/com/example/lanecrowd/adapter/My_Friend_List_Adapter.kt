package com.example.lanecrowd.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.My_Friends_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.activity.My_Friend_Activity

class My_Friend_List_Adapter(val list: ArrayList<My_Friends_Modal>, val context: My_Friend_Activity) : RecyclerView.Adapter<My_Friend_List_Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.my_friends_item, parent, false);
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.frnd_menu.setOnClickListener(View.OnClickListener {
            context.showMenu(holder.frnd_menu)
        })
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val frnd_menu=view.findViewById<ImageView>(R.id.frnd_menu)
    }
}