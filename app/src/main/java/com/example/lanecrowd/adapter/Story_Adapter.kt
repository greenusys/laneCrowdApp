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
import com.example.lanecrowd.activity.Add_Post_Activity
import com.example.lanecrowd.activity.View_Story_Activity
import com.example.lanecrowd.util.URL
import de.hdodenhof.circleimageview.CircleImageView

class Story_Adapter(val list: ArrayList<Story_Modal>, val context: Context) : RecyclerView.Adapter<Story_Adapter.ViewHolder>() {



    private val TYPE_ITEM_ADD_STORY = 0
    private val TYPE_ITEM_NORMAL = 1



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if (viewType == TYPE_ITEM_ADD_STORY) {
            val view = LayoutInflater.from(context).inflate(R.layout.add_story_layout, parent, false);
            return ViewHolder(view)

        }

        else if (viewType == TYPE_ITEM_NORMAL) {
            val view = LayoutInflater.from(context).inflate(R.layout.story_item, parent, false);
            return ViewHolder(view)

        }

        val view = LayoutInflater.from(context).inflate(R.layout.story_item, parent, false);
        return ViewHolder(view)


    }

    override fun getItemCount(): Int {
        return list.size+1
    }

    override fun getItemViewType(position: Int): Int {
        if(position==0)
            return TYPE_ITEM_ADD_STORY

        return TYPE_ITEM_NORMAL
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(position==0)
        {
            setStoryImage(position, URL.profilePicPath + URL.profilePic, holder.user_image)
            holder.add_to_StoryLayout.setOnClickListener(View.OnClickListener {

                context.startActivity(
                    Intent(
                        context, Add_Post_Activity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("from","story")
                )
            })


        }

        else {

            holder.main_layout.setOnClickListener(View.OnClickListener {
                context.startActivity(
                    Intent(context, View_Story_Activity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putStringArrayListExtra("story_files", list.get(position-1).story_files)


                )
            })


            holder.story_userName.setText(list.get(position-1).posted_by)

            setStoryImage(position, list.get(position-1).story_files.get(0), holder.story_image)

        }

    }

    private fun checkIsImage(position: Int): Boolean {

        if(list.get(position-1).story_files.get(0).contains("jpg") || list.get(position-1).story_files.get(0).contains("png") || list.get(position-1).story_files.get(0).contains("jpeg"))
            return true

        return false

    }


    private fun setStoryImage(position: Int,path: String, layott: CircleImageView?) {

        var url:String=""


        if(position!=0) {
            if (checkIsImage(position))
                url = URL.storyImagePath
            else
                url = URL.storyVideoPath
        }


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
            .apply(RequestOptions().placeholder(R.drawable.placeholder_profile))

            .thumbnail(0.01f).into(layott!!).waitForLayout()



    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val main_layout=view.findViewById<LinearLayout>(R.id.main_layout)
        val story_image=view.findViewById<CircleImageView>(R.id.story_image)
        val story_userName=view.findViewById<TextView>(R.id.story_userName)
        val add_to_StoryLayout=view.findViewById<LinearLayout>(R.id.add_to_StoryLayout)
        val user_image=view.findViewById<CircleImageView>(R.id.user_image)

    }
}