package com.CSUF.tabs;


import android.content.Context;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.CSUF.EventFy.R;
import com.CSUF.EventFy_Beans.Events;
import com.CSUF.EventFy_Beans.Location;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * http://joekarl.github.io/2013/11/01/android-map-view-inside-fragment/
 */
public class Tab3 extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MapView mapView;
    private boolean mapsSupported = true;
    private List<Events> eventLst;
    private ObservableScrollView observableScrollView;
    private GetNearbyEvent getNearbyEvent;
    private com.CSUF.EventFy_Beans.Location location;


  private android.location.Location cLocation;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getActivity());

        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

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


        initializeMap();




//        getNearbyEvent = new GetNearbyEvent(true);
//        getNearbyEvent.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    private void initializeMap() {
        if (googleMap == null && mapsSupported) {
            mapView = (MapView) getActivity().findViewById(R.id.map_tab);
            googleMap = mapView.getMap();


            location = new Location();
            location.setDistance(2);
            location.setUserId("xyz1");

            int zoomVal = (int) Math.round(10+ (location.getDistance()/3));

//TODO adjust zoom vaue with radious
            LatLng myLaLn = new LatLng(cLocation.getLatitude(), cLocation.getLongitude());

            CameraPosition camPos = new CameraPosition.Builder().target(myLaLn)
                    .zoom(zoomVal)
                    .bearing(20)
                    .tilt(0)
                    .build();

            CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);

            googleMap.animateCamera(camUpd3);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(myLaLn);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            googleMap.addMarker(markerOptions);
            Circle circle = googleMap.addCircle(new CircleOptions()
                    .center(myLaLn)
                    .radius(location.getDistance()*1000)
                    .strokeColor(Color.CYAN)
                    .fillColor(Color.YELLOW));


            //setup markers etc...
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final LinearLayout parent = (LinearLayout) inflater.inflate(R.layout.tab_3, container, false);
        mapView = (MapView) parent.findViewById(R.id.map_tab);
        return parent;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        initializeMap();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in Sydney and move the camera

//        LatLng sydney = new LatLng(-34, 151);
//        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    private class GetNearbyEvent extends AsyncTask<Void, Void, Void> {

        public GetNearbyEvent(String str) {

        }

        public GetNearbyEvent(boolean b) {
            super();

        }

        @Override
        protected Void doInBackground(Void... params) {
            String url = getResources().getString(R.string.ip_local) + getResources().getString(R.string.get_nearby_event);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());



            if (cLocation != null) {
                Log.d("activity", "LOC by Network");
                location.setLatitude(cLocation.getLatitude());
                location.setLongitude(cLocation.getLongitude());
                Log.e("long : ", ""+location.getLongitude());
                Log.e("lati : ", ""+location.getLatitude());
            }


            HttpEntity<Location> request = new HttpEntity<>(location);

            ResponseEntity<Events[]> response = restTemplate.exchange(url, HttpMethod.POST, request, Events[].class);

            Events [] event = response.getBody();

             eventLst =  Arrays.asList(event);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("done :: ", "****  "+eventLst.size());
            if(eventLst!=null)
                for(Events obj : eventLst) {
                    LatLng temp = new LatLng(obj.getEventLocationLatitude(), obj.getEventLocationLongitude());

                    Log.e("setting :: ", "****  "+obj.getEventName());
                    Log.e("lat :: ", "****  "+obj.getEventLocationLatitude());
                    googleMap.addMarker(new MarkerOptions().position(temp).title(obj.getEventName()));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(temp));

                }

        }
    }

}
