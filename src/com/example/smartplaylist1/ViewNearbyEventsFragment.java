package com.example.smartplaylist1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewNearbyEventsFragment extends Fragment {
	
	 // Google Map
    private GoogleMap googleMap;
    double lat;
    double log;
    String eventId;
    private static final String getEventsURL= "http://ec2-54-84-22-77.compute-1.amazonaws.com/epi/1/event";
    private static final String TAG = "ViewNearbyEventsFragment";
	
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_map_view_events, container, false);

 
        try {
            // Loading map
            initilizeMap();

            // Changing map type
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

            // Showing / hiding your current location
            googleMap.setMyLocationEnabled(true);

            // Enable / Disable zooming controls
            googleMap.getUiSettings().setZoomControlsEnabled(false);

            // Enable / Disable my location button
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);

            // Enable / Disable Compass icon
            googleMap.getUiSettings().setCompassEnabled(true);

            // Enable / Disable Rotate gesture
            googleMap.getUiSettings().setRotateGesturesEnabled(true);

            // Enable / Disable zooming functionality
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
            
            Location location = _getLocation();
            
            try {
                lat = location.getLatitude();
                log = location.getLongitude();
            } catch (NullPointerException e) {
                lat = 33.780969;
                log = -84.400387;
            }
            getNearbyEvents(lat, log);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }
    
    private Location _getLocation() {
        // Get the location manager
        LocationManager locationManager = (LocationManager) 
                getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        return location;
    }
    
    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
 
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText((getActivity()).getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
 
    @Override
	public void onResume() {
        super.onResume();
        initilizeMap();
    }

    public void getNearbyEvents(double latitude, double longitude)
    {
    	try {
        	StringBuilder sb = new StringBuilder("http://ec2-54-84-22-77.compute-1.amazonaws.com/epi/1/eventsnear?Longitude=");
            sb.append(longitude);
            sb.append("&Latitude="+latitude);
            
            new AsyncFetchEvents().execute(sb.toString());
            Log.i("Akshay! sb", sb.toString());
        }
        catch(Exception e) {
        	Log.i(TAG, e.toString());
        }
    }
    
    private class AsyncFetchEvents extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			GetRequest req = new GetRequest();
			Log.e(TAG+"Akshay!! URL", arg0[0].toString());
			req.setURL(arg0[0]);
			String str = req.getData();
			Log.i(TAG, "BACKGROUND:" + str);
			return str;
		}
		
		protected void onPostExecute(String str) {
			try {
		        JSONTokener tokener = new JSONTokener(str);
		        JSONArray jArray = new JSONArray(tokener);
		        for (int i=0; i < jArray.length(); i++)
		        {
	                JSONObject oneObject = jArray.getJSONObject(i);
	                Log.e("Akshay!!!"+i, oneObject.toString());
	                JSONObject Loc = oneObject.getJSONObject("Loc");
	                eventId = oneObject.getString("_id");
	                Double latitude = Loc.getDouble("Latitude");
	                Double longitude = Loc.getDouble("Longitude");
	                String eventName = oneObject.getString("EventName");
	                MarkerOptions marker = new MarkerOptions().position(
                            new LatLng(latitude, longitude))
                            .title("Share Playlist with "+eventName);
	                
	                
	              //Changing Marker Color
                    if (i == 0)
                        marker.icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    if (i == 1)
                        marker.icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    if (i == 2)
                        marker.icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                    if (i == 3)
                        marker.icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    if (i == 4)
                        marker.icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    if (i == 5)
                        marker.icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                    if (i == 6)
                        marker.icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    if (i == 7)
                        marker.icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                    if (i == 8)
                        marker.icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                    if (i == 9)
                        marker.icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

                    googleMap.addMarker(marker);
                    googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            Log.e("Akshay!!! Map infoWindow clicked", marker.getTitle());
                            try {
                            	SharedPreferences settings = getActivity().getSharedPreferences(LoginSplashActivity.LOGIN_PREFS_NAME, 0);
                        	    String facebookID = settings.getString("FacebookID", "");
                        	    if (facebookID == "") {
                        	    		// redirect to main
                        	    		Log.e("BLAHBLAH", "NO FACEBOOKID");
                        	    }
                            	StringBuilder sb = new StringBuilder("http://ec2-54-84-22-77.compute-1.amazonaws.com/epi/1/sharePlaylistWith?eventID=");
                                sb.append(eventId);
                                sb.append("?FacebookID="+facebookID);
                                Log.i("Akshay! URL Sent on Share Playlist", sb.toString());
                                new AsyncSharePlaylist().execute(sb.toString());
                                
                            }
                            catch(Exception e) {
                            	Log.i(TAG, e.toString());
                            }
                        }
                        
                        class AsyncSharePlaylist extends AsyncTask<String, Integer, String> {
                    		@Override
                    		protected String doInBackground(String... arg0) {
                    			// TODO Auto-generated method stub
                    			GetRequest req = new GetRequest();
                    			Log.e(TAG+"Akshay!! URL", arg0[0].toString());
                    			req.setURL(arg0[0]);
                    			req.getData();
                    			return "";
                    		}
                        }
                    });

                // Move the camera to last position with a zoom level
                if (i == 9) {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(lat,
                                    log)).zoom(15).build();

                    googleMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                }
	                
		        }
	                
		    }catch(JSONException jse){
		    	Log.e(TAG, jse.toString());
		    }
		}

	}
}