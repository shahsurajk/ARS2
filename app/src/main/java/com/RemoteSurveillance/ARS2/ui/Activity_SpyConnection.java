package com.RemoteSurveillance.ARS2.ui;

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
import android.net.rtp.RtpStream;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.RemoteSurveillance.ARS2.R;

public class Activity_SpyConnection extends Activity implements SurfaceHolder.Callback, PreviewCallback {

    private MediaPlayer mPlayer;
    private ServerSocket serverSoc;
    private Socket clientSoc;
    private static final int PORT = 45678;
    private TextView info, msg;
    private String messageFromClient, status = "WORKING";
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private SurfaceView mSurfView;
    private SurfaceHolder mHolder;
    private Bitmap mBitmap;
    private Handler mHandler;
    private int[] pixels;
    private Size previewSize;
    private Camera mCamera;
    private Parameters params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_spy);

        info = (TextView) findViewById(R.id.spytxt);
        msg = (TextView) findViewById(R.id.msg);
        info.setText("");
        msg.setText("");
        mSurfView = (SurfaceView) findViewById(R.id.surface);

        mHolder = mSurfView.getHolder();
        mHolder.addCallback(this);
        info.setText("Hey! I'm waiting here; at IP:: " + getIpAddress() + "And Port:: " + PORT);

        new RecieveMessageAsyncTask().execute();
        
    }

    private class RecieveMessageAsyncTask extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (serverSoc == null) {
                try {
                    serverSoc = new ServerSocket(PORT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            msg.setText("The Messge Recieved is:: " + values[0]);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                clientSoc = serverSoc.accept();
                dataOutputStream = new DataOutputStream(clientSoc.getOutputStream());
                dataInputStream = new DataInputStream(clientSoc.getInputStream());
                while (clientSoc.isConnected()) {
                    messageFromClient = dataInputStream.readUTF();
                    dataOutputStream.writeUTF("Message Sent to Client is:: " + messageFromClient);
                    publishProgress(messageFromClient);
                    dataOutputStream.writeInt(streamMJPEG.length);
                    dataOutputStream.write(streamMJPEG);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void playDTMFTones() {
        if (messageFromClient.equalsIgnoreCase("UP")) {
            mPlayer = MediaPlayer.create(Activity_SpyConnection.this, R.raw.dtmf_9);
            mPlayer.start();

        } else if (messageFromClient.equalsIgnoreCase("DOWN")) {
            mPlayer = MediaPlayer.create(Activity_SpyConnection.this, R.raw.dtmf_6);
            mPlayer.start();
        } else if (messageFromClient.equalsIgnoreCase("RIGHT")) {
            mPlayer = MediaPlayer.create(Activity_SpyConnection.this, R.raw.dtmf_1);
            mPlayer.start();
        } else if (messageFromClient.equalsIgnoreCase("LEFT")) {
            mPlayer = MediaPlayer.create(Activity_SpyConnection.this, R.raw.dtmf_8);
            mPlayer.start();
        } else if (messageFromClient.equalsIgnoreCase("ROT")) {
            mPlayer = MediaPlayer.create(Activity_SpyConnection.this, R.raw.dtmf_5);
            mPlayer.start();
        } else if (messageFromClient.equalsIgnoreCase("STOP")) {
            mPlayer = MediaPlayer.create(Activity_SpyConnection.this, R.raw.dtmf_3);
            mPlayer.start();

        }
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
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            serverSoc.close();
            serverSoc = null;
            dataInputStream = null;
            dataOutputStream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        status = null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO fix crash here, unable to open camera
        mCamera = Camera.open();
        params = mCamera.getParameters();
        previewSize = params.getPreviewSize();
        pixels = new int[previewSize.width * previewSize.height];


        try {
            mCamera.setPreviewCallback(this);
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
            mCamera.release();
            mCamera = null;

        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera = null;

    }

    private byte[] streamMJPEG;

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        mBitmap = Bitmap.createBitmap(pixels, previewSize.width, previewSize.height, Config.ARGB_8888);
        mBitmap.compress(CompressFormat.JPEG, 20, out);

        streamMJPEG = out.toByteArray();
      /*  new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                  }catch(NullPointerException e)
                {
                    Log.d("Spy Conn", "Catch d error::" +e);
                }catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
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

        }).start();*/
    }

}
