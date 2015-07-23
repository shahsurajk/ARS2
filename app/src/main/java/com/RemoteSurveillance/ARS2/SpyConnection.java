package com.RemoteSurveillance.ARS2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SpyConnection extends Activity implements SurfaceHolder.Callback , PreviewCallback {
	
	MediaPlayer mPlayer; 
	ServerSocket serverSoc; 
	Socket clientSoc; 
	static final int PORT= 45678;
	TextView info, msg; 
	String messageFromClient, status="WORKING";
	BufferedInputStream bis; 
	BufferedOutputStream bos; 
	DataInputStream dataInputStream; 
	DataOutputStream dataOutputStream; 
	SurfaceView mSurfView; 
	SurfaceHolder mHolder; 
	Bitmap mBitmap;
	Handler mHandler; 
	int[] pixels; 
	Size previewSize; 
	Camera mCamera;
	Parameters params;
	String temp;
	Boolean flagStatus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.spy);

		info= (TextView) findViewById(R.id.spytxt);
		msg=(TextView) findViewById(R.id.msg);
		info.setText("");
		msg.setText("");
		mSurfView= (SurfaceView) findViewById(R.id.surface);

		mHolder= mSurfView.getHolder();
		mHolder.addCallback(this);

		//display IP to connect to..
	info.setText("Hey! I'm waiting here; at IP:: " + getIpAddress()+"And Port:: "+ PORT);
	temp= "empty";
	new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
		try {
			serverSoc= new ServerSocket(PORT);
			
			while(true ){
				clientSoc=serverSoc.accept();
				dataInputStream= new DataInputStream(clientSoc.getInputStream());
				messageFromClient= dataInputStream.readUTF(); 
			
			dataOutputStream= new DataOutputStream(clientSoc.getOutputStream());
			dataOutputStream.writeUTF("Message Sent to Client is:: "+messageFromClient);
		//	dataOutputStream.writeInt(250);
			
			SpyConnection.this.runOnUiThread(new Runnable() {
							
				@Override
				public void run() {
					// TODO Auto-generated method stub
			
					msg.setText("The Messge Recieved is:: "+messageFromClient);
				//messageFromClient="The Message sent is:: ";
				}
			});
			flagStatus= temp.equalsIgnoreCase(messageFromClient);
			temp=""+messageFromClient; 
			if(!(flagStatus))
			{
	if(messageFromClient.equalsIgnoreCase("UP")){
				mPlayer= MediaPlayer.create(SpyConnection.this, R.raw.dtmf_9);
			mPlayer.start();
		
			}
			else if (messageFromClient.equalsIgnoreCase("DOWN")){
				mPlayer= MediaPlayer.create(SpyConnection.this, R.raw.dtmf_6);
				mPlayer.start();
			}
			else if (messageFromClient.equalsIgnoreCase("RIGHT")){
				mPlayer= MediaPlayer.create(SpyConnection.this, R.raw.dtmf_1);
				mPlayer.start();
			}else if (messageFromClient.equalsIgnoreCase("LEFT")){
				mPlayer= MediaPlayer.create(SpyConnection.this, R.raw.dtmf_8);
				mPlayer.start();
			}else if (messageFromClient.equalsIgnoreCase("ROT")){
				mPlayer= MediaPlayer.create(SpyConnection.this, R.raw.dtmf_5);
				mPlayer.start();
			}
			else if (messageFromClient.equalsIgnoreCase("STOP")) {
				mPlayer= MediaPlayer.create(SpyConnection.this, R.raw.dtmf_3);
				mPlayer.start();
	
			}
flagStatus= true;
			}	}// while ends here	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		}
	}).start();;
	
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
@Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
try {
	serverSoc.close();
serverSoc= null;
dataInputStream =null;
dataOutputStream= null; 
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
status=null;
}	@Override 
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	mCamera =Camera.open();
	params= mCamera.getParameters();
	previewSize= params.getPreviewSize();
	pixels= new int[previewSize.width* previewSize.height];
	
	
	try {
		mCamera.setPreviewCallback(this);
		mCamera.setPreviewDisplay(holder);
		mCamera.startPreview();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	mCamera.release();
	mCamera =null;
	
	}
	
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		mCamera.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	mCamera.stopPreview();
	//	mCamera.release();
	mCamera= null;
	
	}
	byte[] streamMJPEG; 
	
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream out= new ByteArrayOutputStream(); 
		decodeYUV420(pixels, data, previewSize.width, previewSize.height);
		mBitmap = Bitmap.createBitmap(pixels, previewSize.width, previewSize.height,Config.ARGB_8888);
		mBitmap.compress(CompressFormat.JPEG	, 25, out);
	
		streamMJPEG= out.toByteArray();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
		try {
			dataOutputStream.writeInt(streamMJPEG.length);
			dataOutputStream.write(streamMJPEG);
		}catch(NullPointerException e)
		{
		Log.d("Spy Conn", "Catch d error::" +e);
		}catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NegativeArraySizeException e){
			Log.d("Spy Conn","Negative Size?" +e);
		}
		catch(ArrayStoreException e)
		{
			e.printStackTrace();
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			e.printStackTrace();
		}
		}
			
		}).start();
	}
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

}
