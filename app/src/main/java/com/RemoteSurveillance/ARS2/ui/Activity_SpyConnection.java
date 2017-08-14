package com.RemoteSurveillance.ARS2.ui;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
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
    private Bitmap mBitmap;
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
        ButterKnife.bind(this);

        infoTV.setText("");
        msgTV.setText("");

        mHolder = surfaceView.getHolder();
        mHolder.addCallback(this);

        //display IP to connect to..
        infoTV.setText("Hey! I'm waiting here; at IP:: "+Utils.getIpAddress("")+ "And Port:: " + PORT);

        try {
            serverSoc = new ServerSocket(PORT);

        } catch (IOException e) {
            e.printStackTrace();
        }

        mCamera = Camera.open();

        Flowable f1 = Flowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(@NonNull FlowableEmitter e) throws Exception {
                if (clientSoc== null)
                    clientSoc = serverSoc.accept();
                dataInputStream = new DataInputStream(clientSoc.getInputStream());
                dataOutputStream = new DataOutputStream(clientSoc.getOutputStream());
                while (clientSoc.isConnected()){
                    e.onNext(dataInputStream.readUTF());
                }
            }
        }, BackpressureStrategy.BUFFER).onErrorReturnItem("STOP");

        f1.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.d(TAG, "accept: "+o);
                        messageFromClient = (String) o;
//                        playDTMFTones();
                    }
                });
        Flowable f2 = Flowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(@NonNull FlowableEmitter e) throws Exception {
                mCamera.setPreviewCallback(new PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        try {
                            if (dataOutputStream!=null){
                                dataOutputStream.writeInt(data.length);
                                dataOutputStream.write(data);
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        }, BackpressureStrategy.BUFFER).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                messageFromClient = "STOP";
                playDTMFTones();
            }
        });
        f2.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(5000, TimeUnit.MILLISECONDS)
                .subscribe();
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
        pixels = new int[previewSize.width * previewSize.height];

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
        mCamera.release();
    }

    private byte[] streamMJPEG = new byte[0];

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

                if (r < 0) r = 0;
                else if (r > 262143) r = 262143;
                if (g < 0) g = 0;
                else if (g > 262143) g = 262143;
                if (b < 0) b = 0;
                else if (b > 262143) b = 262143;

                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
            }
        }
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
