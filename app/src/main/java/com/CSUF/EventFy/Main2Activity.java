package com.CSUF.EventFy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
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


/**
 * Created by Edwin on 15/02/2015.
 */
public class Main2Activity extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener, OnSliderClickListener {

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
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent in = getIntent();
        signUp = (SignUp) in.getSerializableExtra("signup");

if(navigationView==null) {
    init_slider();

    init_navigator();

    try {
        set_loginData(signUp);
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (URISyntaxException e) {
        e.printStackTrace();
    }
}
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Log.e("item clicked : ",""+item);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_item_home)
        {
            // Handle the home action
        } else if (id == R.id.nav_item_create_event_public)
        {
            Intent intent = new Intent(this, CreatePublicEvent.class);
            startActivity(intent);

        } else if (id == R.id.nav_item_create_event_private)
        {
            // Handle the Private event action

        } else if (id == R.id.nav_item_event_history)
        {
            // Handle the Event History action

        } else if (id == R.id.nav_item_about)
        {
            // Handle the About action

        } else if (id == R.id.nav_item_myacc)
        {
            // Handle the MyAccount action

        } else if (id == R.id.nav_item_setting)
        {
            // Handle the Setting action

        } else if (id == R.id.nav_item_logout)
        {
            // Handle Log out Action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);


         navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setTitle(R.string.toolbar_title_home);

        toggle.syncState();

    }


    private void set_loginData(SignUp signUp) throws MalformedURLException, URISyntaxException {



        View headerLayout =
                navigationView.getHeaderView(0);
        Log.e("navoigator : ", ""+headerLayout);

        userName = (TextView) headerLayout.findViewById(R.id.navigation_drawer_account_information_display_name);
        userName.setText(signUp.getUserName());
        profilePic = (ImageView) headerLayout.findViewById(R.id.navigation_drawer_user_account_picture_profile);
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
    public void onSliderClick(BaseSliderView slider) {

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
    public static void distanceBetween (double startLatitude, double startLongitude, double endLatitude, double endLongitude, float[] results)
    {

    }
}