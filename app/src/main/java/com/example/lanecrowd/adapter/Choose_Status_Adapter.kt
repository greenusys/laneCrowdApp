package com.example.lanecrowd.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Chatting_Modal
import com.example.lancrowd.activity.modal.Choose_Status_Modal
import com.example.lancrowd.activity.modal.Home_Post_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.activity.Choose_Status_Activity
import de.hdodenhof.circleimageview.CircleImageView

class Choose_Status_Adapter(val list: ArrayList<Choose_Status_Modal>, val context: Choose_Status_Activity) : RecyclerView.Adapter<Choose_Status_Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.status_item, parent, false);
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.themeImage.setOnClickListener(View.OnClickListener {

            context.setTheme()
        })
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val themeImage=view.findViewById<CircleImageView>(R.id.themeImage)

    }
}