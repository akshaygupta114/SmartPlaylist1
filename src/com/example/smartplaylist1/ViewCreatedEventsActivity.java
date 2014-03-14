package com.example.smartplaylist1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import eu.erikw.PullToRefreshListView;

public class ViewCreatedEventsActivity extends FragmentActivity {
	
	private static final String TAG = "ViewCreatedEventsActivity";
	SimpleAdapter simpleAdpt;
	PullToRefreshListView lv;
	
	List<Map<String, String>> EventsList = new ArrayList<Map<String,String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_created_events);

        // We get the ListView component from the layout
		lv = (PullToRefreshListView) findViewById(R.id.listView);
		
		lv.setRefreshing();
		lv.setEnabled(false);

		SharedPreferences settings = getSharedPreferences(MainActivity.LOGIN_PREFS_NAME, 0);
	    String facebookID = settings.getString("FacebookID", "");
	    if (facebookID == "") {
	    		// redirect to main
	    		Log.e("BLAHBLAH", "NO FACEBOOKID");
	    }
	    new AsyncFetchEvents().execute(facebookID);

		
		lv.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			
			@Override
			public void onRefresh() {
            	SharedPreferences settings = getSharedPreferences(MainActivity.LOGIN_PREFS_NAME, 0);
        	    String facebookID = settings.getString("FacebookID", "");
        	    if (facebookID == "") {
        	    		// redirect to main
        	    		Log.e("BLAHBLAH", "NO FACEBOOKID");
        	    }
        	    new AsyncFetchEvents().execute(facebookID);
				
			}
		});
		


	    // This is a simple adapter that accepts as parameter
	    // Context
	    // Data list
	    // The row layout that is used during the row creation
	    // The keys used to retrieve the data
	    // The View id used to show the data. The key number and the view id must match
	    simpleAdpt = new SimpleAdapter(this, EventsList, R.layout.list_location_view, new String[] {"title", "location"}, new int[] {R.id.title, R.id.location});    
	 
	    lv.setAdapter(simpleAdpt);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_created_events, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
		Intent intent;
	    switch (item.getItemId()) {
	        case R.id.action_new:
	            // Open new activity for this
	        	intent = new Intent(this, CreateNewEvent.class);
		    	startActivity(intent);
	            return true;
	        case R.id.action_settings:
	        	// We do nothing here since we have no settings
	            // openSettings();
	            return true;
	        case R.id.logout:
	        	intent = new Intent(this, MainActivity.class);
	        	intent.putExtra(MainActivity.LOGIN_MESSAGE, MainActivity.LOGOUT);
		    	startActivity(intent);
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	//Called when Back to Login button is clicked
	public void backToLogin(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	 
	private HashMap<String, String> createEvent(String key, String name) {
	    HashMap<String, String> event = new HashMap<String, String>();
	    event.put(key, name);
	     
	    return event;
	}
	
	private class AsyncFetchEvents extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			String URL= "http://ec2-54-84-22-77.compute-1.amazonaws.com/epi/1/event";
			
			URL= URL.concat("?FacebookID="+ arg0[0]);

			// TODO Auto-generated method stub
			GetRequest req = new GetRequest();
			req.setURL(URL);
			String str = req.getData();
			Log.i(TAG, "BACKGROUND:" + str);
			return str;
		}
		
		protected void onPostExecute(String str) {
			EventsList.clear();
			//eventsList.add(createEvent("event", "JSON : " + str));
			
			try {
		        JSONTokener tokener = new JSONTokener(str);
		        JSONArray jArray = new JSONArray(tokener);
		        for (int i=0; i < jArray.length(); i++)
		        {
	                JSONObject oneObject = jArray.getJSONObject(i);
	                HashMap<String, String> mp = createEvent("title", oneObject.getString("EventName"));
	                mp.put("location", oneObject.getString("EventVenue"));
	                // Pulling items from the array
	                String eventName = oneObject.getString("EventName");
	                Log.i(TAG, "EventName "+eventName);
	                String eventVenue = oneObject.getString("EventVenue");
	                Log.i(TAG, "EventVenue "+eventVenue);
	                EventsList.add(mp);
		        }
		    }catch(JSONException jse){
		    	//oops
		    }
			simpleAdpt.notifyDataSetChanged();
			lv.onRefreshComplete();
			lv.setEnabled(true);
		}

	}
}
