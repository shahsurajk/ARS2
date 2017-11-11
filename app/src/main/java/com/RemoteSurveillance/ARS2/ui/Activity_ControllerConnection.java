package com.RemoteSurveillance.ARS2.ui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.RemoteSurveillance.ARS2.R;

//@SuppressWarnings("unused")
@SuppressWarnings("unused")
public class Activity_ControllerConnection extends Activity{


	private  String SERVERIP;
	private final int PORT= 45678;
	private ImageButton up, down, left, right, stop, rot;
	private String msg,str, msgToServer, warnMsg ;
	private TextView errs , read, warn;
	private Socket socket  ;
	private DataOutputStream dataOutputStream ;
	private DataInputStream dataInputStream ;
	private MediaPlayer mPlayer;
	private ImageView mView;
	private int streamSize;
	private boolean status= true;
	private Bitmap streamBitmap;
	private byte[] stream;
	private long maxAvailMemory;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		warn.setText("Press Command Button to start... ");

		Runtime rt= Runtime.getRuntime();
		maxAvailMemory= rt.maxMemory();

		SERVERIP= getIntent().getStringExtra(Activity_Login.KEY_IP);
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					socket= new Socket(SERVERIP, PORT);
                    dataOutputStream= new DataOutputStream(socket.getOutputStream());
                    dataInputStream= new DataInputStream(socket.getInputStream());

                } catch (IOException e) {
					e.printStackTrace();
				}

				buttonControlling();

			}
		}).start();

	}
	String stringToSend, exceptError="";
	void sendMessage(String str)
	{
		stringToSend =str;

        new Thread(new Runnable() {

			@Override
			public void run() {
				while(status)
				{
					try {

                        dataOutputStream.writeUTF(stringToSend);
						msg= dataInputStream.readUTF();
						streamSize =dataInputStream.readInt();
						if(streamSize>0)
						{
							stream= new byte[streamSize];
							dataInputStream.readFully(stream);
							if(streamSize<maxAvailMemory )
							{
								streamBitmap= BitmapFactory.decodeByteArray(stream, 0, streamSize);
							}
							else{

								exceptError="Ahh! Memory Full!";
							}
						}
						else
						{
							exceptError="Negative Array";
						}
						Activity_ControllerConnection.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								warn.setText(""+exceptError);
								errs.setText(""+msg);
								read.setText("Frame Size:: "+streamSize );
								mView.setImageBitmap(streamBitmap);

							}
						});

					}catch(NegativeArraySizeException e)
					{
						e.printStackTrace();
						exceptError=""+e;

					}
					catch(OutOfMemoryError e)
					{
						e.printStackTrace();
						exceptError=""+e;
					}
					catch (ArrayIndexOutOfBoundsException e) {
						// TODO: handle exception
						e.printStackTrace();
						exceptError= ""+e;

					}
					catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch (NullPointerException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}

			}

		}).start();

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
		}catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	void buttonControlling()
	{

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


}
