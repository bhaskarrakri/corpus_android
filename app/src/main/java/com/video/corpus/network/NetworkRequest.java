package com.video.corpus.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.video.corpus.Interface.Response_string;
import com.video.corpus.controllers.BaseActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

///**
// * Created by Bhaskar.c on 12/18/2017.
// */

public class NetworkRequest  extends AsyncTask<String,Void,String>{

    private final String url;
    private final Context context;
    private  int responseCode;
    private final ArrayList<String> params;
    private final Response_string<String> response;
    public  NetworkRequest(Context  mcon, String url_, ArrayList<String> param, Response_string<String> Response)
    {
        context=mcon;
        url=url_;
        response=Response;
        params=param;
    }


    @Override
    public String doInBackground(String...params)
    {
        String response = null;
        try {
            URL u = new URL(url);
            Log.e("url" , url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) u.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(BaseActivity.timeout);
//            httpURLConnection.setRequestProperty(context.getResources().getString(R.string.UserAgent), context.getResources().getString(R.string.Mozilla));
//            httpURLConnection.setRequestProperty(context.getResources().getString(R.string.Accept), context.getResources().getString(R.string.AceeptExtension));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
             responseCode = httpURLConnection.getResponseCode();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            response = stringBuilder.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
//            if(responseCode==BaseActivity.responsecode)
//            {
                response.readResponse(result);
           // }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
