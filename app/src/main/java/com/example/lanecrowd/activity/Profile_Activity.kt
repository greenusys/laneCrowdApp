package com.example.lanecrowd.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.lancrowd.activity.modal.Home_Post_Modal
import com.example.lancrowd.activity.modal.Profile_Detail_Modal
import com.example.lancrowd.activity.modal.RegisterResModal
import com.example.lanecrowd.R
import com.example.lanecrowd.Session_Package.SessionManager
import com.example.lanecrowd.adapter.Home_Post_Adapter
import com.example.lanecrowd.adapter.Profile_Post_Adapter
import com.example.lanecrowd.retrofit.TimeShow
import com.example.lanecrowd.util.ImageFilePath
import com.example.lanecrowd.util.RuntimePermissionsActivity
import com.example.lanecrowd.util.URL
import com.example.lanecrowd.view_modal.MySessionVM
import com.example.lanecrowd.view_modal.Profile_VM
import com.example.lanecrowd.view_modal.factory.ViewModelFactoryC
import com.example.lanecrowd.view_modal.factory.ViewModelProvider_Session
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.view.RxView
import com.mzelzoghbi.zgallery.ZGallery
import com.mzelzoghbi.zgallery.entities.ZColor
import com.xw.repo.VectorCompatTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_profile2.*
import kotlinx.android.synthetic.main.no_data_found_post.*
import kotlinx.android.synthetic.main.no_internet_layout.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File


class Profile_Activity : RuntimePermissionsActivity(), KodeinAware {
    var myCoverPhoto: File? = null
    var coverUri: Uri? = null
    var myProfilePhoto: File? = null
    var profileUri: Uri? = null

    private val session: SessionManager by instance()


    private val REQ_PER_GALLERY_PROFILE = 1
    private val REQ_PER_GALLERY_COVER = 2

    var profile_rv: RecyclerView? = null
    var profilePostAdapter: Profile_Post_Adapter? = null
    var home_post_list = ArrayList<Home_Post_Modal>()
    var profileData = Profile_Detail_Modal()

    var postCounting: Int = 0

    var isScrolling = false

    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0
    var layoutManager: LinearLayoutManager? = null


    var user_name: TextView? = null
    var txt_doneChangeProfile: TextView? = null
    var txt_doneChangeCover: TextView? = null
    var iv_cover_image_profile: ImageView? = null
    var iv_profile_image_profile: ImageView? = null
    var upload_loading_animLogin: LottieAnimationView? = null
    lateinit var viewmodel: Profile_VM

    //this factory method will create and return one object of SessionVM
    var videomodelfactory: ViewModelProvider_Session? = null
    var mySessionVM: MySessionVM? = null
    private var swipe: SwipeRefreshLayout? = null


    override val kodein by kodein()
    //get factory depencey from outside using kodein framework
    private val factory: ViewModelFactoryC by instance()


    var postUserId: String? ="2"
    var postUserName: String? = "Ravish Beg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile2)



        //postUserId = intent.getStringExtra("postUserId")
        //postUserName = intent.getStringExtra("postUserName")

        println("Profile_Activity")
        println("postUserId" + postUserId)
        println("postUserName" + postUserName)

        initViews()

        checkWHoseProfile()




        initAdapter()

        initPostScrollListener()
        swiperefresh_Listener()


        if (!isNetworkAvailable(applicationContext!!)) {
            clearHomePostRV()
            visible_no_internet_layout(true)
        } else {
            setRefreshingfalse(true)

            //call only once
            callFetchProfileAPI()
            fetchPost(postCounting.toString(), "")
        }



    }


    private fun callFetchProfileAPI() {


        try {
            viewmodel.fetchProfileVM(URL.userId,postUserId!!)
                .observe(this, Observer { resultPi ->

                    profileData=resultPi


                    println("proksdfjsd"+profileData.basic_info[0]?.cover_photo)

                    profilePostAdapter = Profile_Post_Adapter(this@Profile_Activity,postUserId!!,profileData,home_post_list, applicationContext)
                    profile_rv!!.adapter = profilePostAdapter


                })
        } catch (e: Exception) {
            e.printStackTrace()
            visibleLoadingAnimation(false)

        }

    }


    private fun initPostScrollListener() {




        profile_rv!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {


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



                /*println("currentItems"+currentItems)
                println("totalItems"+totalItems)
                println("scrollOutItems"+scrollOutItems)
                println("home_post_list"+home_post_list.size)
*/

                if(scrollOutItems>=10)
                    visibleFloatingButton(true)
                else if(scrollOutItems<=3)
                    visibleFloatingButton(false)






                if (isScrolling && home_post_list.size>8 && (currentItems + scrollOutItems == totalItems)) {

                    println("load_more_called")
                    isScrolling = false
                    postCounting++


                    if (!isNetworkAvailable(applicationContext!!)) {
                        visible_no_internet_layout(true)
                    } else

                        visibleLoadingMoreAnim(true)

                    Handler().postDelayed({


                        fetchPost(postCounting.toString(), "more")

                    }, 1200)




                }

            }
        })


    }

    private fun visibleFloatingButton(value: Boolean) {

        if (value)
            scrolltoup!!.visibility = View.VISIBLE
        else
            scrolltoup!!.visibility = View.GONE


        scrolltoup!!.setOnClickListener(View.OnClickListener {

            profile_rv!!.smoothScrollToPosition(0)
            visibleFloatingButton(false)
        })

    }

    private fun clearHomePostRV() {

        home_post_list.clear()
        profile_rv!!.recycledViewPool.clear()
        profile_rv!!.adapter=null
        profilePostAdapter!!.notifyDataSetChanged()

    }

    private fun fetchPost(offsettt: String, from: String) {

        try {
            viewmodel.fetchPostTimelinetvm(postUserId!!, offsettt)
                .observe(this, Observer { resultPi ->

                    storepostDataTOModal(resultPi, from)

                })
        } catch (e: Exception) {
            e.printStackTrace()

        }

    }

    private fun visibleLoadingMoreAnim(value: Boolean) {
        if (value)
            loading_more_anim!!.visibility = View.VISIBLE
        else
            loading_more_anim!!.visibility = View.GONE

    }


    private fun visible_no_internet_layout(value: Boolean) {

        retry!!.setOnClickListener(View.OnClickListener {

            if (!isNetworkAvailable(applicationContext!!)) {
                visible_no_internet_layout(true)
            } else {
                setRefreshingfalse(true)


                profilePostAdapter = Profile_Post_Adapter(this@Profile_Activity,postUserId!!,profileData,home_post_list, applicationContext)
                profile_rv!!.adapter = profilePostAdapter



                visibleInvisibleNoInternet(false)

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



    private fun swiperefresh_Listener() {


        var c1 = ContextCompat.getColor(applicationContext!!,R.color.box_1)
        var c2 = ContextCompat.getColor(applicationContext!!,R.color.box_2)
        var c3 = ContextCompat.getColor(applicationContext!!,R.color.box_8)
        swipe!!.setColorSchemeColors(c1, c2, c3);


        swipe!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            try {


                if (!isNetworkAvailable(applicationContext!!)) {
                    setRefreshingfalse(false)
                    showSnackBar("Please check your internet connection")
                    if (home_post_list.size <= 0 && no_data_anim_post!!.visibility != View.VISIBLE) {
                        clearHomePostRV()
                        visible_no_internet_layout(true)
                    }

                } else {

                    postCounting = 0

                    profilePostAdapter = Profile_Post_Adapter(this@Profile_Activity,postUserId!!,profileData,home_post_list, applicationContext)
                    profile_rv!!.adapter = profilePostAdapter

                    visibleInvisibleNoInternet(false)



                    fetchPost(postCounting.toString(), "swipe")
                }
            } catch (e: IndexOutOfBoundsException) {
                println("on_Refresh_Exception_Found_$e")
            }
        })

    }

    private fun showSnackBar(msg: String) {
        val snackbar = Snackbar.make(CoordinatorLayout!!, msg, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(ContextCompat.getColor(applicationContext!!, (R.color.red)))
        snackbar.setTextColor(ContextCompat.getColor(applicationContext!!, (R.color.white)))
        snackbar.show()
    }





    private fun checkWHoseProfile() {


        //self profile
        if (postUserId.equals(URL.userId)) {


            user_name!!.text = URL.fullName.capitalize()


        }
        //friend profile
        else {

            user_name!!.text = postUserName!!.capitalize()
        }


    }
    fun isNetworkAvailable(context: Context)//check internet of device
            : Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }

    private fun setRefreshingfalse(value: Boolean) {

        swipe!!.isRefreshing = value



    }

    private fun initViews() {


        //this factory method will create and return one object
        videomodelfactory = ViewModelProvider_Session(MySessionVM.instance)
        mySessionVM = ViewModelProvider(this, videomodelfactory!!).get(MySessionVM::class.java)
        swipe = findViewById(R.id.swipe_ref) as SwipeRefreshLayout
        swipe!!.setDistanceToTriggerSync(100)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            swipe!!.setProgressViewOffset(false, 0, 300)
        }

        viewmodel = ViewModelProviders.of(this, factory).get(Profile_VM::class.java)

        upload_loading_animLogin = findViewById(R.id.upload_loading_animLogin)


        profile_rv = findViewById(R.id.profile_rv)


        user_name = findViewById<TextView>(R.id.user_name)


    }


    private fun visibleLoadingAnimation(value: Boolean) {

        if (value) {
            goneView(upload_loading_animLogin!!, true)
            upload_loading_animLogin!!.playAnimation()

        } else {
            goneView(upload_loading_animLogin!!, false)
            upload_loading_animLogin!!.pauseAnimation()
        }


    }


    @SuppressLint("WrongConstant")
    private fun initAdapter() {


        layoutManager = LinearLayoutManager(applicationContext)
        profile_rv!!.layoutManager = layoutManager

       // profilePostAdapter = Profile_Post_Adapter(profileData,home_post_list, applicationContext)
        //profile_rv!!.adapter = profilePostAdapter


    }


    public fun showProfileBottomSheet(from: String) {
        var sheetDialog = BottomSheetDialog(this@Profile_Activity)
        val sheetView =
            LayoutInflater.from(this@Profile_Activity).inflate(R.layout.profilebottomsheet, null)
        sheetDialog.setContentView(sheetView)

        var viewProfile = sheetView.findViewById<VectorCompatTextView>(R.id.viewProfile)
        var changeProfile = sheetView.findViewById<VectorCompatTextView>(R.id.changeProfile)

        if (from.equals("cover")) {
            viewProfile.text = "View Cover Pic"
            changeProfile.text = "Change Cover Pic"
        }


        val observable1 = RxView.clicks(viewProfile!!).map<Any> { o: Any? -> viewProfile }
        val observable2 = RxView.clicks(changeProfile!!).map<Any> { o: Any? -> changeProfile }


        val disposable = Observable.merge(observable1, observable2)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { o ->

                sheetDialog.dismiss()

                if (o == viewProfile) {

                    if (from.equals("cover"))
                        openPic(arrayListOf(URL.coverPicPath + URL.coverPic))
                    else
                        openPic(arrayListOf(URL.profilePicPath + URL.profilePic))


                } else if (o == changeProfile) {
                    if (from.equals("cover"))
                        checkPermissions("cover")
                    else
                        checkPermissions("profile")


                }

            }



        sheetDialog.show()

    }

    public fun openPic(url: ArrayList<String>) {

        //open single image
        ZGallery.with(this, url)
            .setToolbarTitleColor(ZColor.WHITE)
            .setGalleryBackgroundColor(ZColor.WHITE)
            .setToolbarColorResId(R.color.colorPrimary)
            .setTitle(user_name!!.text.toString())
            .show("true")

    }


    private fun checkPermissions(from: String) {

        println("check_called")

        if (ContextCompat.checkSelfPermission(
                this@Profile_Activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            super@Profile_Activity.requestAppPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), R.string.runtimepermission_txt, REQ_PER_GALLERY_PROFILE
            )
        } else {

            println("elserwee")
            if (from.equals("profile"))
                openGalleryProfile()
            else
                openGalleryCover()

        }


    }


    fun openGalleryProfile() {
        startActivityForResult(Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT), 1)
    }

    fun openGalleryCover() {
        startActivityForResult(Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT), 2)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        try {


            println("if_kaif_requestcode$requestCode")
            println("kaif_resultCode$resultCode")
            // println("kaif_data${data!!.data}")


            if (requestCode == 2 && resultCode == RESULT_OK) { // upload cover pic
                coverUri = data!!.data
                Glide.with(baseContext).load(coverUri).into(iv_cover_image_profile!!)
                myCoverPhoto = File(ImageFilePath.getPath(baseContext, coverUri))
                goneView(txt_doneChangeCover!!, true)

            } else if (requestCode == 1 && resultCode == RESULT_OK) {
                profileUri = data!!.data
                Glide.with(baseContext).load(profileUri).into(iv_profile_image_profile!!)
                myProfilePhoto = File(ImageFilePath.getPath(baseContext, profileUri))
                goneView(txt_doneChangeProfile!!, true)

            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun storepostDataTOModal(resultPi: JsonObject?, from: String) {


        var isMylike: Boolean
        var isImage :Boolean
        var main: JSONObject? = null


        if (from.equals("swipe"))
            ClearForRefreshData()


        if(from.equals(""))
        {
            profilePostAdapter = Profile_Post_Adapter(this@Profile_Activity,postUserId!!,profileData,home_post_list, applicationContext)
            profile_rv!!.adapter = profilePostAdapter

        }


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


                   /* if (no_data_anim_post!!.visibility == View.VISIBLE)
                        visibleNoDataFound(false)
*/
                    if (no_internet_anim!!.visibility == View.VISIBLE)
                        visible_no_internet_layout(false)


                    val data: JSONArray = main.getJSONArray("data")

                    // println("data_size" + data.length())

                    for (i in 0 until data.length()) {

                        var item = data.getJSONObject(i)

                        var postList: ArrayList<String> = ArrayList()

                        println("kaiffound")

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


                        var date= TimeShow.getTime(item.getString("posted_on"));


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

                    //if (home_post_list.size <= 0)
                     //   visibleNoDataFound(true)

                    setRefreshingfalse(false)

                }

            } catch (e: Exception) {

                e.printStackTrace()

                visibleLoadingMoreAnim(false)
                setRefreshingfalse(false)
            }

        }
        //for no post available
        else {
            println("no_Data_found")
            //if (home_post_list.size <= 0)
               // visibleNoDataFound(true)

            setRefreshingfalse(false)
        }


        notifiyAdapter()



        if (from.equals("more"))
            visibleLoadingMoreAnim(false)




    }


    private fun ClearForRefreshData() {

        println("ClearForRefreshData_called")
        home_post_list.clear()

        profile_rv!!.recycledViewPool.clear()
        notifiyAdapter()

        println("rvsize"+layoutManager!!.itemCount)



    }


    private fun notifiyAdapter() {


        println("checkPostsize" + home_post_list.size)
        profilePostAdapter!!.notifyDataSetChanged()

    }
    private fun visibleNoDataFound(value: Boolean) {


        println("visible_no_found"+value)
        add_post!!.setOnClickListener(View.OnClickListener {

            startActivity(
                Intent(
                    applicationContext,
                    Add_Post_Activity::class.java
                )
                    .putExtra("from","post")
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )

        })


        if (value) {
            no_data_anim_post!!.visibility = View.VISIBLE
            add_post!!.visibility = View.VISIBLE
            //no post text
            text!!.visibility = View.VISIBLE
            no_data_anim_post!!.playAnimation()
        } else {
            no_data_anim_post!!.visibility = View.GONE
            add_post!!.visibility = View.GONE

            //no post text
            text!!.visibility = View.GONE
            no_data_anim_post!!.pauseAnimation()
        }
    }



    private fun ChangeImage(from: String) {

        visibleLoadingAnimation(true)

        var uri: String? = null
        if (from.equals("profile"))
            uri = profileUri.toString()
        else
            uri = coverUri.toString()


        var file = File(ImageFilePath.getPath(baseContext, Uri.parse(uri)))

        println("file_name" + file.name)


        // Create a request body with file and image media type
        var fileReqBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        // Create MultipartBody.Part using file request-body,file name and part name
        var part = MultipartBody.Part.createFormData("files", file.name, fileReqBody)
        //Create request body with text description and text media type
        var userId = RequestBody.create(MediaType.parse("multipart/form-data"), URL.userId)
        var android = RequestBody.create(MediaType.parse("multipart/form-data"), "")
        //

        disableLayout()

        try {
            viewmodel.changeProfileCoverPic(from, part, userId, android)
                .observe(this, Observer { resultPi ->

                    setAndUpdateProfileOrCoverPic(from, resultPi)


                })
        } catch (e: Exception) {
            e.printStackTrace()
            visibleLoadingAnimation(false)

        }

    }


    private fun disableLayout() {

        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )


    }

    private fun enableLayout() {

        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)


    }

    private fun setAndUpdateProfileOrCoverPic(from: String, resultPi: RegisterResModal?) {


        if (from.equals("profile"))
            goneView(txt_doneChangeProfile, false)
        else
            goneView(txt_doneChangeCover, false)


        enableLayout()

        visibleLoadingAnimation(false)
        println("changeApiResponse" + resultPi)
        if (resultPi!!.msg.equals("profile pic uploaded Successfully")) {

            //update session value of profile
            session.updateProfilePic(resultPi.pic)
            URL.profilePic = resultPi.pic
            updateSessionVM()


        } else if (resultPi.msg.equals("Cover pic uploaded Successfully")) {

            //update session value of profile
            session.updateCoverPic(resultPi.pic)
            URL.coverPic = resultPi.pic
            updateSessionVM()
        }
    }

    private fun updateSessionVM() {

        //set session data to MysessionVM
        mySessionVM!!.StoreValueTOLiveData(
            URL.userId,
            URL.phone,
            URL.fullName,
            "",
            URL.profilePic,
            URL.coverPic,
            URL.dob,
            URL.gender
        )

    }


    private fun goneView(view: View?, value: Boolean) {

        if (value)
            view!!.visibility = View.VISIBLE
        else
            view!!.visibility = View.GONE

    }

    override fun onPermissionsGranted(requestCode: Int) {

        if (requestCode == REQ_PER_GALLERY_PROFILE) {
            openGalleryProfile()
        } else if (requestCode == REQ_PER_GALLERY_COVER) {
            openGalleryCover()
        }
    }


    fun Back(view: View) {
        onBackPressed()
    }

    fun goto_More_Info(view: View) {


    }
}
