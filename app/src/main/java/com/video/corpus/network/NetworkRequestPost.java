package com.video.corpus.network;

///**
// * Created by Bhaskar.c on 2/13/2018.
// */

import android.os.AsyncTask;
import com.video.corpus.Interface.Response_string;
import com.video.corpus.controllers.BaseActivity;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkRequestPost extends AsyncTask<String,Void,String> {
    private  String Macaddr;
    private final String URL;
    private JSONObject JsonObject;
    private final Response_string<String> response;
    public  NetworkRequestPost(String url_, JSONObject jsonObject, String macaddr, Response_string<String> Response)
    {
        URL=url_;
        response=Response;
        Macaddr=macaddr;
        JsonObject=jsonObject;
    }


    @Override
    public String doInBackground(String...params)
    {
        String response = null;
        try {
            URL url = new URL(URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(BaseActivity.timeout);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Access-Control-Allow-Origin", "*");
            conn.setRequestProperty("username", BaseActivity.username_admin);
            conn.setRequestProperty("apikey", BaseActivity.reg_apikey);
            conn.setRequestProperty("subdeviceidentifier", Macaddr);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            OutputStream os = conn.getOutputStream();
            os.write(JsonObject.toString().getBytes("UTF-8"));
            os.close();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
             response= stringBuilder.toString();
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
