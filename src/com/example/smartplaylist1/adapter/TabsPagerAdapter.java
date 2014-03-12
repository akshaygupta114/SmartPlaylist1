package com.example.smartplaylist1.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.smartplaylist1.ViewEventsFragment;
import com.example.smartplaylist1.CreateEventsFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	
	public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
	
	@Override
	public Fragment getItem(int index) {
		
		switch(index) {
		case 0: return new CreateEventsFragment();
		case 1: return new ViewEventsFragment();
		}
		
		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

}
