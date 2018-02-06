package com.video.corpus.network;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.video.corpus.BuildConfig;
import com.video.corpus.global.commonclass;

import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

///**
// * Created by Bhaskar.c on 2/5/2018.
// */

public class AMSRequest extends AsyncTask<String , Void, Void> {

    private String Msg;
    private String[] Params;
    public AMSRequest(String url,String msg)
       {
           Log.e("AMSRequest",msg);
       Msg=msg;
       Params = url.split(":");
       }

        @Override
        protected Void doInBackground(String... params) {
            if (params.length < 2) {
                return null;
            }
            if(Params.length>0)
            {
                String ip =Params[0] ;
                String port = Params[1];
                String message = Msg;
                try {
                    DatagramSocket clientSocket = new DatagramSocket();
                    InetAddress IPAddress = InetAddress.getByName(ip);
                    //  byte sendData[] = new byte[1024];
                    byte sendData[];
                    sendData = message.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, Integer.parseInt(port));
                    clientSocket.send(sendPacket);
                    //   Thread.sleep(2L);
                } catch (Exception  e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

