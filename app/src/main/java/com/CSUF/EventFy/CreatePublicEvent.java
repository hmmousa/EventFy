package com.CSUF.EventFy;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.CSUF.EventFy_Beans.Events;
import com.CSUF.EventFy_Beans.SignUp;
import com.CSUF.Notifications.GCMNotificationIntentService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.gson.Gson;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog.OnTimeSetListener;
import com.soundcloud.android.crop.Crop;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class CreatePublicEvent extends AppCompatActivity implements ObservableScrollViewCallbacks, OnDateSetListener, OnTimeSetListener {

    private Uri dest = null;
    private static final int PICK_IMAGE_ID = 234;
    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    private MaterialViewPager mViewPager;
    private ImageView mImageView;
    private View mOverlayView;
    private ObservableScrollView mScrollView;
    private TextView mTitleView;
    private View mFab;
    private int mActionBarSize;
    private int mFlexibleSpaceShowFabOffset;
    private int mFlexibleSpaceImageHeight;
    private int mFabMargin;
    private boolean mFabIsShown;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Bitmap bm;
    private RangeSeekBar rangeSeekBarTextColorWithCode;
    private TextView mEventDate;
    private TextView mEventTime;
    private UploadImage uploadImage;
    private String[] evenetCapacityItems = new String[]{"5", "10", "20", "30 - 50", "50 - 80", "80 - 100", "100+"};
    private String[] eventTypeItems = new String[]{"Promotion", "Giveaway", "Meeting", "Concert", "Movie", "Study", "Other"};
    private ProgressDialog progressDialog = null;
    private Button mCreateEvent;
    private AddEvent addEvent;
    private Spinner eventCapacity;
    private Spinner eventType;
    private TextView eventDescription;
    private TextView eventName;
    private Events event;
    private String imageUrl;
    private String eventCapacityStr ;
    private String mEventDateStr;
    private String eventDescriptionStr;
    private String eventNameStr;
    private String mEventTimeStr;
    private String eventTypeStr;
    private LocationManager locationManager;
    private SignUp signUp;
    private android.location.Location cLocation;
    private double latitude;
    private double longitude;


    GPSTracker gps;

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_public_event);


        ImageView imgLocation = (ImageView) findViewById(R.id.public_event_current_location);

        if (imgLocation != null) {
            imgLocation.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    gps = new GPSTracker(CreatePublicEvent.this);

                    if(gps.canGetLocation()) {
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();

                        Toast.makeText(
                                getApplicationContext(),
                                "Your Location is -\nLat: " + latitude + "\nLong: "
                                        + longitude, Toast.LENGTH_LONG).show();
                    } else {
                        gps.showSettingsAlert();
                    }
                }
            });
        }


        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
        Intent intent1 = new Intent(this, GCMNotificationIntentService.class);
        startService(intent1);

        SharedPreferences preferences;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();

        String signUpjson = preferences.getString("signUp", "");
        Log.e("sign up str ", ""+signUpjson);

       signUp = gson.fromJson(signUpjson, SignUp.class);

        Log.e("sign pojo ", ""+signUp);
        // mActionBarSize = getActionBarSize();

        mCreateEvent = (Button) findViewById(R.id.public_create_event);


        eventDescription = (TextView) findViewById(R.id.txt_event_descr);
        mEventDate = (TextView) findViewById(R.id.public_event_date);
        mEventTime = (TextView) findViewById(R.id.public_event_time);
        eventName = (TextView) findViewById(R.id.public_event_name);

        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(CreatePublicEvent.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), isVibrate());


//        mEventTime = (TextView) findViewById(R.id.public_event_time);
//        final Calendar calendar1 = Calendar.getInstance();
//        final TimePickerDialog timePickerDialog = timePickerDialog.newInstance(CreatePublicEvent.this, calendar1.get(Calendar.HOUR_OF_DAY), calendar1.get(Calendar.MINUTE), isVibrate());


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

//        Spinner To Set Event CAPACITY

        eventCapacity = (Spinner) findViewById(R.id.spn_evntCpcty);

        assert eventCapacity != null;
        eventCapacity.setPrompt("Event Capacity");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, evenetCapacityItems);
        eventCapacity.setAdapter(adapter);


        //        Spinner To Set Event TYPE
        eventType = (Spinner) findViewById(R.id.spn_evnt_type);

        assert eventType != null;
        eventType.setPrompt("Event Type");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, eventTypeItems);
        eventType.setAdapter(adapter1);


        getSupportActionBar().setTitle("Create Event");


        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);

        mImageView = (ImageView) findViewById(R.id.eventImage);
        mOverlayView = findViewById(R.id.overlay);
        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);
        mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setText(getTitle());
        setTitle(null);
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreatePublicEvent.this, "FAB is clicked", Toast.LENGTH_SHORT).show();
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(v.getContext());
                startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
            }
        });
        mFabMargin = getResources().getDimensionPixelSize(R.dimen.margin_standard);
        ViewHelper.setScaleX(mFab, 0);
        ViewHelper.setScaleY(mFab, 0);

        ScrollUtils.addOnGlobalLayoutListener(mScrollView, new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, mFlexibleSpaceImageHeight - mActionBarSize);
            }
        });

        mEventDate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // datePickerDialog.setVibrate(isVibrate());
                datePickerDialog.setYearRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);
                datePickerDialog.setFirstDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
                datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }

        });

        mEventTime.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        CreatePublicEvent.this,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        false
                );
                datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }

        });


        mCreateEvent.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mCreateEvent.setEnabled(false);

                progressDialog = new ProgressDialog(CreatePublicEvent.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(false);
                // progressDialog.setCancelable(false);
                progressDialog.setMessage("Creating Event...");
                progressDialog.show();

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                if(!mCreateEvent.isEnabled()) {

                                    eventCapacityStr= eventCapacity.getSelectedItem().toString();
                                    mEventDateStr= mEventDate.getText().toString();
                                    eventDescriptionStr= eventDescription.getText().toString();
                                    eventNameStr= eventName.getText().toString();
                                    mEventTimeStr= mEventTime.getText().toString();
                                    eventTypeStr= eventType.getSelectedItem().toString();


                                    uploadImage = new UploadImage(true);

                                    uploadImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



//                                    if(imageUrl!=null) {
//                                        Log.e("iamge url : ", imageUrl);
//
//                                    }
                                    mCreateEvent.setEnabled(true);
                                }

                            }
                        }, 100);

            }


        });




        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new Builder(this).addApi(AppIndex.API).build();
    }

    public void intiGpsLocation()
    {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // getting GPS status
        Boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        Boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            // no network provider is enabled
        } else {

            cLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        }

    }


    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    private boolean isCloseOnSingleTapDay() {
        return true;
    }

    private boolean isVibrate() {
        return false;
    }

    private void showFab() {
        if (!mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(0).scaleY(0).setDuration(200).start();
            mFabIsShown = false;
        }
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        ViewHelper.setPivotX(mTitleView, 0);
        ViewHelper.setPivotY(mTitleView, 0);
        ViewHelper.setScaleX(mTitleView, scale);
        ViewHelper.setScaleY(mTitleView, scale);

        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        ViewHelper.setTranslationY(mTitleView, titleTranslationY);

        // Translate FAB
        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFab.getHeight() / 2;
        float fabTranslationY = ScrollUtils.getFloat(
                -scrollY + mFlexibleSpaceImageHeight - mFab.getHeight() / 2,
                mActionBarSize - mFab.getHeight() / 2,
                maxFabTranslationY);
        if (VERSION.SDK_INT < VERSION_CODES.HONEYCOMB) {
            // On pre-honeycomb, ViewHelper.setTranslationX/Y does not set margin,
            // which causes FAB's OnClickListener not working.
            LayoutParams lp = (LayoutParams) mFab.getLayoutParams();
            lp.leftMargin = mOverlayView.getWidth() - mFabMargin - mFab.getWidth();
            lp.topMargin = (int) fabTranslationY;
            mFab.requestLayout();
        } else {
            ViewHelper.setTranslationX(mFab, mOverlayView.getWidth() - mFabMargin - mFab.getWidth());
            ViewHelper.setTranslationY(mFab, fabTranslationY);
        }

        // Show/hide FAB
        if (fabTranslationY < mFlexibleSpaceShowFabOffset) {
            hideFab();
        } else {
            showFab();
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE_ID) {
            Uri selectedImage = ImagePicker.getImageFromResult(this, resultCode, data);
            dest = beginCrop(selectedImage);
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data, dest);
        }

    }

    private Uri beginCrop(Uri source) {

        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));

        Crop.of(source, destination).withAspect(250, 240).start(this);


        return destination;
    }


    private void handleCrop(int resultCode, Intent result, Uri destination) {


        if (resultCode == RESULT_OK) {
            Log.e("crop : ", "" + getCacheDir());

            bm = decodeBitmap(this, destination, 3);
            mImageView.setImageBitmap(bm);

            //   mImageView.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private static Bitmap decodeBitmap(Context context, Uri theUri, int sampleSize) {
        Options options = new Options();
        options.inSampleSize = sampleSize;


        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(theUri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
                fileDescriptor.getFileDescriptor(), null, options);

        Log.d("img : ", options.inSampleSize + " sample method bitmap ... " +
                actuallyUsableBitmap.getWidth() + " " + actuallyUsableBitmap.getHeight());

        return actuallyUsableBitmap;
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        mEventDate.setText(year + "-" + month + "-" + day);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String time = "" + hourString + "h" + minuteString + "m";
        mEventTime.setText(time);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "CreatePublicEvent Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.CSUF.EventFy/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "CreatePublicEvent Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.CSUF.EventFy/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
    public void loadNextActivity()
    {
        Intent intent = new Intent(this, EventInfoActivity.class);
        intent.putExtra("CurrentEvent", event);
        intent.putExtra("signup", signUp);
        finish();
        startActivity(intent);

    }


    private class AddEvent extends AsyncTask<Void, Void, Void> {

        public AddEvent(String str) {

        }

        public AddEvent(boolean b) {
            super();

        }

        @Override
        protected Void doInBackground(Void... params) {
            String url = getResources().getString(R.string.ip_local) + getResources().getString(R.string.add_event);

            RestTemplate restTemplate = new RestTemplate(true);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


            event = new Events();
            event.getEventAdmin(signUp.getUserId());
            event.setEventCapacity(eventCapacityStr);
            event.setEventDate(mEventDateStr);
            event.setEventDescription(eventDescriptionStr);
            event.setEventImageUrl( imageUrl);
            event.setEventIsVisible("true");
            event.setEventName(eventNameStr);
            event.setEventLocation(null);
            event.setEventTime(mEventTimeStr);
            event.setEventType(eventTypeStr);
            event.setEventLocationLatitude(latitude);
            event.setEventLocationLongitude(longitude);

            HttpEntity<Events> request = new HttpEntity<>(event);

            ResponseEntity<Events> rateResponse =
                    restTemplate.exchange(url, HttpMethod.POST, request, Events.class);
            event = rateResponse.getBody();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("event : ", ""+event.getEventID() );

            progressDialog.dismiss();

            loadNextActivity();

        }
    }


    private class UploadImage extends AsyncTask<Void, Void, Void> {

        public UploadImage(String url) {

        }

        public UploadImage(boolean b) {
            super();

        }

        @Override
        protected Void doInBackground(Void... params) {
            Map config = new HashMap();
            config.put("cloud_name", "eventfy");
            config.put("api_key", "338234664624354");
            config.put("api_secret", "clA_O7equySs8LDK0hJNmmK62J8");
            Cloudinary cloudinary = new Cloudinary(config);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(CompressFormat.PNG, 100, stream);
            byte[] imageBytes = stream.toByteArray();

            try {
                Map uploadResult = cloudinary.uploader().upload(imageBytes, ObjectUtils.emptyMap());
                imageUrl = (String) uploadResult.get("secure_url");


            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("return success" , imageUrl);
            addEvent = new AddEvent(true);
            addEvent.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }
    }
}