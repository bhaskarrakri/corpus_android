package com.video.corpus.global;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.video.corpus.BuildConfig;
import com.video.corpus.R;
import com.video.corpus.network.AMSRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

///**
// * Created by Bhaskar.c on 1/2/2018.
// */

public class Utils {
    private commonclass cc;
    private static Utils util;
    private int seqno=1;

    public static Utils getInstance() {
        if(util==null)
        {
            util=new Utils();
        }

        return  util;
    }

    public void fragmenttransaction(AppCompatActivity activity,Fragment fragment, String tag, boolean backstack)
    {

        FragmentTransaction transaction=activity.getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,fragment);
        Log.e("backstack",""+backstack);
        if(backstack)
        {
            transaction.addToBackStack(tag);
        }

        transaction.commit();
        activity.getFragmentManager().executePendingTransactions();
        Log.e("count",""+tag+"      "+activity.getFragmentManager().getBackStackEntryCount());
    }



    // get device width programatically
    public  int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }



    //set params
    public String setamsobject(Context context, String status)
    {
        JSONObject amsJsonObject = null;
        try {
            commonclass cc=new commonclass(context);
            amsJsonObject = new JSONObject();
            try {
                amsJsonObject.putOpt("seqNo", seqno++);
                amsJsonObject.putOpt("command", "SYSTEM");
                amsJsonObject.putOpt("subject", "");
                amsJsonObject.putOpt("action", status);
                amsJsonObject.putOpt("sessionId", cc.getSessioId());
                amsJsonObject.putOpt("initiatiatingApp", 1);
                amsJsonObject.putOpt("dateTime", System.currentTimeMillis());
                amsJsonObject.putOpt("stb", cc.getusername());
                amsJsonObject.putOpt("subDeviceType", "Mobile");
                amsJsonObject.putOpt("subDeviceOS", "Android v" + Build.VERSION.RELEASE);
                amsJsonObject.put("info", "Product Release v" + BuildConfig.VERSION_CODE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (amsJsonObject != null) {
            return amsJsonObject.toString();
        }
        return "";
    }

    public String getExtension (String fileName) {
        String encoded;
        try {
            encoded = URLEncoder.encode (fileName, "UTF-8").replace ("+", "%20");
        } catch (UnsupportedEncodingException e) {
            encoded = fileName;
        }
        return MimeTypeMap.getFileExtensionFromUrl (encoded).toLowerCase ();
    }


    public MediaSource buildMediaSource (Uri uri, String overrideExtension,
                                         MediaSourceEventListener mediaSourceEventListener,
                                         ExtractorMediaSource.EventListener eventListener,
                                         DefaultDataSourceFactory datasrcfatory,
                                         Handler  handler,
                                         DefaultBandwidthMeter defaultBandwidthMeter,
                                         Context context) {
        int type = TextUtils.isEmpty (overrideExtension) ? Util.inferContentType (uri)
                : Util.inferContentType ("." + overrideExtension);
        switch (type) {
            case C.TYPE_SS:
                return new SsMediaSource (uri, buildDataSourceFactory (false,defaultBandwidthMeter,context),
                        new DefaultSsChunkSource.Factory (datasrcfatory), handler, mediaSourceEventListener);

            case C.TYPE_DASH:
                return new DashMediaSource (uri, buildDataSourceFactory (false,defaultBandwidthMeter,context),
                        new DefaultDashChunkSource.Factory (datasrcfatory), handler, mediaSourceEventListener);
            case C.TYPE_HLS:
                return new HlsMediaSource (uri, datasrcfatory, handler, mediaSourceEventListener);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource (uri, datasrcfatory, new DefaultExtractorsFactory(),
                        handler, eventListener);
            default: {
                throw new IllegalStateException ("Unsupported type: " + type);
            }
        }
    }

    private DataSource.Factory buildDataSourceFactory (boolean useBandwidthMeter,DefaultBandwidthMeter defaultBandwidthMeter,Context context) {
        return buildDataSourceFactory (context,useBandwidthMeter ? defaultBandwidthMeter : null);
    }

    private DataSource.Factory buildDataSourceFactory(Context context,DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(context, bandwidthMeter,
                buildHttpDataSourceFactory (context,bandwidthMeter));
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory(Context context,DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(Util.getUserAgent(context, context.getResources().getString(R.string.app_name)), bandwidthMeter);
    }

    //send playback ams
    public void sendPlaybackAms(Context context,String curSelItem, int selItemId, String playState) {
        JSONObject playbackObj = new JSONObject();
        cc=new commonclass(context);
        try {
            Log.e("seqno",""+seqno);
            playbackObj.putOpt("seqNo", seqno++);
            playbackObj.putOpt("command", curSelItem);
            playbackObj.putOpt("subject", String.valueOf(selItemId));
            playbackObj.putOpt("action", playState);
            playbackObj.putOpt("sessionId", cc.getSessioId());
            playbackObj.putOpt("initiatiatingApp", 2);
            playbackObj.putOpt("dateTime", new Date().getTime());
            playbackObj.putOpt("stb",cc.getusername() );
            playbackObj.putOpt("playPauseCount", "0");
            playbackObj.putOpt("offset", "0");
            playbackObj.putOpt("info", "");
            JSONObject ams = new JSONObject();
            ams.putOpt("ams", playbackObj);
            new AMSRequest(cc.gettrapurl(), ams.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
