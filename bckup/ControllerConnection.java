package com.RemoteSurveillance.ARS2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

//@SuppressWarnings("unused")
public class ControllerConnection extends Activity implements SurfaceHolder.Callback{

	//Socket clientSocket;
	ImageButton up, down, left, right, stop, rot; 
	// OutputStream out; 
		//DataOutputStream dos;
	//	InputStream in;
	//	DataInputStream dis;
		private  String SERVERIP;
		private final int PORT= 45678;
		String msg,str;
		TextView errs ;
	//	EditText ip;
		//Button con;
		Socket socket ;
		DataOutputStream dataOutputStream ;
		DataInputStream dataInputStream ;
		MediaPlayer mPlayer; 
		SurfaceView mSurfView; 
		SurfaceHolder mHolder ;
		ParcelFileDescriptor pfd;
		
		
//private Handler handler = new Handler();

@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		mSurfView =(SurfaceView) findViewById(R.id.surface);
		mHolder = mSurfView.getHolder(); 
		mHolder.addCallback(this);
		//mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		//mSurfView.setZOrderOnTop(true);
		up= (ImageButton) findViewById(R.id.up);
		down= (ImageButton) findViewById(R.id.down);
		left= (ImageButton) findViewById(R.id.left);
		right= (ImageButton) findViewById(R.id.right);
		rot= (ImageButton) findViewById(R.id.rot);
		stop= (ImageButton) findViewById(R.id.stop);
		errs= (TextView) findViewById(R.id.errors);
		errs.setText("");
			
				
			
		up.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					str= "UP";	
				sendMessage(str);	
				
			
				
				}
			})	;
			
down.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					str= "DOWN";	
				sendMessage(str);	
				
			
				
				}
			})	;
right.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		str= "RIGHT";	
	sendMessage(str);	
	

	
	}
})	;
left.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		str= "LEFT";	
	sendMessage(str);	
	

	
	}
})	;
stop.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		str= "STOP";	
	sendMessage(str);	
	

	
	}
})	;
rot.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		str= "ROT";	
	sendMessage(str);	
	

	
	
	}
			});
		
					
			}
		void sendMessage(String str)
	{
		 		// TODO Auto-generated method stub
				
		
		//String msg= sendMessage(str)
	SERVERIP= getIntent().getStringExtra("IP");
			ClientTask clienttask = new ClientTask(SERVERIP, PORT, str);
			clienttask.execute();
		}
	public class ClientTask extends AsyncTask<String, String, String> implements OnPreparedListener	{

		String dstAddress;
		int dstPort;
		String response = "";
		String msgToServer;

		ClientTask(String addr, int port, String msgTo) {
			dstAddress = addr;
			dstPort = port;
			msgToServer = msgTo;
	
		}

		protected void onPostExecute(String result) {
						super.onPostExecute(result);
						errs.setText(response);
			
							
		
					}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			socket= null;
			
			try {
				socket = new Socket(dstAddress, dstPort);
				
				dataOutputStream = new DataOutputStream(
						socket.getOutputStream());
				dataInputStream = new DataInputStream(socket.getInputStream());
				
				if(msgToServer != null){
					dataOutputStream.writeUTF(msgToServer);
				}
				
				response = dataInputStream.readUTF();
				
				
/*
				try {
					
					pfd= ParcelFileDescriptor.fromSocket(socket);
				
					mPlayer= new MediaPlayer();
				 
				pfd.getFileDescriptor().sync();
				      
					// pfd.getFileDescriptor().sync();
		             mPlayer.setDataSource(pfd.getFileDescriptor());
				      pfd.close();
				      mPlayer.prepareAsync();
					       
				      mPlayer.setOnPreparedListener(this);
						
					     mPlayer.setDisplay(mHolder);
		             

				} catch (Exception e) {
					// TODO: handle exception
				e.printStackTrace();
				}
		*/

				
					} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "UnknownHostException: " + e.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "IOException: " + e.toString();
			} finally {
				if (socket != null) {
					try {
						socket.close();
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

				if (dataInputStream != null) {
					try {
						dataInputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
	return null;
		}

		@Override
		public void onPrepared(MediaPlayer mp) {
			// TODO Auto-generated method stub
			mPlayer.start();
		}

	}
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
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
	
		
}
