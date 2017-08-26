package com.RemoteSurveillance.ARS2.ui;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.RemoteSurveillance.ARS2.R;
import com.RemoteSurveillance.ARS2.util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * The server class
 *
 * */

public class Activity_SpyConnection extends Activity implements SurfaceHolder.Callback {

    private static final String TAG = Activity_SpyConnection.class.getCanonicalName().toString();
    private MediaPlayer mPlayer;
    private ServerSocket serverSoc;
    private Socket clientSoc;
    private static final int PORT = 45678;
    @BindView(R.id.spytxt)TextView infoTV;
    @BindView(R.id.msg)TextView msgTV;
    @BindView(R.id.surface)SurfaceView surfaceView;
    private String messageFromClient;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private SurfaceHolder mHolder;
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
        ButterKnife.bind(this);

        infoTV.setText("");
        msgTV.setText("");

        mHolder = surfaceView.getHolder();
        mHolder.addCallback(this);

        //display IP to connect to..
        infoTV.setText("Hey! I'm waiting here; at IP:: " + Utils.getIpAddress("") + "And Port:: " + PORT);

        try {
            serverSoc = new ServerSocket(PORT);

        } catch (IOException e) {
            e.printStackTrace();
        }

        mCamera = Camera.open();

        Flowable f1 = Flowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(@NonNull FlowableEmitter e) throws Exception {
                if (clientSoc == null)
                    clientSoc = serverSoc.accept();
                dataInputStream = new DataInputStream(clientSoc.getInputStream());
                dataOutputStream = new DataOutputStream(clientSoc.getOutputStream());
                while (clientSoc.isConnected()) {
                    String msg = dataInputStream.readUTF();
                    e.onNext(msg);
                    dataOutputStream.writeUTF(" ");
                }
            }
        }, BackpressureStrategy.BUFFER).onErrorReturnItem("STOP");

        f1.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.d(TAG, "accept: " + o);
                        messageFromClient = (String) o;
//                        playDTMFTones();
                    }
                });
    }
    private void playDTMFTones(){
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataInputStream = null;
        dataOutputStream = null;
        mCamera.release();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        params = mCamera.getParameters();
        previewSize = params.getPreviewSize();
        mCamera.getParameters().setPreviewFormat(ImageFormat.RGB_565);
        mCamera.setPreviewCallback(new PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                try {
                    if (dataOutputStream != null) {

                        ByteArrayOutputStream out= new ByteArrayOutputStream();
                        Bitmap mBitmap = Bitmap.createBitmap(previewSize.width, previewSize.height, Bitmap.Config.ARGB_8888);
                        mBitmap.compress(Bitmap.CompressFormat.JPEG	, 25, out);
                        byte[]streamJPEG = out.toByteArray();
                        dataOutputStream.writeInt(streamJPEG.length);
                        dataOutputStream.write(streamJPEG);
                        dataOutputStream.flush();
                    }
                } catch (SocketException e){
                    e.printStackTrace();
               }catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
            mCamera.release();
        }
        mCamera.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
//        mCamera.release();
    }


/*

    @Override
    public void onPreviewFrame(final byte[] data, Camera camera) {
        ;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        decodeYUV420(pixels, data, previewSize.width, previewSize.height);
        mBitmap = Bitmap.createBitmap(pixels, previewSize.width, previewSize.height, Config.ARGB_8888);
        mBitmap.compress(CompressFormat.JPEG, 20, out);
        streamMJPEG = out.toByteArray();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (dataOutputStream!=null){
                        dataOutputStream.writeInt(streamMJPEG.length);
                        dataOutputStream.write(streamMJPEG);
                    }
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NegativeArraySizeException e) {
                    Log.d("Spy Conn", "Negative Size?" + e);
                } catch (ArrayStoreException e) {
                    e.printStackTrace();
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }
*/

}
