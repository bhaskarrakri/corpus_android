package com.video.corpus.media;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.video.corpus.Interface.ItemclickListener;
import com.video.corpus.Interface.Response_string;
import com.video.corpus.R;
import com.video.corpus.adapters.CatchupeventAdapter;
import com.video.corpus.controllers.BaseActivity;
import com.video.corpus.global.Utils;
import com.video.corpus.global.commonclass;
import com.video.corpus.network.NetworkRequest;
import com.video.corpus.pojos.homecontent_model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

///**
// * Created by Bhaskar.c on 2/15/2018.
// */

public class PlayerCatchupActivity extends BaseActivity implements ItemclickListener{

    private SimpleExoPlayerView simpleExoPlayerView;
    private Context context;
    private commonclass cc;
    private SimpleExoPlayer player;
    private ArrayList<homecontent_model> models;
    private ProgressBar progressBar,progressBarplayer;
    private TextView txtPlayerException,txtExceptionplayer;
    private LinearLayout controllers, timer, Linlay;
    private ImageButton exoFull;
    private ConstraintLayout exoConstraint, consLikeLayout;
    private int isfullscreen = 0;
    private LinearLayout frameLayout;
    private ImageView imgShare;
    DefaultDataSourceFactory datasrcfatory;
    private MediaSourceEventListener mediaSourceEventListener;
    private ExtractorMediaSource.EventListener eventListener;
    private Handler handler = new Handler();
    private ArrayList<String> params;
    private Response_string<String> catchupresponse;
    private ImageView imgEvent;
    private RecyclerView recyclerview;
    private TextView txtEventname;
    private ItemclickListener itemclickListener;
    private  ArrayList<JSONObject> catchupVideosList;
    private String  playbackurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catchup_player);
        context = PlayerCatchupActivity.this;
        cc = new commonclass(context);
        cc.setplaybackId(0);
        readresponse();
        itemclickListener=this;

        initiateviews();

        getcatchupcontent();
    }


    @Override
    protected void onResume() {
        super.onResume();
        showlogs("PLAYBACKResume", "Resume" + cc.getplaybackId());
        if (cc.getplaybackId() == 0  && isnotempty(playbackurl)) {
              prepareplayer();
        }
    }

    //initiate views
    private void initiateviews() {
        simpleExoPlayerView = findViewById(R.id.simpleexoplayerview);
        progressBar = findViewById(R.id.progressbar);
        txtPlayerException = findViewById(R.id.Txt_player_exception);
        progressBarplayer = findViewById(R.id.progressbar_player);
        txtExceptionplayer = findViewById(R.id.Txt_player_exception_player);
        controllers = findViewById(R.id.controllers);
        timer = findViewById(R.id.timer);
        exoFull = findViewById(R.id.exo_full);
        exoConstraint = findViewById(R.id.player_lay);
        frameLayout = findViewById(R.id.frame_layout);
        imgShare = findViewById(R.id.img_share);
        consLikeLayout = findViewById(R.id.cons_like_layout);
        Linlay = findViewById(R.id.Linlay);
        imgEvent = findViewById(R.id.img_event);
        recyclerview = findViewById(R.id.recyclerview);
        txtEventname = findViewById(R.id.txt_eventname);

//        prepareplayer();

        exoFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isfullscreen == 0) {
                    maximizePlayer();
                } else if (isfullscreen == 1) {
                    minimizePlayer();
                }
            }
        });
    }

    //prepare player
    private void prepareplayer() {


        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory factory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(factory);
        datasrcfatory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, getResources().getString(R.string.app_name)), bandwidthMeter);

        //dynamic media source
        MediaSource mediaSource = Utils.getInstance().buildMediaSource(Uri.parse(playbackurl),
                Utils.getInstance().getExtension(playbackurl),
                mediaSourceEventListener, eventListener, datasrcfatory,
                handler, bandwidthMeter, context);

        //default media source
//         mediaSource = new ExtractorMediaSource.Factory(datasrcfatory)
//                .createMediaSource(Uri.parse(contenturl()));

        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);



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
                if (playbackState == Player.STATE_BUFFERING) {
                    progressBarplayer.setVisibility(View.VISIBLE);
                    txtExceptionplayer.setVisibility(View.GONE);
                } else if (playbackState == Player.STATE_READY) {
                    txtExceptionplayer.setVisibility(View.GONE);
                    progressBarplayer.setVisibility(View.GONE);
                } else if (playbackState == Player.STATE_IDLE) {
                    progressBarplayer.setVisibility(View.GONE);
                    txtExceptionplayer.setVisibility(View.VISIBLE);
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
        showlogs("PLAYBACKplayer", "player");
        startplaybackams();

        setplayercontrollers();

        simpleExoPlayerView.setPlayer(player);
    }

    //get home content
    private void getcatchupcontent() {
        if (isnotempty(cc.getcatchupContent())) {
            Gson gson = new Gson();
            models = gson.fromJson(cc.getcatchupContent(),
                    new TypeToken<ArrayList<homecontent_model>>() {
                    }.getType());
        }

        String url;
        if (models.size() > 0 && cc.getSessioId().length() > 0) {
            url = APP_SERVER_COMB + cc.getSessioId() + catchup_event_url +
                    models.get(cc.getContentClickpos()).getServiceassetid() +
                    prgmId + models.get(cc.getContentClickpos()).getProgramId();
        } else {
            url = APP_SERVER_COMB + FranchiseId + catchup_event_url +
                    models.get(cc.getContentClickpos()).getServiceassetid() +
                    prgmId + models.get(cc.getContentClickpos()).getProgramId();
        }

        if (isnotempty(url)) {
            setimage();
            setparams();
            new NetworkRequest(context, url, params, catchupresponse).execute();
        } else {
            nodata();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        showlogs("PLAYBACKDestroy", "Destroy" + cc.getplaybackId());
        stopplaybackams();
        if (player != null) {
            player.release();
        }
    }

    //set player controls visible
    private void setplayercontrollers() {
        controllers.setVisibility(View.VISIBLE);
        timer.setVisibility(View.VISIBLE);
    }


    void minimizePlayer() {
        isfullscreen = 0;
        consLikeLayout.setVisibility(View.VISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        exoFull.setImageResource(R.drawable.open);
        setparams(0.4f, 0.6f);
    }


    void maximizePlayer() {
        isfullscreen = 1;
        consLikeLayout.setVisibility(View.GONE);
        exoFull.setImageResource((R.drawable.close));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setparams(1f, 0f);
    }

    //set layout params
    private void setparams(double weightplayer, double weightframe) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        layoutParams.weight = (float) weightplayer;
        exoConstraint.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams layoutParamsframe = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        layoutParamsframe.weight = (float) weightframe;
        frameLayout.setLayoutParams(layoutParamsframe);
    }


    //send playback ams
    private void sendplaybackams(String playbackaction) {
        if (cc.getplaybackId() == 0) {
            if(Integer.valueOf(catchupVideosList.get(cc.getContentClickpos()).optString("channelprogramid",""))>0)
            {
                cc.setplaybackId(Integer.valueOf(catchupVideosList.get(cc.getContentClickpos()).optString("channelprogramid","")));
            }

        }
        Utils.getInstance().sendPlaybackAms(context, PLAYBACK_COMMAND_CHANNEL,
                cc.getplaybackId(), playbackaction);
        showlogs("PLAYBACKaction", playbackaction + cc.getplaybackId());
        if (playbackaction.equals(PLAYBACK_ACTION_STOP)) {
            cc.setplaybackId(0);
        }
    }

    //stop playback ams
    private void stopplaybackams() {
        if (cc.getplaybackId() != 0) {
            sendplaybackams(PLAYBACK_ACTION_STOP);
        }
    }

    //stop playback ams
    private void startplaybackams() {
        sendplaybackams(PLAYBACK_ACTION_START);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showlogs("PLAYBACKonback", "PLAYBACKonback" + cc.getplaybackId());
        stopplaybackams();
    }


    @Override
    public void onfragmentclick() {
        showlogs("PLAYBACKonfragmentclick", "PLAYBACKonfragmentclick");
        stopplaybackams();
    }

    private void setparams() {
        params = new ArrayList<>();
    }

    private void readresponse() {
        catchupresponse = new Response_string<String>() {
            @Override
            public void readResponse(String res) {
                showlogs("catchup_event _response", res);
                if (!TextUtils.isEmpty(res)) {
                    loadcatchupcontent(res);
                } else {
                    nodata();
                }
            }
        };
    }

    //load catch up content
    private void loadcatchupcontent(String res) {
        if (readstatuscode(res, context)) {
            try {
                 catchupVideosList = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(res);
                if (isnotempty(jsonObject.toString())) {
                    JSONObject jsonObject_catchup = jsonObject.optJSONObject("catchupEventVideos");
                    if (isnotempty(jsonObject_catchup.toString())) {
                        JSONArray jsonArray_videos = jsonObject_catchup.optJSONArray("videos");
                        if (isnotempty(jsonArray_videos.toString())) {
                            if (jsonArray_videos.length() > 0) {
                                for (int k = 0; k < jsonArray_videos.length(); k++) {
                                    catchupVideosList.add(jsonArray_videos.getJSONObject(k));
                                }
                            } else {
                                nodata();
                            }
                        }
                    }
                    if (catchupVideosList.size() > 0) {
                        setadapter(catchupVideosList);

                    }
                } else {
                    nodata();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            nodata();
        }
    }


    //set image
    private void setimage() {

        if(isnotempty(models.get(cc.getContentClickpos()).getName()))
        {
            txtEventname.setText(models.get(cc.getContentClickpos()).getName());
        }
        if (isnotempty(models.get(cc.getContentClickpos()).getImage())) {
            Picasso.with(context).load(models.get(cc.getContentClickpos()).getImage())
                    .placeholder(R.mipmap.placeholder_crousel).error(R.mipmap.ic_error_image).into(imgEvent);
        }

    }

    //set nodata
    private void nodata() {
        progressBar.setVisibility(View.GONE);
        txtPlayerException.setVisibility(View.VISIBLE);
    }

    //display data
    private void displaydata() {

        progressBar.setVisibility(View.GONE);
        txtPlayerException.setVisibility(View.GONE);
        Linlay.setVisibility(View.VISIBLE);
    }

    //display playback
    private void displayplayback() {
       imgEvent.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        txtPlayerException.setVisibility(View.GONE);
        Linlay.setVisibility(View.VISIBLE);
        simpleExoPlayerView.setVisibility(View.VISIBLE);
    }

    //set adapter
    private void setadapter(ArrayList<JSONObject> data)
    {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager
                (context,LinearLayoutManager.VERTICAL,false);

        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setAdapter(new CatchupeventAdapter(context,data,
                models.get(cc.getContentClickpos()).getName(),itemclickListener));

        displaydata();

    }


    @Override
    public void onitemclcik(int pos) {
        cc.setContentClickpos(pos);
        setplayback(pos);
    }

    //set playback content
    private void setplayback(int pos)
     {
         if(cc.getplaybackId()!=0)
         {
             stopplaybackams();
         }
         playbackurl="";
        if(isnotempty(catchupVideosList.get(pos).toString()))
        {
            if(isnotempty(catchupVideosList.get (pos).isNull ("playbackurl") ? "" : catchupVideosList.get (pos).optString ("playbackurl","")))
            {
                playbackurl=catchupVideosList.get (pos).optString ("playbackurl","");
            }
            else  if(isnotempty(catchupVideosList.get (pos).isNull ("playbackurl2") ? "" : catchupVideosList.get (pos).optString ("playbackurl2","")))
            {
                playbackurl=catchupVideosList.get (pos).optString ("playbackurl2","");
            }
        }

        if(isnotempty(playbackurl))
        {
            if(player!=null)
            {
                player.release();
            }
            displayplayback();
            prepareplayer();
        }
    }


}
