package com.RemoteSurveillance.ARS2;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class EntryPoint extends Activity {
	
	 ProgressBar prog ;
	
	
	//int clr= 5026082 ;
	Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN 
	        		| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
			setContentView(R.layout.entry);

			Thread load = new Thread()
			{
				@Override
				public void run() {
					// TODO Auto-generated method stub
				//	super.run();
					try {
						sleep(4*1000);	
					} catch (InterruptedException e) {
						// TODO: handle exception
						e.printStackTrace();
					}finally{
					Intent loaded = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(loaded);
					finish();
					}
					}
					
			};load.start();
			/*
			prog= (ProgressBar) findViewById(R.id.progressBar1);
			prog.getProgressDrawable().setColorFilter(Color.GREEN, Mode.MULTIPLY);
			prog.setMax(100);
			
	 new Thread(new Runnable() {
		 int status=0;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//prog.setProgress(status);
			while(status<100){
			
			status=+5;
			mHandler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
				prog.setProgress(status);
				}
				});
		try {
				Thread.sleep(200);
				
		} catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
				finally{
			Intent loader = new Intent(EntryPoint.this, MainActivity.class);
			startActivity(loader);
				}
			}}
	 }).start();
	*/
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
}

