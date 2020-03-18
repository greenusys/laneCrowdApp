package com.rahuljanagouda.statusstories

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.example.lanecrowd.R
import com.example.lanecrowd.Session_Package.SessionManager
import com.example.lanecrowd.activity.Profile_Activity
import com.example.lanecrowd.util.StoryStatusView
import com.example.lanecrowd.util.URL
import com.example.lanecrowd.view_modal.MySessionVM
import com.example.lanecrowd.view_modal.factory.ViewModelProvider_Session
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.jakewharton.rxbinding2.view.RxView
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_view__story_.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import kotlin.time.seconds

class View_Status_Activity : AppCompatActivity(), KodeinAware, StoryStatusView.UserInteractionListener {
    private var image: ImageView? = null
    private var counter = 0
    private var storyStatusView: StoryStatusView? = null


    var files: ArrayList<String>? = null

    var postUserId: String? = null
    var name: String? = null
    var imageva: String? = null

    var pressTime = 0L
    var limit = 500L


    var exoPlayerView: SimpleExoPlayerView? = null
    var progressBar: ProgressBar? = null
    var exoPlayer: SimpleExoPlayer? = null
    var bandwidthMeter: BandwidthMeter? = null
    var trackSelector: TrackSelector? = null

    override val kodein by kodein()

    private val session: SessionManager by instance()

    //this factory method will create and return one object of SessionVM
    var videomodelfactory: ViewModelProvider_Session? = null
    var mySessionVM: MySessionVM? = null

    var send_reply_Button: ImageView? = null
    var reply_comment: EditText? = null
    var name_by: TextView? = null
    var image_by: CircleImageView? = null
    var user_image: CircleImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view__story_)



        files = intent.extras!!.getStringArrayList("story_files")
        postUserId = intent.extras!!.getString("postUserId")
        name = intent.extras!!.getString("name")
        imageva = intent.extras!!.getString("imageva")

        initViews()



    }

    private fun initVideosViews() {


        bandwidthMeter = DefaultBandwidthMeter()
        trackSelector = DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector)


    }


    private fun initViews() {


        //for video
        progressBar = findViewById(com.mzelzoghbi.zgallery.R.id.progressBar)
        exoPlayerView = findViewById(com.mzelzoghbi.zgallery.R.id.exo_player_view)

        initVideosViews()



        //this factory method will create and return one object
        videomodelfactory = ViewModelProvider_Session(MySessionVM.instance)
        mySessionVM = ViewModelProvider(this, videomodelfactory!!).get(MySessionVM::class.java)


        send_reply_Button = findViewById<ImageView>(R.id.send_reply_Button)
        reply_comment = findViewById<EditText>(R.id.reply_comment)
        name_by = findViewById<TextView>(R.id.name_by)
        image_by = findViewById<CircleImageView>(R.id.image_by)
        user_image = findViewById<CircleImageView>(R.id.user_image)

        setImageAndName()




        image = findViewById(R.id.image)
        storyStatusView = findViewById(R.id.storiesStatus)
        storyStatusView!!.setStoriesCount(files!!.size)
        storyStatusView!!.setStoryDuration(3000)
        storyStatusView!!.setUserInteractionListener(this)
        storyStatusView!!.playStories()
        // target = new MyProgressTarget<>(new BitmapImageViewTarget(image), imageProgressBar, textView);
        image!!.setOnClickListener(View.OnClickListener { storyStatusView!!.skip() })
        resume(false, "start")
        loadImages()





        // bind reverse view
        reverse.setOnClickListener { storyStatusView!!.reverse() }
        // bind skip view
        skip.setOnClickListener { storyStatusView!!.skip() }

        //set onhold listener
        skip.setOnTouchListener(onTouchListener)
        reverse.setOnTouchListener(onTouchListener)


        val observable1 = RxView.clicks(send_reply_Button!!).map<Any> { o: Any? -> send_reply_Button }
        val observable2 = RxView.clicks(image_by!!).map<Any> { o: Any? -> image_by }




        setClickListener(observable1, observable2)



    }

    private fun setClickListener(observable1: Observable<Any>, observable2: Observable<Any>) {

        //set click listener
        val disposable = Observable.mergeArray(observable1, observable2)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { o ->

                if (o == image_by)
                    gotoUserProfile()


            }


    }

    private fun gotoUserProfile() {
        startActivity(
            Intent(applicationContext, Profile_Activity::class.java)
                .putExtra("postUserId", postUserId)
                .putExtra("postUserName", name)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        )


    }


    private fun setImageAndName() {


        val user: HashMap<String, String?> = session.userDetails
        setUseDataTOVIew(user)



        mySessionVM!!.getName()!!.observe(this, Observer { resultPi ->


            setUseDataTOVIew(resultPi)


        })


    }

    private fun setUseDataTOVIew(result: HashMap<String, String?>) {

        //set profile pic's user name and image
        name_by!!.text = name


        setImageToGLide(URL.profilePicPath + imageva!!, image_by)


        //set self image
        setImageToGLide(
            URL.profilePicPath + result.get(SessionManager.KEY_PROFILE_PICTURE),
            user_image
        )


    }


    private fun setImageToGLide(url: String, image: CircleImageView?) {

        Glide.with(baseContext)
            .load(url)
            .apply(RequestOptions().placeholder(R.drawable.placeholder_profile))
            .thumbnail(0.01f).into(image!!)
    }


    private fun checkIsImage(position: Int): Boolean {

        if (files!!.get(position).contains("jpg") || files!!.get(position).contains("png") || files!!.get(position).contains("jpeg"))
            return true

        return false

    }



    private val onTouchListener = View.OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pressTime = System.currentTimeMillis()
                resume(false, "down")
                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {
                val now = System.currentTimeMillis()
                resume(true, "down")

                return@OnTouchListener limit < now - pressTime
            }
        }
        false
    }


    private fun loadImages() {


        var url = ""

        //if video
        if (!checkIsImage(counter)) {

            println("found_video")
            //pause story


            //visiBleExoPLayerGOneImage(true)


            resume(false, "playvideo")
            url = URL.storyVideoPath
            playVideo(url + files!![counter])
        }
        //if image
        else {

            exoPlayer!!.stop()
            exoPlayer!!.playWhenReady = true


            println("found_image")

            //visible image layout and gone video layout
            visiBleExoPLayerGOneImage(false)

            url = URL.storyImagePath


            Glide.with(applicationContext).load(url + files!![counter])
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: com.bumptech.glide.request.target.Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        resume(true, "failed")


                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any,
                        target: com.bumptech.glide.request.target.Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        resume(true, "success")

                        return false
                    }
                }).thumbnail(0.01f).into(image!!)
        }

    }

    private fun playVideo(url: String) {

        //visible video view and gone image layout
        visiBleExoPLayerGOneImage(true)



        playVideoURL(url)

    }

    private fun visiBleExoPLayerGOneImage(value: Boolean) {

        if (value) {
            exoPlayerView!!.visibility = View.VISIBLE
            image!!.setImageResource(R.color.colorPrimaryDark)

        } else {

            exoPlayerView!!.visibility = View.GONE

        }
    }

    private fun resume(b: Boolean, from: String) {
        println("kaif_from$from"+b)
        if (b)
            storyStatusView!!.resume()
        else
            storyStatusView!!.pause()
    }

    override fun onNext() {
        resume(false, "next")
        ++counter
        loadImages()
    }

    override fun onPrev() {
        if (counter - 1 < 0) return

        --counter

        resume(false, "next")
        loadImages()
    }


    private fun playVideoURL(url: String) {

        try {
            val videoURI = Uri.parse(url)
            val dataSourceFactory =
                DefaultHttpDataSourceFactory("exoplayer_video")
            val extractorsFactory: ExtractorsFactory = DefaultExtractorsFactory()
            val mediaSource: MediaSource =
                ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null)
            exoPlayerView!!.player = exoPlayer
            exoPlayer!!.prepare(mediaSource)
            exoPlayer!!.playWhenReady = true

            exoPlayer!!.duration


            println("durationOfvideo"+exoPlayer!!.duration)

            exoPlayer!!.addListener(object : ExoPlayer.EventListener {
                override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {}
                override fun onTracksChanged(
                    trackGroups: TrackGroupArray,
                    trackSelections: TrackSelectionArray) {}

                override fun onLoadingChanged(isLoading: Boolean) {

                    if(isLoading)
                        resume(false, "loading")

                }
                override fun onPlayerStateChanged(
                    playWhenReady: Boolean,
                    playbackState: Int) {
                    if (playWhenReady == true && playbackState == SimpleExoPlayer.STATE_ENDED) {
                        resume(true, "end")
                        progressBar!!.visibility = View.GONE
                        println("kaif_complete")
                    }

                    //resume
                    if (playWhenReady == true && playbackState == SimpleExoPlayer.STATE_READY) {
                        println("kaif_resume")
                        progressBar!!.visibility = View.GONE
                    }

                    if (playbackState == SimpleExoPlayer.STATE_BUFFERING) {
                        println("buffering")
                        resume(false, "end")
                        progressBar!!.visibility = View.VISIBLE
                    }
                }

                override fun onPositionDiscontinuity() {}
                override fun onPlayerError(error: ExoPlaybackException) { //Log.v(TAG, "Listener-onPlayerError...");
                    exoPlayer!!.stop()
                    exoPlayer!!.playWhenReady = true
                }

                override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
            })
        } catch (e: java.lang.Exception) {
        }


    }




    override fun onDestroy() {
        storyStatusView!!.destroy()
        super.onDestroy()
    }



    override fun onComplete() {
        exoPlayer!!.stop()
        exoPlayer!!.playWhenReady = true
        onBackPressed()
    }



    fun back(view: View) {
        storyStatusView!!.destroy()
        exoPlayer!!.stop()
        exoPlayer!!.playWhenReady = true
        onBackPressed()
    }

}