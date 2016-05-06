package com.CSUF.tabs;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.CSUF.EventFy.EventInfoActivity;
import com.CSUF.EventFy.R;
import com.CSUF.EventFy_Beans.Events;
import com.CSUF.EventFy_Beans.Location;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Edwin on 15/02/2015.
 */
public class Tab1 extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private SliderLayout mDemoSlider;
    private List<Events> eventLst;
    HashMap<String,String> url_maps;
    private android.location.Location cLocation;

    GetNearbyEventForTab1 getNearbyEventForTab1;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_1,container,false);

        Log.e("tab1","");
        mDemoSlider = (SliderLayout)v.findViewById(R.id.slider);

        getNearbyEventForTab1 = new GetNearbyEventForTab1(true);
        getNearbyEventForTab1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        url_maps = new HashMap<String, String>();


        initView();
        return v;
    }


public void initView()
{

    for(String name : url_maps.keySet()){
        Log.e("in view load :: ", "  "+url_maps.get(name));
        TextSliderView textSliderView = new TextSliderView(getContext());
        // initialize a SliderLayout
        textSliderView
                .description(name)
                .image(url_maps.get(name))
                .setScaleType(BaseSliderView.ScaleType.Fit)
                .setOnSliderClickListener(this);

        //add your extra information
        textSliderView.bundle(new Bundle());
        textSliderView.getBundle()
                .putString("extra", name);

        mDemoSlider.addSlider(textSliderView);

    }
    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
    mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
    mDemoSlider.setCustomAnimation(new DescriptionAnimation());
    mDemoSlider.setDuration(2000);
    mDemoSlider.addOnPageChangeListener(this);


}
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();

        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getContext(), slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity().getApplicationContext(), EventInfoActivity.class);
        startActivity(intent);
    }

    private class GetNearbyEventForTab1 extends AsyncTask<Void, Void, Void> {

        public GetNearbyEventForTab1(String str) {

        }

        public GetNearbyEventForTab1(boolean b) {
            super();

        }

        @Override
        protected Void doInBackground(Void... params) {
            String url = getResources().getString(R.string.ip_local) + getResources().getString(R.string.get_nearby_event);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            Location location = new Location();
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            cLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (cLocation != null) {
                location.setLatitude(cLocation.getLatitude());
                location.setLongitude(cLocation.getLongitude());

                location.setDistance(10);
                Log.e("tab 1 url :  ", "" + url);

                HttpEntity<Location> request = new HttpEntity<>(location);

                ResponseEntity<Events[]> response = restTemplate.exchange(url, HttpMethod.POST, request, Events[].class);

                Events[] event = response.getBody();

                Log.e("event data :: ", ""+event.length);
                eventLst = Arrays.asList(event);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("in first load :: ", "");

            if(eventLst!=null)
                for(Events obj : eventLst) {
                    Log.e("in first load :: ", "  "+obj.getEventImageUrl());
                    if(obj.getEventImageUrl().equals("default"))
                        url_maps.put(obj.getEventName(),"http://res.cloudinary.com/eventfy/image/upload/v1462334816/logo_qe8avs.png");
                    else
                        url_maps.put(obj.getEventName(),obj.getEventImageUrl());


                    initView();
                }
        }
    }

}
