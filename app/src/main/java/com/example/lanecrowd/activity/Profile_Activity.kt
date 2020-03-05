package com.example.lanecrowd.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lancrowd.activity.modal.Home_Post_Modal
import com.example.lancrowd.activity.modal.Photo_Video_Modal
import com.example.lancrowd.activity.modal.RegisterResModal
import com.example.lanecrowd.R
import com.example.lanecrowd.Session_Package.SessionManager
import com.example.lanecrowd.adapter.Photos_Video_Adapter
import com.example.lanecrowd.adapter.Profile_Post_Adapter
import com.example.lanecrowd.util.ImageFilePath
import com.example.lanecrowd.util.RuntimePermissionsActivity
import com.example.lanecrowd.util.URL
import com.example.lanecrowd.view_modal.MySessionVM
import com.example.lanecrowd.view_modal.Profile_VM
import com.example.lanecrowd.view_modal.ViewModelProvider_Custom
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class Profile_Activity : RuntimePermissionsActivity() {
    var myCoverPhoto: File? = null
    var coverUri: Uri? = null
    var myProfilePhoto: File? = null
    var profileUri: Uri? = null

    var timeline: TextView? = null
    var photos: TextView? = null
    var videos: TextView? = null
    private var session: SessionManager? = null


    private val REQ_PER_GALLERY_PROFILE = 1
    private val REQ_PER_GALLERY_COVER = 2

    var profile_rv: RecyclerView? = null
    var profilePostAdapter: Profile_Post_Adapter? = null
    var photosVideoAdapter: Photos_Video_Adapter? = null
    var home_post_list = ArrayList<Home_Post_Modal>()

    var photos_video_list = ArrayList<Photo_Video_Modal>()

    var user_name: TextView? = null
    var txt_changeCover: TextView? = null
    var txt_changeProfile: TextView? = null
    var txt_doneChangeProfile: TextView? = null
    var txt_doneChangeCover: TextView? = null
    var iv_cover_image_profile: ImageView? = null
    var iv_profile_image_profile: ImageView? = null
    var upload_loading_animLogin: LottieAnimationView? = null
    lateinit var viewmodel: Profile_VM

    //this factory method will create and return one object of SessionVM
    var videomodelfactory: ViewModelProvider_Custom? = null
    var mySessionVM: MySessionVM? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        //this factory method will create and return one object
        videomodelfactory = ViewModelProvider_Custom(MySessionVM.instance)
        mySessionVM = ViewModelProvider(this, videomodelfactory!!).get(MySessionVM::class.java)



        initViews()

        set_Time_Post_Adapter()
    }


    private fun initViews() {

        session = SessionManager(applicationContext)

        viewmodel = ViewModelProvider(this).get(Profile_VM::class.java)

        upload_loading_animLogin = findViewById(R.id.upload_loading_animLogin)
        timeline = findViewById(R.id.timeline)
        photos = findViewById(R.id.photos)
        videos = findViewById(R.id.videos)

        profile_rv = findViewById(R.id.profile_rv)


        user_name = findViewById<TextView>(R.id.user_name)


        txt_changeCover = findViewById<TextView>(R.id.txt_changeCover)
        txt_changeProfile = findViewById<TextView>(R.id.txt_changeProfile)
        txt_doneChangeProfile = findViewById<TextView>(R.id.txt_doneChangeProfile)
        txt_doneChangeCover = findViewById<TextView>(R.id.txt_doneChangeCover)

        iv_cover_image_profile = findViewById<ImageView>(R.id.iv_cover_image_profile)
        iv_profile_image_profile = findViewById<ImageView>(R.id.iv_profile_image_profile)


        setUserDataWithImages()


        val observable1 =
            RxView.clicks(txt_changeProfile!!).map<Any> { o: Any? -> txt_changeProfile }
        val observable2 = RxView.clicks(txt_changeCover!!).map<Any> { o: Any? -> txt_changeCover }
        val observable3 =
            RxView.clicks(txt_doneChangeProfile!!).map<Any> { o: Any? -> txt_doneChangeProfile }
        val observable4 =
            RxView.clicks(txt_doneChangeCover!!).map<Any> { o: Any? -> txt_doneChangeCover }

        val observable5 = RxView.clicks(timeline!!).map<Any> { o: Any? -> timeline }
        val observable6 = RxView.clicks(photos!!).map<Any> { o: Any? -> photos }
        val observable7 = RxView.clicks(videos!!).map<Any> { o: Any? -> videos }



        setClickListener(observable1, observable2, observable3, observable4)

        setClickListeneToSlider(observable5, observable6, observable7)


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




    private fun setUserDataWithImages() {


        val user: HashMap<String, String?> = session!!.userDetails
        setUseDataTOVIew(user)



        mySessionVM!!.getName()!!.observe(this, Observer { resultPi ->


            setUseDataTOVIew(resultPi)


        })




    }






    private fun setUseDataTOVIew(result: HashMap<String, String?>) {


        user_name!!.text = URL.fullName

        Glide.with(baseContext)
            .load(URL.profilePicPath + result.get(SessionManager.KEY_PROFILE_PICTURE))
            .apply(RequestOptions().placeholder(R.drawable.placeholder_profile))
            .thumbnail(0.01f).into(iv_profile_image_profile!!)

        Glide.with(baseContext)
            .load(URL.coverPicPath + result.get(SessionManager.KEY_COVER_PHOTO)).apply(
                RequestOptions().placeholder(R.drawable.placeholder)
            ).thumbnail(0.01f).into(iv_cover_image_profile!!)

    }


    private fun setClickListeneToSlider(
        observable1: Observable<Any>?,
        observable2: Observable<Any>?,
        observable3: Observable<Any>?
    ) {

        //set click listener
        val disposable = Observable.merge(observable1, observable2, observable3)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { o ->


                if (o == timeline) {
                    set_Time_Post_Adapter()
                } else if (o == photos) {
                    set_Photos_Videos_Adapter("photo")
                } else if (o == videos) {
                    set_Photos_Videos_Adapter("video")
                }

            }


    }

    @SuppressLint("WrongConstant")
    private fun set_Time_Post_Adapter() {


        timeline!!.setTextColor(resources.getColor(R.color.colorPrimary))
        photos!!.setTextColor(resources.getColor(R.color.black))
        videos!!.setTextColor(resources.getColor(R.color.black))


        profile_rv!!.recycledViewPool.clear()

        profilePostAdapter = Profile_Post_Adapter(home_post_list, applicationContext)
        profile_rv!!.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        profile_rv!!.adapter = profilePostAdapter
        profilePostAdapter!!.notifyDataSetChanged()


    }

    private fun setClickListener(
        observable1: Observable<Any>,
        observable2: Observable<Any>,
        observable3: Observable<Any>,
        observable4: Observable<Any>
    ) {

        //set click listener
        val disposable = Observable.merge(observable1, observable2, observable3, observable4)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { o ->

                if (o == txt_changeProfile) {
                    askToUserChangeProfilePic("profile")
                } else if (o == txt_changeCover) {
                    askToUserChangeProfilePic("cover")
                } else if (o == txt_doneChangeProfile) {

                    if (profileUri != null) {
                        ChangeImage("profile")
                    }

                } else if (o == txt_doneChangeCover) {
                    if (coverUri != null) {
                        ChangeImage("cover")
                    }
                }

            }


    }


    private fun askToUserChangeProfilePic(from: String) {

        var msg: String = ""

        if (from.equals("profile"))
            msg = "Want to change your profile pic?"
        else
            msg = "Want to change your  cover pic?"

        MaterialAlertDialogBuilder(this@Profile_Activity, R.style.AlertDialogTheme)
            .setTitle("LaneCrowd")
            .setMessage(msg)
            .setNegativeButton("No") { dialogInterface, i -> }
            .setPositiveButton("yes") { dialogInterface, i ->


                checkPermissions(from)

            }
            .show()


    }

    private fun checkPermissions(from: String) {

        println("check_called")

        var req: Int
        if (from.equals("profile"))
            req = REQ_PER_GALLERY_PROFILE
        else
            req = REQ_PER_GALLERY_COVER


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
            println("kaif_data${data!!.data}")


            if (requestCode == 2 && resultCode == RESULT_OK) { // upload cover pic
                coverUri = data.data
                Glide.with(baseContext).load(coverUri).into(iv_cover_image_profile!!)
                myCoverPhoto = File(ImageFilePath.getPath(baseContext, coverUri))
                goneView(txt_doneChangeCover!!, true)

            } else if (requestCode == 1 && resultCode == RESULT_OK) {
                profileUri = data.data
                Glide.with(baseContext).load(profileUri).into(iv_profile_image_profile!!)
                myProfilePhoto = File(ImageFilePath.getPath(baseContext, profileUri))
                goneView(txt_doneChangeProfile!!, true)

            }


        } catch (e: Exception) {
            e.printStackTrace()
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

    private fun setAndUpdateProfileOrCoverPic(from: String, resultPi: RegisterResModal?) {


        if (from.equals("profile"))
            goneView(txt_doneChangeProfile, false)
        else
            goneView(txt_doneChangeCover, false)


        visibleLoadingAnimation(false)
        println("changeApiResponse" + resultPi)
        if (resultPi!!.msg.equals("profile pic uploaded Successfully")) {

            //update session value of profile
            session!!.updateProfilePic(resultPi.pic)
            URL.profilePic = resultPi.pic
            updateSessionVM()


        } else if (resultPi.msg.equals("Cover pic uploaded Successfully")) {

            //update session value of profile
            session!!.updateCoverPic(resultPi.pic)
            URL.coverPic = resultPi.pic
            updateSessionVM()
        }
    }

    private fun updateSessionVM() {

        //set session data to MysessionVM
        mySessionVM!!.StoreValueTOLiveData(
            URL.userId,
            URL.email,
            URL.phone,
            URL.fullName,
            "",
            URL.profilePic,
            URL.coverPic
        )

    }


    @SuppressLint("WrongConstant")
    private fun set_Photos_Videos_Adapter(from: String) {

        if (from.equals("photo")) {
            timeline!!.setTextColor(resources.getColor(R.color.black))
            photos!!.setTextColor(resources.getColor(R.color.colorPrimary))
            videos!!.setTextColor(resources.getColor(R.color.black))
        } else {
            timeline!!.setTextColor(resources.getColor(R.color.black))
            photos!!.setTextColor(resources.getColor(R.color.black))
            videos!!.setTextColor(resources.getColor(R.color.colorPrimary))
        }


        profile_rv!!.recycledViewPool.clear()

        photos_video_list.clear()

        photos_video_list.add(Photo_Video_Modal())
        photos_video_list.add(Photo_Video_Modal())
        photos_video_list.add(Photo_Video_Modal())


        photosVideoAdapter = Photos_Video_Adapter(from, photos_video_list, applicationContext)
        profile_rv!!.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        profile_rv!!.adapter = photosVideoAdapter
        photosVideoAdapter!!.notifyDataSetChanged()


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
}
