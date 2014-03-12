package com.example.smartplaylist1;

import java.util.Arrays;

import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class MainFragment extends Fragment{

	//public final static String FACEBOOKID = "com.example.myfirstapp.MESSAGE";
	private static final String TAG = "MainFragment";
	private Session mSession;
	private UiLifecycleHelper uiHelper;	
	String facebookID = "";
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}
	
	private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
		if (state.isOpened()) {
			if(mSession == null || isSessionChanged(session)) {
				mSession = session;
				((MainActivity) getActivity()).Login();
			}
	    } else if (state.isClosed()) {
	    	//MainActivity.Logout(getActivity().getApplicationContext());
	        Log.i(TAG, ""+state.name().toString());
	    }
	}

	
	@Override
	public void onResume() {
	    super.onResume();
	    // For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
	    final Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	        
	    }
	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, 
	        Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.main, container, false);
	    LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
	    authButton.setFragment(this);
	    authButton.setReadPermissions(Arrays.asList("user_likes", "user_status"));
	    return view;
	}
	
	private boolean isSessionChanged(Session session) {

	    // Check if session state changed
	    if (mSession.getState() != session.getState())
	        return true;

	    // Check if accessToken changed
	    if (mSession.getAccessToken() != null) {
	        if (!mSession.getAccessToken().equals(session.getAccessToken()))
	            return true;
	    }
	    else if (session.getAccessToken() != null) {
	        return true;
	    }

	    // Nothing changed
	    return false;
	}
}
