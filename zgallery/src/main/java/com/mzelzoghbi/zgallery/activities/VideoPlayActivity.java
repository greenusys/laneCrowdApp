package com.mzelzoghbi.zgallery.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import com.mzelzoghbi.zgallery.R;


public class VideoPlayActivity extends AppCompatActivity {
    SimpleExoPlayerView exoPlayerView;
    ProgressBar progressBar;
    SimpleExoPlayer exoPlayer;
    TextView userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);


        System.out.println("Kaifname"+getIntent().getStringArrayExtra("name"));

        initViews();

        playVideo();

    }

    private void playVideo() {

        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            Uri videoURI = Uri.parse(getIntent().getStringExtra("url"));
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);

            exoPlayer.addListener(new ExoPlayer.EventListener() {


                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                    if (playbackState == SimpleExoPlayer.STATE_ENDED)
                        System.out.println("kaif_complete");

                    //resume
                    if (playWhenReady == true && playbackState == SimpleExoPlayer.STATE_READY) {
                        System.out.println("kaif_resume");
                        progressBar.setVisibility(View.GONE);

                    }
                    if (playbackState == SimpleExoPlayer.STATE_BUFFERING)
                        progressBar.setVisibility(View.VISIBLE);


                }


                @Override
                public void onPositionDiscontinuity() {

                }


                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    //Log.v(TAG, "Listener-onPlayerError...");
                    exoPlayer.stop();
                    // exoPlayer.prepare(loopingSource);
                    exoPlayer.setPlayWhenReady(true);
                }


                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }


            });


        } catch (Exception e) {
        }
    }

    private void initViews() {

        progressBar = findViewById(R.id.progressBar);
        userName = findViewById(R.id.userName);
        exoPlayerView = findViewById(R.id.exo_player_view);

        userName.setText(getIntent().getStringExtra("name"));

    }


    @Override
    protected void onStop() {
        super.onStop();
        exoPlayer.stop();

        // exoPlayer.prepare(loopingSource);
        exoPlayer.setPlayWhenReady(true);
    }

    @Override
    protected void onDestroy() {
        exoPlayer.stop();
        // exoPlayer.prepare(loopingSource);
        exoPlayer.setPlayWhenReady(true);
        super.onDestroy();
    }

    public void back_activity(View view) {
        onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}