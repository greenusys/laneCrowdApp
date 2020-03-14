package com.example.lanecrowd.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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
import com.example.lancrowd.activity.modal.Story_Modal
import com.example.lanecrowd.Home_Fragment.Home_Post_Fragment
import com.example.lanecrowd.R
import com.example.lanecrowd.activity.Add_Post_Activity
import com.example.lanecrowd.activity.Profile_Activity
import com.example.lanecrowd.activity.ShowPhotoActivity
import com.example.lanecrowd.activity.Show_Comment_Activity
import com.example.lanecrowd.util.URL
import com.example.lanecrowd.view_modal.FetchPostVm
import com.like.LikeButton
import com.like.OnLikeListener
import de.hdodenhof.circleimageview.CircleImageView


class Home_Post_Adapter(
    val storyLIst: ArrayList<Story_Modal>,
    val list: ArrayList<Home_Post_Modal>,
    val context: Context,
    val activity: Home_Post_Fragment) : RecyclerView.Adapter<Home_Post_Adapter.ViewHolder>() {


    private val TYPE_ITEM_STORY = 0
    private val TYPE_ITEM_WHATS = 1
    private val TYPE_ITEM_NORMAL = 2

     var viewmodel: FetchPostVm

    init {
        viewmodel = ViewModelProviders.of(activity).get(FetchPostVm::class.java)

    }


    private val viewPool = RecyclerView.RecycledViewPool()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        if (viewType == TYPE_ITEM_STORY) {

            val view =
                LayoutInflater.from(context).inflate(R.layout.story_recyclerview, parent, false)
            return ViewHolder(view)

        } else if (viewType == TYPE_ITEM_WHATS) {

            val view =
                LayoutInflater.from(context).inflate(R.layout.whats_mind_layout, parent, false)
            return ViewHolder(view)

        } else if (viewType == TYPE_ITEM_NORMAL) {

            val view = LayoutInflater.from(context).inflate(R.layout.home_post_item, parent, false)
            return ViewHolder(view)

        }


        val view = LayoutInflater.from(context).inflate(R.layout.home_post_item, parent, false)
        return ViewHolder(view)


    }

    override fun getItemCount(): Int {
        return list.size + 2
    }


    override fun getItemViewType(position: Int): Int {

        if (position == 0)
            return TYPE_ITEM_STORY
        if (position == 1)
            return TYPE_ITEM_WHATS


        return TYPE_ITEM_NORMAL


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if (position == 0) {

            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager.initialPrefetchItemCount = 8
            holder.story_rv.layoutManager = layoutManager
            val storyAdapter = Story_Adapter(layoutManager,activity,holder.story_rv,storyLIst, context)
            holder.story_rv.adapter = storyAdapter
            holder.story_rv.setRecycledViewPool(viewPool)
        }
        else if (position == 1)
            setWhatLayoutLIstener(holder)
        else {
            //  println("elseeeee"+position)
            //set Comment Layout listener
            setCommentLayoutLIstener(holder, position)

            // menu listener
            showMenuListener(holder, position)


            //set User Profile, User Name and Post time
            setUSerImageNameAndTime(holder, position)


            //set Like Button Listener
            setLikeListener(holder, position)

            setPostMediaData(holder, position)


            setPhotoVideoViewListener(position, holder)
        }


    }



    private fun setPhotoVideoViewListener(position: Int, holder: ViewHolder) {


        holder.postProfilePic.setOnClickListener(View.OnClickListener {
            println("checkImageee" + checkIsImage(position - 2))
            context.startActivity(
                Intent(context, Profile_Activity::class.java)
                    .putExtra("postUserId", list.get(position-2).user_id)
                    .putExtra("postUserName", list.get(position-2).posted_by.capitalize())


            )



        })

        holder.iv_postImgA.setOnClickListener(View.OnClickListener {
            println("checkImageee" + checkIsImage(position - 2))
            context.startActivity(
                Intent(context, ShowPhotoActivity::class.java)
                    .putExtra("isImage", checkIsImage(position - 2).toString())
                    .putExtra("position", "0")
                    .putExtra("name", list.get(position-2).posted_by.capitalize())
                    .putStringArrayListExtra("files", list.get(position - 2).post_files)
            )

        })

        holder.iv_postImgB.setOnClickListener(View.OnClickListener {
            println("checkImageee" + checkIsImage(position - 2))
            context.startActivity(
                Intent(context, ShowPhotoActivity::class.java)
                    .putExtra("isImage", checkIsImage(position - 2).toString())
                    .putExtra("position", "1")
                    .putExtra("name", list.get(position-2).posted_by.capitalize())
                    .putStringArrayListExtra("files", list.get(position - 2).post_files)
            )

        })
        holder.postImageC.setOnClickListener(View.OnClickListener {
            println("checkImageee" + checkIsImage(position - 2))
            context.startActivity(
                Intent(context, ShowPhotoActivity::class.java)
                    .putExtra("isImage", checkIsImage(position - 2).toString())
                    .putExtra("position", "2")
                    .putExtra("name", list.get(position-2).posted_by.capitalize())
                    .putStringArrayListExtra("files", list.get(position - 2).post_files)
            )

        })

        holder.iv_postImgD.setOnClickListener(View.OnClickListener {
            println("checkImageee" + checkIsImage(position - 2))
            context.startActivity(
                Intent(context, ShowPhotoActivity::class.java)
                    .putExtra("isImage", checkIsImage(position - 2).toString())
                    .putExtra("position", "3")
                    .putExtra("name", list.get(position-2).posted_by.capitalize())
                    .putStringArrayListExtra("files", list.get(position - 2).post_files)
            )

        })


    }


    private fun setWhatLayoutLIstener(holder: ViewHolder) {
        holder.whatmain_layout.setOnClickListener(View.OnClickListener {
            context.startActivity(
                Intent(
                    context, Add_Post_Activity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra("from","post")
            )

        })
    }

    private fun setPostMediaData(holder: ViewHolder, position: Int) {


        //  var position:Int=position-2

        var url: String
        var isImage: Boolean


         println("check_sayed"+list.get(position - 2).post_files.get(0))


        //1 or more images exist
        if (!list.get(position - 2).post_files.get(0).equals("empty")) {


            //only 1 image
            if (list.get(position - 2).post_files.size == 1) {


                if (checkIsImage(position - 2)) {
                    url = URL.imagePath
                    isImage = true
                } else {
                    isImage = false
                    url = URL.videoPath
                }

                println("isImageeee" + list.get(position - 2).isImage)
                println("mmmmm" + url + list.get(position - 2).post_files.get(0))

                visibleOtherImageLayout(holder.media_layout)
                goneOtherImageLayout( holder.threeImgLayout)
                visibleOtherImageLayout(holder.iv_postImgA)

                set_Success_Glide_Data(
                    url + list.get(position - 2).post_files.get(0),
                    holder.loading_iconA,
                    holder.iv_postImgA,
                    holder.video_iconA,
                    isImage
                )





            }

            //only 2 images
            else if (list.get(position - 2).post_files.size == 2) {


                if (checkIsImage(position - 2)) {
                    url = URL.imagePath
                    isImage = true
                } else {
                    isImage = false
                    url = URL.videoPath
                }

                visibleOtherImageLayout(holder.media_layout)
                goneOtherImageLayout( holder.frame_postB)
                goneOtherImageLayout( holder.frame_postC)
                visibleOtherImageLayout(holder.iv_postImgA)
                visibleOtherImageLayout(holder.iv_postImgB)


                set_Success_Glide_Data(
                    url + list.get(position - 2).post_files.get(0),
                    holder.loading_iconA,
                    holder.iv_postImgA,
                    holder.video_iconA,
                    isImage
                )
                set_Success_Glide_Data(
                    url + list.get(position - 2).post_files.get(1),
                    holder.loading_iconB,
                    holder.iv_postImgB,
                    holder.video_iconB,
                    isImage
                )



            }

            //only 3 images
            else if (list.get(position - 2).post_files.size == 3) {


                if (checkIsImage(position - 2)) {
                    url = URL.imagePath
                    isImage = true
                } else {
                    isImage = false
                    url = URL.videoPath
                }

                visibleOtherImageLayout(holder.media_layout)
                goneOtherImageLayout(holder.frame_postC)
                visibleOtherImageLayout(holder.iv_postImgA)
                visibleOtherImageLayout(holder.iv_postImgB)
                visibleOtherImageLayout(holder.postImageC)

                set_Success_Glide_Data(
                    url + list.get(position - 2).post_files.get(0),
                    holder.loading_iconA,
                    holder.iv_postImgA,
                    holder.video_iconA,
                    isImage
                )
                set_Success_Glide_Data(
                    url + list.get(position - 2).post_files.get(1),
                    holder.loading_iconB,
                    holder.iv_postImgB,
                    holder.video_iconB,
                    isImage
                )
                set_Success_Glide_Data(
                    url + list.get(position - 2).post_files.get(2),
                    holder.loading_iconC,
                    holder.postImageC,
                    holder.video_iconC,
                    isImage
                )



            }

            //only 4 images
            else if (list.get(position - 2).post_files.size == 4) {


                if (checkIsImage(position - 2)) {
                    url = URL.imagePath
                    isImage = true
                } else {
                    isImage = false
                    url = URL.videoPath
                }
                holder.more_text.visibility = View.GONE
                visibleOtherImageLayout(holder.media_layout)
                visibleOtherImageLayout(holder.threeImgLayout)
                visibleOtherImageLayout(holder.iv_postImgA)
                visibleOtherImageLayout(holder.iv_postImgB)
                visibleOtherImageLayout(holder.postImageC)
                visibleOtherImageLayout(holder.iv_postImgD)


                set_Success_Glide_Data(
                    url + list.get(position - 2).post_files.get(0),
                    holder.loading_iconA,
                    holder.iv_postImgA,
                    holder.video_iconA,
                    isImage
                )
                set_Success_Glide_Data(
                    url + list.get(position - 2).post_files.get(1),
                    holder.loading_iconB,
                    holder.iv_postImgB,
                    holder.video_iconB,
                    isImage
                )
                set_Success_Glide_Data(
                    url + list.get(position - 2).post_files.get(2),
                    holder.loading_iconC,
                    holder.postImageC,
                    holder.video_iconC,
                    isImage
                )
                set_Success_Glide_Data(
                    url + list.get(position - 2).post_files.get(3),
                    holder.loading_iconD,
                    holder.iv_postImgD,
                    holder.video_iconD,
                    isImage
                )


            }

            //more than 4 images
            else if (list.get(position - 2).post_files.size > 4) {


                if (checkIsImage(position - 2)) {
                    url = URL.imagePath
                    isImage = true
                } else {
                    isImage = false
                    url = URL.videoPath
                }


                visibleOtherImageLayout(holder.media_layout)
                visibleOtherImageLayout(holder.threeImgLayout)
                visibleOtherImageLayout(holder.iv_postImgA)
                visibleOtherImageLayout(holder.iv_postImgB)
                visibleOtherImageLayout(holder.postImageC)
                visibleOtherImageLayout(holder.iv_postImgD)

                set_Success_Glide_Data(
                    url + list.get(position - 2).post_files.get(0),
                    holder.loading_iconA,
                    holder.iv_postImgA,
                    holder.video_iconA,
                    isImage
                )
                set_Success_Glide_Data(
                    url + list.get(position - 2).post_files.get(1),
                    holder.loading_iconB,
                    holder.iv_postImgB,
                    holder.video_iconB,
                    isImage
                )
                set_Success_Glide_Data(
                    url + list.get(position - 2).post_files.get(2),
                    holder.loading_iconC,
                    holder.postImageC,
                    holder.video_iconC,
                    isImage
                )
                set_Success_Glide_Data(
                    url + list.get(position - 2).post_files.get(3),
                    holder.loading_iconD,
                    holder.iv_postImgD,
                    holder.video_iconD,
                    list.get(position - 2).isImage
                )

                holder.more_text.visibility = View.VISIBLE


            }


        } else {
            //println("else_called" + position-2)
            holder.media_layout.visibility = View.GONE
        }

    }

    private fun checkIsImage(position: Int): Boolean {

        if (list.get(position).post_files.get(0).contains("jpg") || list.get(position).post_files.get(
                0
            ).contains("png") || list.get(position).post_files.get(0).contains("jpeg")
        )
            return true

        return false

    }

    private fun goneOtherImageLayout(imageLayout: View?) {


        imageLayout!!.visibility = View.GONE


    }

    private fun visibleOtherImageLayout(imageLayout: View?) {

        imageLayout!!.visibility = View.VISIBLE


    }


    private fun setLikeListener(holder: ViewHolder, position: Int) {

        // var position:Int=position-2

        holder.likeIcon.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton) {

                println("bbbbb_liked" + position)
                likePost(position)

            }

            override fun unLiked(likeButton: LikeButton) {

                println("bbbbb_disliked" + position)

                likePost(position)


            }
        })


    }

    private fun likePost(position: Int) {


        activity.showVibration()

        if (list[position - 2].isMyLike) {
            list[position - 2].total_likes =
                (Integer.parseInt(list[position - 2].total_likes) - 1).toString() + ""
        } else {
            list[position - 2].total_likes =
                (Integer.parseInt(list[position - 2].total_likes) + 1).toString() + ""

        }

        println("total_likes" + list[position - 2].total_likes)
        println("timeofpost" + list[position - 2].posted_on)

        //update their value
        list[position - 2].isMyLike = !list[position - 2].isMyLike



        notifyItemChanged(position, position)

        activity.likeDislike(list.get(position - 2).post_id, URL.userId)


    }

    private fun setUSerImageNameAndTime(holder: ViewHolder, position: Int) {

        Glide.with(context)
            .load(
                URL.profilePicPath + list.get(position - 2)
                    .profile_pic).apply(
                RequestOptions().placeholder(R.drawable.placeholder_profile)
            )
            .thumbnail(0.01f).into(holder.postProfilePic!!)



        //for change profile or cover pic
        if(!list.get(position - 2).post_head.equals("")) {
            var postHead ="<b>"+ list.get(position - 2).posted_by.capitalize()+"</b> "+  list.get(position - 2).post_head

            holder.postUserName.text = HtmlCompat.fromHtml(postHead, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        //print only name
        else
            holder.postUserName.text = list.get(position - 2).posted_by.capitalize()


        holder.postTime.text = list.get(position - 2).posted_on


        if (!list.get(position - 2).post.equals(""))
            holder.postStatus.text = list.get(position - 2).post
        else
            goneOtherImageLayout( holder.postStatus)




        if (!list.get(position - 2).total_likes.equals("0"))
            holder.likesText.text = list.get(position - 2).total_likes
        else
            holder.likesText.text = ""


        println("total_likes_kaif" + list.get(position - 2).total_likes)
        println("isMyLIke" + list.get(position - 2).isMyLike)


        if (!list.get(position - 2).total_share.equals("0"))
            holder.shareText.text = list.get(position - 2).total_share
        else
            holder.shareText.text = ""


        if (!list.get(position - 2).total_comments.equals("0"))
            holder.commentText.text = list.get(position - 2).total_comments
        else
            holder.commentText.text = ""


        // holder.likeIcon.setImageResource(if (list.get(position).isMyLike) R.drawable.ic_like_fill else R.drawable.ic_like)

        holder.likeIcon.isLiked = list.get(position - 2).isMyLike

    }

    private fun showMenuListener(holder: ViewHolder, position: Int) {

        holder.post_menu.setOnClickListener(View.OnClickListener {

            //self post
            if (list.get(position - 2).user_id.equals(URL.userId))
                activity.showMenu(position, list.get(position - 2).post_id, holder.post_menu, true)

            //other post
            else
                activity.showMenu(position, list.get(position - 2).post_id, holder.post_menu, false)

        })


    }


    private fun setCommentLayoutLIstener(holder: ViewHolder, position: Int) {

        holder.commentButton.setOnClickListener(View.OnClickListener {
            activity.startActivityForResult(
                Intent(context, Show_Comment_Activity::class.java)
                    .putExtra("isImage", checkIsImage(position - 2).toString())
                    .putExtra("post_id", list.get(position - 2).post_id)
                    .putExtra("user_name", list.get(position - 2).posted_by.capitalize())
                    .putExtra("user_pic", list.get(position - 2).profile_pic)
                    .putExtra("time", list.get(position - 2).posted_on)
                    .putExtra("total_likes", list.get(position - 2).total_likes)
                    .putExtra("total_comment", list.get(position - 2).total_comments)
                    .putExtra("total_shared", list.get(position - 2).total_share)
                    .putExtra("staus", list.get(position - 2).post)
                    .putExtra("post_position", position.toString())
                    .putExtra("isMyLike", list.get(position - 2).isMyLike.toString())
                    .putStringArrayListExtra("files", list.get(position - 2).post_files)
           ,1 )

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




      /*  Glide.with(context).load(url)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {

                    loading_icon_gone.visibility = View.GONE
                    videoIcona.visibility = View.GONE

                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    loading_icon_gone.visibility = View.GONE

                    if (isImage)
                        videoIcona.visibility = View.GONE
                    else
                        videoIcona.visibility = View.VISIBLE

                    return false
                }
            })
            .apply(RequestOptions().placeholder(R.drawable.placeholder))
            .thumbnail(0.01f).into(post_img).waitForLayout()

*/

        Glide.with(context).load(url).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {



                    loading_icon_gone.visibility = View.GONE
                    videoIcona.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
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

        val whatmain_layout = view.findViewById<CardView>(R.id.main_layout)


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


        val story_rv = view.findViewById<RecyclerView>(R.id.story_rv)


    }


}