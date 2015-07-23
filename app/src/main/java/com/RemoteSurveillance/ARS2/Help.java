package com.RemoteSurveillance.ARS2;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Help extends Activity {

	TextView helpText; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN 
        		| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
    	setContentView(R.layout.help);
    	
    	helpText = (TextView) findViewById(R.id.helpcontent);
    	
    	helpText.setText("Hello, hope this helps.. \n\nEssentials:: Two Android Devices.\n\nPrerequistes:: Both the android devices connected to each other"
    			+ " via Wifi, i.e. Hotspot of one device connected to another. \n\nConnection:: Enter the IP displayed at the Spy Activity Screen"
    			+ "\n(Please enter only the static IP, starting with extension 192.168.1.XXX, for devices running Lollipop) \nat the "
    			+ "Control authentication screen.\n\nPassword:: The password is preset to 123, (Under-development for user-defined passwords)."
    			+ "\n\nFor Any further assistance please feel free to contact the developers for their contact information in the Info Screen. "
    			+ "\n\nTHANK YOU!, And do rate us!");
    	
	helpText.setMovementMethod(new ScrollingMovementMethod());
		
	}	
	}


