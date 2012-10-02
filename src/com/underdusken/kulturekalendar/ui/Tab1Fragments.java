package com.underdusken.kulturekalendar.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.data.EventsItem;
import com.underdusken.kulturekalendar.ui.Adapter.AdapterEventsItem;

import java.util.ArrayList;
import java.util.List;

public class Tab1Fragments extends Fragment {


    private AdapterEventsItem adapterEventsItem = null;
    private List<EventsItem> eventsItemList = null;


    private ListView lvEvents = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lvEvents = (ListView)getActivity().findViewById(R.id.tab1_events_list);

        //TODO Only for test !!!
        createTestList();

        // load events
        loadEvents();



    }


    private void createTestList(){
        eventsItemList = new ArrayList<EventsItem>();

        eventsItemList.add(new EventsItem("Event 1", "12.10.2012"));
        eventsItemList.add(new EventsItem("Event 3", "14.10.2012"));
        eventsItemList.add(new EventsItem("Event 5", "16.10.2012"));

    }


    private void loadEvents(){
        adapterEventsItem = new AdapterEventsItem(this.getActivity(), 0, eventsItemList);
        lvEvents.setAdapter(adapterEventsItem);
    }


}
