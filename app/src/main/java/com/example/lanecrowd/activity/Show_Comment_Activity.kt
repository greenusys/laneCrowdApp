package com.example.lanecrowd.activity

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lanecrowd.R
import com.example.lanecrowd.adapter.CommentAdapter
import com.example.lanecrowd.modal.CommentMediaModal
import com.example.lanecrowd.modal.CommentModel
import com.example.lanecrowd.modal.PowerMenuUtils
import com.example.lanecrowd.util.URL
import com.example.lanecrowd.view_modal.CommentVM
import com.example.lanecrowd.view_modal.FetchPostVm
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type

class Show_Comment_Activity : AppCompatActivity() {

    var firstTime: Boolean = false
    private var hamburgerMenu: PowerMenu? = null
    private var vibe: Vibrator? = null


    var isScrolling = false
    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0

    var mediaData: CommentMediaModal? = null
    var comment_rv: RecyclerView? = null
    var adapter: CommentAdapter? = null
    var files: ArrayList<String>? = null
    var post_id: String? = null
    var user_name: String? = null
    var isMyLike: Boolean=false
    var isImage: String? = null
    var time: String? = null
    var staus: String? = null
    var user_pic: String? = null
    var url: String = ""
    var total_comment: String? = null
    var total_likes: String? = null
    var total_shared: String? = null
    var comment_list = ArrayList<CommentModel>()
    var counting: Int = 0

    internal var comment_id:String=""
    internal var postposition:Int = -1

    var layoutManager: LinearLayoutManager? = null
    lateinit var viewmodellike: FetchPostVm


    private var edt_comment: EditText? = null
    private var iv_sendComment: ImageView? = null
    private var swipe: SwipeRefreshLayout? = null
    var loading_more_anim: SpinKitView? = null

    lateinit var viewmodel: CommentVM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show__comment_)


        files = intent.extras!!.getStringArrayList("files")
        post_id = intent.extras!!.getString("post_id")
        isImage = intent.extras!!.getString("isImage")
        user_name = intent.extras!!.getString("user_name")
        time = intent.extras!!.getString("time")
       var mYLIke = intent.extras!!.getString("isMyLike")
        staus = intent.extras!!.getString("staus")
        user_pic = intent.extras!!.getString("user_pic")
        total_likes = intent.extras!!.getString("total_likes")
        total_comment = intent.extras!!.getString("total_comment")
        total_shared = intent.extras!!.getString("total_shared")

        if (isImage.equals("true"))
            url = URL.imagePath
        else
            url = URL.videoPath


        if(mYLIke.equals("true")) {
            isMyLike = true

        }

        initViesForVM()



    }

    private fun setProfileImage(url: String, layott: CircleImageView?) {
        Glide.with(applicationContext).load(url).apply(
            RequestOptions().placeholder(R.drawable.placeholder_profile)
        )
            .thumbnail(0.01f).into(layott!!)

    }

    @SuppressLint("WrongConstant")
    private fun initViesForVM() {

        //power
        hamburgerMenu = PowerMenuUtils.getCommentMenu(this, this, onHamburgerItemClickListener, onHamburgerMenuDismissedListener)
        vibe = this@Show_Comment_Activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        viewmodellike = ViewModelProvider(this).get(FetchPostVm::class.java)



        mediaData = CommentMediaModal(post_id!!,isMyLike,files!!, url, staus!!, total_likes!!, isImage!!, total_shared!!,total_comment!!)
        swipe = findViewById<SwipeRefreshLayout>(R.id.comment_swipe)
        loading_more_anim = findViewById<SpinKitView>(R.id.loading_more_anim)
        iv_sendComment = findViewById<ImageView>(R.id.iv_sendComment)
        edt_comment = findViewById<EditText>(R.id.edt_comment)

        val user_profile_comment = findViewById<CircleImageView>(R.id.user_profile_comment)
        val user_name_comment = findViewById<TextView>(R.id.user_name_comment)
        val post_time_comment = findViewById<TextView>(R.id.post_time_comment)


        setProfileImage(URL.profilePicPath+user_pic, user_profile_comment)
        user_name_comment.text = user_name
        post_time_comment.text = time
        user_name_comment.isAllCaps = true

        println("user_pic"+URL.profilePicPath+user_pic)


        comment_rv = findViewById<RecyclerView>(R.id.comment_rv)
        layoutManager = LinearLayoutManager(applicationContext)
        comment_rv!!.layoutManager = layoutManager
        adapter = CommentAdapter(this, comment_list, mediaData!!)
        comment_rv!!.adapter = adapter


        viewmodel = ViewModelProvider(this).get(CommentVM::class.java)


        swiperefresh_Listener()
        initScrollListener()


        if (!isNetworkAvailable(applicationContext!!)) {
            // visible_no_internet_layout(true)
        } else {
            setRefreshingfalse(true)

            fetchPostCommentAPI("")
        }


        iv_sendComment!!.setOnClickListener(View.OnClickListener {

            if(edt_comment!!.text.toString().length>0) {
                edt_comment!!.text.clear()
                hideSoftKeyBoard()
                callAddCommentAPI(post_id!!, edt_comment!!.text.toString(), URL.userId)


            }})

    }



    private fun hideSoftKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        try {


            if (imm.isAcceptingText) {
                // verify if the soft keyboard is open
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            }
        } catch (e: Exception) {

        }
    }

    private fun callAddCommentAPI(post_id: String, comment: String, comment_by: String) {

        try {


            viewmodel.addCommentVM(post_id,comment,comment_by)
                .observe(this, Observer { resultPi ->




                })


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun initScrollListener() {

        comment_rv!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {


            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
                //  println("isScrollli"+isScrolling)

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                currentItems = layoutManager!!.childCount
                totalItems = layoutManager!!.itemCount
                scrollOutItems = layoutManager!!.findFirstVisibleItemPosition()


                println("totalItems" + totalItems)
                println("scrollOutItems" + scrollOutItems)
                println("currentItems" + currentItems)
                println("isScrolling" + isScrolling)



                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    visibleLoadingMoreAnim(true)

                    println("load_more_called")
                    isScrolling = false
                    counting++
                    if (!isNetworkAvailable(applicationContext!!)) {
                        //visible_no_internet_layout(true)
                    } else
                        fetchPostCommentAPI("more")


                }

            }
        })


    }


    private fun swiperefresh_Listener() {

        swipe!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            try {

                counting = 0
                //if (!isNetworkAvailable(applicationContext!!)) {
                ////  setRefreshingfalse(false)
                // showSnackBar("Please check your internet connection")
                //  if (comment_list.size <= 0 && no_data_anim_post!!.visibility != View.VISIBLE)
                //   visible_no_internet_layout(true)
                //  } else {
                fetchPostCommentAPI("swipe")
                //  }
            } catch (e: IndexOutOfBoundsException) {
                println("on_Refresh_Exception_Found_$e")
            }
        })

    }

    /*  private fun showSnackBar(msg: String) {
          val snackbar = Snackbar.make(main_layout!!, msg, Snackbar.LENGTH_SHORT)
          snackbar.show()
      }*/

    private fun visibleLoadingMoreAnim(value: Boolean) {
        if (value)
            loading_more_anim!!.visibility = View.VISIBLE
        else
            loading_more_anim!!.visibility = View.GONE

    }


    private fun setRefreshingfalse(value: Boolean) {

        swipe!!.isRefreshing = value

    }


    fun isNetworkAvailable(context: Context)//check internet of device
            : Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }


    private fun fetchPostCommentAPI(from: String) {


        try {
            viewmodel.fetchCommentVM(post_id!!, counting.toString())
                .observe(this, Observer { resultPi ->


                    storepostDataTOModal(resultPi, from)


                })
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


    private fun updateAdapterForMultipleData() {


        if (swipe!!.isRefreshing) {
            println("Refreshing;......")
            if (this@Show_Comment_Activity != null) {
                this@Show_Comment_Activity.runOnUiThread {
                    if (adapter != null) {
                        comment_rv!!.recycledViewPool.clear()
                        adapter!!.notifyDataSetChanged()
                        setRefreshingfalse(false)
                    }
                    comment_list.clear()

                }
            }
        }


    }


    private fun ClearForRefreshData() {

        println("ClearForRefreshData_called")
        comment_list.clear()
        comment_rv!!.recycledViewPool.clear()
        notifiyAdapter()


    }

    private fun storepostDataTOModal(resultPi: JsonObject?, from: String) {




        if (from.equals("swipe"))
            ClearForRefreshData()

        //call after load more
        updateAdapterForMultipleData()


        if(resultPi!=null) {
            var main = JSONObject(resultPi.toString())

            if (resultPi != null && main.getString("code").equals("1")) {

                val data: JSONArray = main.getJSONArray("data")

                println("comement_list_before" + comment_list.size)


                val gson = Gson()
                val listType: Type = object : TypeToken<ArrayList<CommentModel?>?>() {}.type
                comment_list.addAll( gson.fromJson(data.toString(), listType))

                println("Comment_list_xize" + comment_list.size)

            }
        }else {

            println("no_data_found_for_offset" + counting.toString())


        }


        if (from.equals("more"))
            visibleLoadingMoreAnim(false)


        notifiyAdapter()


    }

    private fun notifiyAdapter() {
        println("comement_list" + comment_list.size)
        println("check_value"+layoutManager!!.itemCount)

        // adapter = CommentAdapter(this, comment_list, mediaData!!)
        //comment_rv!!.adapter = adapter
        adapter!!.notifyDataSetChanged()
    }





    //power option menu
    private val onHamburgerItemClickListener =
        OnMenuItemClickListener<PowerMenuItem> { position, item ->
            // hamburgerMenu!!.selectedPosition = position

            if (!firstTime) {

                    if (item.title.equals(getString(R.string.delete)))
                       askToDeleteSelfPost()

                         else  if (item.title.equals(getString(R.string.edit)))
                            callEditCommentAPI()

            }


        }

    private fun callEditCommentAPI() {

        viewmodel.editCommentVM(URL.userId,edt_comment!!.text.toString(),comment_id)

    }


    private fun askToDeleteSelfPost() {


        MaterialAlertDialogBuilder(this@Show_Comment_Activity, R.style.AlertDialogTheme)
            .setTitle("LaneCrowd")
            .setMessage("Are you sure want to delete comment?")
            .setNegativeButton("No") { dialogInterface, i ->
            }
            .setPositiveButton("Yes") { dialogInterface, i ->
                println("show_psoitio_2"+postposition)


                viewmodel.deleteCommentVM(comment_id)
                adapter!!.notifyItemRemoved(postposition)

            }
            .show()
    }

    private val onHamburgerMenuDismissedListener = {
        Log.d("Test", "onDismissed hamburger menu")
    }


    fun showMenu(frndMenu: ImageView,comment_id:String,position:Int) {
        showVibration()

        this.comment_id=comment_id
        this.postposition=position

        if (hamburgerMenu!!.isShowing) {
            hamburgerMenu!!.dismiss()
            return
        }
        hamburgerMenu!!.showAsDropDown(frndMenu)


    }

     fun showVibration() {

        vibe!!.vibrate(80)
    }

    fun likeDislike(postId: String, userId: String) {


        viewmodellike.likeDislikeAPI(postId,userId)

    }


    fun back_activity(view: View) {
        onBackPressed()
    }

}
