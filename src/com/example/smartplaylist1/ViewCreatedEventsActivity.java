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

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ViewCreatedEventsActivity extends FragmentActivity {
	
	private static final String TAG = "ViewCreatedEventsActivity";
	SimpleAdapter simpleAdpt;
	
	String eventsJSON = "{{\"OwnerID\":\"674481742\",\"EventName\":\"Fund Raiser 5k\",\"Loc\":{\"Latitude\":\"33.7896627\",\"Longitude\":\"-84.3946271\"},\"EventDesc\":\"A fund raiser 5k, Share your playlists if you will attend\",\"EventVenue\":\"Griffin Track\"},{\"OwnerID\":\"674481742\",\"EventName\":\"Mobile Apps Lab Party\",\"Loc\":{\"Latitude\":\"33.7773242\",\"Longitude\":\"-84.3899984\"},\"EventDesc\":\"Party at Mobile Apps Lab\",\"EventVenue\":\"Mobile Apps Lab\"}}";
	JSONArray events; 
	List<Map<String, String>> planetsList = new ArrayList<Map<String,String>>();
	

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
		
		/*AsyncGetRequest login = new AsyncGetRequest();
		login.setURL(URL);
		login.execute();
		*/
		
		initList();
	     
	    // We get the ListView component from the layout
	    ListView lv = (ListView) findViewById(R.id.listView);
	     
	     
	    // This is a simple adapter that accepts as parameter
	    // Context
	    // Data list
	    // The row layout that is used during the row creation
	    // The keys used to retrieve the data
	    // The View id used to show the data. The key number and the view id must match
	    simpleAdpt = new SimpleAdapter(this, planetsList, android.R.layout.simple_list_item_1, new String[] {"planet"}, new int[] {android.R.id.text1});
	    
	 
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
	
	private void initList() {
	    // We populate the planets
	    planetsList.add(createPlanet("planet", "Mercury"));
	    planetsList.add(createPlanet("planet", "Venus"));
	    planetsList.add(createPlanet("planet", "Mars"));
	    planetsList.add(createPlanet("planet", "Jupiter"));
	    planetsList.add(createPlanet("planet", "Saturn"));
	    planetsList.add(createPlanet("planet", "Uranus"));
	    planetsList.add(createPlanet("planet", "Neptune"));   
	}
	 
	private HashMap<String, String> createPlanet(String key, String name) {
	    HashMap<String, String> planet = new HashMap<String, String>();
	    planet.put(key, name);
	     
	    return planet;
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
			planetsList.clear();
			planetsList.add(createPlanet("planet", "JSON : " + str));
			Log.i(TAG, "Returned shit:" + str);
			simpleAdpt.notifyDataSetChanged();
		}

	}
}
