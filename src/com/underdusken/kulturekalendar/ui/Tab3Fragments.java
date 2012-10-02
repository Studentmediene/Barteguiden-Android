package com.underdusken.kulturekalendar.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.data.EventsItem;
import com.underdusken.kulturekalendar.ui.Adapter.AdapterMyEventsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pavelarteev
 * Date: 9/25/12
 * Time: 8:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class Tab3Fragments extends Fragment {


    private AdapterMyEventsItem adapterEventsItem = null;
    private List<EventsItem> eventsItemList = null;


    private ListView lvEvents = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab3, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lvEvents = (ListView)getActivity().findViewById(R.id.tab3_events_list);

        //TODO Only for test !!!
        createTestList();

        // load events
        loadEvents();



    }


    private void createTestList(){
        eventsItemList = new ArrayList<EventsItem>();

        eventsItemList.add(new EventsItem("Event 1", "12.10.2012"));
        eventsItemList.add(new EventsItem("Event 2", "13.10.2012"));

    }


    private void loadEvents(){
        adapterEventsItem = new AdapterMyEventsItem(this.getActivity(), 0, eventsItemList);
        lvEvents.setAdapter(adapterEventsItem);
    }
}
