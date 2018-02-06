package com.video.corpus.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.video.corpus.R;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to set full screen without timebar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        Navigation();
    }

    //Navigation to home page
    private void Navigation() {
      new Thread(new Runnable() {
          @Override
          public void run() {
              try {
                  Thread.sleep(splash_milli);
                  Intent intent=new Intent(SplashActivity.this,HomeActivity.class);
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                  startActivity(intent);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
      }).start();
    }



}

