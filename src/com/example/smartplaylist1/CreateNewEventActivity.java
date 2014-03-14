package com.example.smartplaylist1;


import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class CreateNewEventActivity extends Activity {

	private static final String FacebookID= "100000492412441"; 
	private static final String TAG = "CreateNewEventActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_event);
		/*
		EditText edit_Name = (EditText) findViewById(R.id.edit_Name);
		String name = edit_Name.getText().toString();
		EditText edit_Description = (EditText) findViewById(R.id.edit_Description);
		String description = edit_Name.getText().toString();
		EditText edit_Venue = (EditText) findViewById(R.id.edit_Venue);
		String venue = edit_Name.getText().toString();
		EditText edit_Latitude = (EditText) findViewById(R.id.edit_Latitude);
		String latitude = edit_Name.getText().toString();
		EditText edit_Longitude = (EditText) findViewById(R.id.edit_Longitude);
		String longitude = edit_Name.getText().toString();
		
		Log.v(TAG, ""+name+" " );
		
		AsyncPostRequest login = new AsyncPostRequest();
		login.setURL("http://ec2-54-84-22-77.compute-1.amazonaws.com/epi/1/event");
		login.execute(new BasicNameValuePair[] {
		new BasicNameValuePair("FacebookID", FacebookID),
		new BasicNameValuePair("EventName", name),
		new BasicNameValuePair("EventDesc", description),
		new BasicNameValuePair("EventVenue", venue),
		new BasicNameValuePair("Latitude", latitude),
		new BasicNameValuePair("Longitude", longitude),
        });
        */
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_new_event, menu);
		return true;
	}
	
	public void createEvent(View v)
	{
		EditText edit_Name = (EditText) findViewById(R.id.edit_Name);
		String name = edit_Name.getText().toString();
		EditText edit_Description = (EditText) findViewById(R.id.edit_Description);
		String description = edit_Name.getText().toString();
		EditText edit_Venue = (EditText) findViewById(R.id.edit_Venue);
		String venue = edit_Name.getText().toString();
		EditText edit_Latitude = (EditText) findViewById(R.id.edit_Latitude);
		String latitude = edit_Name.getText().toString();
		EditText edit_Longitude = (EditText) findViewById(R.id.edit_Longitude);
		String longitude = edit_Name.getText().toString();
		
		Log.i(TAG, "event name: "+name+" " );
		
		AsyncPostRequest login = new AsyncPostRequest();
		login.setURL("http://ec2-54-84-22-77.compute-1.amazonaws.com/epi/1/event");
		login.execute(new BasicNameValuePair[] {
		new BasicNameValuePair("FacebookID", FacebookID),
		new BasicNameValuePair("EventName", name),
		new BasicNameValuePair("EventDesc", description),
		new BasicNameValuePair("EventVenue", venue),
		new BasicNameValuePair("Latitude", latitude),
		new BasicNameValuePair("Longitude", longitude),
        });
        
	}
}