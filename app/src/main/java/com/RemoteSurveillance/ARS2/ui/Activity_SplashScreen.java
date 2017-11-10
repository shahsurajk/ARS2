package com.RemoteSurveillance.ARS2.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.RemoteSurveillance.ARS2.R;

public class Activity_SplashScreen extends AppCompatActivity {

    private static final int DELAY_IN_MILLIS = 2*1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loaded = new Intent(Activity_SplashScreen.this, Activity_Dashboard.class);
                startActivity(loaded);
            }
        }, DELAY_IN_MILLIS);
    }
}

