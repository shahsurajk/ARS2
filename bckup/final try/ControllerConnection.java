package com.RemoteSurveillance.ARS2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ResponseCache;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.hardware.Camera.Size;
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
import android.widget.ImageView;
import android.widget.TextView;

//@SuppressWarnings("unused")
@SuppressWarnings("unused")
public class ControllerConnection extends Activity{


	ImageButton up, down, left, right, stop, rot;
	private  String SERVERIP;
	private final int PORT= 45678;
	String msg,str, msgToServer, warnMsg ;
	TextView errs , read, warn;
	Socket socket  ;
	DataOutputStream dataOutputStream ;
	DataInputStream dataInputStream ;
	MediaPlayer mPlayer;
	ImageView mView;
	byte[] stream;
	int streamSize;
	boolean status= true;
	Bitmap streamBitmap;

//private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);
		up= (ImageButton) findViewById(R.id.up);
		down= (ImageButton) findViewById(R.id.down);
		left= (ImageButton) findViewById(R.id.left);
		right= (ImageButton) findViewById(R.id.right);
		rot= (ImageButton) findViewById(R.id.rot);
		stop= (ImageButton) findViewById(R.id.stop);
		errs= (TextView) findViewById(R.id.errors);
		errs.setText("");

		mView=(ImageView) findViewById(R.id.imgView);
		read= (TextView) findViewById(R.id.read);
		read.setText("");
		warn = (TextView) findViewById(R.id.warn);
		warn.setText("");
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

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				SERVERIP= getIntent().getStringExtra("IP");

				try {

					socket= new Socket(SERVERIP, PORT);
					while(socket.isConnected())
					{
						dataInputStream= new DataInputStream(socket.getInputStream());

						dataOutputStream= new DataOutputStream(socket.getOutputStream());

						streamSize = dataInputStream.readInt();
						stream = new byte[streamSize];

						dataInputStream.readFully(stream);
						streamBitmap = BitmapFactory.decodeByteArray(stream, 0, streamSize);

						dataOutputStream.writeUTF("Connected to Client at IP:: "+ SERVERIP + " Streaming...");

						ControllerConnection.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								read.setText("Frame Size:: "+ streamSize);
								mView.setImageBitmap(streamBitmap);
							}
						});
					}
				}catch(OutOfMemoryError e)
				{
					e.printStackTrace();
					warnMsg= "Memory Full! :( Reduce Quality. "+e;
				}
				catch(NullPointerException e){
					e.printStackTrace();
					warnMsg= "No Server? "+e;
				}
				catch(ArrayIndexOutOfBoundsException e){
					e.printStackTrace();
					warnMsg="Array Full? "+e;
				}
				catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (NegativeArraySizeException e) {
					// TODO: handle exception
					e.printStackTrace();
					warnMsg ="Neg. array? "+e;
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				//while ends here. 
			}
		})	.start();


	}
	String msgFromServer;
	void sendMessage(String command)
	{

		try {
			dataOutputStream= new DataOutputStream(socket.getOutputStream());

			dataOutputStream.writeUTF(msgToServer);


			//dataInputStream= new DataInputStream(socket.getInputStream());
			msgFromServer= dataInputStream.readUTF();

			ControllerConnection.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					errs.setText(msgFromServer);

				}
			});
		}catch(OutOfMemoryError e)
		{
			e.printStackTrace();
			status=false;

		}catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		catch (UnknownHostException e) {
			status=false;

			// TODO Auto-generated catch block
			e.printStackTrace();
			msgToServer = "UnknownHostException: " + e.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgToServer = "IOException: " + e.toString();
			status=false;

		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			socket.close();
			dataInputStream.close();
			dataOutputStream.close();
			status=false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
