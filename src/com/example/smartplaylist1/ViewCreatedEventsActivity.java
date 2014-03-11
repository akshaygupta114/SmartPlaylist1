package com.example.smartplaylist1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

public class ViewCreatedEventsActivity extends FragmentActivity {
	
	private static final String TAG = "ViewCreatedEventsActivity";
	SimpleAdapter simpleAdpt;
	
	String eventsJSON = "{{\"OwnerID\":\"674481742\",\"EventName\":\"Fund Raiser 5k\",\"Loc\":{\"Latitude\":\"33.7896627\",\"Longitude\":\"-84.3946271\"},\"EventDesc\":\"A fund raiser 5k, Share your playlists if you will attend\",\"EventVenue\":\"Griffin Track\"},{\"OwnerID\":\"674481742\",\"EventName\":\"Mobile Apps Lab Party\",\"Loc\":{\"Latitude\":\"33.7773242\",\"Longitude\":\"-84.3899984\"},\"EventDesc\":\"Party at Mobile Apps Lab\",\"EventVenue\":\"Mobile Apps Lab\"}}";
	JSONArray events; 
	List<Map<String, String>> EventsList = new ArrayList<Map<String,String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_created_events);

		Session session = Session.getActiveSession();
		Request.newMeRequest(session, 
        		new Request.GraphUserCallback() {
					
					@Override
					public void onCompleted(GraphUser user, Response response) {
						// TODO Auto-generated method stub
						Log.i(TAG, user.getId());
						String facebookID = user.getId();

						new AsyncFetchEvents().execute(facebookID);
					}
				}).executeAsync();
		
		
		initList();
	     
	    // We get the ListView component from the layout
	    ListView lv = (ListView) findViewById(R.id.listView);
	     
	     
	    // This is a simple adapter that accepts as parameter
	    // Context
	    // Data list
	    // The row layout that is used during the row creation
	    // The keys used to retrieve the data
	    // The View id used to show the data. The key number and the view id must match
	    simpleAdpt = new SimpleAdapter(this, EventsList, android.R.layout.simple_list_item_1, new String[] {"event"}, new int[] {android.R.id.text1});    
	 
	    lv.setAdapter(simpleAdpt);

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
	
	public void createNewEvent(View view) {
		Intent intent = new Intent(this, CreateNewEventActivity.class);
		startActivity(intent);
	}
	
	private void initList() {
	    // We populate the events
	    EventsList.add(createEvent("event", "Connecting to Server..."));  
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
	                // Pulling items from the array
	                String eventName = oneObject.getString("EventName");
	                Log.i(TAG, "EventName "+eventName);
	                String eventVenue = oneObject.getString("EventVenue");
	                Log.i(TAG, "EventVenue "+eventVenue);
	                EventsList.add(createEvent("event", eventName + "\n at " + eventVenue));
		        }
		    }catch(JSONException jse){
		    	//oops
		    }
			Log.i(TAG, EventsList.toString()+""+EventsList.size());
			simpleAdpt.notifyDataSetChanged();
		}

	}
}
