package com.example.smartplaylist1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


import eu.erikw.PullToRefreshListView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.widget.TextView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
 
public class ViewCreatedEventsFragment extends Fragment {
    
    private static final String TAG = "ViewCreatedEventsActivity";
	SimpleAdapter simpleAdpt;
	PullToRefreshListView lv;
	
	List<Map<String, String>> EventsList = new ArrayList<Map<String,String>>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View rootView = inflater.inflate(R.layout.activity_view_created_events, container, false);

        // We get the ListView component from the layout
		Log.e("BLAHBLAH", "" + (rootView.findViewById(R.id.listView) == null));
		lv = (PullToRefreshListView) rootView.findViewById(R.id.listView);
		
		lv.setRefreshing();
		lv.setEnabled(false);
		lv.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
				String itemSelected =  (String) ((TextView)arg1.findViewById(R.id.eventId)).getText();
				System.out.println("TextView vehicleSrc"+itemSelected);
				
				Intent intent = new Intent(getActivity(), ViewRecommendedPlaylistActivity.class);
				intent.putExtra("id", itemSelected);
		    	startActivity(intent);
			}
		});

		SharedPreferences settings = getActivity().getSharedPreferences(LoginSplashActivity.LOGIN_PREFS_NAME, 0);
	    String facebookID = settings.getString("FacebookID", "");
	    if (facebookID == "") {
	    		// redirect to main
	    		Log.e("BLAHBLAH", "NO FACEBOOKID");
	    }
	    new AsyncFetchEvents().execute(facebookID);

		
		lv.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			
			@Override
			public void onRefresh() {
            	SharedPreferences settings = getActivity().getSharedPreferences(LoginSplashActivity.LOGIN_PREFS_NAME, 0);
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
	    simpleAdpt = new SimpleAdapter(getActivity(), EventsList, R.layout.list_location_view, new String[] {"title", "location", "eventId"}, new int[] {R.id.title, R.id.location, R.id.eventId});    
	 
	    lv.setAdapter(simpleAdpt);
	    
	    return rootView;
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
	                HashMap<String, String> mp = new HashMap<String,String>();
	                mp.put("title", oneObject.getString("EventName"));
	                mp.put("location", oneObject.getString("EventVenue"));
	                mp.put("eventId", oneObject.getString("_id"));
	                // Pulling items from the array
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