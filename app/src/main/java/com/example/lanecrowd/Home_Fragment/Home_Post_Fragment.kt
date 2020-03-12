package com.example.lanecrowd.Home_Fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.util.Log
import android.view.*
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.airbnb.lottie.LottieAnimationView
import com.example.lancrowd.activity.modal.Home_Post_Modal
import com.example.lancrowd.activity.modal.RegisterResModal
import com.example.lancrowd.activity.modal.Story_Modal
import com.example.lanecrowd.view_modal.factory.ViewModelFactoryC
import com.example.lanecrowd.R
import com.example.lanecrowd.Session_Package.SessionManager
import com.example.lanecrowd.activity.Add_Post_Activity
import com.example.lanecrowd.adapter.Home_Post_Adapter
import com.example.lanecrowd.modal.PowerMenuUtils
import com.example.lanecrowd.retrofit.TimeShow
import com.example.lanecrowd.util.URL
import com.example.lanecrowd.view_modal.FetchPostVm
import com.example.lanecrowd.view_modal.LoginRegUserVM
import com.example.lanecrowd.view_modal.MySessionVM
import com.example.lanecrowd.view_modal.factory.ViewModelProvider_Session
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import org.json.JSONArray
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class Home_Post_Fragment : Fragment(),KodeinAware,SearchView.OnQueryTextListener {


    override fun onQueryTextSubmit(query: String?): Boolean {
    return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }




    //this factory method will create and return one object of SessionVM
    var videomodelfactory: ViewModelProvider_Session?=null
    var mySessionVM: MySessionVM?=null


    //for menu
    var firstTime: Boolean = false

    var postCounting: Int = 0

    var isScrolling = false

    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0


    internal var postId: String = ""
    internal var postposition: Int = -1

    private var selPostfMenu: PowerMenu? = null
    private var otherPostMenu: PowerMenu? = null
    private var main_layout: RelativeLayout? = null
    private var swipe: SwipeRefreshLayout? = null
    var loading_more_anim: SpinKitView? = null

    private var no_data_anim_post: LottieAnimationView? = null
    private var add_post: TextView? = null
    private var no_post_text: TextView? = null


    private var no_internet_anim: LottieAnimationView? = null
    private var retry: TextView? = null
    private var internet_text: TextView? = null


    private var vibe: Vibrator? = null

    var home_post_rv: RecyclerView? = null

    var homePostAdapter: Home_Post_Adapter? = null

    var home_post_list = ArrayList<Home_Post_Modal>()
    var story_list = ArrayList<Story_Modal>()


    override val kodein by kodein()

    //get factory depencey from outside using kodein framework
    private val factory: ViewModelFactoryC by instance()
    private val session: SessionManager by  instance()


    private lateinit var viewmodel: FetchPostVm


    lateinit var loginVM: LoginRegUserVM;
    var layoutManager: LinearLayoutManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home__post_, container, false)



        viewmodel = ViewModelProviders.of(this, factory).get(FetchPostVm::class.java)

        loginVM = ViewModelProviders.of(this,factory).get(LoginRegUserVM::class.java)



        //power menu
        selPostfMenu = PowerMenuUtils.getSelfPostMenu(
            context!!,
            this,
            onHamburgerItemClickListener,
            onHamburgerMenuDismissedListener
        )
        otherPostMenu = PowerMenuUtils.getOtherPostMenu(
            context!!,
            this,
            onHamburgerItemClickListener,
            onHamburgerMenuDismissedListener
        )



       // session = SessionManager(context!!)
        //this factory method will create and return one object
        videomodelfactory =
            ViewModelProvider_Session(
                MySessionVM.instance
            )
        mySessionVM = ViewModelProvider(this, videomodelfactory!!).get(MySessionVM::class.java)



        vibe = context!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        swipe = view.findViewById(R.id.swipe) as SwipeRefreshLayout
        loading_more_anim = view.findViewById(R.id.loading_more_anim) as SpinKitView
        home_post_rv = view.findViewById(R.id.home_post_rv) as RecyclerView
        main_layout = view.findViewById(R.id.main_layout) as RelativeLayout


        //no data found
        no_data_anim_post = view.findViewById(R.id.no_data_anim_post) as LottieAnimationView
        add_post = view.findViewById(R.id.add_post) as TextView
        no_post_text = view.findViewById(R.id.text) as TextView

        //no internet found
        no_internet_anim = view.findViewById(R.id.no_internet_anim) as LottieAnimationView
        retry = view.findViewById(R.id.retry) as TextView
        internet_text = view.findViewById(R.id.internet_text) as TextView




        swipe!!.setDistanceToTriggerSync(100)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            swipe!!.setProgressViewOffset(false, 0, 300)
        }



        layoutManager = LinearLayoutManager(context)
        home_post_rv!!.layoutManager = layoutManager
        homePostAdapter = Home_Post_Adapter(story_list, home_post_list, context!!, this@Home_Post_Fragment)
        home_post_rv!!.adapter = homePostAdapter



        swiperefresh_Listener()
        initPostScrollListener()


        if (!isNetworkAvailable(context!!)) {
            clearHomePostRV()
            visible_no_internet_layout(true)
        } else {
            setRefreshingfalse(true)

            //call only once
            fetchStory()
            fetchPost(postCounting.toString(), "")
        }





        return view
    }

    private fun visible_no_internet_layout(value: Boolean) {

        retry!!.setOnClickListener(View.OnClickListener {

            if (!isNetworkAvailable(context!!)) {
                visible_no_internet_layout(true)
            } else {
                setRefreshingfalse(true)

                    homePostAdapter = Home_Post_Adapter(story_list, home_post_list, context!!, this@Home_Post_Fragment)
                    home_post_rv!!.adapter=homePostAdapter

                visibleInvisibleNoInternet(false)

                //call only once
                fetchUpdateUserDetails()
                fetchStory()

                fetchPost(postCounting.toString(), "")
            }

        })


        if (value) {
            visibleInvisibleNoInternet(true)
        } else {
            visibleInvisibleNoInternet(false)
        }




    }

    private fun visibleInvisibleNoInternet(value: Boolean) {

        if(value)
        { no_internet_anim!!.visibility = View.VISIBLE
            retry!!.visibility = View.VISIBLE
            internet_text!!.visibility = View.VISIBLE
            no_internet_anim!!.playAnimation()

        }
        else
        {
            no_internet_anim!!.visibility = View.GONE
            retry!!.visibility = View.GONE
            internet_text!!.visibility = View.GONE
            no_internet_anim!!.pauseAnimation()

        }

    }

    fun isNetworkAvailable(context: Context)//check internet of device
            : Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }

    private fun visibleNoDataFound(value: Boolean) {


        println("visible_no_found"+value)
        add_post!!.setOnClickListener(View.OnClickListener {

            startActivity(
                Intent(
                    context,
                    Add_Post_Activity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )

        })


        if (value) {
            no_data_anim_post!!.visibility = View.VISIBLE
            add_post!!.visibility = View.VISIBLE
            no_post_text!!.visibility = View.VISIBLE
            no_data_anim_post!!.playAnimation()
        } else {
            no_data_anim_post!!.visibility = View.GONE
            add_post!!.visibility = View.GONE
            no_post_text!!.visibility = View.GONE
            no_data_anim_post!!.pauseAnimation()
        }
    }


    private fun initPostScrollListener() {

        home_post_rv!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {


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



                println("currentItems"+currentItems)
                println("totalItems"+totalItems)
                println("scrollOutItems"+scrollOutItems)
                println("home_post_list"+home_post_list.size)

                if (isScrolling && home_post_list.size>8 && (currentItems + scrollOutItems == totalItems)) {
                    visibleLoadingMoreAnim(true)

                    println("load_more_called")
                    isScrolling = false
                    postCounting++
                    if (!isNetworkAvailable(context!!)) {
                        visible_no_internet_layout(true)
                    } else

                        Handler().postDelayed({

                            fetchPost(postCounting.toString(), "more")

                        }, 2000)




                }

            }
        })


    }

    private fun visibleLoadingMoreAnim(value: Boolean) {
        if (value)
            loading_more_anim!!.visibility = View.VISIBLE
        else
            loading_more_anim!!.visibility = View.GONE

    }


    private fun swiperefresh_Listener() {


        var c1 = ContextCompat.getColor(context!!,R.color.box_1)
        var c2 = ContextCompat.getColor(context!!,R.color.box_2)
        var c3 = ContextCompat.getColor(context!!,R.color.box_8)
        swipe!!.setColorSchemeColors(c1, c2, c3);


        swipe!!.setOnRefreshListener(OnRefreshListener {
            try {

                postCounting = 0
                if (!isNetworkAvailable(context!!))
                {
                    setRefreshingfalse(false)
                    showSnackBar("Please check your internet connection")
                    if (home_post_list.size <= 0 && no_data_anim_post!!.visibility != View.VISIBLE)
                    {
                        clearHomePostRV()
                        visible_no_internet_layout(true)
                    }

                }
                else {

                    homePostAdapter = Home_Post_Adapter(story_list, home_post_list, context!!, this@Home_Post_Fragment)
                    home_post_rv!!.adapter=homePostAdapter

                    visibleInvisibleNoInternet(false)



                    //call only once
                    fetchUpdateUserDetails()
                    fetchStory()
                    fetchPost(postCounting.toString(), "swipe")
                }
            } catch (e: IndexOutOfBoundsException) {
                println("on_Refresh_Exception_Found_$e")
            }
        })

    }

    private fun fetchUpdateUserDetails() {

        try {
            loginVM.loginUser(URL.email,URL.password).observe(viewLifecycleOwner, Observer { resultPi ->



                val userdata: ArrayList<RegisterResModal.UserData> =resultPi.data


                if(resultPi.status.equals("1")) {

                    createUserSession(userdata)
                }




            })
        }catch (e:Exception)
        {
            e.printStackTrace()
        }



    }



    private fun createUserSession(userdata: ArrayList<RegisterResModal.UserData>) {

        var phone: String = ""
        var email: String = ""

        if (userdata.get(0).phone == null)
        else
            phone = userdata.get(0).phone

        if (userdata.get(0).email == null)
        else
            email = userdata.get(0).email


        println("new_profile_pic"+userdata.get(0).profile_picture)


        //creating user's session
        session!!.createLoginSession(
            userdata.get(0).user_id,
            email,
            URL.password,
            phone,
            userdata.get(0).full_name,
            userdata.get(0).bio_graphy,
            userdata.get(0).profile_picture,
            userdata.get(0).cover_photo,
            userdata.get(0).date_of_birth,
            userdata.get(0).gender
        )


        //set session data to MysessionVM
        mySessionVM!!.StoreValueTOLiveData(
            userdata.get(0).user_id,
            email,
            phone,
            userdata.get(0).full_name,
            userdata.get(0).bio_graphy,
            userdata.get(0).profile_picture,
            userdata.get(0).cover_photo,
            userdata.get(0).date_of_birth,
            userdata.get(0).gender
        )


    }

    private fun showSnackBar(msg: String) {
        val snackbar = Snackbar.make(main_layout!!, msg, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(ContextCompat.getColor(context!!, (R.color.red)))
        snackbar.setTextColor(ContextCompat.getColor(context!!, (R.color.white)))
        snackbar.show()
    }

    private fun ClearForRefreshData() {

        println("ClearForRefreshData_called")
        home_post_list.clear()
        home_post_rv!!.recycledViewPool.clear()
        notifiyAdapter()


    }



    private fun setRefreshingfalse(value: Boolean) {

        swipe!!.isRefreshing = value



    }

    private fun fetchStory() {

        viewmodel.fetchStoryVM(URL.userId, "0")
            .observe(viewLifecycleOwner, Observer { story_result ->

                println("fetch_story" + story_result)

                setStoryPost(story_result)


            })

    }


    private fun fetchPost(offsettt: String, from: String) {

        try {


            viewmodel.fetchPostvm(URL.userId, offsettt)
                .observe(viewLifecycleOwner, Observer { resultPi ->

                    println("fetch_post" + resultPi)



                    storepostDataTOModal(resultPi, from)


                })






        } catch (e: Exception) {
            e.printStackTrace()


        }


    }


    //this will run only once
    private fun setStoryPost(storyResult: JsonObject?) {

        var main: JSONObject? = null

        if (storyResult != null) {


            try {


                main = JSONObject(storyResult.toString())
                if (storyResult != null && main.has("code") && main.getString("code").equals("1"))
                {

                    story_list.clear()

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



                        story_list.add(Story_Modal(
                            item.getString("story_id"),
                            item.getString("user_id"),
                            item.getString("story"),
                            postList,
                            item.getString("posted_by"),
                            item.getString("profile_pic"),
                            item.getString("posted_on")
                        ))
                    }



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


        println("Sotry_size"+story_list.size)

    }

    private fun storepostDataTOModal(resultPi: JsonObject?, from: String) {


        var isMylike: Boolean
        var isImage :Boolean
        var main: JSONObject? = null


        if (from.equals("swipe"))
            ClearForRefreshData()


        //call after load more
        //updateAdapterForMultipleData()

        if (swipe!!.isRefreshing) {
            setRefreshingfalse(false)

        }


        if (resultPi != null) {

            try {


                main = JSONObject(resultPi.toString())



                if (main.has("code") && main.getString("code").equals("1"))
                {

                    if (no_data_anim_post!!.visibility == View.VISIBLE)
                        visibleNoDataFound(false)

                    if (no_internet_anim!!.visibility == View.VISIBLE)
                        visible_no_internet_layout(false)


                    val data: JSONArray = main.getJSONArray("data")

                    // println("data_size" + data.length())

                    for (i in 0 until data.length()) {

                        var item = data.getJSONObject(i)

                        var postList: ArrayList<String> = ArrayList()


                        //for post files
                        try {

                            if (!item.getString("post_files").equals("")) {
                                postList = item.getString("post_files").split(",") as ArrayList<String>

                                isImage =
                                    item.getString("post_files").contains(".jpg") || item.getString(
                                        "post_files"
                                    ).contains(
                                        ".jpeg"
                                    ) || item.getString("post_files").contains(".png")

                            } else {
                                isImage = false
                                postList.add("empty")
                            }

                        } catch (e: Exception) {
                            isImage = false
                            postList.add(item.getString("post_files"))

                        }


                        //for likes
                        val like: JSONArray = item.getJSONArray("likes_data")
                        if (like.length() > 0) {

                            // println("like_kaif" +i+" " +like)

                            var likeitem = like.getJSONObject(0)

                            isMylike = likeitem.getString("like_or_dislike").equals("1")

                        } else
                            isMylike = false


                        var date=TimeShow.getTime(item.getString("posted_on"));


                        //store data to list
                        home_post_list.add(
                            Home_Post_Modal(
                                item.getString("post_id"),
                                item.getString("user_id"),
                                item.getString("post"),
                                item.getString("post_head"),
                                postList,
                                item.getString("posted_by"),
                                item.getString("profile_pic"),
                                date,
                                item.getString("total_likes"),
                                item.getString("total_share"),
                                item.getString("total_comments"),
                                isImage,
                                isMylike
                            )
                        )


                    }


                }
                else
                {
                    println("no_Data_foun_sallu"+home_post_list.size)

                    if (home_post_list.size <= 0)
                    visibleNoDataFound(true)

                    setRefreshingfalse(false)

                }

            } catch (e: Exception) {

                visibleLoadingMoreAnim(false)
                setRefreshingfalse(false)
            }

        }
        //for no post available
        else {
            println("no_Data_found")
            if (home_post_list.size <= 0)
                visibleNoDataFound(true)

            setRefreshingfalse(false)
        }


        notifiyAdapter()



        if (from.equals("more"))
            visibleLoadingMoreAnim(false)




    }

    private fun clearHomePostRV() {

        home_post_list.clear()
        home_post_rv!!.recycledViewPool.clear()
        home_post_rv!!.adapter=null
        homePostAdapter!!.notifyDataSetChanged()

    }




    private fun notifiyAdapter() {


        println("check_value" + home_post_list.size)
        homePostAdapter!!.notifyDataSetChanged()

    }


    //power option menu
    private val onHamburgerItemClickListener =
        OnMenuItemClickListener<PowerMenuItem> { position, item ->
            // hamburgerMenu!!.selectedPosition = position

            if (!firstTime) {


                println("")



                if (item.title.equals(getString(R.string.delete)))
                    askToDeleteSelfPost(postId)


/*

                    if (item.title.equals(getString(R.string.rate_me)))
                        rateMe()
                    else if (item.title.equals(getString(R.string.share)))
                        share()
                    else if (item.title.equals(getString(R.string.about)))
                        gotoABoutActivity()*/
            }


        }

    private fun askToDeleteSelfPost(postId: String) {


        MaterialAlertDialogBuilder(activity, R.style.AlertDialogTheme)
            .setTitle("LaneCrowd")
            .setMessage("Are you sure want to delete post?")
            .setNegativeButton("No") { dialogInterface, i ->
            }
            .setPositiveButton("Yes") { dialogInterface, i ->
                println("show_psoitio_2" + postposition)


                viewmodel.deletePosetAPI(postId)
                homePostAdapter!!.notifyItemRemoved(postposition)

            }
            .show()
    }

    private val onHamburgerMenuDismissedListener = {
        Log.d("Test", "onDismissed hamburger menu")
    }


    fun showMenu(position: Int, postId: String, menu: ImageView, value: Boolean) {
        showVibration()


        println("show_psoitio" + position)


        this.postId = postId
        this.postposition = position

        if (selPostfMenu!!.isShowing) {
            selPostfMenu!!.dismiss()
            return
        }

        if (otherPostMenu!!.isShowing) {
            otherPostMenu!!.dismiss()
            return
        }


        println("menu_check" + value)

        if (value)
            selPostfMenu!!.showAsDropDown(menu)
        else
            otherPostMenu!!.showAsDropDown(menu)


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.maxWidth = Integer.MAX_VALUE

        searchView.setOnQueryTextListener(this)

    }

    fun showVibration() {

        vibe!!.vibrate(80)
    }

    fun likeDislike(postId: String, userId: String) {


        viewmodel.likeDislikeAPI(postId, userId)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            println("total_likes" + data!!.getStringExtra("total_likes"))
            println("total_comments" + data.getStringExtra("total_comment"))
            println("postPosition" + data.getStringExtra("postPosition"))
            println("isMylikepost" + data.getStringExtra("isMylike"))


            home_post_list.get(data.getStringExtra("postPosition").toInt()-2).total_likes=data!!.getStringExtra("total_likes")

            home_post_list.get(data.getStringExtra("postPosition").toInt()-2).isMyLike = data.getStringExtra("isMylike").equals("true")

            home_post_list.get(data.getStringExtra("postPosition").toInt()-2).total_comments=data!!.getStringExtra("total_comment")
            homePostAdapter!!.notifyItemChanged(data.getStringExtra("postPosition").toInt(),data.getStringExtra("postPosition").toInt())

        }

    }



}