package com.CSUF.EventFy;


import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.CSUF.EventFy_Beans.SignUp;
import com.CSUF.customViews.ScrimInsetsFrameLayout;
import com.CSUF.sliding.SlidingTabLayout;
import com.CSUF.tabs.ViewPagerAdapter;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView.OnSliderClickListener;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import utils.UtilsDevice;
import utils.UtilsMiscellaneous;


/**
 * Created by Edwin on 15/02/2015.
 */
public class Main2Activity extends ActionBarActivity implements OnSliderClickListener {

    // Declaring Your View and Variables

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"NearBy", "Map", "Trending"};
    int Numboftabs =3;
    ProgressDialog pDialog;
    TextView userName;
    ImageView profilePic;
    SignUp signUp;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ScrimInsetsFrameLayout mScrimInsetsFrameLayout;
    private boolean isFacebook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


   //     try {
        Intent in = getIntent();
        signUp = (SignUp) in.getSerializableExtra("signup");

        init_slider();

        init_navigator();

          //  set_loginData(signUp);

//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void init_slider() {
        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

    }



    private void init_navigator(){
        // Navigation Drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_activity_DrawerLayout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark));
        mScrimInsetsFrameLayout = (ScrimInsetsFrameLayout) findViewById(R.id.main_activity_navigation_drawer_rootLayout);

        mActionBarDrawerToggle = new ActionBarDrawerToggle
                (
                        this,
                        mDrawerLayout,
                        toolbar,
                        R.string.navigation_drawer_opened,
                        R.string.navigation_drawer_closed
                )
        {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                // Disables the burger/arrow animation by default
                super.onDrawerSlide(drawerView, 0);
            }
        };

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mActionBarDrawerToggle.syncState();

        // Navigation Drawer layout width
        int possibleMinDrawerWidth = UtilsDevice.getScreenWidth(this) -
                UtilsMiscellaneous.getThemeAttributeDimensionSize(this, android.R.attr.actionBarSize);
        int maxDrawerWidth = getResources().getDimensionPixelSize(R.dimen.navigation_drawer_max_width);

        mScrimInsetsFrameLayout.getLayoutParams().width = Math.min(possibleMinDrawerWidth, maxDrawerWidth);
        // Set the first item as selected for the first time
        getSupportActionBar().setTitle(R.string.toolbar_title_home);


    }


    private void set_loginData(SignUp signUp) throws MalformedURLException, URISyntaxException {

        userName = (TextView) findViewById(R.id.navigation_drawer_account_information_display_name);
        userName.setText(signUp.getUserName());
        profilePic = (ImageView) findViewById(R.id.navigation_drawer_user_account_picture_profile);
        //String url = ""+R.string.profile_start_url+userFbId+""+R.string.profile_end_url;
        if(signUp.getIsFacebook().equals("true")) {
            String url = "https://graph.facebook.com/" + signUp.getUserId() + "/picture?type=large";
            new LoadImage().execute(url);
        }
       if(signUp.getImageUrl().equals("default")) {
           Drawable myDrawable  = getResources().getDrawable(R.drawable.ic_account_circle_white_64dp);
           profilePic.setImageDrawable(myDrawable);
       }
           else
           new LoadImage().execute(signUp.getImageUrl());
    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {

    }




    private class LoadImage extends AsyncTask<String, String, Bitmap> {
      Bitmap bitmap;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Main2Activity.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();

        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if(image != null){

                profilePic.setImageBitmap(image);
                Log.e("prof : ", "" + image.getByteCount());
                pDialog.dismiss();

            }else{

                pDialog.dismiss();
                Toast.makeText(Main2Activity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }

}




