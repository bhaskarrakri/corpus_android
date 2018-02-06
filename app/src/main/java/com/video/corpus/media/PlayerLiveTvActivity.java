package com.video.corpus.media;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.video.corpus.R;
import com.video.corpus.controllers.BaseActivity;
import com.video.corpus.fragments.LiveTvSynopsisFragment;
import com.video.corpus.global.commonclass;
import com.video.corpus.pojos.homecontent_model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

///**
// * Created by Bhaskar.c on 1/29/2018.
// */

public class PlayerLiveTvActivity extends BaseActivity {

    private SimpleExoPlayerView simpleExoPlayerView;
    private Context context;
    private commonclass cc;
    private SimpleExoPlayer player;
    private ArrayList<homecontent_model> models;
    private ProgressBar progressBar;
    private TextView txtPlayerException;
    private LinearLayout controllers,timer;
    private ImageButton exoFull;
    private ConstraintLayout exoConstraint, consLikeLayout;
    private int isfullscreen=0;
    private FrameLayout frameLayout;
    private ImageView imgShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        context=PlayerLiveTvActivity.this;
        cc=new commonclass(context);

        getlivetvcontent();
        initiateviews();

    }

    //initiate views
    private  void initiateviews()
    {
        simpleExoPlayerView=findViewById(R.id.simpleexoplayerview);
        progressBar=findViewById(R.id.progressbar);
        txtPlayerException=findViewById(R.id.Txt_player_exception);
        controllers=findViewById(R.id.controllers);
        timer=findViewById(R.id.timer);
        exoFull=findViewById(R.id.exo_full);
        exoConstraint=findViewById(R.id.exoplayercons);
        frameLayout=findViewById(R.id.frame_layout);
        imgShare=findViewById(R.id.img_share);
        consLikeLayout=findViewById(R.id.cons_like_layout);

        prepareplayer();


        loadfragment();

        exoFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isfullscreen==0)
                {
                    maximizePlayer();
                }
                else if(isfullscreen==1)
                {
                    minimizePlayer();
                }
            }
        });


    }

    //prepare player
    private void prepareplayer()
    {
        DefaultBandwidthMeter bandwidthMeter=new DefaultBandwidthMeter();
        TrackSelection.Factory factory=new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector=new DefaultTrackSelector(factory);
        DataSource.Factory datasrcfatory=new DefaultDataSourceFactory(context,
                Util.getUserAgent(context,getResources().getString(R.string.app_name)),bandwidthMeter);
        MediaSource mediaSource=new ExtractorMediaSource.Factory(datasrcfatory)
                .createMediaSource(Uri.parse(contenturl()));

        player= ExoPlayerFactory.newSimpleInstance(context,trackSelector);
        player.addListener(new Player.EventListener() {

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if(playbackState==Player.STATE_BUFFERING)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    txtPlayerException.setVisibility(View.GONE);
                }
                else  if(playbackState==Player.STATE_READY)
                {
                    txtPlayerException.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
                else  if(playbackState==Player.STATE_IDLE)
                {
                    progressBar.setVisibility(View.GONE);
                    txtPlayerException.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onSeekProcessed() {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }
        });

        player.prepare(mediaSource);
        player.setPlayWhenReady(true);


        setplayercontrollers();

        simpleExoPlayerView.setPlayer(player);
    }

    //get home content
    private void getlivetvcontent()
    {

        if(cc.ishomecontent())
        {
            if(isnotempty(cc.getHomeContent())) {
                Gson gson = new Gson();
                models = gson.fromJson(cc.getHomeContent(),
                        new TypeToken<ArrayList<homecontent_model>>() {
                        }.getType());
            }
        }
        else
        {
            if(isnotempty(cc.getlivetvContent())) {
                Gson gson = new Gson();
                models = gson.fromJson(cc.getlivetvContent(),
                        new TypeToken<ArrayList<homecontent_model>>() {
                        }.getType());
            }
        }

    }

    //get content url
    private  String  contenturl()
    {
        String mediacontent="";
        if(isnotempty(models.toString()))
        {
            mediacontent=models.get(cc.getContentClickpos()).getMediacontent();
            showlogs("url_player",cc.getContentClickpos()+" "+mediacontent);
        }
        return mediacontent ;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(player!=null)
        {
            player.setPlayWhenReady(false);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(player!=null)
        {
            player.release();
        }
    }

    //set player controls visible
    private void setplayercontrollers()
    {

            controllers.setVisibility(View.GONE);
            timer.setVisibility(View.GONE);

    }



    void minimizePlayer () {
        isfullscreen=0;
        consLikeLayout.setVisibility(View.VISIBLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        exoFull.setImageResource (R.drawable.open);
        setparams(0.4f,0.6f);
    }


    void maximizePlayer ()
    {
        isfullscreen=1;
        consLikeLayout.setVisibility(View.GONE);
        exoFull.setImageResource ((R.drawable.close));
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setparams(1f,0f);
    }

    //set layout params
    private void setparams(double weightplayer,double weightframe)
    {
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        layoutParams.weight=(float) weightplayer;
        exoConstraint.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams layoutParamsframe=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        layoutParamsframe.weight=(float) weightframe;
        frameLayout.setLayoutParams(layoutParamsframe);
    }


    //load fragment
    private void loadfragment()
    {

            consLikeLayout.setVisibility(View.GONE);
            com.video.corpus.global.Util.getInstance().fragmenttransaction(this,new LiveTvSynopsisFragment(),
                    LiveTvSynopsisFragment.class.getName(),false);

    }
}
