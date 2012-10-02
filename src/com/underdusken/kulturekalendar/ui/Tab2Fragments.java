package com.underdusken.kulturekalendar.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.data.EventsItem;
import com.underdusken.kulturekalendar.ui.Adapter.AdapterEventsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pavelarteev
 * Date: 9/25/12
 * Time: 8:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class Tab2Fragments extends Fragment {

    private AdapterEventsItem adapterEventsItem = null;
    private List<EventsItem> eventsItemList = null;


    private ListView lvEvents = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab2, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RadioButton radioButton = (RadioButton) getActivity().findViewById(R.id.tab2_check_all_events);

        radioButton.setText(R.string.tab2_check_all_events);

        radioButton = (RadioButton) getActivity().findViewById(R.id.tab2_check_my_events);
        radioButton.setText(R.string.tab2_check_my_events);

        lvEvents = (ListView)getActivity().findViewById(R.id.tab2_events_list);

        //TODO Only for test !!!
        createTestList();

        // load events
        loadEvents();



    }


    private void createTestList(){
        eventsItemList = new ArrayList<EventsItem>();

        eventsItemList.add(new EventsItem("Event 1", "12.10.2012"));
        eventsItemList.add(new EventsItem("Event 2", "13.10.2012"));
        eventsItemList.add(new EventsItem("Event 3", "14.10.2012"));
        eventsItemList.add(new EventsItem("Event 4", "15.10.2012"));
        eventsItemList.add(new EventsItem("Event 5", "16.10.2012"));
        eventsItemList.add(new EventsItem("Event 6", "17.10.2012"));
        eventsItemList.add(new EventsItem("Event 7", "18.10.2012"));
        eventsItemList.add(new EventsItem("Event 8", "19.10.2012"));

    }


    private void loadEvents(){
        adapterEventsItem = new AdapterEventsItem(this.getActivity(), 0, eventsItemList);
        lvEvents.setAdapter(adapterEventsItem);
    }

}
