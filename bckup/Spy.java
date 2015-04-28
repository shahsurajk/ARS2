package com.RemoteSurveillance.ARS2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.VideoView;

public class Spy extends Activity{

	VideoView mView;
	Camera mCamera;
	SurfaceHolder mHolder;
	//SurfaceView mSurfView;
	TextView connectionStatus;
	String TAG="Spy";
	//ImageButton stop, start; 
	//Handler handler;
	MediaRecorder recorder;
	ParcelFileDescriptor pfd;
	public static String SERVERIP="192.168.1.2";
	public static final int SERVERPORT = 21111;
	private Handler handler = new Handler();
	private ServerSocket serverSocket;  
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.spy);	
		//mSurfView = (SurfaceView) findViewById(R.id.surface);
		connectionStatus=(TextView) findViewById(R.id.spytxt);
	//	stop = (ImageButton) findViewById(R.id.stpbtn);
		//start= (ImageButton) findViewById(R.id.startbtn);
		mView = (VideoView) findViewById(R.id.videoView1);
		//mSurfView= (SurfaceView) findViewById(R.id.surfaceView1);
		mHolder= mView.getHolder();
		//mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	/*
		Thread load= new Thread()
		{
			@Override
			public void run() {
				// TODO Auto-generated method stub
			//	super.run();
				try{
			         mCamera = Camera.open();
			     }catch(RuntimeException e){
			         Log.e(TAG, "init_camera: " + e);
			         return;
			     }
			     Camera.Parameters param;
			     param = mCamera.getParameters();
			     //modify parameter
			     param.setPreviewFrameRate(20);
			     param.setPreviewSize(480, 320);
			     mCamera.setParameters(param);
			     try {
			         mCamera.setPreviewDisplay(mHolder);
			         mCamera.startPreview();
			     } catch (Exception e) {
			         Log.e(TAG, "init_camera: " + e);
			         return;
			
			     }
			 	  
			}
			
		};
		load.start();
		
		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					mCamera.setPreviewDisplay(mHolder);
					mCamera.startPreview();
				     				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		     //    mCamera.startPreview();
		     
			}
		});
stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mCamera.stopPreview();
				//mCamera.release();
				
			}
		});
	
	    */
	    Thread sendVideo = new Thread(new SendVideoThread());
	    sendVideo.start();
	}

	 public class SendVideoThread implements Runnable{
	   
		public void run(){
	        // From Server.java
	    			 	  

	    	
	    	try {
	            if(SERVERIP!=null){
/*	            handler.post(new Runnable() {
	            		
	            		@Override
	            		public void run() {
	            			// TODO Auto-generated method stub
	            			
	            		    	
	            	try{
	   		         mCamera = Camera.open();
	   		     }catch(RuntimeException e){
	   		         Log.e(TAG, "init_camera: " + e);
	   		         return;
	   		     }
	   		     Camera.Parameters param;
	   		     param = mCamera.getParameters();
	   		     //modify parameter
	   		     param.setPreviewFrameRate(20);
	   		     param.setPreviewSize(480, 320);
	   		     mCamera.setParameters(param);
	   		     try {
	   		         mCamera.setPreviewDisplay(mHolder);
	   		         mCamera.startPreview();
	   		     } catch (Exception e) {
	   		         Log.e(TAG, "init_camera: " + e);
	   		         return;
	   		
	   		     }

	            }
	    	
	    	}); 
	*/
	            	handler.post(new Runnable() {
	                    @Override
	                    public void run() {
	                        connectionStatus.setText("Listening on IP: " + SERVERIP);
	                    }
	                });
	                serverSocket = new ServerSocket(SERVERPORT);
	                while(true) {
	                    //listen for incoming clients
	                    Socket client = serverSocket.accept();
	                    handler.post(new Runnable(){
	                        @Override
	                        public void run(){
	                            connectionStatus.setText("Connected.");
	                        }
	                    });
	                    try{
	                            // Begin video communication
	                            pfd = ParcelFileDescriptor.fromSocket(client);
	                            handler.post(new Runnable(){
	                                @Override
	                                public void run(){
	                                	mCamera= Camera.open();
	                                	
	                                    recorder = new MediaRecorder();
	                                    recorder.setCamera(mCamera);
	                                    
	                                    recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
	             //                      recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
	                                    recorder.setOutputFormat(8);                 
	                                    recorder.setOutputFile(pfd.getFileDescriptor());
	                                    recorder.setVideoFrameRate(20);
	                                    recorder.setVideoSize(176,144);
	                                    recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
	               //                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
	                                    recorder.setPreviewDisplay(mHolder.getSurface());
	                                     handler.post( new Runnable() {
											public void run() {
												try {
													mCamera.setPreviewDisplay(mHolder);
												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
	    	                                //    mCamera.startPreview();
	    	                                    	
											}
										});
	                                    try {
	                                        recorder.prepare();
	                                    } catch (IllegalStateException e) {
	                                        // TODO Auto-generated catch block
	                                        e.printStackTrace();
	                                    } catch (IOException e) {
	                                        // TODO Auto-generated catch block
	                                        e.printStackTrace();
	                                    }
	                                    recorder.start();
	                                }
	                            });
	                    } catch (Exception e) {
	                        handler.post(new Runnable(){
	                            @Override
	                            public void run(){
	                                connectionStatus.setText("Oops.Connection interrupted. Please reconnect your phones.");
	                            }
	                        });
	                        e.printStackTrace();
	                    }
	                }
	            } else {
	                handler.post(new Runnable() {
	                    @Override
	                    public void run(){
	                        connectionStatus.setText("Couldn't detect internet connection.");
	                    }
	                });
	            }
	        } catch (Exception e){
	            handler.post(new Runnable() {
	                @Override
	                public void run() {
	                    connectionStatus.setText("Error");
	                }
	            });
	            e.printStackTrace();
	        
	
		
	        }

	    }
	 
	 }
	
}