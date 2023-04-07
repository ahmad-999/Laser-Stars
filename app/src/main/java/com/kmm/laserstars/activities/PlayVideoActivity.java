package com.kmm.laserstars.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.WindowManager;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.kmm.laserstars.R;
import com.kmm.laserstars.databinding.ActivityPlayVideoBinding;
import com.kmm.laserstars.services.API;
import com.kmm.laserstars.util.Constant;

public class PlayVideoActivity extends AppCompatActivity {

    private ActivityPlayVideoBinding binding;
    private ExoPlayer exoPlayer;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer!=null){
            exoPlayer.release();
            exoPlayer=null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (exoPlayer!=null){
            exoPlayer.release();
            exoPlayer=null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String videoURL = intent.getExtras().getString(Constant.VIDEO_URL,"");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Uri uri= Uri.parse(API.RES_URL+videoURL);
        MediaSource mediaSource = buildMediaSource(uri);
        exoPlayer = new SimpleExoPlayer.Builder(this).build();
        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);
        binding.video.setPlayer(exoPlayer);

    }
    private MediaSource buildMediaSource(Uri uri) {
        DefaultDataSourceFactory dataSourceFactory = new
                DefaultDataSourceFactory(this, "javiermarsicano-VR-app");
        // Create a media source using the supplied URI
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }
}