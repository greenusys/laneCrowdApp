package com.example.lanecrowd.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Choose_Status_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.adapter.Choose_Status_Adapter
import com.example.lanecrowd.util.ImageFilePath
import com.example.lanecrowd.view_modal.AddPostVM
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Choose_Status_Activity : AppCompatActivity() {

    var mainLayout: RelativeLayout? = null
    var theme: ImageView? = null

    var status_rv: RecyclerView? = null
    var adapter: Choose_Status_Adapter? = null

    var comment_list = ArrayList<Choose_Status_Modal>()

    lateinit var viewmodel: AddPostVM;
    internal var files = java.util.ArrayList<File>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose__status_)

        initViews()
    }

    @SuppressLint("WrongConstant")
    private fun initViews() {

        viewmodel = ViewModelProviders.of(this).get(AddPostVM::class.java!!)



        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())


        mainLayout = findViewById(R.id.mainLayout)
        theme = findViewById(R.id.theme)


        status_rv = findViewById<RecyclerView>(R.id.status_rv)
        adapter = Choose_Status_Adapter(comment_list,this)
        status_rv!!.adapter = adapter
        status_rv!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayout.HORIZONTAL, false)
        adapter!!.notifyDataSetChanged()

    }

    fun back_activity(view: View) {

        onBackPressed()
    }

    fun setTheme() {


    }

    fun saveTheme(view: View) {

        if (findViewById<EditText>(R.id.theme_txt).text.toString().length <= 0) {
        }
        else
        {




        val savingLayout = findViewById(R.id.lay_theme) as RelativeLayout
        val file = saveBitMap(this@Choose_Status_Activity, savingLayout)
        if (file != null) {
            Log.i("TAG", "Drawing saved to the gallery!")

            try {


                if(files!=null)
                    files.clear()

                files.add(file)




                viewmodel.addPostvm("",findViewById<EditText>(R.id.theme_txt).text.toString(),files,true,applicationContext).observe(this, Observer { resultPi ->

                    println("add_post"+resultPi)





                })



            }catch (e:java.lang.Exception)
            {
                e.printStackTrace()
            }


        } else {
            Log.i("TAG", "Oops! Image could not be saved.")
        }



        }

    }


    private fun saveBitMap(context: Context, drawView: View): File? {
        val pictureFileDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Logicchip")
        if (!pictureFileDir.exists()) {
            val isDirectoryCreated = pictureFileDir.mkdirs()
            if (!isDirectoryCreated)
                Log.i("TAG", "Can't create directory to save the image")
            return null
        }

        println("one")
        val filename = pictureFileDir.path + File.separator + System.currentTimeMillis() + ".jpg"
        val pictureFile = File(filename)
        val bitmap = getBitmapFromView(drawView)
        println("two$bitmap")
        try {
            println("three")
            pictureFile.createNewFile()
            println("four")
            val oStream = FileOutputStream(pictureFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, oStream)
            oStream.flush()
            oStream.close()
        } catch (e: IOException) {
            println("five")
            e.printStackTrace()
            Log.i("TAG", "There was an issue saving the image.")
        }

        scanGallery(context, pictureFile.absolutePath)
        return pictureFile
    }





    //create bitmap from view and returns it
    private fun getBitmapFromView(view: View): Bitmap {
        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas)
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        }
        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        return returnedBitmap
    }


    // used for scanning gallery
    private fun scanGallery(cntx: Context, path: String) {
        try {
            MediaScannerConnection.scanFile(cntx, arrayOf(path), null) { path, uri -> }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("TAG", "There was an issue scanning gallery.")
        }

    }

}
