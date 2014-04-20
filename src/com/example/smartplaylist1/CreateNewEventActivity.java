/**
 * 
 */
package com.example.smartplaylist1;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

/**
 * @author thiag_000
 *
 */
public class CreateNewEventActivity extends Activity implements OnItemClickListener {
	private static final String TAG = "CreateNewEventActivity";
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String GEOCODE_API_BASE = "https://maps.googleapis.com/maps/api/geocode/json";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";
	String lat, lng;

	private static final String API_KEY = "AIzaSyA1AWGDPbOm7JfHqiYj27KngSW5TiG3Psg";

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_create_new_event);
	    AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
	    autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
	    autoCompView.setOnItemClickListener(this);
	    // TODO Auto-generated method stub
	}
	
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        Log.i(TAG, str);
        try {
        	//String geocode_url = URLEncoder.encode(str, "utf-8");
        	StringBuilder sb = new StringBuilder(GEOCODE_API_BASE);
            sb.append("?address="+URLEncoder.encode(str, "utf-8"));
            sb.append("&sensor=true&key="+URLEncoder.encode(API_KEY, "utf-8"));
            
            new AsyncFetchEvents().execute(sb.toString());
            Log.i("Akshay! sb", sb.toString());
        }
        catch(UnsupportedEncodingException e) {
        	Log.i(TAG, e.toString());
        }
    }
	
	private class AsyncFetchEvents extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
	        GetRequest latlongRequest = new GetRequest();
	       // latlongRequest.setURL(arg0[0]);
//	        latlongRequest.setURL("https://maps.googleapis.com/maps/api/geocode/json?address=116%20Ponce%20De%20Leon%20Avenue%20Northeast+Atlanta+GA+United+States&sensor=true&key=AIzaSyA1AWGDPbOm7JfHqiYj27KngSW5TiG3Psg");
		       
	        Log.i("Akshay! Request URL", arg0[0]);
	        
//	        String latlongRequestResult = latlongRequest.getData();
	        
			//Debug Code starts
			HttpURLConnection conn = null;
		    StringBuilder jsonResults = new StringBuilder();
			try {
				URL url = new URL(arg0[0].toString());
		        conn = (HttpURLConnection) url.openConnection();
		        InputStreamReader in = new InputStreamReader(conn.getInputStream());

		        // Load the results into a StringBuilder
		        int read;
		        char[] buff = new char[1024];
		        while ((read = in.read(buff)) != -1) {
		            jsonResults.append(buff, 0, read);
		        }
		    } catch (MalformedURLException e) {
		        Log.e(TAG, "Error processing Geocoding API URL", e);

		        
		    } catch (IOException e) {
		        Log.e(TAG, "Error connecting to Geocoding API", e);
		        
		    } finally {
		        if (conn != null) {
		            conn.disconnect();
		        }
		    }
			//Debug Code ends
			
	        return jsonResults.toString();
	 	        
		}
		
		protected void onPostExecute(String latlongRequestResult) {
			
		       try {

		        	JSONTokener tokener = new JSONTokener(latlongRequestResult);
		        	JSONObject output = new JSONObject(tokener);
		        	Log.i("TAG", output.toString());
		        	JSONArray results = output.getJSONArray("results");
		        	Log.i("TAG", results.toString());
		        	JSONObject firstObject = (JSONObject)results.get(0);
		        	Log.i("TAG", firstObject.toString());
		        	JSONObject geometry = firstObject.getJSONObject("geometry");
		        	JSONObject location = geometry.getJSONObject("location");
		        	lat = location.getString("lat");
		        	lng = location.getString("lng");
		        	Log.e("Akshay!", "Lat:"+lat+"Longitude"+lng);
			        

			    }catch(JSONException jse){
			    	Log.e(TAG, jse.toString());
			    }
		}
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
		new BasicNameValuePair("Latitude", lat),
		new BasicNameValuePair("Longitude", lng),
        });
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	
	private ArrayList<String> autocomplete(String input) {
	    ArrayList<String> resultList = null;

	    HttpURLConnection conn = null;
	    StringBuilder jsonResults = new StringBuilder();
	    try {
	        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
	        sb.append("?sensor=false&key=" + API_KEY);
	        sb.append("&components=country:us");
	        sb.append("&input=" + URLEncoder.encode(input, "utf8"));

	        URL url = new URL(sb.toString());
	        conn = (HttpURLConnection) url.openConnection();
	        InputStreamReader in = new InputStreamReader(conn.getInputStream());

	        // Load the results into a StringBuilder
	        int read;
	        char[] buff = new char[1024];
	        while ((read = in.read(buff)) != -1) {
	            jsonResults.append(buff, 0, read);
	        }
	    } catch (MalformedURLException e) {
	        Log.e(TAG, "Error processing Places API URL", e);
	        return resultList;
	    } catch (IOException e) {
	        Log.e(TAG, "Error connecting to Places API", e);
	        return resultList;
	    } finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }

	    try {
	        // Create a JSON object hierarchy from the results
	        JSONObject jsonObj = new JSONObject(jsonResults.toString());
	        JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

	        // Extract the Place descriptions from the results
	        resultList = new ArrayList<String>(predsJsonArray.length());
	        for (int i = 0; i < predsJsonArray.length(); i++) {
	            resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
	        }
	    } catch (JSONException e) {
	        Log.e(TAG, "Cannot process JSON results", e);
	    }

	    return resultList;
	}
	
	private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
	    private ArrayList<String> resultList;

	    public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
	        super(context, textViewResourceId);
	    }

	    @Override
	    public int getCount() {
	        return resultList.size();
	    }

	    @Override
	    public String getItem(int index) {
	        return resultList.get(index);
	    }

	    @Override
	    public Filter getFilter() {
	        Filter filter = new Filter() {
	            @Override
	            protected FilterResults performFiltering(CharSequence constraint) {
	                FilterResults filterResults = new FilterResults();
	                if (constraint != null) {
	                    // Retrieve the autocomplete results.
	                    resultList = autocomplete(constraint.toString());

	                    // Assign the data to the FilterResults
	                    filterResults.values = resultList;
	                    filterResults.count = resultList.size();
	                }
	                return filterResults;
	            }

	            @Override
	            protected void publishResults(CharSequence constraint, FilterResults results) {
	                if (results != null && results.count > 0) {
	                    notifyDataSetChanged();
	                }
	                else {
	                    notifyDataSetInvalidated();
	                }
	            }};
	        return filter;
	    }
	}
}
