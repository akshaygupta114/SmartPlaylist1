package com.example.smartplaylist1;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
 
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CreateEventsFragment extends Fragment {
	
	 // Google Map
    private GoogleMap googleMap;
    double lat;
    double log;
    
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
            
            _getLocation();
            double latitude = lat; //17.385044;
            double longitude = log; //78.486671;

            // lets place some 10 random markers
            for (int i = 0; i < 10; i++) {
                // random latitude and logitude
                double[] randomLocation = createRandLocation(latitude,
                        longitude);

                // Adding a marker
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(randomLocation[0], randomLocation[1]))
                        .title("Hello Maps " + i);

                Log.e("Random", "> " + randomLocation[0] + ", "
                        + randomLocation[1]);

                // changing marker color
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

                // Move the camera to last position with a zoom level
                if (i == 9) {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(randomLocation[0],
                                    randomLocation[1])).zoom(15).build();

                    googleMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                }
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }
    
    private void _getLocation() {
        // Get the location manager
        LocationManager locationManager = (LocationManager) 
                getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        try {
            lat = location.getLatitude();
            log = location.getLongitude();
        } catch (NullPointerException e) {
            lat = -1.0;
            log = -1.0;
        }
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

    /*
     * creating random postion around a location for testing purpose only
     */
    private double[] createRandLocation(double latitude, double longitude) {

        return new double[] { latitude + ((Math.random() - 0.5) / 500),
                longitude + ((Math.random() - 0.5) / 500),
                150 + ((Math.random() - 0.5) * 10) };
    }
}