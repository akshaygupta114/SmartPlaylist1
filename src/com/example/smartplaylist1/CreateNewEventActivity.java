/**
 * 
 */
package com.example.smartplaylist1;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * @author thiag_000
 *
 */
public class CreateNewEventActivity extends Activity {
	private static final String TAG = "CreateNewEventActivity";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_create_new_event);
	
	    // TODO Auto-generated method stub
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_created_events, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
		Intent intent;
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	        	// We do nothing here since we have no settings
	            // openSettings();
	            return true;
	        case R.id.logout:
	        	intent = new Intent(this, LoginSplashActivity.class);
	        	intent.putExtra(LoginSplashActivity.LOGIN_MESSAGE, LoginSplashActivity.LOGOUT);
	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		    	startActivity(intent);
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void createEvent(View v)
	{
		EditText edit_Name = (EditText) findViewById(R.id.edit_Name);
		String name = edit_Name.getText().toString();
		EditText edit_Description = (EditText) findViewById(R.id.edit_Description);
		String description = edit_Description.getText().toString();
		EditText edit_Venue = (EditText) findViewById(R.id.edit_Venue);
		String venue = edit_Venue.getText().toString();
		EditText edit_Latitude = (EditText) findViewById(R.id.edit_Latitude);
		String latitude = edit_Latitude.getText().toString();
		EditText edit_Longitude = (EditText) findViewById(R.id.edit_Longitude);
		String longitude = edit_Longitude.getText().toString();

		Log.i(TAG, "event name: "+name+" " );
		
		SharedPreferences settings = getSharedPreferences(LoginSplashActivity.LOGIN_PREFS_NAME, 0);
	    String facebookID = settings.getString("FacebookID", "");
	    if (facebookID == "") {
	    		// redirect to main
	    		Log.e(TAG, "NO FACEBOOKID");
	    }

		AsyncPostRequest login = new AsyncPostRequest();
		login.setURL("http://ec2-54-84-22-77.compute-1.amazonaws.com/epi/1/event");
		login.execute(new BasicNameValuePair[] {
		new BasicNameValuePair("FacebookID", facebookID),
		new BasicNameValuePair("EventName", name),
		new BasicNameValuePair("EventDesc", description),
		new BasicNameValuePair("EventVenue", venue),
		new BasicNameValuePair("Latitude", latitude),
		new BasicNameValuePair("Longitude", longitude),
        });
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	
}
