package com.example.lanecrowd.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lancrowd.activity.modal.Home_Post_Modal
import com.example.lancrowd.activity.modal.Profile_Detail_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.activity.Add_Post_Activity
import com.example.lanecrowd.activity.Profile_Activity
import com.example.lanecrowd.util.URL
import com.like.LikeButton
import de.hdodenhof.circleimageview.CircleImageView


class Profile_Post_Adapter(
    val activity: Profile_Activity,
    val postUserId:String,
    val profileData: Profile_Detail_Modal,
    val list: ArrayList<Home_Post_Modal>,
    val context: Context
) :
    RecyclerView.Adapter<Profile_Post_Adapter.ViewHolder>(),View.OnClickListener {


    private val TYPE_ITEM_COVER_PROFILE = 0
    private val TYPE_ITEM_ADD_FOLLOW_LAYOUT = 1
    private val TYPE_ITEM_USER_INFO_LAYOUT = 2
    private val TYPE_ITEM_FRIENDS_LAYOUT = 3
    private val TYPE_ITEM_WHATS_MIND_LAYOUT = 4
    private val TYPE_ITEM_PHOTO_VIDEO_LAYOUT = 5
    private val TYPE_ITEM_NO_POST_LAYOUT = 6
    private val TYPE_ITEM_POST_LAYOUT = 7

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        if (viewType == TYPE_ITEM_COVER_PROFILE) {
            val view =
                LayoutInflater.from(context).inflate(R.layout.cover_profile_layout, parent, false)
            return ViewHolder(view)
        } else if (viewType == TYPE_ITEM_ADD_FOLLOW_LAYOUT) {
            val view =
                LayoutInflater.from(context).inflate(R.layout.addfollow_layout, parent, false)
            return ViewHolder(view)
        } else if (viewType == TYPE_ITEM_USER_INFO_LAYOUT) {
            val view =
                LayoutInflater.from(context).inflate(R.layout.profile_detail_layout, parent, false)
            return ViewHolder(view)
        } else if (viewType == TYPE_ITEM_FRIENDS_LAYOUT) {
            val view =
                LayoutInflater.from(context).inflate(R.layout.userfriends_layout, parent, false)
            return ViewHolder(view)
        } else if (viewType == TYPE_ITEM_WHATS_MIND_LAYOUT) {
            val view =
                LayoutInflater.from(context).inflate(R.layout.whats_mind_layout, parent, false)
            return ViewHolder(view)
        } else if (viewType == TYPE_ITEM_PHOTO_VIDEO_LAYOUT) {
            val view =
                LayoutInflater.from(context).inflate(R.layout.photo_video_layout, parent, false)
            return ViewHolder(view)
        } else if (viewType == TYPE_ITEM_NO_POST_LAYOUT) {

            val view =
                LayoutInflater.from(context).inflate(R.layout.no_data_found_post, parent, false)
            return ViewHolder(view)
        }

        val view = LayoutInflater.from(context).inflate(R.layout.home_post_item, parent, false)
        return ViewHolder(view)


    }


    override fun getItemViewType(position: Int): Int {


        if (position == 0)
            return TYPE_ITEM_COVER_PROFILE
        else if (position == 1)
            return TYPE_ITEM_ADD_FOLLOW_LAYOUT
        else if (position == 2)
            return TYPE_ITEM_USER_INFO_LAYOUT
        else if (position == 3)
            return TYPE_ITEM_FRIENDS_LAYOUT
        else if (position == 4)
            return TYPE_ITEM_WHATS_MIND_LAYOUT
        else if (position == 5)
            return TYPE_ITEM_PHOTO_VIDEO_LAYOUT
        else if(position==6) {
            if (list == null || list.size == 0)
                return TYPE_ITEM_NO_POST_LAYOUT
        }


        return TYPE_ITEM_POST_LAYOUT
    }

    override fun getItemCount(): Int {

        //check if list.size==null then show no data layout
        if (list == null || list.size == 0)
            return 6

        return list.size + 6
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        //for profile and cover pic
        if (position == 0) {


            holder.iv_cover_image_profile.setOnClickListener(this)
            holder.iv_profile_image_profile.setOnClickListener(this)
            holder.txt_changeCover.setOnClickListener(this)
            holder.txt_changeProfile.setOnClickListener(this)

            setImagetoGLide(
                URL.profilePicPath + profileData.basic_info.get(0).profile_picture,
                holder.iv_profile_image_profile!!,
                R.drawable.placeholder_profile
            )
            setImagetoGLide(
                URL.coverPicPath + profileData.basic_info.get(0).cover_photo,
                holder.iv_cover_image_profile!!,
                R.drawable.placeholder
            )


        }
        //for add follow layout
        else if (position == 1) {

        }
        //user info layout
        else if (position == 2) {

            //set user's info
            setProfileData(holder, profileData)

        }
        //show 7 friend layout
        else if (position == 3) {

        }

        //what's mind layout
        else if (position == 4) {

            setWhatLayoutLIstener(holder)

        }
        //photo video layout
        else if (position == 5) {

        }

        //no post layout
        else if (position == 6) {

        }

        //profile post layout
        else  {


            //set Comment Layout listener
           // setCommentLayoutLIstener(holder, position)

            // menu listener
            //showMenuListener(holder, position)


            //set User Profile, User Name and Post time
            setUSerImageNameAndTime(holder, position)


            //set Like Button Listener
           // setLikeListener(holder, position)

            //set Share Post Listener
           // setSharePostListener(holder, position)

            //set images and videos
           // setPostMediaData(holder, position)


            //set listener
           // setPhotoVideoViewListener(position, holder)


            //set copy listener
           // copyStatus(position, holder)


        }

    }


    private fun setUSerImageNameAndTime(holder: ViewHolder, position: Int) {


        println("position"+position)
        println("postedby"+list.get(position-7).posted_by)
        println("checkaif"+list.get(position-7).post_id)

             Glide.with(context)
                 .load(
                     URL.profilePicPath + list.get(position-7)
                         .profile_pic
                 ).apply(
                     RequestOptions().placeholder(R.drawable.placeholder_profile)
                 )
                 .thumbnail(0.01f).into(holder.postProfilePic)


             //for change profile or cover pic
             if (!list.get(position - 7).post_head.equals("")) {
                 println("iffkaif")
                 var postHead = "<b>" + list.get(position - 7).posted_by.capitalize() + "</b> " + list.get(position - 7).post_head

                 holder.postUserName.text =
                     HtmlCompat.fromHtml(postHead, HtmlCompat.FROM_HTML_MODE_LEGACY)
             }

             //print only name
             else
             {
                 println("ksdjfksd"+list.get(position - 7).posted_by.capitalize())
                 holder.postUserName.text = list.get(position - 7).posted_by.capitalize()

             }


             holder.postTime.text = list.get(position - 7).posted_on


             if (!list.get(position - 7).post.equals("")) {

                 visibleOtherImageLayout(holder.postStatus)
                 holder.postStatus.text = list.get(position - 7).post
             } else
                 goneOtherImageLayout(holder.postStatus)




             if (!list.get(position - 7).total_likes.equals("0"))
                 holder.likesText.text = list.get(position - 7).total_likes
             else
                 holder.likesText.text = ""


             println("total_likes_kaif" + list.get(position - 7).total_likes)
             println("isMyLIke" + list.get(position - 2).isMyLike)


             if (!list.get(position - 7).total_share.equals("0"))
                 holder.shareText.text = list.get(position - 7).total_share
             else
                 holder.shareText.text = ""


             if (!list.get(position - 7).total_comments.equals("0"))
                 holder.commentText.text = list.get(position - 7).total_comments
             else
                 holder.commentText.text = ""


             holder.likeIcon.isLiked = list.get(position - 7).isMyLike

    }
    private fun goneOtherImageLayout(imageLayout: View?) {


        imageLayout!!.visibility = View.GONE


    }

    private fun visibleOtherImageLayout(imageLayout: View?) {

        imageLayout!!.visibility = View.VISIBLE


    }


    private fun setWhatLayoutLIstener(holder: ViewHolder) {
        holder.whatmain_layout.setOnClickListener(View.OnClickListener {
            context.startActivity(
                Intent(
                    context, Add_Post_Activity::class.java
                )
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra("from", "post")
            )

        })
    }

    private fun setProfileData(holder: ViewHolder, result: Profile_Detail_Modal) {

        var status = ""

        //work status
        if (result.work_details.size > 0)
            holder.work_status.text = HtmlCompat.fromHtml(
                result!!.work_details.get(0).position.capitalize() + " at " + "<b>" + result!!.work_details.get(
                    0
                ).company_name.capitalize() + "</b> ", HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        else
            goneView(holder.work_status!!, false)


        //college status
        if (result.user_university_details.size > 0) {
            status = if (result.user_university_details.get(0).graduated.equals("1"))
                "Studied "
            else
                "Study "

            holder.college_status.text = HtmlCompat.fromHtml(
                status + result!!.user_university_details.get(0).course.capitalize() + " at " + "<b>" + result.user_university_details.get(
                    0
                ).university.capitalize() + "</b>", HtmlCompat.FROM_HTML_MODE_LEGACY
            )

        } else
            goneView(holder.college_status!!, false)


        //school staus
        if (result.school_details.size > 0)
            holder.school_status.text = HtmlCompat.fromHtml(
                "Went to " + "<b>" + result!!.school_details.get(0).school.capitalize() + "</b> ",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        else
            goneView(holder.school_status!!, false)


        //relationship status
        if (result.relationship_status.size > 0)
            holder.relatino_status.text =
                result!!.relationship_status.get(0).relationship_status.capitalize()
        else
            goneView(holder.relatino_status!!, false)


    }

    private fun goneView(view: View?, value: Boolean) {

        if (value)
            view!!.visibility = View.VISIBLE
        else
            view!!.visibility = View.GONE


    }

    private fun setImagetoGLide(url: String, view: ImageView, placeholder: Int) {


        Glide.with(context)
            .load(url)
            .apply(RequestOptions().placeholder(placeholder))
            .thumbnail(0.01f).into(view!!)

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        //add follow layout
        var addFollowLayout = view.findViewById<LinearLayout>(R.id.addFollowLayout)
        var follow_friend = view.findViewById<TextView>(R.id.follow_friend)
        var add_friend = view.findViewById<TextView>(R.id.add_friend)
        var menu_friend = view.findViewById<TextView>(R.id.menu_friend)


        //for cover and profile pic
        var txt_changeCover = view.findViewById<TextView>(R.id.txt_changeCover)
        var txt_changeProfile = view.findViewById<TextView>(R.id.txt_changeProfile)
        var txt_doneChangeProfile = view.findViewById<TextView>(R.id.txt_doneChangeProfile)
        var txt_doneChangeCover = view.findViewById<TextView>(R.id.txt_doneChangeCover)

        var iv_cover_image_profile = view.findViewById<ImageView>(R.id.iv_cover_image_profile)
        var iv_profile_image_profile = view.findViewById<ImageView>(R.id.iv_profile_image_profile)




        //for basic details
        var work_status = view.findViewById<TextView>(R.id.work_status)
        var college_status = view.findViewById<TextView>(R.id.college_status)
        var school_status = view.findViewById<TextView>(R.id.school_status)
        var relatino_status = view.findViewById<TextView>(R.id.relatino_status)




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

        //share icon
        val shareIcon = view.findViewById<ImageView>(R.id.iv_postShare)


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

    override fun onClick(v: View?) {

        if(v== v!!.findViewById<ImageView>(R.id.iv_cover_image_profile) ||v== v!!.findViewById<TextView>(R.id.txt_changeCover) )
        {

            //for self
            if (postUserId.equals(URL.userId))
                activity.showProfileBottomSheet("cover")
            //for friend
            else {

                activity.openPic(arrayListOf(URL.coverPicPath +profileData.basic_info.get(0).cover_photo))
            }


        }
        else if(v== v!!.findViewById<ImageView>(R.id.iv_profile_image_profile) ||v== v!!.findViewById<TextView>(R.id.txt_changeProfile) )
        {
            //for self
            if (postUserId.equals(URL.userId))
                activity.showProfileBottomSheet("profile")
            //for friend
            else {

                activity.openPic(arrayListOf(URL.profilePicPath +profileData.basic_info.get(0).profile_picture))
            }

        }


    }


}