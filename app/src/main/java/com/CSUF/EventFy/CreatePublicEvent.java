package com.CSUF.EventFy;

import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.soundcloud.android.crop.Crop;
import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

public class CreatePublicEvent extends ActionBarActivity implements ObservableScrollViewCallbacks, OnDateSetListener {

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
 //   @Bind(R.id.public_event_visiblity_seekbar)
    RangeSeekBar rangeSeekBarTextColorWithCode;
    private TextView mEventDate;
    private TextView mEventTime;



    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_public_event);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);

       // mActionBarSize = getActionBarSize();
        Log.e("action bar size : ", ""+mActionBarSize);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout1);

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        mEventDate = (TextView) findViewById(R.id.public_event_date);
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

        Spinner dropdown = (Spinner)findViewById(R.id.spn_evntCpcty);

        assert dropdown != null;
        dropdown.setPrompt("Event Capacity");
        String[] items = new String[]{"5", "10", "20", "30 - 50", "50 - 80", "80 - 100", "100+"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        //        Spinner To Set Event TYPE
        Spinner dropdown1 = (Spinner)findViewById(R.id.spn_evnt_type);

        assert dropdown1 != null;
        dropdown1.setPrompt("Event Type");
        String[] items1 = new String[]{"Hang Out", "Meeting", "Wedding", "Concert", "Movie", "Study", "Other"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        dropdown1.setAdapter(adapter1);


        getSupportActionBar().setTitle("Create Event");
        mActionBarDrawerToggle = new ActionBarDrawerToggle
                (
                        this,
                        mDrawerLayout,
                        toolbar,
                        R.string.navigation_drawer_opened,
                        R.string.navigation_drawer_closed
                );        //  Log.e("mdrawer", "" + findViewById(R.id.drawer_layout));
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();


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
            mFab.setOnClickListener(new View.OnClickListener() {
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

                    // If you'd like to start from scrollY == 0, don't write like this:
                    //mScrollView.scrollTo(0, 0);
                    // The initial scrollY is 0, so it won't invoke onScrollChanged().
                    // To do this, use the following:
                    // onScrollChanged(0, false, false);

                    // You can also achieve it with the following codes.
                    // This causes scroll change from 1 to 0.
                    //mScrollView.scrollTo(0, 1);
                  //  mScrollView.scrollTo(0, 0);
                }
            });

        mEventDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // datePickerDialog.setVibrate(isVibrate());
                datePickerDialog.setYearRange(calendar.get(Calendar.YEAR) , calendar.get(Calendar.YEAR) + 10);
                datePickerDialog.setFirstDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK ));
                datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }

        });

//        mEventTime.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // datePickerDialog.setVibrate(isVibrate());
//                timePickerDialog.setYearRange(calendar.get(Calendar.YEAR) , calendar.get(Calendar.YEAR) + 10);
//                datePickerDialog.setFirstDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK ));
//                datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
//                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
//            }
//
//        });

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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // On pre-honeycomb, ViewHelper.setTranslationX/Y does not set margin,
            // which causes FAB's OnClickListener not working.
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFab.getLayoutParams();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e("request code  : ", ""+requestCode);
        Log.e("request code crop : ", ""+Crop.REQUEST_CROP);

        if (requestCode == PICK_IMAGE_ID) {
            Uri selectedImage = ImagePicker.getImageFromResult(this, resultCode, data);
            dest = beginCrop(selectedImage);
            Log.e("request code dest : ", ""+dest);
        } else if (requestCode == Crop.REQUEST_CROP) {
            Log.e("request code dest h : ", ""+dest);
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
            Log.e("crop : ", "" +getCacheDir());

            Bitmap bm = decodeBitmap(this,destination,3);
            mImageView.setImageBitmap(bm);

            //   mImageView.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private static Bitmap decodeBitmap(Context context, Uri theUri, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
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
}