package com.RemoteSurveillance.ARS2;

import android.view.View.OnClickListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

@SuppressWarnings("unused")
public class Controller extends Activity implements SurfaceHolder.Callback {
 private static final String TAG= "ARS2";
 String ip, pass; 
 //Socket s; 
 OutputStream out; 
	DataOutputStream dos;
	InputStream in;
	DataInputStream dis;
	ImageButton up, down, left, right, stop, rot; 
	 public static final int MESSAGE_DATA_RECEIVE = 0;
		Boolean task_state = true;
		private static final int SERVERPORT= 21111;
		   private static final String SERVERIP="192.168.1.168";
		   private Handler handler=new Handler();
		   SurfaceView mSurfaceView; 
		   MediaPlayer mp; 
		   SurfaceHolder mHolder;
		   Camera camera; 
		   Socket clientSocket;
		 
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
	
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN 
        		| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
		setContentView(R.layout.activity_main);
	
		// button implementation goes here...
 		up= (ImageButton) findViewById(R.id.up);
		down= (ImageButton) findViewById(R.id.down);
		left= (ImageButton) findViewById(R.id.left);
		right= (ImageButton) findViewById(R.id.right);
		rot= (ImageButton) findViewById(R.id.rot);
		stop= (ImageButton) findViewById(R.id.stop);
		
		up.setOnClickListener(new OnClickListener() {
			
					public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sendString("UP");
			}
		});
		down.setOnClickListener(new OnClickListener() {
			
					public void onClick(View arg0) {
				// TODO Auto-generated method stub
		sendString("DOWN");		
			}
		});
		left.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			sendString("LEFT");	
			}
		});
		right.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			sendString("RIGHT");	
			}
		});
		
		rot.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			sendString("ROT");	
			}
		});
		
		stop.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			sendString("STOP");	
			}
		});
	}
 /*
	    Runnable readThread = new Runnable() {
		//Bitmap bitmap;
		
		public void run() {
			try {
				clientSocket = new Socket();
				clientSocket.connect((new InetSocketAddress(InetAddress.getByName(ip), 21111)), 5000);

				out = clientSocket.getOutputStream(); 
				dos = new DataOutputStream(out);

				in = clientSocket.getInputStream();
			    dis = new DataInputStream(in);
				//sendString(pass);
			} catch (EOFException e) {
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getApplicationContext()
								, "Connection Down"
								, Toast.LENGTH_SHORT).show();
					}
				});
				Log.e(TAG, e.toString());
			} catch (NumberFormatException e) {
				Log.e(TAG, e.toString());
			} catch (UnknownHostException e) {
				Log.e(TAG, e.toString());
			} catch (IOException e) {
				Log.e(TAG, e.toString());
			}
		}
	    };
		finish();
	    	    


new Thread(readThread).start();

mSurfaceView= (SurfaceView) findViewById(R.id.surface);
mHolder= mSurfaceView.getHolder();
mHolder.addCallback(this);
mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
mp = new MediaPlayer();
Thread t = new Thread(){
    public void run(){
        try {
                clientSocket = new Socket(SERVERIP,SERVERPORT);
            handler.post(new Runnable() {
                @Override
                public void run() {
                   // mTextview.setText("Connected to server");
                }
            });
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        ParcelFileDescriptor pfd = ParcelFileDescriptor.fromSocket(clientSocket);
                        pfd.getFileDescriptor().sync();
                        mp.setDataSource(pfd.getFileDescriptor());
                        pfd.close();
                        mp.setDisplay(mHolder);
                        mp.prepareAsync();
                        mp.start();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            });

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
};
t.start();
}
*/	/*
public void onPause() {
	super.onPause();

	task_state = false;
	try {
		clientSocket.close();
		out.close();
		dos.close();
	} catch (IOException e) {
		Log.e(TAG, e.toString());
	} catch (NullPointerException e) {
		Log.e(TAG, e.toString());
	}
	
	finish();
}
*/
public void sendString(String str) {
	try {
		dos.writeInt(str.length());
		dos.write(str.getBytes());
		out.flush();
	} catch (IOException e) {
		Log.e(TAG, e.toString());
	} catch (NullPointerException e) {
		Log.e(TAG, e.toString());
	}
}
/*
public void send(final String str) {
	new Thread(new Runnable() {
		public void run() {
			try {
				DatagramSocket s = new DatagramSocket();
				InetAddress local = InetAddress.getByName(ip);
				DatagramPacket p = new DatagramPacket(str.getBytes(), str.getBytes().length, local, 21111);
				s.send(p);
				s.close();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}).start();
}
*/
@Override
public void surfaceChanged(SurfaceHolder holder, int format, int width,
		int height) {
	// TODO Auto-generated method stub
	
}

@Override
public void surfaceCreated(SurfaceHolder holder) {
	// TODO Auto-generated method stub
	
}

@Override
public void surfaceDestroyed(SurfaceHolder holder) {
	// TODO Auto-generated method stub
	
}
	}
