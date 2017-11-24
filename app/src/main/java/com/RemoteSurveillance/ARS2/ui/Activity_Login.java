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

import com.RemoteSurveillance.ARS2.R;

public class Activity_Login extends AppCompatActivity {

    public static final String KEY_SERVER_IP = "server_ip_address";
    private ImageButton connectBtn;
    private EditText ipEditText, passwordEditText;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        ipEditText = findViewById(R.id.iptxt);
//        todo reset
        ipEditText.setText("192.168.11.182");

        passwordEditText = findViewById(R.id.passtxt);
        passwordEditText.setText("123");
        connectBtn = findViewById(R.id.authbtn);
        connectBtn.setOnClickListener(arg0 -> {
            String password, serverip = ipEditText.getText().toString();
            password = passwordEditText.getText().toString();
            if (password.equals("123")) {
                Intent clientIntent = new Intent(Activity_Login.this, Activity_RTSPClientConnection.class);
                clientIntent.putExtra(KEY_SERVER_IP, serverip);
                startActivity(clientIntent);

            } else {
                Toast.makeText(Activity_Login.this, "Wrong Password! Try Again.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
