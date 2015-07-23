package com.RemoteSurveillance.ARS2;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Info extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN 
        		| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.info);
	
    	TextView info; 
    	
    	info= (TextView) findViewById(R.id.infotxt);
    	
    	info.setText("Design & Programming:: Suraj Shah\nshah.suraj.k@gmail.com\n\nMedia Player Handling:: Nishad Sawant\n\t\tnishadron@gmail.com"
    			+ "\n\nNetworking:: Shailesh Saindane\nssaindane77@gmail.com\n\nResources:: Shekhar Zunzarrao\nshekhar.zunzarrao@gmail.com\n\nTHANK YOU!");

	}

	}
