package com.example.lanecrowd.adapter


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.lanecrowd.R
import com.example.lanecrowd.activity.Profile_Activity
import com.example.lanecrowd.activity.ShowPhotoActivity
import com.example.lanecrowd.activity.Show_Comment_Activity
import com.example.lanecrowd.modal.CommentMediaModal
import com.example.lanecrowd.modal.CommentModel
import com.example.lanecrowd.retrofit.TimeShow
import com.example.lanecrowd.util.URL
import com.like.LikeButton
import com.like.OnLikeListener
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class CommentAdapter(
    var context: Show_Comment_Activity,
    var list: ArrayList<CommentModel>,
    var mediaData: CommentMediaModal
) : RecyclerView.Adapter<CommentAdapter.MyViewHolder>() {

    private val TYPE_ITEM_MEDIA = 0
    private val TYPE_ITEM_NORMAL = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        if (viewType == TYPE_ITEM_MEDIA) {
            val view = LayoutInflater.from(context).inflate(R.layout.home_post_item, parent, false)
            return MyViewHolder(view)
        } else if (viewType == TYPE_ITEM_NORMAL) {
            val view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false)
            return MyViewHolder(view)
        }

        val view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0)
            return TYPE_ITEM_MEDIA

        return TYPE_ITEM_NORMAL
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (position == 0) {


            //set  comment status listener
            copyComment(position, holder.postStatus)


            holder.main_cardView.radius = 0f



            holder.likeIcon.isLiked = mediaData.isMyLike


            //set Like Button Listener
            setLikeListener(holder, position)


            //set Share Post Listener
            setSharePostListener(holder, position)




            goneLayout(holder.postProfilePic)
            goneLayout(holder.postUserName)
            goneLayout(holder.postTime)


            if (!mediaData.staus.equals(""))
                holder.postStatus.text = mediaData.staus
            else
                goneLayout(holder.postStatus)


            if (!mediaData.total_likes.equals("0"))
                holder.likesText.text = mediaData.total_likes
            else
                holder.likesText.text = ""

            if (!mediaData.total_shared.equals("0"))
                holder.shareText.text = mediaData.total_shared
            else
                holder.shareText.text = ""

            if (!mediaData.totalComment.equals("0"))
                holder.commentText.text = mediaData.totalComment
            else
                holder.commentText.text = ""


            setPhotoVideoViewListener(position, holder)

            goneLayout(holder.post_menu)


            println("medi_file_size" + mediaData.files.get(0))

            if (!mediaData.files.get(0).equals("empty")) {

                if (mediaData.files.size == 1) {


                    set_Success_Glide_Data(
                        mediaData.urlPath + mediaData.files.get(0),
                        holder.loading_iconA,
                        holder.iv_postImgA,
                        holder.video_iconA
                    )

                    goneLayout(holder.threeImgLayout)
                } else if (mediaData.files.size == 2) {


                    set_Success_Glide_Data(
                        mediaData.urlPath + mediaData.files.get(0),
                        holder.loading_iconA,
                        holder.iv_postImgA,
                        holder.video_iconA
                    )

                    set_Success_Glide_Data(
                        mediaData.urlPath + mediaData.files.get(1),
                        holder.loading_iconB,
                        holder.iv_postImgB,
                        holder.video_iconB
                    )

                    goneLayout(holder.frame_postB)
                    goneLayout(holder.frame_postC)
                } else if (mediaData.files.size == 3) {

                    set_Success_Glide_Data(
                        mediaData.urlPath + mediaData.files.get(0),
                        holder.loading_iconA,
                        holder.iv_postImgA,
                        holder.video_iconA
                    )

                    set_Success_Glide_Data(
                        mediaData.urlPath + mediaData.files.get(1),
                        holder.loading_iconB,
                        holder.iv_postImgB,
                        holder.video_iconB
                    )

                    set_Success_Glide_Data(
                        mediaData.urlPath + mediaData.files.get(2),
                        holder.loading_iconC,
                        holder.postImageC,
                        holder.video_iconC
                    )

                    goneLayout(holder.frame_postC)
                } else if (mediaData.files.size == 4) {

                    holder.more_text.visibility = View.GONE

                    set_Success_Glide_Data(
                        mediaData.urlPath + mediaData.files.get(0),
                        holder.loading_iconA,
                        holder.iv_postImgA,
                        holder.video_iconA
                    )

                    set_Success_Glide_Data(
                        mediaData.urlPath + mediaData.files.get(1),
                        holder.loading_iconB,
                        holder.iv_postImgB,
                        holder.video_iconB
                    )

                    set_Success_Glide_Data(
                        mediaData.urlPath + mediaData.files.get(2),
                        holder.loading_iconC,
                        holder.postImageC,
                        holder.video_iconC
                    )
                    set_Success_Glide_Data(
                        mediaData.urlPath + mediaData.files.get(3),
                        holder.loading_iconD,
                        holder.iv_postImgD,
                        holder.video_iconD
                    )

                } else if (mediaData.files.size > 4) {

                    set_Success_Glide_Data(
                        mediaData.urlPath + mediaData.files.get(0),
                        holder.loading_iconA,
                        holder.iv_postImgA,
                        holder.video_iconA
                    )

                    set_Success_Glide_Data(
                        mediaData.urlPath + mediaData.files.get(1),
                        holder.loading_iconB,
                        holder.iv_postImgB,
                        holder.video_iconB
                    )

                    set_Success_Glide_Data(
                        mediaData.urlPath + mediaData.files.get(2),
                        holder.loading_iconC,
                        holder.postImageC,
                        holder.video_iconC
                    )
                    set_Success_Glide_Data(
                        mediaData.urlPath + mediaData.files.get(3),
                        holder.loading_iconD,
                        holder.iv_postImgD,
                        holder.video_iconD
                    )

                    holder.more_text.visibility = View.VISIBLE


                }

            } else {
                holder.media_layout.visibility = View.GONE

            }


        } else {


            holder.comment_menu.visibility =
                if (list.get(position - 1).commented_by_.equals(URL.userId)) View.VISIBLE else View.GONE

            holder.comment_menu.setOnClickListener(View.OnClickListener {


                println("kaifPosti" + position)


                //self post
                if (list.get(position - 1).commented_by_.equals(URL.userId))
                    context.showMenu(holder.comment_menu, list.get(position - 1).id, position)

            })


            //goto User Profile
            gotoUserProfile(holder, position - 1)

            setProfileImage(
                URL.profilePicPath + list.get(position - 1).profile_picture,
                holder.comment_profile_pic
            )


            holder.txt_comment.text = list.get(position - 1).comment

            var date = TimeShow.getTime(list.get(position - 1).commented_on)

            holder.txt_time.text = date
            holder.txt_name.text = list.get(position - 1).full_name


            //set copy comment listener
            copyComment(position - 1, holder.txt_comment)

        }


    }

    private fun copyComment(
        position: Int,
        view: View
    ) {


        view.setOnLongClickListener {

            var copiedText = ""
            if (position == 0)
                copiedText = mediaData.staus
            else
                copiedText = list.get(position).comment


            var clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            var clip = ClipData.newPlainText("label", copiedText)
            clipboard.setPrimaryClip(clip)

            context.showVibration()
            DynamicToast.make(context, "Text copied").show()

            return@setOnLongClickListener true
        }

    }


    private fun gotoUserProfile(
        holder: MyViewHolder,
        position: Int
    ) {

        holder.comment_profile_pic.setOnClickListener(View.OnClickListener {

            context.startActivity(
                Intent(context, Profile_Activity::class.java)
                    .putExtra("postUserId", list.get(position).user_id)
                    .putExtra("postUserName", list.get(position).full_name)


            )


        })

    }


    private fun set_Success_Glide_Data(
        url: String,
        loading_icon_gone: ProgressBar,
        post_img: ImageView,
        videoIcona: ImageView
    ) {


        Glide.with(context).load(url)
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

                    if (mediaData.isImage.equals("true"))
                        videoIcona.visibility = View.GONE
                    else
                        videoIcona.visibility = View.VISIBLE

                    return false
                }
            })
            .apply(RequestOptions().placeholder(R.drawable.placeholder))

            .thumbnail(0.01f).into(post_img).waitForLayout()


    }


    private fun checkIsImage(position: Int): Boolean {

        if (mediaData.files.get(0).contains("jpg") || mediaData.files.get(0).contains("png") || mediaData.files.get(
                0
            ).contains("jpeg")
        )
            return true

        return false

    }

    private fun setPhotoVideoViewListener(position: Int, holder: MyViewHolder) {


        holder.iv_postImgA.setOnClickListener(View.OnClickListener {
            println("checkImageee" + checkIsImage(position))
            context.startActivity(
                Intent(context, ShowPhotoActivity::class.java)
                    .putExtra("isImage", checkIsImage(position).toString())
                    .putExtra("position", "0")
                    .putExtra("name", mediaData.postUserName)
                    .putStringArrayListExtra("files", mediaData.files)
            )

        })

        holder.iv_postImgB.setOnClickListener(View.OnClickListener {
            println("checkImageee" + checkIsImage(position))
            context.startActivity(
                Intent(context, ShowPhotoActivity::class.java)
                    .putExtra("isImage", checkIsImage(position).toString())
                    .putExtra("position", "1")
                    .putExtra("name", mediaData.postUserName)
                    .putStringArrayListExtra("files", mediaData.files)
            )

        })
        holder.postImageC.setOnClickListener(View.OnClickListener {
            println("checkImageee" + checkIsImage(position))
            context.startActivity(
                Intent(context, ShowPhotoActivity::class.java)
                    .putExtra("isImage", checkIsImage(position).toString())
                    .putExtra("position", "2")
                    .putExtra("name", mediaData.postUserName)
                    .putStringArrayListExtra("files", mediaData.files)
            )

        })

        holder.iv_postImgD.setOnClickListener(View.OnClickListener {
            println("checkImageee" + checkIsImage(position))
            context.startActivity(
                Intent(context, ShowPhotoActivity::class.java)
                    .putExtra("isImage", checkIsImage(position).toString())
                    .putExtra("position", "3")
                    .putExtra("name", mediaData.postUserName)
                    .putStringArrayListExtra("files", mediaData.files)
            )

        })


    }

    private fun setSharePostListener(holder: MyViewHolder, position: Int) {


        holder.shareIcon.setOnClickListener(View.OnClickListener {


            mediaData.total_shared = (Integer.parseInt(mediaData.total_shared) + 1).toString() + ""

            notifyItemChanged(position, position)

            context.sharePost(mediaData.total_shared, mediaData.postId, URL.userId)


        })
    }


    private fun setLikeListener(holder: MyViewHolder, position: Int) {

        // var position:Int=position-2

        holder.likeIcon.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton) {

                println("bbbbb_liked" + position)
                likePost(holder, position)

            }

            override fun unLiked(likeButton: LikeButton) {

                println("bbbbb_disliked" + position)

                likePost(holder, position)


            }
        })


    }


    private fun likePost(holder: MyViewHolder, position: Int) {


        context.showVibration()

        if (mediaData.isMyLike) {
            mediaData.total_likes = (Integer.parseInt(mediaData.total_likes) - 1).toString() + ""
        } else {
            mediaData.total_likes = (Integer.parseInt(mediaData.total_likes) + 1).toString() + ""

        }


        //update their value
        mediaData.isMyLike = !mediaData.isMyLike


        notifyItemChanged(position, position)

        context.likeDislike(
            mediaData.isMyLike,
            mediaData.total_likes,
            mediaData.totalComment,
            mediaData.postId,
            URL.userId
        )

    }


    private fun setProfileImage(url: Any, layott: CircleImageView?) {
//set user image,name and post time
        Glide.with(context).load(
            url
        ).apply(
            RequestOptions().placeholder(R.drawable.placeholder_profile)
        )
            .thumbnail(0.01f).into(layott!!)

    }


    private fun goneLayout(layout: View?) {

        layout!!.visibility = View.GONE
    }


    override fun getItemCount(): Int {

        return list.size + 1
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val comment_menu = view.findViewById<ImageView>(R.id.comment_menu)
        val comment_profile_pic = view.findViewById<CircleImageView>(R.id.comment_profile_pic)
        val txt_comment = view.findViewById<TextView>(R.id.txt_comment)
        val txt_time = view.findViewById<TextView>(R.id.txt_time)
        val txt_name = view.findViewById<TextView>(R.id.txt_name)


        val likeIcon = view.findViewById<LikeButton>(R.id.iv_postLike)


        //for media layout

        val main_cardView = view.findViewById<CardView>(R.id.main_cardView)

        //media layout
        val media_layout = view.findViewById<LinearLayout>(R.id.lay_postMedia)


        //post pic,username,status,posttime of homepost item
        val postProfilePic = view.findViewById<CircleImageView>(R.id.postProfilePic)
        val postUserName = view.findViewById<TextView>(R.id.postUserName)
        val postTime = view.findViewById<TextView>(R.id.postTime)
        val postStatus = view.findViewById<TextView>(R.id.postStatus)


        //loading icons
        val loading_iconA = view.findViewById<ProgressBar>(R.id.loading_iconA)
        val loading_iconB = view.findViewById<ProgressBar>(R.id.loading_iconB)
        val loading_iconC = view.findViewById<ProgressBar>(R.id.loading_iconC)
        val loading_iconD = view.findViewById<ProgressBar>(R.id.loading_iconD)


        //share icon
        val shareIcon = view.findViewById<ImageView>(R.id.iv_postShare)


        //no. of likes,comment and shares
        val likesText = view.findViewById<TextView>(R.id.likes)
        val commentText = view.findViewById<TextView>(R.id.comment)
        val shareText = view.findViewById<TextView>(R.id.share)


        //image layout
        val iv_postImgA = view.findViewById<ImageView>(R.id.iv_postImgA)
        val iv_postImgB = view.findViewById<ImageView>(R.id.iv_postImgB)
        val postImageC = view.findViewById<ImageView>(R.id.postImageC)
        val iv_postImgD = view.findViewById<ImageView>(R.id.iv_postImgD)


        //video icons
        val video_iconA = view.findViewById<ImageView>(R.id.iv_postIsVideoA)
        val video_iconB = view.findViewById<ImageView>(R.id.iv_postIsVideoB)
        val video_iconC = view.findViewById<ImageView>(R.id.iv_postIsVideoC)
        val video_iconD = view.findViewById<ImageView>(R.id.iv_postIsVideoD)

        val frame_postB = view.findViewById<FrameLayout>(R.id.frame_postB)
        val frame_postC = view.findViewById<FrameLayout>(R.id.frame_postC)


        //more text
        val more_text = view.findViewById<TextView>(R.id.more_text)

        val like_comment_layout = view.findViewById<LinearLayout>(R.id.like_comment_layout)


        //three image layout
        val threeImgLayout = view.findViewById<LinearLayout>(R.id.lay_postForBelowImgs)

        //menu button
        val post_menu = view.findViewById<ImageView>(R.id.post_menu)


    }


}