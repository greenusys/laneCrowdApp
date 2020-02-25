package com.example.lanecrowd.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.lanecrowd.R
import com.example.lanecrowd.adapter.CommentAdapter
import com.example.lanecrowd.modal.CommentModel
import com.example.lanecrowd.modal.PowerMenuUtils
import com.example.lanecrowd.util.URL
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import de.hdodenhof.circleimageview.CircleImageView

class Show_Comment_Activity : AppCompatActivity() {

    var firstTime: Boolean = false
    private var hamburgerMenu: PowerMenu? = null
    private var vibe: Vibrator? = null

    var comment_rv: RecyclerView? = null
    var adapter: CommentAdapter? = null
    var files: ArrayList<String>? = null
    var post_id: String? = null
    var user_name: String? = null
    var isImage: String? = null
    var time: String? = null
    var staus: String? = null
    var user_pic: String? = null
    var url: String = ""
    var total_comment: String ? = null
    var total_likes: String ? = null
    var total_shared: String ? = null
    var comment_list = ArrayList<CommentModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show__comment_)


        files = intent.extras!!.getStringArrayList("files")
        post_id = intent.extras!!.getString("post_id")
        isImage = intent.extras!!.getString("isImage")
        user_name = intent.extras!!.getString("user_name")
        time = intent.extras!!.getString("time")
        staus = intent.extras!!.getString("staus")
        user_pic = intent.extras!!.getString("user_pic")
        total_likes = intent.extras!!.getString("total_likes")
        total_comment = intent.extras!!.getString("total_comment")
        total_shared = intent.extras!!.getString("total_shared")

        if (isImage.equals("true"))
            url = URL.imagePath
        else
            url = URL.videoPath

/*
        println("files_size"+files!!.size)
        println("post_id"+post_id)
        println("user_name"+user_name)
        println("time"+time)
        println("staus"+staus)*/


        initViews()


    }

    @SuppressLint("WrongConstant")
    private fun initViews() {


        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())

        //power
        hamburgerMenu = PowerMenuUtils.getCommentMenu(
            this,
            this,
            onHamburgerItemClickListener,
            onHamburgerMenuDismissedListener
        )

        vibe = this@Show_Comment_Activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator


        comment_rv = findViewById<RecyclerView>(R.id.comment_rv)
        adapter = CommentAdapter(this, comment_list)
        comment_rv!!.adapter = adapter
        comment_rv!!.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        adapter!!.notifyDataSetChanged()


        val user_profile_comment = findViewById<CircleImageView>(R.id.user_profile_comment)
        val user_name_comment = findViewById<TextView>(R.id.user_name_comment)
        val post_time_comment = findViewById<TextView>(R.id.post_time_comment)


        //set user image,name and post time
        Glide.with(applicationContext).load(
            URL.profilePicPath + user_pic
        ).apply(
            RequestOptions().placeholder(R.drawable.placeholder_profile)
        )
            .thumbnail(0.01f).into(user_profile_comment!!)

        user_name_comment.text = user_name
        post_time_comment.text = time


        //post pic,username,status,posttime of homepost item
        val postProfilePic = findViewById<CircleImageView>(R.id.postProfilePic)
        val postUserName = findViewById<TextView>(R.id.postUserName)
        val postTime = findViewById<TextView>(R.id.postTime)
        val postStatus = findViewById<TextView>(R.id.postStatus)


        postUserName.isAllCaps=true
        goneLayout(postProfilePic)
        goneLayout(postUserName)
        goneLayout(postTime)
        goneLayout(postStatus)


        //loading icons
        val loading_iconA = findViewById<ProgressBar>(R.id.loading_iconA)
        val loading_iconB = findViewById<ProgressBar>(R.id.loading_iconB)
        val loading_iconC = findViewById<ProgressBar>(R.id.loading_iconC)
        val loading_iconD = findViewById<ProgressBar>(R.id.loading_iconD)



        //no. of likes,comment and shares
        val likesText = findViewById<TextView>(R.id.likes)
        val commentText = findViewById<TextView>(R.id.comment)
        val shareText = findViewById<TextView>(R.id.share)

        likesText.setText(total_likes)
        commentText.setText(total_comment)
        shareText.setText(total_shared)


        //image layout
        val iv_postImgA = findViewById<ImageView>(R.id.iv_postImgA)
        val iv_postImgB = findViewById<ImageView>(R.id.iv_postImgB)
        val postImageC = findViewById<ImageView>(R.id.postImageC)
        val iv_postImgD = findViewById<ImageView>(R.id.iv_postImgD)


        //video icons
        val video_iconA = findViewById<ImageView>(R.id.iv_postIsVideoA)
        val video_iconB = findViewById<ImageView>(R.id.iv_postIsVideoB)
        val video_iconC = findViewById<ImageView>(R.id.iv_postIsVideoC)
        val video_iconD = findViewById<ImageView>(R.id.iv_postIsVideoD)

        val frame_postB = findViewById<FrameLayout>(R.id.frame_postB)
        val frame_postC = findViewById<FrameLayout>(R.id.frame_postC)



        //more text
        val more_text = findViewById<TextView>(R.id.more_text)

        val like_comment_layout = findViewById<LinearLayout>(R.id.like_comment_layout)
        goneLayout(like_comment_layout)


        //three image layout
        val threeImgLayout = findViewById<LinearLayout>(R.id.lay_postForBelowImgs)

        //menu button
        val post_menu = findViewById<ImageView>(R.id.post_menu)
        goneLayout(post_menu)




        if (files!!.size == 1) {


            set_Success_Glide_Data(
                url + files!!.get(0),
                loading_iconA,
                iv_postImgA,
                video_iconA
            )

            goneLayout(threeImgLayout)
        } else if (files!!.size == 2) {


            set_Success_Glide_Data(
                url + files!!.get(0),
                loading_iconA,
                iv_postImgA,
                video_iconA
            )

            set_Success_Glide_Data(
                url + files!!.get(1),
                loading_iconB,
                iv_postImgB,
                video_iconB
            )

            goneLayout(frame_postB)
            goneLayout(frame_postC)
        } else if (files!!.size == 3) {

            set_Success_Glide_Data(
                url + files!!.get(0),
                loading_iconA,
                iv_postImgA,
                video_iconA
            )

            set_Success_Glide_Data(
                url + files!!.get(1),
                loading_iconB,
                iv_postImgB,
                video_iconB
            )

            set_Success_Glide_Data(
                url + files!!.get(2),
                loading_iconC,
                postImageC,
                video_iconC
            )

            goneLayout(frame_postC)
        } else if (files!!.size == 4) {

            set_Success_Glide_Data(
                url + files!!.get(0),
                loading_iconA,
                iv_postImgA,
                video_iconA
            )

            set_Success_Glide_Data(
                url + files!!.get(1),
                loading_iconB,
                iv_postImgB,
                video_iconB
            )

            set_Success_Glide_Data(
                url + files!!.get(2),
                loading_iconC,
                postImageC,
                video_iconC
            )
            set_Success_Glide_Data(
                url + files!!.get(3),
                loading_iconD,
                iv_postImgD,
                video_iconD
            )

        } else if (files!!.size > 4) {

            set_Success_Glide_Data(
                url + files!!.get(0),
                loading_iconA,
                iv_postImgA,
                video_iconA
            )

            set_Success_Glide_Data(
                url + files!!.get(1),
                loading_iconB,
                iv_postImgB,
                video_iconB
            )

            set_Success_Glide_Data(
                url + files!!.get(2),
                loading_iconC,
                postImageC,
                video_iconC
            )
            set_Success_Glide_Data(
                url + files!!.get(3),
                loading_iconD,
                iv_postImgD,
                video_iconD
            )

            more_text.visibility = View.VISIBLE


        }


    }


    private fun set_Success_Glide_Data(
        url: String,
        loading_icon_gone: ProgressBar,
        post_img: ImageView,
        videoIcona: ImageView
    ) {


        println("sayed_url" + url)
        println("isImage" + isImage)




        Glide.with(applicationContext).load(url)
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

                    if (isImage.equals("true"))
                        videoIcona.visibility = View.GONE
                    else
                        videoIcona.visibility = View.VISIBLE

                    return false
                }
            })
            .apply(RequestOptions().placeholder(R.drawable.placeholder))

            .thumbnail(0.01f).into(post_img).waitForLayout()


    }


    private fun goneLayout(layout: View?) {

        layout!!.visibility = View.GONE
    }


    //power option menu
    private val onHamburgerItemClickListener =
        OnMenuItemClickListener<PowerMenuItem> { position, item ->
            // hamburgerMenu!!.selectedPosition = position

            if (!firstTime) {
/*
                    if (item.title.equals(getString(R.string.rate_me)))
                        rateMe()
                    else if (item.title.equals(getString(R.string.share)))
                        share()
                    else if (item.title.equals(getString(R.string.about)))
                        gotoABoutActivity()*/
            }


        }

    private val onHamburgerMenuDismissedListener = {
        Log.d("Test", "onDismissed hamburger menu")
    }


    fun showMenu(frndMenu: ImageView) {
        showVibration()

        if (hamburgerMenu!!.isShowing) {
            hamburgerMenu!!.dismiss()
            return
        }
        hamburgerMenu!!.showAsDropDown(frndMenu)


    }

    private fun showVibration() {

        vibe!!.vibrate(80)
    }


    fun back_activity(view: View) {
        onBackPressed()
    }

}
