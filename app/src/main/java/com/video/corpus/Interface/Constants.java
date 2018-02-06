package com.video.corpus.Interface;

///**
// * Created by Bhaskar.c on 12/18/2017.
// */

import android.view.ViewGroup;

public interface Constants {

       ViewGroup nullParent = null;
    int recyclerview_columns=2;
    int recyclerview_columns_catcup=1;

   int amsdeftimeinterval = 60;
   String AMS_ACTIVE="ALIVE";
 String AMS_INACTIVE="LOGOUT";
    //toast type
    int toast_short=0;
    int toast_long=1;

      int width_num=3;
      double aspectratio=0.56;
    double aspectratio_movies=1.4;

    //ADD constants
    int LEADERBOARD=0;
    int BIGBOX=1;

    //MAx record count in home page
    int MAXCONTENTCOUNT=7;

    String APP_SERVER_PORT="http://demo.corpus.com:8080/";

    String APP_SERVER_PORT_EXT="appserver/rest/";

    String APP_SERVER_COMB = APP_SERVER_PORT+APP_SERVER_PORT_EXT;


    //Without login
    String carousel_ads_url_free=APP_SERVER_PORT+APP_SERVER_PORT_EXT+"50001/homePageCarousel?device=OTHER_DEVICE";

    String home_content_url_free=APP_SERVER_PORT+APP_SERVER_PORT_EXT+"50001/homePageContent?deviceType=OTHER_DEVICE";

    String livetv_url_free=APP_SERVER_PORT+APP_SERVER_PORT_EXT+"50001/freeltv/channellist";

    String movies_url_free=APP_SERVER_PORT+APP_SERVER_PORT_EXT+"50001/freevod/movies";

    String catchup_url_free=APP_SERVER_PORT+APP_SERVER_PORT_EXT+"50001/catchup/events";

    String login_url=APP_SERVER_PORT+APP_SERVER_PORT_EXT+"iptv/userauth";


    //With login
    String carousel_ads_url_prm="/homePageCarousel?device=OTHER_DEVICE";

    String home_content_url_prm="/homePageContent?deviceType=OTHER_DEVICE";

    String livetv_url_prm="/ltv/channellist";

    String movies_url_prm="/vod/movies";

    String catchup_url_prm="/catchup/events";

    String device_config_url="/deviceConfig";



    String VIDEO_URL="http://demo.corpus.com/demovideo/TEN_GOLF_HD.mp4";
    String adsconstant_LEADER="LEADERBOARDAD";
    String adsconstant_BIGBOX="BigBoxBannerAD";

    //for livetv/VOD differntiation
//    String LIVETV="freeltv";
//    String VOD="freevod";

    String LIVETV="ltv";
    String VOD="vod";
}
