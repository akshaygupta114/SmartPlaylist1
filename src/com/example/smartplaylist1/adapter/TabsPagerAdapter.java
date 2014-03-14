package com.example.smartplaylist1.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.smartplaylist1.ViewCreatedEventsFragment;
import com.example.smartplaylist1.ViewNearbyEventsFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	
	public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
	
	@Override
	public Fragment getItem(int index) {
		
		switch(index) {
		case 0: return new ViewNearbyEventsFragment();
		case 1: return new ViewCreatedEventsFragment();
		}
		
		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

}
