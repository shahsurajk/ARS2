package com.RemoteSurveillance.ARS2.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.RemoteSurveillance.ARS2.R;
import com.RemoteSurveillance.ARS2.Utils;

import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.rtsp.RtspServer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Activity_RTSPServer_Spy extends AppCompatActivity {

    private static final String TAG = Activity_RTSPServer_Spy.class.getCanonicalName();
    @BindView(R.id.surface)
    SurfaceView surfaceView;
    @BindView(R.id.msgTxt)
    TextView msgTxt;
    @BindView(R.id.spytxt)
    TextView spytxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_rtspserver_spy);
        ButterKnife.bind(this);

        // Sets the port of the RTSP server to 1234
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString(RtspServer.KEY_PORT, String.valueOf(1234));
        editor.commit();

        spytxt.setText("Listening for client on: " + Utils.getInetIPAddress());
        msgTxt.setVisibility(View.GONE);

        SessionBuilder.getInstance()
                .setSurfaceView(surfaceView)
                .setPreviewOrientation(90)
                .setContext(getApplicationContext())
                .setAudioEncoder(SessionBuilder.AUDIO_NONE)
                .setVideoEncoder(SessionBuilder.VIDEO_H264);

        // Starts the RTSP server
        this.startService(new Intent(this, RtspServer.class));
    }
}
