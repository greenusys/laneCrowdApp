package com.example.lanecrowd.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.lanecrowd.R
import com.example.lanecrowd.Session_Package.SessionManager
import com.example.lanecrowd.util.URL
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import jp.shts.android.storiesprogressview.StoriesProgressView
import java.util.concurrent.TimeUnit


class View_Story_Activity : AppCompatActivity(), StoriesProgressView.StoriesListener {

    private var storiesProgressView: StoriesProgressView? = null
    private var image: ImageView? = null
    private var counter = 0
    private val resources = intArrayOf(
        R.drawable.test,
        R.drawable.login,
        R.drawable.login_1,
        R.drawable.test,
        R.drawable.login_2,
        R.drawable.test
    )
    private val durations = longArrayOf(
        500L, 1000L, 1500L, 4000L, 5000L, 1000
    )
    var pressTime = 0L
    var limit = 500L

    var files: ArrayList<String>? = null

    var send_reply_Button: ImageView? = null
    var reply_comment: EditText? = null
    var staus_video_icon: ImageView? = null
    var name_by: TextView? = null
    var image_by: CircleImageView? = null
    var user_image: CircleImageView? = null

    var name: String? = null
    var imageva: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view__story_)



        files = intent.extras!!.getStringArrayList("story_files")
        name = intent.extras!!.getString("name")
        imageva = intent.extras!!.getString("imageva")



        initViews()





    }



    private fun initViews() {





        send_reply_Button = findViewById<ImageView>(R.id.send_reply_Button)
        reply_comment = findViewById<EditText>(R.id.reply_comment)
        name_by = findViewById<TextView>(R.id.name_by)
        image_by = findViewById<CircleImageView>(R.id.image_by)
        user_image = findViewById<CircleImageView>(R.id.user_image)

        setImageAndName()


        storiesProgressView = findViewById<View>(R.id.stories) as StoriesProgressView
        storiesProgressView!!.setStoriesCount(files!!.size)
        storiesProgressView!!.setStoryDuration(3000L)
        // or
        // storiesProgressView.setStoriesCountWithDurations(durations);
        storiesProgressView!!.setStoriesListener(this)

        //storiesProgressView.startStories();
        counter = 0

        storiesProgressView!!.startStories(counter)
        image = findViewById<View>(R.id.image) as ImageView


        setStoryImage(counter,files!!.get(counter),image)

        // bind reverse view
        val reverse = findViewById<View>(R.id.reverse)
        reverse.setOnClickListener { storiesProgressView!!.reverse() }
        reverse.setOnTouchListener(onTouchListener)
        // bind skip view
        val skip = findViewById<View>(R.id.skip)
        skip.setOnClickListener { storiesProgressView!!.skip() }
        skip.setOnTouchListener(onTouchListener)



        //on text listener
        val d2: Disposable = RxTextView.textChanges(findViewById(R.id.reply_comment))
            .subscribe(object : Consumer<CharSequence?> {
                @Throws(Exception::class)
                override fun accept(charSequence: CharSequence?) { //Add your logic to work on the Charsequence

                    //pause story view
                    if(charSequence.toString().length>0)
                        pauseStoryProgressView(true)
                    else
                        pauseStoryProgressView(false)



                }
            })


        RxView.clicks(findViewById(R.id.send_reply_Button)).throttleFirst(2, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

                //call reply story api
            }








    }

    private fun setImageAndName() {


        //set profile pic's user name and image
        name_by!!.setText(name)
        setImageToGLide(URL.profilePicPath+imageva!!,image_by)


        //set self image
        setImageToGLide(URL.profilePicPath + URL.profilePic,user_image)


        println("imageby"+URL.profilePicPath+imageva)
        println("useriamge"+URL.profilePicPath + URL.profilePic)

    }

    private fun setImageToGLide(url: String, image: CircleImageView?) {

        Glide.with(baseContext)
            .load(url)
            .apply(RequestOptions().placeholder(R.drawable.placeholder_profile))
            .thumbnail(0.01f).into(image!!)
    }


    private fun checkIsImage(position: Int): Boolean {

        if(files!!.get(position).contains("jpg") || files!!.get(position).contains("png") || files!!.get(position).contains("jpeg"))
            return true

        return false

    }


    private fun playVideo(url: String) {

        //visible video view and gone image layout
        visiBleExoPLayerGOneImage(true)

        //pause the storyprogrss view
        pauseStoryProgressView(true)

        //play url when ready
        //andExoPlayerView!!.setSource("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")


    }

    private fun visiBleExoPLayerGOneImage(value: Boolean) {

            if(value)
            {
                image!!.setBackgroundResource(R.color.dark_grey2)
               // andExoPlayerView!!.visibility=View.VISIBLE

            }else
            {

                image!!.visibility=View.VISIBLE
            }
    }


    private fun setStoryImage(position: Int,path: String, layott: ImageView?) {


        var url:String=""

        if(checkIsImage(position)) {
            url = URL.storyImagePath
        }
        else {
            url = URL.storyVideoPath
           // playVideo(url+path)

        }


        Glide.with(applicationContext).load(url+path)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {

                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {

                    return false
                }
            })
            .apply(RequestOptions().placeholder(R.drawable.placeholder))

            .thumbnail(0.01f).into(layott!!).waitForLayout()



    }



    private fun pauseStoryProgressView(value: Boolean)
    {

        if (value)
            storiesProgressView!!.pause()
        else
            storiesProgressView!!.resume()


    }


    private val onTouchListener = View.OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pressTime = System.currentTimeMillis()
                storiesProgressView!!.pause()
                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {
                val now = System.currentTimeMillis()
                storiesProgressView!!.resume()
                return@OnTouchListener limit < now - pressTime
            }
        }
        false
    }




    override fun onNext() {



// image!!.setImageResource(resources[++counter])

        setStoryImage(++counter,files!!.get(counter),image)

    }


    override fun onPrev() {
        if (counter - 1 < 0) return


       // setStoryImage(--counter,files!!.get(counter),image)

        image!!.setImageResource(resources[--counter])
    }

    override fun onComplete() {
        onBackPressed()
    }
    override fun onDestroy() { // Very important !
        storiesProgressView!!.destroy()
        super.onDestroy()
    }




    fun back(view: View) {
        onBackPressed()
    }
}
