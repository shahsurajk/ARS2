package com.RemoteSurveillance.ARS2;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
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
public class SpyConnection  extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback{

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
	SurfaceView mSurfView; 
	SurfaceHolder mHolder; 
	Camera mCamera;
	VideoView mView;
	ParcelFileDescriptor pfd; 
	Socket socket ;
	DataInputStream dataInputStream ;
	DataOutputStream dataOutputStream ;
	OutputStream out;

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
		mSurfView= (SurfaceView) findViewById(R.id.surface);
		//mView= (VideoView) findViewById(R.id.videoView);
		mHolder=mSurfView.getHolder();
		//mHolder=mView.getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
	new Thread(new Runnable() {
		
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
					//	mRecorder= new  MediaRecorder();
										
		while (true) {
			
						socket = serverSocket.accept();
						dataInputStream = new DataInputStream(
								socket.getInputStream());
						out= socket.getOutputStream();
						dataOutputStream = new DataOutputStream(out);

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
								/*	
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
								
									*/
										 try{
									         mCamera = Camera.open();
									     }catch(RuntimeException e){
									         Log.e(tag, "init_camera: " + e);
									         return;
									     }
									     Camera.Parameters param;
									     param = mCamera.getParameters();
									     //modify parameter
									     //param.setPreviewFrameRate(20);
									     param.setPreviewSize(480, 320);
									     mCamera.setParameters(param);
									     mCamera.setPreviewCallback(SpyConnection.this);
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
	/*	@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			Status= false;
		try {
			mPlayer.stop();
			mPlayer.release();
			
			mCamera.stopPreview();
			mCamera.release();
				socket.close();
		
			serverSocket.close();
			finish();
		//mRecorder.stop();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//mPlayer.release();
		
		}catch(IllegalStateException e){
			e.printStackTrace();
		}
		}
		*/
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
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			mCamera = Camera.open(0);
			try {
		         mCamera.setPreviewCallback(this);
		         mCamera.setPreviewDisplay(holder);
			       
		         mCamera.startPreview();
		     } catch (Exception e) {
		         Log.e(tag, "init_camera: " + e);
		         return;
		     }

		}
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			try {
				mCamera.setPreviewDisplay(holder);
				mCamera.setPreviewCallback(this);
				mCamera.startPreview();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;

			
			
			
		}
		@Override
		public void onPreviewFrame(byte[] holder, Camera camera) {
			// TODO Auto-generated method stub
			int[] rgbs= new int[480*320];
			Bitmap bitmap;
		if(holder!=null & socket!=null){
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			/*YuvImage img= new YuvImage(holder,ImageFormat.NV21, 480,320 , null);
		img.compressToJpeg(new Rect(0,0,480,320), 0,out );
		byte[] imgBytes= out.toByteArray();
		Bitmap image = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
		
		try {
			dataOutputStream.writeInt(imgBytes.length);
			dataOutputStream.write(imgBytes);
			dataOutputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
			try {
				decodeYUV420(rgbs, holder, 480, 320);
				bitmap = Bitmap.createBitmap(rgbs, 480,320 , Config.ARGB_8888);
				out = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.JPEG, 25, out);
				sendImage(out.toByteArray());
			} catch (OutOfMemoryError e) {
				Toast.makeText(getApplicationContext()
						, "Out of memory,  please decrease image quality"
						, Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				finish();
			}
	
		}}	
		public void decodeYUV420(int[] rgb, byte[] yuv420, int width, int height) {
	    	final int frameSize = width * height;
	    	
	    	for (int j = 0, yp = 0; j < height; j++) {
	    		int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
	    		for (int i = 0; i < width; i++, yp++) {
	    			int y = (0xff & ((int) yuv420[yp])) - 16;
	    			if (y < 0) y = 0;
	    			if ((i & 1) == 0) {
	    				v = (0xff & yuv420[uvp++]) - 128;
	    				u = (0xff & yuv420[uvp++]) - 128;
	    			}
	    			
	    			int y1192 = 1192 * y;
	    			int r = (y1192 + 1634 * v);
	    			int g = (y1192 - 833 * v - 400 * u);
	    			int b = (y1192 + 2066 * u);
	    			
	    			if (r < 0) r = 0; else if (r > 262143) r = 262143;
	    			if (g < 0) g = 0; else if (g > 262143) g = 262143;
	    			if (b < 0) b = 0; else if (b > 262143) b = 262143;
	    			
	    			rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
	    		}
	    	}
	    }
		public void sendImage(byte[] data) {
			try {
				
				dataOutputStream.writeInt(data.length);
				dataOutputStream.write(data);
				out.flush();
			} catch (IOException e) {
				Log.e(tag, e.toString());
						} catch (NullPointerException e) { 
				Log.e(tag, e.toString());
			}
		}
		
}

