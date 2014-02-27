package com.example.smartplaylist1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;

public class ViewCreatedEventsActivity extends FragmentActivity {
	
	private MainFragment mainFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_created_events);
		//Intent intent = getIntent();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_created_events, menu);
		return true;
	}
	
	//Called when Back to Login button is clicked
	public void backToLogin(View view) {
		
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

}
