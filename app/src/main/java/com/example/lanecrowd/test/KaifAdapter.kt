package com.example.lanecrowd.test

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Home_Post_Modal
import com.example.lanecrowd.R
import com.like.LikeButton
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

/**
 * Created by qr on 25-Oct-17.
 */
class KaifAdapter(private val mcontext: Context, val homelist: ArrayList<Home_Post_Modal?>, recyclerView: RecyclerView) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {


    var homelist2: ArrayList<Home_Post_Modal?>?=null
    private val mactivity: Activity
    private var listener: OnItemClickListener? = null
    // for load more
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private var onLoadMoreListener: OnLoadMoreListener? =
        null
    // The minimum amount of items to have below your current scroll position
// before loading more.
    private var isLoading = false
    private val visibleThreshold = 5
    private var lastVisibleItem = 0
    private var totalItemCount = 0

    interface OnItemClickListener {
        fun onItemClick(homelist: Home_Post_Modal)
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    fun add(position: Int, item: Home_Post_Modal) {
        homelist.add(position, item)
        notifyItemInserted(position)
    }



    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(mactivity).inflate(R.layout.home_post_item, parent, false)
            return ViewHolderRow(view)
        }
        else if (viewType == VIEW_TYPE_LOADING) {
            val view = LayoutInflater.from(mactivity).inflate(R.layout.item_progressbar, parent, false)
            return ViewHolderLoading(view)
        }
        val view = LayoutInflater.from(mactivity).inflate(R.layout.item_progressbar, parent, false)
        return ViewHolderLoading(view)

    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return homelist.size ?: 0
    }

    fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener?) {
        onLoadMoreListener = mOnLoadMoreListener
    }

    fun setOnItemListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun getItemViewType(position: Int): Int {
       if (homelist[position] == null)

           return VIEW_TYPE_LOADING
       else
           return VIEW_TYPE_ITEM
    }

    fun setLoaded() {
        isLoading = false
    }

    private inner class ViewHolderLoading(view: View) :
        RecyclerView.ViewHolder(view) {
        var progressBar: ProgressBar

        init {
            progressBar = view.findViewById<View>(R.id.itemProgressbar) as ProgressBar
        }
    }

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    inner class ViewHolderRow(view: View) : RecyclerView.ViewHolder(view) {
        //var txtEmail: TextView
        //var txtPhone: TextView
        fun bind(homelist: Home_Post_Modal, listener: OnItemClickListener?) {

            itemView.setOnClickListener {
                listener!!.onItemClick(homelist) }
        }




            //menu button
            val post_menu = view.findViewById<ImageView>(R.id.post_menu)


            //post pic,username,status,posttime
            val postProfilePic = view.findViewById<CircleImageView>(R.id.postProfilePic)
            val postUserName = view.findViewById<TextView>(R.id.postUserName)
            val postTime = view.findViewById<TextView>(R.id.postTime)
            val postStatus = view.findViewById<TextView>(R.id.postStatus)

            //image layout
            val iv_postImgA = view.findViewById<ImageView>(R.id.iv_postImgA)
            val iv_postImgB = view.findViewById<ImageView>(R.id.iv_postImgB)
            val postImageC = view.findViewById<ImageView>(R.id.postImageC)
            val iv_postImgD = view.findViewById<ImageView>(R.id.iv_postImgD)

            //media layout
            val media_layout = view.findViewById<LinearLayout>(R.id.lay_postMedia)

            //three image layout
            val threeImgLayout = view.findViewById<LinearLayout>(R.id.lay_postForBelowImgs)

            //more text
            val more_text = view.findViewById<TextView>(R.id.more_text)


            //like comment and share button
            val likeButton = view.findViewById<LinearLayout>(R.id.lay_postLike)
            val commentButton = view.findViewById<LinearLayout>(R.id.lay_postComment)
            val sharePostButton = view.findViewById<LinearLayout>(R.id.lay_postShare)


            //no. of likes,comment and shares
            val likesText = view.findViewById<TextView>(R.id.likes)
            val commentText = view.findViewById<TextView>(R.id.comment)
            val shareText = view.findViewById<TextView>(R.id.share)


            //loading icons
            val loading_iconA = view.findViewById<ProgressBar>(R.id.loading_iconA)
            val loading_iconB = view.findViewById<ProgressBar>(R.id.loading_iconB)
            val loading_iconC = view.findViewById<ProgressBar>(R.id.loading_iconC)
            val loading_iconD = view.findViewById<ProgressBar>(R.id.loading_iconD)


            val frame_postA = view.findViewById<FrameLayout>(R.id.frame_postAi)
            val frame_postB = view.findViewById<FrameLayout>(R.id.frame_postB)
            val frame_postC = view.findViewById<FrameLayout>(R.id.frame_postC)


            //video icons
            val video_iconA = view.findViewById<ImageView>(R.id.iv_postIsVideoA)
            val video_iconB = view.findViewById<ImageView>(R.id.iv_postIsVideoB)
            val video_iconC = view.findViewById<ImageView>(R.id.iv_postIsVideoC)
            val video_iconD = view.findViewById<ImageView>(R.id.iv_postIsVideoD)

            val likeIcon = view.findViewById<LikeButton>(R.id.iv_postLike)





    }

    // Provide a suitable constructor (depends on the kind of dataset)
    init {
        mactivity = mcontext as Activity
        homelist2 = homelist
        // load more
        val linearLayoutManager =
            recyclerView.layoutManager as LinearLayoutManager?
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayoutManager!!.itemCount
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener!!.onLoadMore()
                    }
                    isLoading = true
                }
            }
        })
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

     //   place the contents of the view with that element
        if (holder is ViewHolderRow) {
            val userViewHolder = holder


            //userViewHolder.po.text = map!!["KEY_EMAIL"]
           // userViewHolder.txtPhone.text = map["KEY_PHONE"]


            userViewHolder.postTime.setText(position.toString())

            homelist2!![position]?.let { userViewHolder.bind(it, listener) }


        }

        else if (holder is ViewHolderLoading) {
            holder.progressBar.isIndeterminate = true
        }


    }


}