/**
 * 
 */
package com.example.smartplaylist1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.example.smartplaylist1.adapter.TabsPagerAdapter;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class TabbedActivity extends FragmentActivity implements
       ActionBar.TabListener {

   private ViewPager viewPager;
   private TabsPagerAdapter mAdapter;
   private ActionBar actionBar;
   // Tab titles
   private String[] tabs = { "View Events", "Your Events" };

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	   getShaKey();
	   Log.e("BLAHBLAH", "DONE");
       super.onCreate(savedInstanceState);
       setContentView(R.layout.tabbed_activity);

       // Initialization
       viewPager = (ViewPager) findViewById(R.id.pager);
       actionBar = getActionBar();
       mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

       viewPager.setAdapter(mAdapter);
       actionBar.setHomeButtonEnabled(false);
       actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        

       // Adding Tabs
       for (String tab_name : tabs) {
           actionBar.addTab(actionBar.newTab().setText(tab_name)
                   .setTabListener(this));
       }

       /**
        * on swiping the viewpager make respective tab selected
        * */
       viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

           @Override
           public void onPageSelected(int position) {
               // on changing the page
               // make respected tab selected
               actionBar.setSelectedNavigationItem(position);
           }

           @Override
           public void onPageScrolled(int arg0, float arg1, int arg2) {
           }

           @Override
           public void onPageScrollStateChanged(int arg0) {
           }
       });
   }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_created_events, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	 private void getShaKey() {

		 try {
		 PackageInfo info = getPackageManager().getPackageInfo("com.example.smartplaylist1",
		 PackageManager.GET_SIGNATURES);
		 for (Signature signature : info.signatures) {
		 MessageDigest md = MessageDigest.getInstance("SHA");
		 md.update(signature.toByteArray());
		 Log.e("BLAHBLAH", "KeyHash:" + Base64.encodeToString(md.digest(),
		 Base64.DEFAULT));
		 }
		 } catch (NameNotFoundException e) {
		 e.printStackTrace();

		 } catch (NoSuchAlgorithmException e) {
		 e.printStackTrace();

		 }

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

   @Override
   public void onTabReselected(Tab tab, FragmentTransaction ft) {
   }

   @Override
   public void onTabSelected(Tab tab, FragmentTransaction ft) {
       // on tab selected
       // show respected fragment view
       viewPager.setCurrentItem(tab.getPosition());
   }

   @Override
   public void onTabUnselected(Tab tab, FragmentTransaction ft) {
   }

}