package com.RemoteSurveillance.ARS2;

 import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.View.OnClickListener;
public class MainActivity extends Activity {
	
	ImageButton spy, control ; 
	TextView txt1, txt2;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN 
        		| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.decide);
    	 spy= (ImageButton) findViewById(R.id.spybtn);
    	 control= (ImageButton) findViewById(R.id.cntrlbtn);
    	
txt1= (TextView) findViewById(R.id.spytxt);
txt2= (TextView) findViewById(R.id.cntrltxt);
	spy.setOnClickListener(new OnClickListener() {
		
		public void onClick(View arg0) {
			// TODO Auto-generated method stub

			Intent load= new Intent(MainActivity.this,SpyConnection.class);
			startActivity(load);
		}
	});
		
		control.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
			Intent load = new Intent(getBaseContext(), ControllerConnection.class);
			startActivity(load);
			}
		});
		
		
	}
}
	
     
		
		
