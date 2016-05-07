package com.CSUF.EventFy;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.CSUF.EventFy.fragment.Attendance_tab;
import com.CSUF.EventFy.fragment.Eventinfo_tab;
import com.CSUF.EventFy.fragment.ImageComment_tab;
import com.CSUF.EventFy_Beans.Events;
import com.CSUF.EventFy_Beans.SignUp;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;


public class EventInfoActivity extends AppCompatActivity {

    private MaterialViewPager mViewPager;

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private Events event;
    private SignUp signUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        Intent in = getIntent();
        event = (Events) in.getSerializableExtra("CurrentEvent");
        signUp = (SignUp) in.getSerializableExtra("signup");
        setTitle(event.getEventName());

        if(event.getEventImageUrl().equals("default"))
           event.setEventImageUrl("http://res.cloudinary.com/eventfy/image/upload/v1462334816/logo_qe8avs.png");

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        toolbar = mViewPager.getToolbar();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }
        }


        final int arr []= new int[3];
        arr[0]=4;
        arr[1]=8;

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
//                       return RecyclerViewExampleActivity.newInstance(position);

                switch (position) {
                    case 0:
                        return ImageComment_tab.newInstance(arr[position], getApplicationContext(), position, event);
                    case 1:
                        return Attendance_tab.newInstance(arr[position], getApplicationContext(), position, ""+event.getEventId());

                    default:
                        return new Eventinfo_tab().newInstance(getApplicationContext(),event, signUp);

                }
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 3) {
                    case 0:
                        return "Comments";
                    case 1:
                        return "Attendance";
                    case 2:
                        return "Event Info.";

                }
                return "";
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    default:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                event.getEventImageUrl());
                }

                //execute others actions if needed (ex : modify your header logo)

            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());


    }



    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

               this.finish();
                Intent intent = new Intent(this, Main2Activity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

    }
}
}