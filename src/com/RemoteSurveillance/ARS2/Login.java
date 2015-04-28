package com.RemoteSurveillance.ARS2;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class Login extends Activity {

	ImageButton connect; 
	EditText ip, pass; 
	//Boolean isLogin= false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.login);
		
		ip= (EditText) findViewById(R.id.iptxt);
		ip.setText("Set Server IP here!");
	
		pass= (EditText) findViewById(R.id.passtxt);
		pass.setText("Password");
		connect= (ImageButton) findViewById(R.id.authbtn);
	connect.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String  password, serverip= ip.getText().toString();
			password= pass.getText().toString();
			if(password.equals("123"))
			{
				Intent go= new Intent(Login.this, ControllerConnection.class);
				go.putExtra("IP", serverip);
				startActivity(go);
				
			}else
			{
				Toast.makeText(Login.this, "Wrong Password! Try Again.", Toast.LENGTH_SHORT).show();
			}
		}
	});
	}
	

	
}
