package com.example.smartplaylist1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ViewCreatedEventsActivity extends FragmentActivity {
	
	List<Map<String, String>> planetsList = new ArrayList<Map<String,String>>();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_created_events);
		//Intent intent = getIntent();
		
		initList();
	     
	    // We get the ListView component from the layout
	    ListView lv = (ListView) findViewById(R.id.listView);
	     
	     
	    // This is a simple adapter that accepts as parameter
	    // Context
	    // Data list
	    // The row layout that is used during the row creation
	    // The keys used to retrieve the data
	    // The View id used to show the data. The key number and the view id must match
	    SimpleAdapter simpleAdpt = new SimpleAdapter(this, planetsList, android.R.layout.simple_list_item_1, new String[] {"planet"}, new int[] {android.R.id.text1});
	    
	 
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

}
