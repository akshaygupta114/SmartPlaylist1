/**
 * 
 */
package com.example.smartplaylist1;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * @author thiag_000
 *
 */
public class CreateNewEvent extends Activity {
	private static final String TAG = "CreateNewEventActivity";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_create_new_event);
	
	    // TODO Auto-generated method stub
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
		
		SharedPreferences settings = getSharedPreferences(MainActivity.LOGIN_PREFS_NAME, 0);
	    String facebookID = settings.getString("FacebookID", "");
	    if (facebookID == "") {
	    		// redirect to main
	    		Log.e("BLAHBLAH", "NO FACEBOOKID");
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
	}
	
}
