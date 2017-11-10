package com.RemoteSurveillance.ARS2.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.RemoteSurveillance.ARS2.R;

import butterknife.BindView;

public class Activity_Dashboard extends AppCompatActivity {

    @BindView(R.id.spybtn)ImageButton spyButton;
    @BindView(R.id.cntrlbtn)ImageButton controlButton;
    @BindView(R.id.helpbtn)ImageButton helpButton;
    @BindView(R.id.infobtn)ImageButton infoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_dashboard);

        spyButton = (ImageButton) findViewById(R.id.spybtn);
        controlButton = (ImageButton) findViewById(R.id.cntrlbtn);
        infoButton = (ImageButton) findViewById(R.id.infobtn);
        helpButton =(ImageButton) findViewById(R.id.helpbtn);

        spyButton.setOnClickListener(arg0 -> {
            Intent load = new Intent(getApplicationContext(), Activity_SpyConnection.class);
            startActivity(load);
        });

        controlButton.setOnClickListener(v -> {
                Intent load1 = new Intent(getApplicationContext(), Activity_Login.class);
                startActivity(load1);
        });
        infoButton.setOnClickListener(v -> {
            Intent load2 = new Intent(getApplicationContext(), Activity_Info.class);
            startActivity(load2);
        });
        helpButton.setOnClickListener(v -> {
            Intent load = new Intent(getApplicationContext(), Activity_Help.class);
            startActivity(load);
        });
    }
}
	
     
		
		
