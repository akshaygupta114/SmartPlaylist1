package com.example.smartplaylist1;

import org.apache.http.message.BasicNameValuePair;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

public class MainActivity extends FragmentActivity {
	public static final String LOGIN_PREFS_NAME = "FacebookLoginDetails";
	public static final String LOGIN_MESSAGE = "LoginMessage";
	public static final String LOGIN = "Login";
	public static final String LOGOUT = "Logout";
	private static final String TAG = "BLAH";
	
	private MainFragment mainFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    if (savedInstanceState == null) {
	        // Add the fragment on initial activity setup
	        mainFragment = new MainFragment();
	        getSupportFragmentManager()
	        .beginTransaction()
	        .add(android.R.id.content, mainFragment)
	        .commit();
	    } else {
	        // Or set the fragment from restored state info
	        mainFragment = (MainFragment) getSupportFragmentManager()
	        .findFragmentById(android.R.id.content);
	    }
	    
		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.LOGIN_MESSAGE);
		if (message != null && message.equals(LOGOUT)) {
			Logout();
		}
	}

		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	public void Login() {
		findViewById(R.id.authButton).setVisibility(View.GONE);
		Log.e("BLAHBLAH", "HERE");
		final Session session = Session.getActiveSession();
		Log.e("BLAHBLAH", "HERE" + session.isOpened() + session.getState());
        Request.newMeRequest(session, 
        		new Request.GraphUserCallback() {
					
					@Override
					public void onCompleted(GraphUser user, Response response) {
						Log.e("BLAHBLAH", "after on completed" + (user == null));
						// TODO Auto-generated method stub
						String facebookID = user.getId();
						
						AsyncPostRequest login = new AsyncPostRequest();
						login.setURL("http://54.84.22.77/epi/1/login");
						login.execute(new BasicNameValuePair[] {
						new BasicNameValuePair("FacebookID", facebookID),
				        new BasicNameValuePair("AccessToken", session.getAccessToken())});
						
						SharedPreferences settings = getSharedPreferences(MainActivity.LOGIN_PREFS_NAME, 0);
						SharedPreferences.Editor editor = settings.edit();
						editor.putString("FacebookID", user.getId());
						editor.putString("AccessToken", session.getAccessToken());
						editor.commit();
						
						Context ctxt = getApplicationContext();
				        Intent intent = new Intent(ctxt, TabbedActivity.class);
				    	startActivity(intent);

					}
				}).executeAsync();
	}

	public boolean isLoggedIn() {
		Session session = Session.getActiveSession();
		if(!session.isOpened()) {
			return false;
		}
		
		SharedPreferences settings = getSharedPreferences(MainActivity.LOGIN_PREFS_NAME, 0);
	    String facebookID = settings.getString("FacebookID", "");
	    if (facebookID == "") {
	    		return false;
	    }
	    
		return true;
	}
	
	public void Logout() {
		// Set in main fragment after login
    	Session session = Session.getActiveSession();
    	session.closeAndClearTokenInformation();
    	
		SharedPreferences settings = getSharedPreferences(MainActivity.LOGIN_PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove("FacebookID");
		editor.remove("AccessToken");
		editor.commit();
		
		//findViewById(R.id.authButton).setVisibility(View.VISIBLE);
	}
	
}
