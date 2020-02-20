package com.example.lanecrowd.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.lancrowd.activity.modal.Home_Post_Modal
import com.example.lanecrowd.Home_Fragment.Home_Post_Fragment
import com.example.lanecrowd.R
import com.example.lanecrowd.activity.Show_Comment_Activity
import com.example.lanecrowd.util.URL
import com.like.LikeButton
import com.like.OnLikeListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class Home_Post_Adapter_New(val list: ArrayList<Home_Post_Modal>, val context: Context, val activity: Home_Post_Fragment) : RecyclerView.Adapter<Home_Post_Adapter_New.ViewHolder>() {


    private var mcontext: Context? = null
    private var mactivity: Activity? = null
    private var clickListener: OnItemClickListener? = null

    // for load more
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private val onLoadMoreListener: OnLoadMoreListener? = null

    // The minimum amount of items to have below your current scroll position
// before loading more.
    private var isLoading = false
    private val visibleThreshold = 5
    private var lastVisibleItem = 0
    private  var totalItemCount:Int = 0

    interface OnItemClickListener {
        fun onItemClick(item: HashMap<String?, String?>?)
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }






    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view = LayoutInflater.from(context).inflate(R.layout.home_post_item, parent, false)
        return ViewHolder(view)


    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {




        //set Comment Layout listener
        setCommentLayoutLIstener(holder)

        // menu listener
        showMenuListener(holder,position)


        //set User Profile, User Name and Post time
        setUSerImageNameAndTime(holder, position)


        //set Like Button Listener
        setLikeListener(holder, position)



        setPostMediaData(holder, position)


    }

    private fun setPostMediaData(holder: Home_Post_Adapter_New.ViewHolder, position: Int) {

        var url: String = ""
        var isImage: Boolean=false


        //1 or more images exist
        if (!list.get(position).post_files.get(0).equals("empty")) {


            //only 1 image
            if (list.get(position).post_files.size == 1) {


                if (list.get(position).post_files.get(0).contains("jpg") || list.get(position).post_files.get(0).contains("png") || list.get(position).post_files.get(0).contains("jpeg") ) {
                    url = URL.imagePath
                    isImage = true
                }
                else {
                    isImage = false
                    url = URL.videoPath
                }

                println("isImageeee" + list.get(position).isImage)
                println("mmmmm" + url + list.get(position).post_files.get(0))

                set_Success_Glide_Data(
                    url + list.get(position).post_files.get(0),
                    holder.loading_iconA,
                    holder.iv_postImgA,
                    holder.video_iconA,
                    isImage
                )


                goneOtherImageLayout(holder, holder.threeImgLayout)

            }

            //only 2 images
            else if (list.get(position).post_files.size == 2) {


                if (list.get(position).post_files.get(0).contains("jpg") || list.get(position).post_files.get(0).contains("png") || list.get(position).post_files.get(0).contains("jpeg") ) {
                    url = URL.imagePath
                    isImage = true
                }
                else {
                    isImage = false
                    url = URL.videoPath
                }
                set_Success_Glide_Data(
                    url + list.get(position).post_files.get(0),
                    holder.loading_iconA,
                    holder.iv_postImgA,
                    holder.video_iconA,
                    isImage
                )
                set_Success_Glide_Data(
                    url + list.get(position).post_files.get(1),
                    holder.loading_iconB,
                    holder.iv_postImgB,
                    holder.video_iconB,
                 isImage
                )

                goneOtherImageLayout(holder, holder.frame_postB)
                goneOtherImageLayout(holder, holder.frame_postC)

            }

            //only 3 images
            else if (list.get(position).post_files.size == 3) {


                if (list.get(position).post_files.get(0).contains("jpg") || list.get(position).post_files.get(0).contains("png") || list.get(position).post_files.get(0).contains("jpeg") ) {
                    url = URL.imagePath
                    isImage = true
                }
                else {
                    isImage = false
                    url = URL.videoPath
                }

                set_Success_Glide_Data(
                    url + list.get(position).post_files.get(0),
                    holder.loading_iconA,
                    holder.iv_postImgA,
                    holder.video_iconA,
                    isImage
                )
                set_Success_Glide_Data(
                    url + list.get(position).post_files.get(1),
                    holder.loading_iconB,
                    holder.iv_postImgB,
                    holder.video_iconB,
                   isImage
                )
                set_Success_Glide_Data(
                    url + list.get(position).post_files.get(2),
                    holder.loading_iconC,
                    holder.postImageC,
                    holder.video_iconC,
                    isImage
                )

                goneOtherImageLayout(holder, holder.frame_postC)


            }

            //only 4 images
            else if (list.get(position).post_files.size == 4) {


                if (list.get(position).post_files.get(0).contains("jpg") || list.get(position).post_files.get(0).contains("png") || list.get(position).post_files.get(0).contains("jpeg") ) {
                    url = URL.imagePath
                    isImage = true
                }
                else {
                    isImage = false
                    url = URL.videoPath
                }

                set_Success_Glide_Data(
                    url + list.get(position).post_files.get(0),
                    holder.loading_iconA,
                    holder.iv_postImgA,
                    holder.video_iconA,
                    isImage
                )
                set_Success_Glide_Data(
                    url + list.get(position).post_files.get(1),
                    holder.loading_iconB,
                    holder.iv_postImgB,
                    holder.video_iconB,
                   isImage
                )
                set_Success_Glide_Data(
                    url + list.get(position).post_files.get(2),
                    holder.loading_iconC,
                    holder.postImageC,
                    holder.video_iconC,
                   isImage
                )
                set_Success_Glide_Data(
                    url + list.get(position).post_files.get(3),
                    holder.loading_iconD,
                    holder.iv_postImgD,
                    holder.video_iconD,
                   isImage
                )


            }

            //more than 4 images
            else if (list.get(position).post_files.size > 4) {


                if (list.get(position).post_files.get(0).contains("jpg") || list.get(position).post_files.get(0).contains("png") || list.get(position).post_files.get(0).contains("jpeg") ) {
                    url = URL.imagePath
                    isImage = true
                }
                else {
                    isImage = false
                    url = URL.videoPath
                }
                set_Success_Glide_Data(
                    url + list.get(position).post_files.get(0),
                    holder.loading_iconA,
                    holder.iv_postImgA,
                    holder.video_iconA,
                   isImage
                )
                set_Success_Glide_Data(
                    url + list.get(position).post_files.get(1),
                    holder.loading_iconB,
                    holder.iv_postImgB,
                    holder.video_iconB,
                   isImage
                )
                set_Success_Glide_Data(
                    url + list.get(position).post_files.get(2),
                    holder.loading_iconC,
                    holder.postImageC,
                    holder.video_iconC,
                    isImage
                )
                set_Success_Glide_Data(
                    url + list.get(position).post_files.get(3),
                    holder.loading_iconD,
                    holder.iv_postImgD,
                    holder.video_iconD,
                    list.get(position).isImage
                )

                holder.more_text.visibility = View.VISIBLE

            }


        } else {
            println("else_called" + position)
            holder.media_layout.visibility = View.GONE
        }

    }

    private fun goneOtherImageLayout(holder: ViewHolder, imageLayout: View?) {

        imageLayout!!.visibility = View.GONE


    }

    private fun setLikeListener(holder: ViewHolder, position: Int) {



        holder.likeIcon.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton) {

           likePost(position)

            }
            override fun unLiked(likeButton: LikeButton) {

                likePost(position)



            }
        })



    }

    private fun likePost(position: Int) {




        activity.showVibration()

             if (list[position].isMyLike) {
                 list[position].total_likes =
                     (Integer.parseInt(list[position].total_likes) - 1).toString() + ""
             } else {
                 list[position].total_likes = (Integer.parseInt(list[position].total_likes) + 1).toString() + ""

             }

             println("kksd"+list[position].total_likes)

             //update their value
             list[position].isMyLike = !list[position].isMyLike



           notifyItemChanged(position,position)

            activity.likeDislike(list.get(position).post_id,URL.userId)




    }

    private fun setUSerImageNameAndTime(holder: ViewHolder, position: Int) {



        Picasso.get()
            .load(URL.profilePicPath + list.get(position).profile_pic)
            .placeholder(R.drawable.placeholder_profile)
            .error(R.drawable.placeholder_profile)
            .into(holder.postProfilePic)


        holder.postUserName.text = list.get(position).posted_by
        holder.postTime.text = list.get(position).posted_on


        if(!list.get(position).post.equals(""))
        holder.postStatus.text = list.get(position).post
        else
            goneOtherImageLayout(holder, holder.postStatus)




        if (!list.get(position).total_likes.equals("0"))
            holder.likesText.text = list.get(position).total_likes
        else
            holder.likesText.text = ""


        println("total_likes" + list.get(position).total_likes)


        if (!list.get(position).total_share.equals("0"))
            holder.shareText.text = list.get(position).total_share
        else
            holder.shareText.text = ""


        if (!list.get(position).total_comments.equals("0"))
            holder.commentText.text = list.get(position).total_comments
        else
            holder.commentText.text = ""




       // holder.likeIcon.setImageResource(if (list.get(position).isMyLike) R.drawable.ic_like_fill else R.drawable.ic_like)

        holder.likeIcon.isLiked=list.get(position).isMyLike

    }

    private fun showMenuListener(
        holder: ViewHolder,
        position: Int
    ) {

        holder.post_menu.setOnClickListener(View.OnClickListener {

            //self post
            if(list.get(position).user_id.equals(URL.userId))
            activity.showMenu(position,list.get(position).post_id,holder.post_menu,true)

            //other post
            else
                activity.showMenu(position,list.get(position).post_id,holder.post_menu,false)

        })


    }



    private fun setCommentLayoutLIstener(holder: ViewHolder) {

        holder.commentButton.setOnClickListener(View.OnClickListener {
            context.startActivity(
                Intent(context, Show_Comment_Activity::class.java).setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                )
            )

        })

    }


    private fun set_Success_Glide_Data(
        url: String,
        loading_icon_gone: ProgressBar,
        post_img: ImageView,
        videoIcona: ImageView,
        isImage: Boolean
    ) {


        println("sayed_url" + url)
        println("isImage" + isImage)


        Glide.with(context).load(url).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)).listener(object : RequestListener<Drawable?> {
            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {

                loading_icon_gone.visibility = View.GONE

                videoIcona.visibility = View.GONE

                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                loading_icon_gone.visibility = View.GONE

                if (isImage)
                    videoIcona.visibility = View.GONE
                else
                    videoIcona.visibility = View.VISIBLE

                return false
            }
        }).thumbnail(0.01f).into(post_img)


    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


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




}