package com.example.lanecrowd.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lanecrowd.R
import com.example.lanecrowd.Session_Package.SessionManager
import com.example.lanecrowd.adapter.CommentAdapter
import com.example.lanecrowd.modal.CommentMediaModal
import com.example.lanecrowd.modal.CommentModel
import com.example.lanecrowd.modal.PowerMenuUtils
import com.example.lanecrowd.retrofit.TimeShow
import com.example.lanecrowd.util.URL
import com.example.lanecrowd.view_modal.CommentVM
import com.example.lanecrowd.view_modal.FetchPostVm
import com.example.lanecrowd.view_modal.MySessionVM
import com.example.lanecrowd.view_modal.factory.ViewModelProvider_Session
import com.example.lanecrowd.view_modal.factory.ViewModelFactoryC
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.jakewharton.rxbinding2.view.RxView
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import org.json.JSONArray
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Show_Comment_Activity : AppCompatActivity(), KodeinAware {

    var firstTime: Boolean = false
    private var hamburgerMenu: PowerMenu? = null
    private var vibe: Vibrator? = null



    var edit = false
    var isScrolling = false
    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0

    var mediaData: CommentMediaModal? = null
    var comment_rv: RecyclerView? = null
    var adapter: CommentAdapter? = null
    var files: ArrayList<String>? = null
    var post_id: String? = null
    var user_id: String? = null
    var user_name: String? = null
    var isImage: String? = null
    var time: String? = null
    var staus: String? = null
    var user_pic: String? = null
    var url: String = ""
    var isMylikepost: Boolean = false
    var total_comment: String? = null
    var total_likes: String? = null
    var total_shared: String? = null
    var comment_list = ArrayList<CommentModel>()
    var counting: Int = 0
    var post_position: String?=null

    internal var comment_id: String = ""
    internal var postposition: Int = -1
    var layoutManager: LinearLayoutManager? = null



    private var userLayout: LinearLayout? = null
    private var no_comment: TextView? = null
    private var edt_comment: EditText? = null
    private var iv_sendComment: ImageView? = null
    private var user_profile_comment: ImageView? = null
    private var swipe: SwipeRefreshLayout? = null
    var loading_more_anim: SpinKitView? = null


    override val kodein by kodein()

    //get factory depencey from outside using kodein framework
    private val factory: ViewModelFactoryC by instance()

    lateinit var viewmodellike: FetchPostVm

    lateinit var viewmodel: CommentVM


    private val session: SessionManager by  instance()

    //this factory method will create and return one object of SessionVM
    var videomodelfactory: ViewModelProvider_Session? = null
    var mySessionVM: MySessionVM? = null




    var user_image: CircleImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show__comment_)


        files = intent.extras!!.getStringArrayList("files")
        post_id = intent.extras!!.getString("post_id")
        user_id = intent.extras!!.getString("user_id")
        post_position = intent.extras!!.getString("post_position")
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


        if (mYLIke.equals("true")) {
            isMylikepost=true

        }

        initViesForVM()


    }

    private fun setProfileImage(url: String, layott: ImageView?) {
        Glide.with(applicationContext).load(url).apply(
            RequestOptions().placeholder(R.drawable.placeholder_profile)
        )
            .thumbnail(0.01f).into(layott!!)

    }

    @SuppressLint("WrongConstant")
    private fun initViesForVM() {

        //this factory method will create and return one object
        videomodelfactory =
            ViewModelProvider_Session(
                MySessionVM.instance
            )
        mySessionVM = ViewModelProvider(this, videomodelfactory!!).get(MySessionVM::class.java)



        //power
        hamburgerMenu = PowerMenuUtils.getCommentMenu(
            this,
            this,
            onHamburgerItemClickListener,
            onHamburgerMenuDismissedListener
        )
        vibe = this@Show_Comment_Activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        viewmodellike = ViewModelProviders.of(this,factory).get(FetchPostVm::class.java)

        viewmodel = ViewModelProviders.of(this,factory).get(CommentVM::class.java)



        user_image = findViewById<CircleImageView>(R.id.user_image)


        mediaData = CommentMediaModal(
            post_id!!,
            user_name!!,
            isMylikepost,
            files!!,
            url,
            staus!!,
            total_likes!!,
            isImage!!,
            total_shared!!,
            total_comment!!
        )
        userLayout = findViewById<LinearLayout>(R.id.userLayout)
        no_comment = findViewById<TextView>(R.id.no_comment)
        swipe = findViewById<SwipeRefreshLayout>(R.id.comment_swipe)
        loading_more_anim = findViewById<SpinKitView>(R.id.loading_more_anim)
        iv_sendComment = findViewById<ImageView>(R.id.iv_sendComment)
        edt_comment = findViewById<EditText>(R.id.edt_comment)

         user_profile_comment = findViewById<ImageView>(R.id.user_profile_comment)
        val user_name_comment = findViewById<TextView>(R.id.user_name_comment)
        val post_time_comment = findViewById<TextView>(R.id.post_time_comment)



        setUserDataWithImages()


        user_name_comment.text = user_name
        post_time_comment.text = time

        println("user_pic" + URL.profilePicPath + user_pic)


        comment_rv = findViewById<RecyclerView>(R.id.comment_rv)
        layoutManager = LinearLayoutManager(applicationContext)
        comment_rv!!.layoutManager = layoutManager
        adapter = CommentAdapter(this, comment_list, mediaData!!)
        comment_rv!!.adapter = adapter




        swiperefresh_Listener()
        initScrollListener()


        if (!isNetworkAvailable(applicationContext!!)) {
             visible_no_internet_layout(true)
        } else {
            setRefreshingfalse(true)

            fetchPostCommentAPI("firsttime")
        }




        val observable1 = RxView.clicks(iv_sendComment!!).map<Any> { o: Any -> iv_sendComment }
        val observable2 = RxView.clicks(userLayout!!).map<Any> { o: Any -> userLayout }


        //set Send and Goto User Profile Listener
        setListener(observable1, observable2)


    }


    private fun setListener(observable1: Observable<Any>, observable2: Observable<Any>) {

        //set click listener
        val disposable = Observable.mergeArray(observable1, observable2)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { o ->

                if(o==iv_sendComment)
                    CallAPI()
                else if(o==userLayout)
                    gotoUserProfile()


            }


    }

    private fun gotoUserProfile() {
        startActivity(
            Intent(applicationContext, Profile_Activity::class.java)
                .putExtra("postUserId", user_id)
                .putExtra("postUserName", user_name))




    }

    private fun visible_no_internet_layout(value: Boolean) {

        if(value)
        {
            visibleNoComment(true)
            no_comment!!.setText("Please Check Your Internet Connection")

        }
        else
        {

            visibleNoComment(false)
        }

    }


    private fun setUserDataWithImages() {


        val user: HashMap<String, String?> = session!!.userDetails
        setUseDataTOVIew(user)




        mySessionVM!!.getName()!!.observe(this, Observer { resultPi ->


            setUseDataTOVIew(resultPi)


        })




    }


    private fun setUseDataTOVIew(result: HashMap<String, String?>) {



        //user's post image
         setProfileImage(URL.profilePicPath + user_pic, user_profile_comment!!)

        //self images
        setProfileImage(URL.profilePicPath + result[SessionManager.KEY_PROFILE_PICTURE], user_image)

    }


    private fun CallAPI() {

        hideSoftKeyBoard()

        //for insert
        if (edit==false &&  edt_comment!!.text.toString().length > 0) {

            callAddCommentAPI(post_id!!, edt_comment!!.text.toString(), URL.userId)

        }
        //for edit comment
        else if (edit &&  edt_comment!!.text.toString().length > 0)
        {


            edit=false
            viewmodel.editCommentVM(URL.userId, edt_comment!!.text.toString(), comment_id)

            comment_list.get(postposition-1).comment=edt_comment!!.text.toString()
            comment_list.get(postposition-1).commented_on=TimeShow.getTime(getCurrentTime())

            adapter!!.notifyItemChanged(postposition,postposition)

            clearEditText()
        }



    }

    private fun clearEditText() {
        edt_comment!!.text.clear()


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


            viewmodel.addCommentVM(post_id, comment, comment_by)
                .observe(this, Observer { resultPi ->

                    updateList(resultPi)

                })


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun updateList(resultPi: JsonObject?) {

        visibleNoComment(false)
        var main: JSONObject? = null


        if (resultPi != null) {

            try {

                main = JSONObject(resultPi.toString())


                println("newskd"+edt_comment!!.text.toString())

                if (resultPi != null && main!!.getString("code").equals("1")) {

                    var comment_id=main.getString("id")

                    comment_list.add(CommentModel(
                        comment_id,
                        post_id!!,
                        URL.userId,
                        edt_comment!!.text.toString(),
                        getCurrentTime(),
                        URL.userId,
                        user_name!!,
                        user_pic!!))

                    adapter!!.notifyItemInserted(comment_list.size)
                    comment_rv!!.scrollToPosition(comment_list.size)

                    clearEditText()

                    println("sizeofList"+comment_list.size)



                }


            } catch (e: Exception) {

                clearEditText()
            }

        }
        else
        {
            clearEditText()
        }


        setTotalComment()


    }

    private fun setTotalComment() {

        mediaData!!.totalComment=comment_list.size.toString()
        total_comment=comment_list.size.toString()
        adapter!!.notifyItemChanged(0,0)
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
                if (!isNetworkAvailable(applicationContext!!)) {
                 setRefreshingfalse(false)
                showSnackBar("Please check your internet connection")
                  if (comment_list.size <= 0 && no_comment!!.visibility != View.VISIBLE)
                   visible_no_internet_layout(true)
                  }
                else {
                    setRefreshingfalse(true)
                    visible_no_internet_layout(false)
                fetchPostCommentAPI("swipe")
                  }
            } catch (e: IndexOutOfBoundsException) {
                println("on_Refresh_Exception_Found_$e")
            }
        })

    }

      private fun showSnackBar(msg: String) {
          val snackbar = Snackbar.make(swipe!!, msg, Snackbar.LENGTH_SHORT)
          snackbar.setBackgroundTint(ContextCompat.getColor(this, (R.color.red)))
          snackbar.setTextColor(ContextCompat.getColor(this, (R.color.white)))
          snackbar.show()
      }

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


        try {



        if (resultPi != null && resultPi.has("code")) {
            var main = JSONObject(resultPi.toString())

            if (resultPi != null && main.getString("code").equals("1")) {

                val data: JSONArray = main.getJSONArray("data")

                val gson = Gson()
                val listType: Type = object : TypeToken<ArrayList<CommentModel?>?>() {}.type
                comment_list.addAll(gson.fromJson(data.toString(), listType))

            }
        } else {

            println("no_data_found_for_offset" + counting.toString())


        }
        }catch (e:Exception)
        {


            e.printStackTrace()
        }

        no_comment!!.setText("No Comment Found")

        if(comment_list.size<=0) {
            no_comment!!.setText("No Comment Found")
            visibleNoComment(true)
        }
        else
            visibleNoComment(false)


        visibleLoadingMoreAnim(false)
        notifiyAdapter()


    }

    private fun visibleNoComment(value: Boolean) {

        println("visibleNoComment"+value)
        if(value)
            no_comment!!.visibility=View.VISIBLE
        else
            no_comment!!.visibility=View.GONE
    }

    private fun notifiyAdapter() {
        println("comement_list" + comment_list.size)
        println("check_value" + layoutManager!!.itemCount)

        // adapter = CommentAdapter(this, comment_list, mediaData!!)
        //comment_rv!!.adapter = adapter
        adapter!!.notifyDataSetChanged()
    }



    fun showMenu(frndMenu: ImageView, comment_id: String, position: Int) {
        showVibration()

        println("menu_comment_id"+comment_id)
        println("menu_position"+position)

        this.comment_id = comment_id
        this.postposition = position


        if (hamburgerMenu!!.isShowing) {
            hamburgerMenu!!.dismiss()
            return
        }
        hamburgerMenu!!.showAsDropDown(frndMenu)

    }


    //power option menu
    private val onHamburgerItemClickListener =
        OnMenuItemClickListener<PowerMenuItem> { position, item ->
            // hamburgerMenu!!.selectedPosition = position



            if (firstTime) {


                println("sallu"+item.title)

                if (item.title.equals(getString(R.string.delete)))
                    askToDeleteSelfPost()

                    else if (item.title.equals(getString(R.string.edit)))
                {
                    edit=true
                    edt_comment!!.setText(comment_list.get(postposition-1).comment)

                }

            }
            firstTime=true


        }




    private fun askToDeleteSelfPost() {


        MaterialAlertDialogBuilder(this@Show_Comment_Activity, R.style.AlertDialogTheme)
            .setTitle("LaneCrowd")
            .setMessage("Are you sure want to delete comment?")
            .setNegativeButton("No") { dialogInterface, i ->
            }
            .setPositiveButton("Yes") { dialogInterface, i ->
                println("show_psoitio_2" + postposition)



                visibleLoadingMoreAnim(false)
                comment_list.removeAt(postposition-1)
                mediaData!!.totalComment=comment_list.size.toString()
                total_comment=comment_list.size.toString()
                adapter!!.notifyItemRemoved(postposition)
                adapter!!.notifyItemChanged(0,0)

                viewmodel.deleteCommentVM(comment_id)



                println("list_size"+comment_list.size)





                if(comment_list.size<=0)
                    visibleNoComment(true)

            }
            .show()
    }

    private val onHamburgerMenuDismissedListener = {
        Log.d("Test", "onDismissed hamburger menu")
    }




    fun showVibration() {

        vibe!!.vibrate(80)
    }

    fun likeDislike(isMylike:Boolean,totalLike:String,totalComment:String,postId: String, userId: String) {

        this.total_likes=totalLike
        this.total_comment=totalComment
        this.isMylikepost=isMylike

        println("likkk"+totalLike)
        println("totalComment"+totalComment)
        println("isMylikepost"+isMylikepost)

        viewmodellike.likeDislikeAPI(postId, userId)


    }

    fun sharePost(totalShare:String,postId: String, userId: String) {
        showVibration()

        this.total_shared=totalShare

        DynamicToast.make(applicationContext!!, "You have shared this post",2).show()


        viewmodellike.sharePost(postId, userId)

    }


    private fun getCurrentTime():String {

       var formatter =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        var date =  Date();


        println("current_time"+formatter.format(date))

        return  formatter.format(date)
    }


    fun back_activity(view: View) {
        setDataTOBack()
    }

    private fun setDataTOBack() {

        val intent = Intent()
        intent.putExtra("total_likes",total_likes)
        intent.putExtra("total_comment", total_comment)
        intent.putExtra("total_shared", total_shared)
        intent.putExtra("postPosition", post_position)
        intent.putExtra("isMylike", isMylikepost.toString())
        setResult(Activity.RESULT_OK, intent)
        finish()
        super.onBackPressed()

    }

    override fun onBackPressed() {
        setDataTOBack()

    }






}
