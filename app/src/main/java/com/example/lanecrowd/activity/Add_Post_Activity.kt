package com.example.lanecrowd.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lanecrowd.R
import com.example.lanecrowd.adapter.Show_Selected_File_Adapter
import com.example.lanecrowd.util.AppController
import com.example.lanecrowd.util.ImageFilePath
import com.example.lanecrowd.util.RuntimePermissionsActivity
import com.example.lanecrowd.util.URL
import com.example.lanecrowd.view_modal.AddPostVM
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.xw.repo.VectorCompatTextView
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class Add_Post_Activity : RuntimePermissionsActivity() {


    private var gallery: Boolean = false
    private var camera: Boolean = false
    internal lateinit var appController: AppController
    lateinit var viewmodel: AddPostVM

    companion object {
        var isImage: Boolean = false
    }


    private val REQ_PER_GALLERY = 10
    private val REQ_PER_CAMERA = 30
    private val REQ_PER_GALLERY_VIDEO = 20
    private val REQ_PER_CAMERA_VIDEO = 40


    internal var rv_video_list = java.util.ArrayList<String>()
    internal var format_path: String = ""
    internal var files = java.util.ArrayList<File>()
    private var mCurrentPhotoPath: String? = null
    internal var imageFilePath: String = ""
    private var videoFilePath: String? = null


    var tag_friend: TextView? = null
    var post_loading_anim: LottieAnimationView? = null
    var selected_layout: LinearLayout? = null
    var openCameraChooser: TextView? = null
    var openPhotoVideoChooser: TextView? = null
    var userName: TextView? = null
    var post_title: TextView? = null
    var userImage: CircleImageView? = null
    var addPostBtn: TextView? = null

    var rv_addFiles: RecyclerView? = null
    var adapter: Show_Selected_File_Adapter? = null
    var from: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add__post_)



        from = intent.getStringExtra("from")
        println("add_post_activity_oncreate" + from)

        initViews()
    }


    @SuppressLint("WrongConstant")
    private fun initViews() {


        viewmodel = ViewModelProviders.of(this).get(AddPostVM::class.java)


        appController = applicationContext as AppController

        post_title = findViewById(R.id.post_title)
        userName = findViewById(R.id.user_name)
        userImage = findViewById(R.id.user_image)
        addPostBtn = findViewById(R.id.addPostBtn)

        setNameandImage()


        selected_layout = findViewById(R.id.selected_layout)
        tag_friend = findViewById(R.id.tag_friend)
        post_loading_anim = findViewById(R.id.post_loading_animLogin)
        openCameraChooser = findViewById(R.id.openCameraChooser)
        openPhotoVideoChooser = findViewById(R.id.openPhotoVideoChooser)


        rv_addFiles = findViewById<RecyclerView>(R.id.rv_addFiles)
        adapter = Show_Selected_File_Adapter(rv_video_list, this)
        rv_addFiles!!.adapter = adapter
        rv_addFiles!!.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayout.HORIZONTAL, false)


        val observable1 =
            RxView.clicks(openPhotoVideoChooser!!).map<Any> { o: Any -> openPhotoVideoChooser }
        val observable2 =
            RxView.clicks(openCameraChooser!!).map<Any> { o: Any -> openCameraChooser }
        val observable3 = RxView.clicks(addPostBtn!!).map<Any> { o: Any -> addPostBtn }




        setPhotoVideoListener(observable1, observable2, observable3)


        if (from.equals("story")) {
            tag_friend!!.visibility = View.GONE
            post_title!!.text = "Add Story"
            findViewById<EditText>(R.id.status_input)!!.visibility = View.GONE
        }


    }


    private fun setPhotoVideoListener(
        observable1: Observable<Any>,
        observable2: Observable<Any>,
        observable3: Observable<Any>
    ) {

        //set click listener
        val disposable = Observable.mergeArray(observable1, observable2, observable3)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { o ->

                if (o == openPhotoVideoChooser)
                    showPhotoVideoBottomSheet("photo")
                else if (o == openCameraChooser)
                    showPhotoVideoBottomSheet("camera")
                else if (o == addPostBtn)
                    upload_Post()


            }


    }

    private fun enableDisableButton(value: Boolean) {
        addPostBtn!!.isEnabled = value
    }


    private fun showPhotoVideoBottomSheet(from: String) {
        var sheetDialog = BottomSheetDialog(this@Add_Post_Activity)
        val sheetView =
            LayoutInflater.from(this@Add_Post_Activity)
                .inflate(R.layout.photovideobottomsheet, null)
        sheetDialog.setContentView(sheetView)

        var uploadPhoto = sheetView.findViewById<VectorCompatTextView>(R.id.uploadPhoto)
        var uploadVideo = sheetView.findViewById<VectorCompatTextView>(R.id.uploadVideo)


        val observable1 = RxView.clicks(uploadPhoto!!).map<Any> { o: Any? -> uploadPhoto }
        val observable2 = RxView.clicks(uploadVideo!!).map<Any> { o: Any? -> uploadVideo }


        val disposable = Observable.merge(observable1, observable2)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { o ->

                sheetDialog.dismiss()

                if (o == uploadPhoto) {


                    if (!format_path.equals("") && format_path.contains("video"))
                        showSnackBar("You can't select photo and video at the same time")
                    else
                        choosePhoto()

                } else if (o == uploadVideo) {
                    if (!format_path.equals("") && format_path.contains("image"))
                        showSnackBar("You can't select photo and video at the same time")
                    else
                        openGalleryVideo()
                }

            }



        sheetDialog.show()

    }

    private fun setNameandImage() {

        userName!!.text = URL.fullName.capitalize()

        Glide.with(baseContext)
            .load(URL.profilePicPath + URL.profilePic)
            .apply(RequestOptions().placeholder(R.drawable.placeholder_profile))
            .thumbnail(0.01f).into(userImage!!)
    }


    fun choose_Status(view: View) {

        startActivity(
            Intent(
                applicationContext,
                Choose_Status_Activity::class.java
            ).putExtra("from", from)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        )

    }


    override fun onPermissionsGranted(requestCode: Int) {

        if (requestCode == REQ_PER_GALLERY) {
            openGallery()
            isImage = true
        } else if (requestCode == REQ_PER_CAMERA) {
            openCameraImages()
            isImage = true
        } else if (requestCode == REQ_PER_GALLERY_VIDEO) {
            openGalleryVideo()
            isImage = false
        } else if (requestCode == REQ_PER_CAMERA_VIDEO) {
            openCameraVideo()
            isImage = false
        }

    }

    //for upload image
    //1
    fun choosePhoto() {
        if (ContextCompat.checkSelfPermission(
                this@Add_Post_Activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            super@Add_Post_Activity.requestAppPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), R.string.runtimepermission_txt, REQ_PER_GALLERY
            )
        } else {
            openGallery()
            isImage = true
        }
    }


    fun open_video_gallery() {
        if (ContextCompat.checkSelfPermission(
                this@Add_Post_Activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            super@Add_Post_Activity.requestAppPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), R.string.runtimepermission_txt, REQ_PER_GALLERY_VIDEO
            )
        } else {
            openGalleryVideo()
            isImage = false
        }
    }


    //for camera video
    //1
    fun open_video_camera() {
        if (ContextCompat.checkSelfPermission(
                this@Add_Post_Activity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this@Add_Post_Activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            super@Add_Post_Activity.requestAppPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), R.string.runtimepermission_txt, REQ_PER_CAMERA_VIDEO
            )
        } else {
            openCameraVideo()
            isImage = false
        }
    }

    //2
    private fun openCameraVideo() {

        isImage = false
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        val pictureIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if (pictureIntent.resolveActivity(packageManager) != null) {
            //Create a file to store the image
            var photoFile: File? = null
            try {
                photoFile = createvideoFile()
            } catch (ex: IOException) {

            }

            if (photoFile != null) {
                val ctx = applicationContext

                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                startActivityForResult(pictureIntent, 4)

            }
        }

    }


    fun openGallery() {

        if (ContextCompat.checkSelfPermission(
                this@Add_Post_Activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            super@Add_Post_Activity.requestAppPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), R.string.runtimepermission_txt, REQ_PER_GALLERY_VIDEO
            )
        } else
            startActivityForResult(
                Intent.createChooser(
                    Intent().setType("image/*").putExtra(
                        Intent.EXTRA_ALLOW_MULTIPLE,
                        true
                    ).setAction(Intent.ACTION_GET_CONTENT), "Select Images"
                ), 1
            )
    }


    fun openGalleryVideo() {
        if (ContextCompat.checkSelfPermission(
                this@Add_Post_Activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            super@Add_Post_Activity.requestAppPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), R.string.runtimepermission_txt, REQ_PER_GALLERY_VIDEO
            )
        } else
            startActivityForResult(
                Intent.createChooser(
                    Intent().setType("video/*").putExtra(
                        Intent.EXTRA_ALLOW_MULTIPLE,
                        true
                    ).setAction(Intent.ACTION_GET_CONTENT), "Select Video"
                ), 2
            )
    }


    //3
    @Throws(IOException::class)
    private fun createvideoFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "Vid_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".mp4", /* suffix */
            storageDir      /* directory */
        )
        // videoFilePath = "file:" + image.getAbsolutePath();
        videoFilePath = image.absolutePath
        return image
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        try {


            println("if_kaif_requestcode$requestCode")
            println("kaif_resultCode$resultCode")
            println("kaif_data${data!!.data}")
            //println("ClipData" + cd!!)





            //for gallery image
            if (requestCode == 1 && resultCode == RESULT_OK) {
                // from gallery
                val cd = data.clipData

                isImage = true



                if (cd != null) {
                    for (i in 0 until cd.itemCount) {
                        rv_video_list.add("" + cd.getItemAt(i).uri)

                        println("bbb1" + rv_video_list.get(0))
                        println("bbb2" + Uri.parse(rv_video_list[i]))


                        format_path = data.toString()

                        files.add(
                            File(
                                ImageFilePath.getPath(
                                    baseContext,
                                    Uri.parse(rv_video_list[i])
                                )
                            )
                        )
                        println("bbb3" + files.get(0))
                    }
                } else {

                    format_path = data.toString()
                    if (data.data != null) {
                        rv_video_list.add("" + data.data!!)
                        files.add(
                            File(
                                ImageFilePath.getPath(
                                    baseContext,
                                    Uri.parse(rv_video_list[0])
                                )
                            )
                        )
                    }


                }
                //disable Camera TextView
                disableCameraTextView(true, false)

                notifyAdapter()
            }

            //for camera images
            else if (requestCode == 3 && resultCode == RESULT_OK) {


                format_path = data.toString()
                isImage = true
                //disable Photo/Video TextView
                disableCameraTextView(false, false)

                format_path = imageFilePath
                println("camera_data_image" + imageFilePath)
                //uris.add(Uri.parse(imageFilePath));
                rv_video_list.add(imageFilePath)
                files.add(File(imageFilePath))
                //files.add(new File(imageFilePath));
                //txt_share.setTextColor(Color.parseColor("#000000"));
                notifyAdapter()
            }

            //for gallery video
            else if (requestCode == 2) {

                isImage = false
                val cd = data.clipData


                //for multiple videos

                if (cd != null) {

                    //for from add story
                    if(from.equals("story"))
                    {
                        var flag=false


                        for (i in 0 until cd.itemCount) {

                            if(getVideoDuration(Uri.parse(""+cd.getItemAt(i).uri))>30) {
                               flag=true
                                break
                            }

                        }
                        if(flag)
                            showSnackBar("You can only upload 30 second story video")

                        else
                        {

                            format_path = data.toString()
                            for (i in 0 until cd.itemCount) {
                                rv_video_list.add("" + cd.getItemAt(i).uri)
                                files.add(
                                    File(
                                        ImageFilePath.getPath(
                                            applicationContext,
                                            Uri.parse(rv_video_list[i])
                                        )
                                    )
                                )

                            }

                            //disable Camera TextView
                            disableCameraTextView(true, false)
                        }



                    }

                    //for from add post

                    else
                    {

                        format_path = data.toString()
                        for (i in 0 until cd.itemCount) {
                            rv_video_list.add("" + cd.getItemAt(i).uri)
                            files.add(
                                File(
                                    ImageFilePath.getPath(
                                        applicationContext,
                                        Uri.parse(rv_video_list[i])
                                    )
                                )
                            )

                        }

                        //disable Camera TextView
                        disableCameraTextView(true, false)


                    }
                }

                //for single video
                else {


                    //for from add story
                    if(from.equals("story"))
                    {

                        //check if video duration is greater than 30
                       if(getVideoDuration(Uri.parse(""+data.data))>30)
                       {
                           showSnackBar("You can only upload 30 second story video")

                       }
                        else
                       {
                           format_path = data.toString()
                           rv_video_list.add("" + data.data!!)
                           files.add(
                               File(
                                   ImageFilePath.getPath(
                                       applicationContext,
                                       Uri.parse(rv_video_list[0])
                                   )
                               )
                           )

                           //disable Camera TextView
                           disableCameraTextView(true, false)

                       }

                    }
                    //for from add post
                    else
                    {
                        format_path = data.toString()
                        rv_video_list.add("" + data.data!!)
                        files.add(
                            File(
                                ImageFilePath.getPath(
                                    applicationContext,
                                    Uri.parse(rv_video_list[0])
                                )
                            )
                        )

                        //disable Camera TextView
                        disableCameraTextView(true, false)
                    }




                }


                notifyAdapter()
            }
            //for camera video
            else if (requestCode == 4) {

                format_path = data.toString()
                isImage = false
                //disable Photo/Video TextView
                disableCameraTextView(false, false)

                println("camera_data_video" + videoFilePath)
                format_path = videoFilePath!!

                rv_video_list.add(videoFilePath!!)
                files.add(File(videoFilePath))
                notifyAdapter()

            }


        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun getVideoDuration(parse: Uri): Long {

        var mp = MediaPlayer.create(this, parse)
        var duration = mp.duration

        println("duratinOfvid"+TimeUnit.MILLISECONDS.toSeconds(duration.toLong()))


        return TimeUnit.MILLISECONDS.toSeconds(duration.toLong())


    }

    private fun notifyAdapter() {

        if (rv_video_list.size > 0)
            visibleRVlayout(true)
        else
            visibleRVlayout(false)


        adapter!!.notifyDataSetChanged()


    }

    private fun visibleRVlayout(value: Boolean) {

        if (value)
            selected_layout!!.visibility = View.VISIBLE
        else
            selected_layout!!.visibility = View.GONE
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        val image = File.createTempFile(
            imageFileName, // prefix
            ".jpg", // suffix
            storageDir      // directory
        )

        // Save a file: path for use with ACTION_VIEW intents
        // mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        mCurrentPhotoPath = image.absolutePath
        //imageFilePath = image.getAbsolutePath();
        imageFilePath = mCurrentPhotoPath as String
        return image
    }


    //for camera image
    //1
    fun open_image_camera() {
        if (ContextCompat.checkSelfPermission(
                this@Add_Post_Activity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this@Add_Post_Activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            super@Add_Post_Activity.requestAppPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), R.string.runtimepermission_txt, REQ_PER_CAMERA
            )
        } else {
            openCameraImages()
            isImage = true
        }

    }

    //2
    private fun openCameraImages() {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                val ctx = applicationContext
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                startActivityForResult(cameraIntent, 3)
            }
        }

    }


    private fun showSnackBar(msg: String) {

        val snackbar = Snackbar.make(findViewById(R.id.add_root), msg, Snackbar.LENGTH_LONG)
        snackbar.setBackgroundTint(ContextCompat.getColor(applicationContext, R.color.red))
        snackbar.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
        snackbar.show()

    }

    fun openPhotoVideoChooser() {


        gallery = true





        if (this@Add_Post_Activity != null) {
            this@Add_Post_Activity.runOnUiThread(Runnable {
                val dialog = Dialog(this@Add_Post_Activity, R.style.RoundShapeTheme)
                dialog.setCancelable(true)
                val inflater = layoutInflater
                val dialogLayout: View = inflater.inflate(R.layout.photo_video_aler, null)
                dialog.setContentView(dialogLayout)
                val close_box: TextView = dialogLayout.findViewById(R.id.close_box)
                val photo: Button = dialogLayout.findViewById(R.id.photo)
                val video: Button = dialogLayout.findViewById(R.id.video)


                close_box.setOnClickListener(View.OnClickListener { dialog.dismiss() })

                photo.setOnClickListener(View.OnClickListener {


                    if (!format_path.equals("") && format_path.contains("video"))
                        showSnackBar("You can't select photo and video at the same time")
                    else
                        choosePhoto()

                    dialog.dismiss()

                })

                video.setOnClickListener(View.OnClickListener {


                    if (!format_path.equals("") && format_path.contains("image"))
                        showSnackBar("You can't select photo and video at the same time")
                    else
                        openGalleryVideo()

                    dialog.dismiss()

                })



                dialog.show()
                /*val window = dialog.window
                window!!.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )*/
            })

        }

    }

    private fun disableCameraTextView(value: Boolean, visibleBoth: Boolean) {


        if (!visibleBoth) {
            //clicked on Photo/Video
            if (value) {

                openPhotoVideoChooser!!.isEnabled = true
                openCameraChooser!!.isEnabled = false

            } else {
                openPhotoVideoChooser!!.isEnabled = false
                openCameraChooser!!.isEnabled = true
            }
        } else {
            openPhotoVideoChooser!!.isEnabled = true
            openCameraChooser!!.isEnabled = true

        }


    }

    fun openCameraChooser() {

        camera = true




        if (this@Add_Post_Activity != null) {
            this@Add_Post_Activity.runOnUiThread(Runnable {
                val dialog = Dialog(this@Add_Post_Activity, R.style.RoundShapeTheme)
                dialog.setCancelable(true)
                val inflater = layoutInflater
                val dialogLayout: View = inflater.inflate(R.layout.photo_video_aler, null)
                dialog.setContentView(dialogLayout)
                val close_box: TextView = dialogLayout.findViewById(R.id.close_box)
                val photo: Button = dialogLayout.findViewById(R.id.photo)
                val video: Button = dialogLayout.findViewById(R.id.video)


                close_box.setOnClickListener(View.OnClickListener { dialog.dismiss() })


                println("sallu" + format_path)
                photo.setOnClickListener(View.OnClickListener {


                    if (!format_path.equals("") && format_path.contains("video") || checkVideoFormat())
                        showSnackBar("You can't select photo and video at the same time")
                    else
                        openCameraImages()

                    dialog.dismiss()
                })

                video.setOnClickListener(View.OnClickListener {

                    if (!format_path.equals("") && format_path.contains("image") || checkImageFormat())
                        showSnackBar("You can't select photo and video at the same time")
                    else
                        openCameraVideo()


                    dialog.dismiss()

                })

                dialog.show()
/*
                dialog.show()
                val window = dialog.window
                window!!.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )*/
            })

        }

    }

    private fun checkImageFormat(): Boolean {
        if (format_path.contains(".jpg") ||
            format_path.contains(".png") ||
            format_path.contains(".jpeg") ||
            format_path.contains(".bmp")
        )

            return true

        return false
    }


    private fun checkVideoFormat(): Boolean {


        if (format_path.contains(".mp4") ||
            format_path.contains(".3gp") ||
            format_path.contains(".mkv") ||
            format_path.contains(".webm")
        )

            return true

        return false
    }

    fun removeItem(position: Int) {


        if (rv_video_list.size == 1) {
            format_path = ""
            gallery = false
            camera = false
            disableCameraTextView(false, true)

        }

        rv_video_list.removeAt(position)
        files.removeAt(position)
        notifyAdapter()


    }


    fun upload_Post() {


        hideSoftKeyBoard()

        if (rv_video_list.size <= 0 && findViewById<EditText>(R.id.status_input).text.toString().length <= 0) {

        } else {


            try {

                enableDisableButton(false)


                visibleLoadingAnimation(true)

                //add post
                viewmodel.addPostvm(
                    from!!,
                    "",
                    findViewById<EditText>(R.id.status_input).text.toString(),
                    files,
                    isImage,
                    applicationContext
                ).observe(this, Observer { resultPi ->

                    println("add_post" + resultPi)

                    enableDisableButton(true)

                    if (resultPi != null && resultPi.getString("status").equals("1")) {
                        visibleLoadingAnimation(false)
                        finish()
                    } else {
                        visibleLoadingAnimation(false)
                    }


                })


            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }


        }


    }


    private fun visibleLoadingAnimation(value: Boolean) {

        if (value) {
            post_loading_anim!!.visibility = View.VISIBLE
            post_loading_anim!!.playAnimation()

        } else {
            post_loading_anim!!.visibility = View.GONE
            post_loading_anim!!.pauseAnimation()
        }


    }

    private fun askTOExit() {


        MaterialAlertDialogBuilder(this@Add_Post_Activity, R.style.AlertDialogTheme)
            .setTitle("LaneCrowd")
            .setMessage("Are you sure want to exit?")
            .setNegativeButton("No") { dialogInterface, i ->
            }
            .setPositiveButton("yes") { dialogInterface, i ->

                viewmodel.cancleNetworkCall()
                super.onBackPressed()
            }
            .show()


    }

    fun back_activity(view: View) {
        if (post_loading_anim!!.isAnimating)
            askTOExit()
        else
            super.onBackPressed()
    }


    override fun onBackPressed() {

        if (post_loading_anim!!.isAnimating)
            askTOExit()
        else
            super.onBackPressed()


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


}
