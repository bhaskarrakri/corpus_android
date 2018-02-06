package com.video.corpus.network;

import android.os.AsyncTask;
import com.video.corpus.Interface.Response_string;
import com.video.corpus.controllers.BaseActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

///**
// * Created by Bhaskar.c on 2/1/2018.
// */

public class NetworkRequestHeaders extends AsyncTask<String,Void,String> {
    private final String url;
    private  HashMap<String,String> HashMaps;
    private final Response_string<String> response;
    public NetworkRequestHeaders(String url_, HashMap<String,String> hashMaps, Response_string<String> Response)
    {
        url=url_;
        response=Response;
        HashMaps=hashMaps;
    }


    @Override
    public String doInBackground(String...params)
    {
        String response = null;
        try {
            URL u = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) u.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(BaseActivity.timeout);
            for (Map.Entry<String, String> entry : HashMaps.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                httpURLConnection.setRequestProperty(key, value);
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
         //   responseCode = httpURLConnection.getResponseCode();
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
            response.readResponse(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
