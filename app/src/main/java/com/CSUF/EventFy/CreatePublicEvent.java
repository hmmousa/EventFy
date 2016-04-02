package com.CSUF.EventFy;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileNotFoundException;

public class CreatePublicEvent extends ActionBarActivity implements ObservableScrollViewCallbacks {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_public_event);
//
        toolbar = (Toolbar) findViewById(R.id.tool_bar);


        Log.e("toolbar : ", "" + toolbar);
//        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout1);

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

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
        // if (toolbar != null) {
         //   setSupportActionBar(toolbar);


            mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
            mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
            //mActionBarSize = getActionBarSize();

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
                    //onScrollChanged(0, false, false);

                    // You can also achieve it with the following codes.
                    // This causes scroll change from 1 to 0.
                    //mScrollView.scrollTo(0, 1);
                    //mScrollView.scrollTo(0, 0);
                }
            });
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
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

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

}