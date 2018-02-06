package com.video.corpus.global;

import android.content.Context;
import android.content.SharedPreferences;

import com.video.corpus.R;

///**
// * Created by Bhaskar.c on 1/2/2018.
// */

public class commonclass {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;



    public boolean islivetv() {
        return sharedPreferences.getBoolean("islivetv",false);
    }

    public void setIslivetv(boolean islivetv) {
        editor.putBoolean("islivetv",islivetv).apply();
    }


    public boolean ishomecontent() {
        return sharedPreferences.getBoolean("ishomecontent",false);
    }

    public void setIshomecontent(boolean ishomecontent) {
        editor.putBoolean("ishomecontent",ishomecontent).apply();
    }


    public commonclass(Context con)
    {
        sharedPreferences= con.getSharedPreferences(con.getString(R.string.commondata),Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.apply();

    }

    public String getHomeContent() {
        return sharedPreferences.getString("homeContent","");
    }

    public void setHomeContent(String homeContent) {
        editor.putString("homeContent",homeContent).apply();
    }

    public String getlivetvContent() {
        return sharedPreferences.getString("livetvContent","");
    }

    public void setlivetvContent(String homeContent) {
        editor.putString("livetvContent",homeContent).apply();
    }

    public String getmovieContent() {
        return sharedPreferences.getString("movieContent","");
    }

    public void setmovieContent(String homeContent) {
        editor.putString("movieContent",homeContent).apply();
    }

    public String getcatchupContent() {
        return sharedPreferences.getString("catchupContent","");
    }

    public void setcatchupContent(String homeContent) {
        editor.putString("catchupContent",homeContent).apply();
    }

    public int getContentClickpos() {
       return sharedPreferences.getInt("contentclickpos",0);
    }

    public void setContentClickpos(int contentClickpos) {
        editor.putInt("contentclickpos",contentClickpos);
    }


     public boolean isLoggedIn()
     {
         return sharedPreferences.getBoolean("isloggedin",false);
     }

     public void  setisLoggedIn(boolean loggedin)
     {
         editor.putBoolean("isloggedin",loggedin).apply();
     }

     public  String getSessioId()
     {
         return sharedPreferences.getString("sessionId","");
     }

     public void setSessionId(String SessionId)
     {
         editor.putString("sessionId",SessionId).apply();
     }

    public void setcustomerName(String username)
    {
        editor.putString("customerName",username).apply();
    }
    public  String getcustomerName()
    {
        return sharedPreferences.getString("customerName","");
    }

    public  void resetsharedpref()
    {
        editor.clear().apply();
    }

    public  String getusername()
    {
        return sharedPreferences.getString("username","");
    }

    public void setusername(String username)
    {
        editor.putString("username",username).apply();
    }

    public  int getamstimeinterval()
    {
        return sharedPreferences.getInt("amstimeinterval",0);
    }

    public void setamstimeinterval(int amstimeinterval)
    {
        editor.putInt("amstimeinterval",amstimeinterval).apply();
    }

    public  String gettrapurl()
    {
        return sharedPreferences.getString("trapurl","");
    }

    public void settrapurl(String trapurl)
    {
        editor.putString("trapurl",trapurl).apply();
    }
}
