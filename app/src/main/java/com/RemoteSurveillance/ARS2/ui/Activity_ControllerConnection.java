package com.RemoteSurveillance.ARS2.ui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.RemoteSurveillance.ARS2.R;
import com.RemoteSurveillance.ARS2.util.Utils;

import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.socket.client.IO;
import io.socket.emitter.Emitter;

//@SuppressWarnings("unused")
@SuppressWarnings("unused")
public class Activity_ControllerConnection extends Activity {


    private static final String TAG = Activity_ControllerConnection.class.getCanonicalName().toString();
    private String SERVERIP;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private final int PORT = 45678;
    private ImageButton up, down, left, right, stop, rot;
    private String msg, str, msgToServer, warnMsg;
    private TextView errs, read, warn;
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private MediaPlayer mPlayer;
    private ImageView mView;
    private int streamSize;
    private boolean status = true;
    private Bitmap streamBitmap;
    private byte[] stream = new byte[0];
    private Handler mHandler = new Handler();
    private long maxAvailMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        up = (ImageButton) findViewById(R.id.up);
        down = (ImageButton) findViewById(R.id.down);
        left = (ImageButton) findViewById(R.id.left);
        right = (ImageButton) findViewById(R.id.right);
        rot = (ImageButton) findViewById(R.id.rot);
        stop = (ImageButton) findViewById(R.id.stop);
        errs = (TextView) findViewById(R.id.errors);
        errs.setText("");

        mView = (ImageView) findViewById(R.id.imgView);
        read = (TextView) findViewById(R.id.read);
        read.setText("");
        warn = (TextView) findViewById(R.id.warn);
        warn.setText("Press Command Button to start... ");

        buttonControlling();

        Runtime rt = Runtime.getRuntime();
        maxAvailMemory = rt.maxMemory();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String ipAddr = getIntent().getStringExtra(Activity_Login.KEY_IP);
                try {
                    clientSocket = new Socket(ipAddr, PORT);
                    dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                    dataInputStream = new DataInputStream(clientSocket.getInputStream());
                    while (dataInputStream!=null){
//                        msg = dataInputStream.readUTF();
                        streamSize = dataInputStream.readInt();
                        Log.d(TAG, "run: "+streamSize);
//                        stream = new byte[streamSize];
                        if (streamSize>=0){
//                            dataInputStream.readFully(stream,0,streamSize);
//                            streamBitmap = BitmapFactory.decodeByteArray(stream, 0, streamSize);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    warn.setText("" + exceptError);
                                    errs.setText("" + msg);
                                    read.setText("Frame Size:: " + streamSize);
                                    mView.setImageBitmap(streamBitmap);
                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String stringToSend, exceptError = "";

    private void sendMessage(String str) {
        stringToSend = str;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dataOutputStream.writeUTF(stringToSend);
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        }).start();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            clientSocket.close();
            dataInputStream.close();
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    void buttonControlling()
    {
        up.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                str= "UP";
                sendMessage(str);
            }
        })	;

        down.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                str= "DOWN";
                sendMessage(str);
            }
        })	;
        right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                str= "RIGHT";
                sendMessage(str);
            }
        })	;
        left.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                str= "LEFT";
                sendMessage(str);
            }
        })	;
        stop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                str= "STOP";
                sendMessage(str);
            }
        })	;
        rot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                str= "ROT";
                sendMessage(str);
            }
        });

    }


}
