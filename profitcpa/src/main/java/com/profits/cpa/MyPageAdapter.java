package com.profits.cpa;

import com.profits.cpa.checkout.Checkout;
import com.profits.cpa.frag.Home;
import com.profits.cpa.frag.Settings;
import com.profits.cpa.rank.Ranking;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

class MyPageAdapter extends FragmentStatePagerAdapter {

	CharSequence Titles[];
	int NumbOfTabs;

	public MyPageAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
		super(fm);

		this.Titles = mTitles;
		this.NumbOfTabs = mNumbOfTabsumb;
	}

	@Override
	public Fragment getItem(int position) {

		switch (position) {
		case 0:
			return new Home();
		case 1:
			return new Checkout();
		case 2:
			return new Ranking();
		case 3:
			return new Settings();

		default:
			return new Home();
		}
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return Titles[position];
	}

	@Override
	public int getCount() {
		return NumbOfTabs;
	}
	
}