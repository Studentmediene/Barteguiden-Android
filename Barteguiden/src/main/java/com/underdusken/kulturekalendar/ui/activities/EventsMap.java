package com.underdusken.kulturekalendar.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.underdusken.kulturekalendar.R;

public class EventsMap extends FragmentActivity {

    private GoogleMap mMap;

    private float latitude;
    private float longitude;
    private String placeName;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_map);

        // Get event coordinates
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
        }

        try {
            MapsInitializer.initialize(this);
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

        latitude = extras.getFloat("events_latitude");
        longitude = extras.getFloat("events_longitude");
        title = extras.getString("events_title");
        placeName = extras.getString("events_place_name");

        // Set view position
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        if (mMap != null) {

            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);

            mMap.moveCamera(center);
            mMap.animateCamera(zoom);

            // Making marker
            LatLng eventMarker = new LatLng(latitude, longitude);
            Marker melbourne = mMap.addMarker(
                    new MarkerOptions().title(title).snippet(placeName).position(eventMarker));
        }
    }
}
