package com.example.lanecrowd.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lancrowd.activity.modal.Home_Post_Modal
import com.example.lancrowd.activity.modal.Photo_Video_Modal
import com.example.lancrowd.activity.modal.Profile_Detail_Modal
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
import com.example.lanecrowd.view_modal.factory.ViewModelFactoryC
import com.example.lanecrowd.view_modal.factory.ViewModelProvider_Session
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jakewharton.rxbinding2.view.RxView
import com.mzelzoghbi.zgallery.ZGallery
import com.mzelzoghbi.zgallery.entities.ZColor
import com.xw.repo.VectorCompatTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.profile_detail_layout.*
import kotlinx.android.synthetic.main.profile_detail_layout.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File


class Profile_Activity : RuntimePermissionsActivity(), KodeinAware {
    var myCoverPhoto: File? = null
    var coverUri: Uri? = null
    var myProfilePhoto: File? = null
    var profileUri: Uri? = null

    var timeline: TextView? = null
    var photos: TextView? = null
    var videos: TextView? = null
    private val session: SessionManager by instance()


    private val REQ_PER_GALLERY_PROFILE = 1
    private val REQ_PER_GALLERY_COVER = 2

    var profile_rv: RecyclerView? = null
    var profilePostAdapter: Profile_Post_Adapter? = null
    var photosVideoAdapter: Photos_Video_Adapter? = null
    var home_post_list = ArrayList<Home_Post_Modal>()

    var photos_video_list = ArrayList<Photo_Video_Modal>()

    var addFollowLayout: LinearLayout? = null
    var follow_friend: TextView? = null
    var add_friend: TextView? = null
    var menu_friend: TextView? = null


    var frndProfileurl: String=""
    var frndCoverurl: String=""


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
    var videomodelfactory: ViewModelProvider_Session? = null
    var mySessionVM: MySessionVM? = null


    override val kodein by kodein()
    //get factory depencey from outside using kodein framework
    private val factory: ViewModelFactoryC by instance()


    var postUserId: String? = null
    var postUserName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)



        postUserId = intent.getStringExtra("postUserId")
        postUserName = intent.getStringExtra("postUserName")

        println("Profile_Activity")
        println("postUserId"+postUserId)
        println("postUserName"+postUserName)

        initViews()
        checkWHoseProfile()


        //set Profile Details
        callFetchProfileAPI()

        //call Timeline
        callFetchTimelinAPI()


        set_Time_Post_Adapter()
    }

    private fun callFetchTimelinAPI() {
        try {
            viewmodel.fetchPostTimelinetvm(postUserId!!,"0")
                .observe(this, Observer { resultPi ->

                    //storepostDataTOModal(resultPi, from)

                })
        } catch (e: Exception) {
            e.printStackTrace()

        }



    }

    private fun callFetchProfileAPI() {


        try {
            viewmodel.fetchProfileVM(URL.userId,postUserId!!)
                .observe(this, Observer { resultPi ->

                    setProfileData(resultPi)




                })
        } catch (e: Exception) {
            e.printStackTrace()
            visibleLoadingAnimation(false)

        }

    }

    private fun setProfileData(result: Profile_Detail_Modal?) {




        var status=""

        frndProfileurl=result!!.basic_info.get(0).profile_picture
        frndCoverurl=result!!.basic_info.get(0).cover_photo


        //work status
       if(result.work_details.size>0)
           work_status.text =HtmlCompat.fromHtml(result!!.work_details.get(0).position.capitalize()+" at "+"<b>"+ result!!.work_details.get(0).company_name.capitalize()+"</b> ", HtmlCompat.FROM_HTML_MODE_LEGACY)
       else
           goneView(work_status!!,false)


        //college status
        if(result.user_university_details.size>0)
        {
            status = if(result.user_university_details.get(0).graduated.equals("1"))
                "Studied "
            else
                "Study "

            college_status.text =HtmlCompat.fromHtml(status+ result!!.user_university_details.get(0).course.capitalize()+" at "+"<b>"+result.user_university_details.get(0).university.capitalize()+"</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)

        }
        else
            goneView(college_status!!,false)


        //school staus
       if(result.school_details.size>0)
           school_status.text =HtmlCompat.fromHtml("Went to "+"<b>"+ result!!.school_details.get(0).school.capitalize()+"</b> ", HtmlCompat.FROM_HTML_MODE_LEGACY)
        else
            goneView(school_status!!,false)


        //relationship status
        if(result.relationship_status.size>0)
            relatino_status.text = result!!.relationship_status.get(0).relationship_status.capitalize()
        else
            goneView(relatino_status!!,false)



        visibleLayout(userInfoLayout)

        setImagetoGLide(URL.profilePicPath + result!!.basic_info.get(0).profile_picture,iv_profile_image_profile!!,R.drawable.placeholder_profile)
        setImagetoGLide(URL.coverPicPath + result!!.basic_info.get(0).cover_photo,iv_cover_image_profile!!,R.drawable.placeholder)



    }

    private fun checkWHoseProfile() {


        //self profile
        if (postUserId.equals(URL.userId)) {
            goneLayout(addFollowLayout)

            user_name!!.text = URL.fullName.capitalize()

            //set User's pics from session
            setUserDataWithImages()

        }
        //friend profile
        else {
            goneLayout(txt_changeCover)
            goneLayout(txt_changeProfile)
            visibleLayout(addFollowLayout)
            user_name!!.text = postUserName!!.capitalize()
        }





    }

    private fun goneLayout(view: View?) {
        view!!.visibility = View.GONE

    }

    private fun visibleLayout(view: View?) {
        view!!.visibility = View.VISIBLE

    }


    private fun initViews() {


        goneLayout(userInfoLayout)

        //this factory method will create and return one object
        videomodelfactory = ViewModelProvider_Session(MySessionVM.instance)
        mySessionVM = ViewModelProvider(this, videomodelfactory!!).get(MySessionVM::class.java)


        viewmodel = ViewModelProviders.of(this, factory).get(Profile_VM::class.java)

        upload_loading_animLogin = findViewById(R.id.upload_loading_animLogin)
        timeline = findViewById(R.id.timeline)
        photos = findViewById(R.id.photos)
        videos = findViewById(R.id.videos)

        profile_rv = findViewById(R.id.profile_rv)




        //add follow layout
        addFollowLayout = findViewById(R.id.addFollowLayout)
        follow_friend = findViewById(R.id.follow_friend)
        add_friend = findViewById(R.id.add_friend)
        menu_friend = findViewById(R.id.menu_friend)



        user_name = findViewById<TextView>(R.id.user_name)


        txt_changeCover = findViewById<TextView>(R.id.txt_changeCover)
        txt_changeProfile = findViewById<TextView>(R.id.txt_changeProfile)
        txt_doneChangeProfile = findViewById<TextView>(R.id.txt_doneChangeProfile)
        txt_doneChangeCover = findViewById<TextView>(R.id.txt_doneChangeCover)

        iv_cover_image_profile = findViewById<ImageView>(R.id.iv_cover_image_profile)
        iv_profile_image_profile = findViewById<ImageView>(R.id.iv_profile_image_profile)





        val observable1 = RxView.clicks(txt_changeProfile!!).map<Any> { o: Any? -> txt_changeProfile }
        val observable2 = RxView.clicks(txt_changeCover!!).map<Any> { o: Any? -> txt_changeCover }
        val observable3 =
            RxView.clicks(txt_doneChangeProfile!!).map<Any> { o: Any? -> txt_doneChangeProfile }
        val observable4 =
            RxView.clicks(txt_doneChangeCover!!).map<Any> { o: Any? -> txt_doneChangeCover }

        val observable5 = RxView.clicks(timeline!!).map<Any> { o: Any? -> timeline }
        val observable6 = RxView.clicks(photos!!).map<Any> { o: Any? -> photos }
        val observable7 = RxView.clicks(videos!!).map<Any> { o: Any? -> videos }


        val observable8 = RxView.clicks(iv_profile_image_profile!!)
            .map<Any> { o: Any? -> iv_profile_image_profile }
        val observable9 =
            RxView.clicks(iv_cover_image_profile!!).map<Any> { o: Any? -> iv_cover_image_profile }



        setClickListener(
            observable1,
            observable2,
            observable3,
            observable4,
            observable8,
            observable9
        )

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


        val user: HashMap<String, String?> = session.userDetails
        setUseDataTOVIew(user)



        mySessionVM!!.getName()!!.observe(this, Observer { resultPi ->


            setUseDataTOVIew(resultPi)


        })


    }


    private fun setUseDataTOVIew(result: HashMap<String, String?>) {


        setImagetoGLide(URL.profilePicPath + URL.profilePic,iv_profile_image_profile!!,R.drawable.placeholder_profile)
        setImagetoGLide(URL.coverPicPath + URL.coverPic,iv_cover_image_profile!!,R.drawable.placeholder)



    }

    private fun setImagetoGLide(url: String, view:ImageView, placeholder: Int) {



        Glide.with(baseContext)
            .load(url)
            .apply(RequestOptions().placeholder(placeholder))
            .thumbnail(0.01f).into(view!!)

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
        observable4: Observable<Any>,
        observable8: Observable<Any>,
        observable9: Observable<Any>
    ) {

        //set click listener
        val disposable = Observable.mergeArray(
            observable1,
            observable2,
            observable3,
            observable4,
            observable8,
            observable9
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { o ->

                if (o == txt_changeProfile || o == iv_profile_image_profile) {

                    //for self
                    if (postUserId.equals(URL.userId))
                        showProfileBottomSheet("profile")
                    //for friend
                    else {




                        if(frndProfileurl.equals(""))
                        openPic(arrayListOf(URL.profilePicPath + URL.profilePic))
                        else
                            openPic(arrayListOf(URL.profilePicPath +frndProfileurl))

                    }


                } else if (o == txt_changeCover || o == iv_cover_image_profile) {
                    //  askToUserChangeProfilePic("cover")

                    //for self

                    if (postUserId.equals(URL.userId))
                        showProfileBottomSheet("cover")

                    //for friend
                    else {
                        if(frndCoverurl.equals(""))
                            openPic(arrayListOf(URL.coverPicPath + URL.coverPic))
                        else
                            openPic(arrayListOf(URL.coverPicPath +frndCoverurl))

                    }


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

    private fun showProfileBottomSheet(from: String) {
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


                }
                else if(o==changeProfile)
                {
                    if (from.equals("cover"))
                    checkPermissions("cover")
                    else
                        checkPermissions("profile")


                }

            }



        sheetDialog.show()

    }

    private fun openPic(url: ArrayList<String>) {

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

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


    }

    private fun enableLayout() {

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);



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



        photosVideoAdapter = Photos_Video_Adapter(from, applicationContext)
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

    fun goto_More_Info(view: View) {


    }
}
