package com.CSUF.tabs;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by Edwin on 15/02/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    Tab3 tab3;
    Tab2 tab2;
    Tab1 tab1;


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        Log.e("position is ::: ", ""+position);
        if(position == 0) // if the position is 0 we are returning the First tab
        {
            if(tab1 == null)
                tab1 = new Tab1();
            return tab1;

        }
        else if(position == 1)            // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            if(tab3 == null)
                tab3 = new Tab3();
            return tab3;

        }
        else{
            if(tab2 == null)
                tab2 = new Tab2();
            return tab2;
        }

    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}