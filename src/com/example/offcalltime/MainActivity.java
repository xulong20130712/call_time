package com.example.offcalltime;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_main, null));
		
		Button button= (Button) findViewById(R.id.start);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent= new Intent();
				intent.setAction(Intent.ACTION_NEW_OUTGOING_CALL);
				intent.setAction(Intent.ACTION_CALL);
				sendBroadcast(intent);
			}
		});
	}
}
