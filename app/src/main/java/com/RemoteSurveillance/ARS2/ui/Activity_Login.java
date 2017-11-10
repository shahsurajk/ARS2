package com.RemoteSurveillance.ARS2.ui;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.RemoteSurveillance.ARS2.ControllerConnection;
import com.RemoteSurveillance.ARS2.R;

public class Activity_Login extends AppCompatActivity {

    private ImageButton connectBtn;
    private EditText ipEditText, passwordEditText;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        ipEditText = (EditText) findViewById(R.id.iptxt);
        ipEditText.setText("192.168.0.1");

        passwordEditText = (EditText) findViewById(R.id.passtxt);
        passwordEditText.setText("123");
        connectBtn = (ImageButton) findViewById(R.id.authbtn);
        connectBtn.setOnClickListener(arg0 -> {
            String password, serverip = ipEditText.getText().toString();
            password = passwordEditText.getText().toString();
            if (password.equals("123")) {
                Intent go = new Intent(Activity_Login.this, ControllerConnection.class);
                go.putExtra("IP", serverip);
                startActivity(go);

            } else {
                Toast.makeText(Activity_Login.this, "Wrong Password! Try Again.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
