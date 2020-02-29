package com.example.lanecrowd.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.lancrowd.activity.modal.Home_Post_Modal
import com.example.lancrowd.activity.modal.Story_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.activity.View_Story_Activity
import com.example.lanecrowd.util.URL
import de.hdodenhof.circleimageview.CircleImageView

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
            context.startActivity(Intent(context,View_Story_Activity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putStringArrayListExtra("story_files",list.get(position).story_files)


            )
        })


        holder.story_userName.setText(list.get(position).posted_by)




        setStoryImage(position,list.get(position).story_files.get(0),holder.story_image)



    }

    private fun checkIsImage(position: Int): Boolean {

        if(list.get(position).story_files.get(0).contains("jpg") || list.get(position).story_files.get(0).contains("png") || list.get(position).story_files.get(0).contains("jpeg"))
            return true

        return false

    }


    private fun setStoryImage(position: Int,path: String, layott: CircleImageView?) {

        var url:String=""

        if(checkIsImage(position))
            url=URL.storyImagePath
        else
            url=URL.storyVideoPath


        println("Story_path"+url+path)



        Glide.with(context).load(url+path)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {

                    println("story_failed")

                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                    println("story_ready")



                    return false
                }
            })
            .apply(RequestOptions().placeholder(R.drawable.profile))

            .thumbnail(0.01f).into(layott!!).waitForLayout()



    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val main_layout=view.findViewById<LinearLayout>(R.id.main_layout)
        val story_image=view.findViewById<CircleImageView>(R.id.story_image)
        val story_userName=view.findViewById<TextView>(R.id.story_userName)

    }
}