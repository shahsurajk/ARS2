package com.RemoteSurveillance.ARS2;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.View.OnClickListener;

import com.RemoteSurveillance.ARS2.ui.Activity_ControllerConnection;
import com.RemoteSurveillance.ARS2.ui.Activity_Login;
import com.RemoteSurveillance.ARS2.ui.Activity_SpyConnection;

import butterknife.BindView;

public class MainActivity extends Activity {

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

        setContentView(R.layout.decide);

        spyButton = (ImageButton) findViewById(R.id.spybtn);
        controlButton = (ImageButton) findViewById(R.id.cntrlbtn);
        infoButton = (ImageButton) findViewById(R.id.infobtn);
        helpButton =(ImageButton) findViewById(R.id.helpbtn);

        spyButton.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                Intent load= new Intent(getApplicationContext(),Activity_SpyConnection.class);
                startActivity(load);
            }
        });

        controlButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent load1 = new Intent(getApplicationContext(), Activity_Login.class);
                startActivity(load1);
            }
        });
        infoButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent load2 = new Intent(getApplicationContext(), Info.class);
                startActivity(load2);
            }
        });
        helpButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent load = new Intent(getApplicationContext(), Help.class);
                startActivity(load);
            }
        });
    }
}
	
     
		
		
