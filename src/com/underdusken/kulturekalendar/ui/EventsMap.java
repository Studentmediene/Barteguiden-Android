package com.underdusken.kulturekalendar.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.SupportMapFragment;
import com.underdusken.kulturekalendar.R;

public class EventsMap extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SupportMapFragment fragment = new SupportMapFragment();
        getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, fragment).commit();

        setContentView(R.layout.event_map);
    }
}
