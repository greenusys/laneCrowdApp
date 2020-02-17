package com.example.lanecrowd.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.lanecrowd.R
import com.example.lanecrowd.Session_Package.ImageFilePath
import com.example.lanecrowd.Session_Package.RuntimePermissionsActivity
import com.example.lanecrowd.adapter.Show_Selected_File_Adapter
import com.example.lanecrowd.retrofit.AppController
import com.google.android.material.snackbar.Snackbar
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class Add_Post_Activity : RuntimePermissionsActivity() {


    private var gallery: Boolean = false
    private var camera: Boolean = false
    internal lateinit var appController: AppController


    private var isImage: Boolean = false
    private val REQ_PER_GALLERY = 10
    private val REQ_PER_CAMERA = 30
    private val REQ_PER_GALLERY_VIDEO = 20
    private val REQ_PER_CAMERA_VIDEO = 40


    internal var rv_video_list = java.util.ArrayList<String>()
    internal var format_path:String=""
    internal var files = java.util.ArrayList<File>()
    private var mCurrentPhotoPath: String? = null
    internal  var imageFilePath: String=""
    private var videoFilePath: String? = null


    var post_loading_anim: LottieAnimationView? = null
    var openCameraChooser: TextView? = null
    var openPhotoVideoChooser: TextView? = null

    var rv_addFiles: RecyclerView? = null
    var adapter: Show_Selected_File_Adapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add__post_)

        initViews()
    }


    @SuppressLint("WrongConstant")
    private fun initViews() {

        appController = applicationContext as AppController

        post_loading_anim = findViewById(R.id.post_loading_animLogin)
        openCameraChooser = findViewById(R.id.openCameraChooser)
        openPhotoVideoChooser = findViewById(R.id.openPhotoVideoChooser)


        rv_addFiles = findViewById<RecyclerView>(R.id.rv_addFiles)
        adapter = Show_Selected_File_Adapter(rv_video_list, this)
        rv_addFiles!!.adapter = adapter
        rv_addFiles!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayout.HORIZONTAL, false)
        adapter!!.notifyDataSetChanged()





        openPhotoVideoChooser!!.setOnClickListener(View.OnClickListener {

            openPhotoVideoChooser()

        })

        openCameraChooser!!.setOnClickListener(View.OnClickListener {

            openCameraChooser()

        })


    }


    fun choose_Status(view: View) {

        startActivity(Intent(applicationContext, Choose_Status_Activity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

    }


    override fun onPermissionsGranted(requestCode: Int) {

        if (requestCode == REQ_PER_GALLERY) {
            openGallery()
             isImage = false;
        } else if (requestCode == REQ_PER_CAMERA) {
            openCameraImages()
            isImage = true;
        } else if (requestCode == REQ_PER_GALLERY_VIDEO) {
            openGalleryVideo()
             isImage = false;
        } else if (requestCode == REQ_PER_CAMERA_VIDEO) {
            openCameraVideo()
            isImage = false
        }

    }

    //for upload image
    //1
    fun choosePhoto() {
        if (ContextCompat.checkSelfPermission(this@Add_Post_Activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            super@Add_Post_Activity.requestAppPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), R.string.runtimepermission_txt, REQ_PER_GALLERY)
        } else {
            openGallery()
            isImage = true
        }
    }


    fun open_video_gallery() {
        if (ContextCompat.checkSelfPermission(this@Add_Post_Activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            super@Add_Post_Activity.requestAppPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), R.string.runtimepermission_txt, REQ_PER_GALLERY_VIDEO)
        } else {
            openGalleryVideo()
            isImage = false
        }
    }


    //for camera video
    //1
    fun open_video_camera() {
        if (ContextCompat.checkSelfPermission(this@Add_Post_Activity,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this@Add_Post_Activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            super@Add_Post_Activity.requestAppPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), R.string.runtimepermission_txt, REQ_PER_CAMERA_VIDEO)
        } else {
            openCameraVideo()
            isImage = false
        }
    }

    //2
    private fun openCameraVideo() {
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
        startActivityForResult(Intent.createChooser(Intent().setType("image/*").putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true).setAction(Intent.ACTION_GET_CONTENT), "Select Images"), 1)
    }


    fun openGalleryVideo() {
        startActivityForResult(Intent.createChooser(Intent().setType("video/*").putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true).setAction(Intent.ACTION_GET_CONTENT), "Select Video"), 2)
    }


    //3
    @Throws(IOException::class)
    private fun createvideoFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "Vid_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)
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



            format_path=data.toString()



            //for gallery image
            if (requestCode == 1 && resultCode == RESULT_OK) {
                // from gallery
                val cd = data!!.clipData





                if (cd != null) {
                    for (i in 0 until cd.itemCount) {
                        rv_video_list.add("" + cd.getItemAt(i).uri)
                        files.add(File(ImageFilePath.getPath(baseContext, Uri.parse(rv_video_list[i]))))

                    }
                } else {
                    if (data.data != null) {
                        rv_video_list.add("" + data.data!!)
                        files.add(File(ImageFilePath.getPath(baseContext, Uri.parse(rv_video_list[0]))))
                    }





                }
                //disable Camera TextView
                disableCameraTextView(true,false)
                // txt_share.setTextColor(Color.parseColor("#000000"));
                adapter!!.notifyDataSetChanged()
            }

            //for camera images
            else if (requestCode == 3 && resultCode == RESULT_OK) {

                //disable Photo/Video TextView
                disableCameraTextView(false,false)

                format_path=imageFilePath!!
                println("camera_data_image"+imageFilePath)
                //uris.add(Uri.parse(imageFilePath));
                rv_video_list.add(imageFilePath)
                files.add(File(imageFilePath))
                //files.add(new File(imageFilePath));
                //txt_share.setTextColor(Color.parseColor("#000000"));
                adapter!!.notifyDataSetChanged()
            }

            //for gallery video
            else if (requestCode == 2) {


                val cd = data!!.clipData
                if (cd != null) {
                    for (i in 0 until cd.itemCount) {
                        rv_video_list.add("" + cd.getItemAt(i).uri)
                        files.add(File(ImageFilePath.getPath(applicationContext, Uri.parse(rv_video_list[i]))))
                    }
                } else {
                    rv_video_list.add("" + data.data!!)
                    files.add(File(ImageFilePath.getPath(applicationContext, Uri.parse(rv_video_list[0]))))
                }

                //disable Camera TextView
                disableCameraTextView(true,false)
                adapter!!.notifyDataSetChanged()
            }
            //for camera video
            else if (requestCode == 4) {


                //disable Photo/Video TextView
                disableCameraTextView(false,false)

                println("camera_data_video"+videoFilePath)
                format_path=videoFilePath!!

                rv_video_list.add(videoFilePath!!)
                files.add(File(videoFilePath))
                adapter!!.notifyDataSetChanged()

            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }




    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)
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
        if (ContextCompat.checkSelfPermission(this@Add_Post_Activity,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this@Add_Post_Activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            super@Add_Post_Activity.requestAppPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), R.string.runtimepermission_txt, REQ_PER_CAMERA)
        } else {
            openCameraImages()
             isImage = true;
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

    fun back_activity(view: View) {
        onBackPressed()
    }


    private fun showSnackBar(msg:String) {

        val snackbar = Snackbar.make(findViewById(R.id.add_root), msg, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(ContextCompat.getColor(applicationContext, R.color.red)
        )
        snackbar.setTextColor(ContextCompat.getColor(applicationContext, R.color.white)
        )
        snackbar.show()

    }

    fun openPhotoVideoChooser() {


        gallery=true





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
                        showSnackBar("You can't select photo video at the same time")
                    else
                    choosePhoto()

                    dialog.dismiss()

                })

                video.setOnClickListener(View.OnClickListener {



                    if (!format_path.equals("") && format_path.contains("image"))
                        showSnackBar("You can't select photo video at the same time")
                    else
                        openGalleryVideo()

                    dialog.dismiss()

                })



                dialog.show()
                val window = dialog.window
                window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            })

        }

    }

    private fun disableCameraTextView(value: Boolean,visibleBoth:Boolean) {


        if(!visibleBoth) {
            //clicked on Photo/Video
            if (value) {

                openPhotoVideoChooser!!.setEnabled(true)
                openCameraChooser!!.setEnabled(false)

            } else {
                openPhotoVideoChooser!!.setEnabled(false)
                openCameraChooser!!.setEnabled(true)
            }
        }
        else
        {
            openPhotoVideoChooser!!.setEnabled(true)
            openCameraChooser!!.setEnabled(true)

        }


    }

    fun openCameraChooser() {

        camera=true




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


                println("sallu"+format_path)
                photo.setOnClickListener(View.OnClickListener {


                    if (!format_path.equals("") && format_path.contains("video") || checkVideoFormat())
                        showSnackBar("You can't select photo video at the same time")
                    else
                        openCameraImages()

                    dialog.dismiss()
                })

                video.setOnClickListener(View.OnClickListener {

                    if (!format_path.equals("") && format_path.contains("image") || checkImageFormat())
                        showSnackBar("You can't select photo video at the same time")
                    else
                        openCameraVideo()


                    dialog.dismiss()

                })



                dialog.show()
                val window = dialog.window
                window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            })

        }

    }

    private fun checkImageFormat(): Boolean {
        if(format_path.contains(".jpg") ||
                format_path.contains(".png")||
                format_path.contains(".jpeg")||
                format_path.contains(".bmp"))

            return true

        return false
    }


 private fun checkVideoFormat(): Boolean {


     if(format_path.contains(".mp4") ||
             format_path.contains(".3gp")||
             format_path.contains(".mkv")||
             format_path.contains(".webm"))

         return true

     return false
    }

    fun removeItem(position: Int) {

        if(rv_video_list.size==1) {
            format_path = ""
            gallery = false
            camera=false
            disableCameraTextView(false,true)

        }

        rv_video_list.removeAt(position)
       adapter!!.notifyDataSetChanged()



    }




    private fun addPost(post: String?, imgageData: String, user_id: String,
                        files: ArrayList<File>, isImage: Boolean?) {





        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("android", "")
        builder.addFormDataPart("post_type", "1")
        builder.addFormDataPart("post", post)
        builder.addFormDataPart("imgageData", imgageData)
        builder.addFormDataPart("user_id", user_id)

        for (f in files) {

            val filePath = f.absolutePath
            val mediaType = MediaType.parse(if (isImage!!) "image/" else "video/" + filePath.substring(filePath.lastIndexOf(".") + 1))
            builder.addFormDataPart("files[]", filePath.substring(filePath.lastIndexOf("/") + 1),
            RequestBody.create(mediaType, f))
        }

        val request = Request.Builder().url("http://www.lanecrowd.com/addPost").post(builder.build()).build()


        appController.getOkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                this@Add_Post_Activity.runOnUiThread(Runnable {
                    Log.e("kaif_Excetpion-3", "skdj")
                    visibleLoadingAnimation(false)

                    Toast.makeText(baseContext, "Error$e", Toast.LENGTH_SHORT).show()
                })
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val myResponse = response.body()!!.string()
                this@Add_Post_Activity.runOnUiThread(Runnable {
                    try {
                        val jo = JSONObject(myResponse)

                        println("hsdf$jo")



                        if (jo.getString("status") == "1") {
                            visibleLoadingAnimation(false)
                            //setResult(RESULT_OK)
                           // finish()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        visibleLoadingAnimation(false)

                    }
                })
            }
        })
    }

    fun upload_Post(view: View) {


        if(rv_video_list!!.size<=0 && findViewById<EditText>(R.id.status_input).text.toString().length<=0)
        {

        }
        else
        {
            addPost(findViewById<EditText>(R.id.status_input).text.toString(),"","2",files,isImage)
            visibleLoadingAnimation(true)

        }



    }

    private fun visibleLoadingAnimation(value: Boolean) {

        if(value)
        {
            post_loading_anim!!.visibility=View.VISIBLE
            post_loading_anim!!.playAnimation()

        }
        else
        {
            post_loading_anim!!.visibility=View.GONE
            post_loading_anim!!.pauseAnimation()
        }
    }


}
