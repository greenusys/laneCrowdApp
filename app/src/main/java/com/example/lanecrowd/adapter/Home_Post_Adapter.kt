package com.example.lanecrowd.adapter

import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Home_Post_Modal
import com.example.lancrowd.activity.modal.Story_Modal
import com.example.lanecrowd.Home_Fragment.Home_Post_Fragment
import com.example.lanecrowd.R
import com.example.lanecrowd.activity.Add_Post_Activity
import com.example.lanecrowd.activity.Show_Comment_Activity

class Home_Post_Adapter(val list: ArrayList<Home_Post_Modal>, val context: Context,val activity:Home_Post_Fragment) : RecyclerView.Adapter<Home_Post_Adapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if (viewType == 0) {

            val view = LayoutInflater.from(context).inflate(R.layout.whats_mind_layout, parent, false)
            return ViewHolder(view)
        }
        else if (viewType == 1) {
            val view = LayoutInflater.from(context).inflate(R.layout.home_post_item, parent, false)
            return ViewHolder(view)
        }


        return throw RuntimeException("Invalid view type")
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {




        if(position==0)
        holder.whatmain_layout.setOnClickListener(View.OnClickListener {
            context.startActivity(Intent(context,Add_Post_Activity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        })

        if(position!=0) {
            holder.lay_postComment.setOnClickListener(View.OnClickListener {
                context.startActivity(Intent(context, Show_Comment_Activity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) })


            holder.post_menu.setOnClickListener(View.OnClickListener {
                activity!!.showMenu(holder.post_menu) })





        }//main if
    }




    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        val lay_postComment=view.findViewById<LinearLayout>(R.id.lay_postComment)
        val whatmain_layout=view.findViewById<CardView>(R.id.main_layout)
        val post_menu=view.findViewById<ImageView>(R.id.post_menu)

    }

    override fun getItemViewType(position: Int): Int {

        if (position == 0)
            return 0


            return 1




    }
}