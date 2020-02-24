package com.mzelzoghbi.zgallery.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mzelzoghbi.zgallery.R;

import tcking.github.com.giraffeplayer2.VideoView;


public class VideoPlayActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

         VideoView videoView = (VideoView) findViewById(R.id.video_view);
         videoView.setVideoPath(getIntent().getStringExtra("url")).getPlayer().start();







}

}