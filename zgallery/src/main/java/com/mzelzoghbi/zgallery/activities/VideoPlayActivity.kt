package com.mzelzoghbi.zgallery.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mzelzoghbi.zgallery.R
import tcking.github.com.giraffeplayer2.VideoView

class VideoPlayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)
        val videoView = findViewById<View>(R.id.video_view) as VideoView
        videoView.setVideoPath(intent.getStringExtra("url")).player.start()
    }
}