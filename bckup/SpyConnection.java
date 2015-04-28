package com.RemoteSurveillance.ARS2;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

//import com.example.androidserversocket.MainActivity;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceActivity.Header;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

@SuppressWarnings("unused")
public class SpyConnection  extends Activity {

	ServerSocket serverSocket; 
	
	String read;
	public static String tag = "SpyConnection has some faul";
	TextView serverStatus, msg; 
	public static int PORT =45678;
	private Handler handler = new Handler();
	//Socket clientSocket; 
	String clientmsg,messageFromClient ;
	BufferedReader input;
	MediaPlayer mPlayer;
	Boolean Status= true;
	MediaRecorder mRecorder; 
	//SurfaceView mSurfView; 
	SurfaceHolder mHolder; 
	Camera mCamera;
	VideoView mView;
	ParcelFileDescriptor pfd; 
		@SuppressWarnings("deprecation")
		@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.spy);	
		serverStatus = (TextView) findViewById(R.id.spytxt);
		msg= (TextView) findViewById(R.id.msg);
		msg.setText("");
		//mSurfView= (SurfaceView) findViewById(R.id.surface1);
		mView= (VideoView) findViewById(R.id.videoView);
		mHolder=mView.getHolder();
		//mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
	new Thread(new Runnable() {
		Socket socket = null;
		DataInputStream dataInputStream = null;
		DataOutputStream dataOutputStream =null;
		
			@Override
			public void run() {
								try {
					
					//final String addr= socket.;
					serverSocket = new ServerSocket(PORT);
						SpyConnection.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							serverStatus.setText("I'm waiting here: at IP " +getIpAddress() +"and port no. :"
									+ serverSocket.getLocalPort()  );
											}
					});
						mRecorder= new  MediaRecorder();
										
		while (true) {
			
						socket = serverSocket.accept();
						dataInputStream = new DataInputStream(
								socket.getInputStream());
						dataOutputStream = new DataOutputStream(
								socket.getOutputStream());

						messageFromClient = dataInputStream.readUTF();
							String msgReply = "Message sent is:: "+ messageFromClient;
							dataOutputStream.writeUTF(msgReply);
						clientmsg = "Message from Client:: " +messageFromClient;
					
						SpyConnection.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
																msg.setText(clientmsg );

		SpyConnection.this.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
								if(messageFromClient.equals("UP")){
									mPlayer= MediaPlayer.create(SpyConnection.this, R.raw.dtmf_9);
								mPlayer.start();
							
								}
								else if (messageFromClient.equals("DOWN")){
									mPlayer= MediaPlayer.create(SpyConnection.this, R.raw.dtmf_6);
									mPlayer.start();
								}
								else if (messageFromClient.equals("RIGHT")){
									mPlayer= MediaPlayer.create(SpyConnection.this, R.raw.dtmf_2);
									mPlayer.start();
								}else if (messageFromClient.equals("LEFT")){
									mPlayer= MediaPlayer.create(SpyConnection.this, R.raw.dtmf_8);
									mPlayer.start();
								}else if (messageFromClient.equals("ROT")){
									mPlayer= MediaPlayer.create(SpyConnection.this, R.raw.dtmf_5);
									mPlayer.start();
								}else {
									mPlayer= MediaPlayer.create(SpyConnection.this, R.raw.dtmf_3);
									mPlayer.start();
								}
								SpyConnection.this.runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
									
										pfd= ParcelFileDescriptor.fromSocket(socket);
										try {
												
								//	mRecorder.setCamera(mCamera);
										mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
							//mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
							//mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
									mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
									mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);
									mRecorder.setVideoFrameRate(20);
									mRecorder.setVideoSize(176,144);
									
									mRecorder.setOutputFile(pfd.getFileDescriptor());
									
										mRecorder.setPreviewDisplay(mHolder.getSurface());
										mRecorder.prepare();
										mRecorder.start();
										
									} catch (IllegalStateException e) {
										// TODO: handle exception
										e.printStackTrace();
									}catch (IOException e) {
										// TODO: handle exception
									e.printStackTrace();
									}
									
									}
								});
								
											}});
																							
							}
						});
						messageFromClient = "";
							
					//serverSocket.fl
						}
						
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					final String errMsg = e.toString();
					SpyConnection.this.runOnUiThread(new Runnable() 
					{

						@Override
						public void run() {
							msg.setText(errMsg);
							
						}
					});
				
						
				
/*SpyConnection.this.runOnUiThread(new Runnable() {
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
	if(messageFromClient.equals("DOWN"))
	{
		msg.setText("DOWN");
	}
	}
})					;*/
				} finally {
					if (socket != null) {
						try {
							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					if (dataInputStream != null) {
						try {
							dataInputStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					if (dataOutputStream != null) {
						try {
							dataOutputStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			
				}
	
		}).start();
		}
		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			Status= false;
		try {
			serverSocket.close();
			mRecorder.stop();
			mRecorder.release();
		//mRecorder.stop();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//mPlayer.release();
		
		}catch(IllegalStateException e){
			e.printStackTrace();
		}
		}
		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			super.onBackPressed();
			mPlayer.release();
			mPlayer.stop();
			mRecorder.stop();
			mRecorder.release();
				}
		
		private String getIpAddress() {
			String ip = "";
			try {
				Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
						.getNetworkInterfaces();
				while (enumNetworkInterfaces.hasMoreElements()) {
					NetworkInterface networkInterface = enumNetworkInterfaces
							.nextElement();
					Enumeration<InetAddress> enumInetAddress = networkInterface
							.getInetAddresses();
					while (enumInetAddress.hasMoreElements()) {
						InetAddress inetAddress = enumInetAddress.nextElement();

						if (inetAddress.isSiteLocalAddress()) {
							ip += "SiteLocalAddress: "
									+ inetAddress.getHostAddress() + "\n";
						}

					}

				}

			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ip += "Something Wrong! " + e.toString() + "\n";
			}

			return ip;
		}
		/*
		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void surfaceCreated(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
			
		}
	*/		}

