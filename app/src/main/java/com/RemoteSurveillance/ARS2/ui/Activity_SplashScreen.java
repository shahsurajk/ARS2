package com.RemoteSurveillance.ARS2.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.RemoteSurveillance.ARS2.R;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Activity_SplashScreen extends AppCompatActivity {

    private static final int DELAY_IN_MILLIS = 2*1000;
    private static final int RC_APP_PERM = 1234;
    private static final String[] APP_PERMISSIONS = {CAMERA, RECORD_AUDIO, WRITE_EXTERNAL_STORAGE};
    private static final String TAG = Activity_SplashScreen.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_splash);

//        ask for permissions
        checkForPermissionsAndRedirect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkForPermissionsAndRedirect() {
        if (EasyPermissions.hasPermissions(this, APP_PERMISSIONS)) {
            redirect();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.app_permissions), RC_APP_PERM, APP_PERMISSIONS);
        }
    }

    @AfterPermissionGranted(RC_APP_PERM)
    private void redirect() {
        new Handler().postDelayed(() -> {
            Intent loaded = new Intent(Activity_SplashScreen.this, Activity_Dashboard.class);
            startActivity(loaded);
        }, DELAY_IN_MILLIS);
    }
}

