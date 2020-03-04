package com.example.lanecrowd.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.lancrowd.activity.modal.Story_Modal
import com.example.lanecrowd.Home_Fragment.Home_Post_Fragment
import com.example.lanecrowd.R
import com.example.lanecrowd.activity.Add_Post_Activity
import com.example.lanecrowd.activity.View_Story_Activity
import com.example.lanecrowd.util.URL
import com.example.lanecrowd.view_modal.FetchPostVm
import com.google.gson.JsonObject
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONArray
import org.json.JSONObject


class Story_Adapter(var layoutManager:LinearLayoutManager,var activity:Home_Post_Fragment, val story_rv:RecyclerView,val list: ArrayList<Story_Modal>, val context: Context) : RecyclerView.Adapter<Story_Adapter.ViewHolder>() {

    var isScrolling = false
    var storyCounting: Int = 0


    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0

    private val TYPE_ITEM_ADD_STORY = 0
    private val TYPE_ITEM_NORMAL = 1



    init {

        lateinit var viewmodel: FetchPostVm

        viewmodel = ViewModelProvider(activity).get(FetchPostVm::class.java)


        println("stksdjf"+story_rv)
        // load more
        //val layoutManager: LinearLayoutManager = story_rv.getLayoutManager() as LinearLayoutManager


        story_rv!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {


            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                currentItems = layoutManager!!.childCount
                totalItems = layoutManager!!.itemCount
                scrollOutItems = layoutManager!!.findFirstVisibleItemPosition()

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                   // visibleLoadingMoreAnim(true)

                    println("load_more_called")
                    isScrolling = false
                    storyCounting++


                    viewmodel.fetchStoryVM(URL.userId, storyCounting.toString())
                        .observe(activity, Observer { story_result ->

                            println("fetch_story_rv" + story_result)

                            setStoryPost(story_result)


                        })




                }


            }
        })

    }


    private fun setStoryPost(storyResult: JsonObject?) {


        //call after load more
        //updateAdapterForMultipleData()

        var main: JSONObject? = null

        if (storyResult != null) {


            try {


                main = JSONObject(storyResult.toString())
                if (storyResult != null && main.has("code") && main.getString("code").equals("1"))
                {

                    val data: JSONArray = main.getJSONArray("data")

                    for(i in 0 until data.length())
                    {

                        var postList: ArrayList<String> = ArrayList()

                        var item=data.getJSONObject(i);


                        //for post files
                        try {

                            if (!item.getString("story_files").equals("null")) {
                                postList = item.getString("story_files").split(",") as ArrayList<String>

                            } else {

                                postList.add("empty")
                            }

                        } catch (e: Exception) {
                            postList.add(item.getString("story_files"))

                        }



                        list.add(Story_Modal(
                            item.getString("story_id"),
                            item.getString("user_id"),
                            item.getString("story"),
                            postList,
                            item.getString("posted_by"),
                            item.getString("profile_pic"),
                            item.getString("posted_on")
                        ))
                    }

                    notifyDataSetChanged()

                    println("sotry_Lise_size"+list.size)

                }

                else
                {

                }

            } catch (e: Exception)
            {
                e.printStackTrace()
            }
        } else {

        }



    }




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
                        .putExtra("name",list.get(position-1).posted_by)
                        .putExtra("imageva",list.get(position-1).profile_pic)


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