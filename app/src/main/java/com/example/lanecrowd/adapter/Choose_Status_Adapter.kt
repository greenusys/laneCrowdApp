package com.example.lanecrowd.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.lancrowd.activity.modal.Choose_Status_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.activity.Choose_Status_Activity
import com.example.lanecrowd.util.URL
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


        Glide.with(context)
            .load(URL.themeImage.get(position)).apply(
                RequestOptions()
                    .placeholder(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            ).thumbnail(0.01f).into(holder.themeImage)


        holder.themeImage.setOnClickListener(View.OnClickListener {



            context.setTheme(URL.themeImage.get(position))
        })
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val themeImage=view.findViewById<CircleImageView>(R.id.themeImage)

    }
}