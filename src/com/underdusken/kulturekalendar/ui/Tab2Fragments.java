package com.underdusken.kulturekalendar.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
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
    private List<EventsItem> filterEventsItem = new ArrayList<EventsItem>();

    // ui
    private ListView lvEvents = null;
    private EditText etSearch = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab2, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lvEvents = (ListView) getActivity().findViewById(R.id.tab2_events_list);
        etSearch = (EditText) getActivity().findViewById(R.id.tab2_search_field);

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateFilter();
                adapterEventsItem = new AdapterEventsItem(Tab2Fragments.this.getActivity(), 0, filterEventsItem);
                lvEvents.setAdapter(adapterEventsItem);
            }
        });

        //TODO Only for test !!!
        createTestList();

        // update filter list
        updateFilter();
        // load events
        loadEvents();
    }

    //TODO use this only for test
    private void createTestList() {
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


    private void updateFilter(){
        filterEventsItem.clear();
        String searchText = etSearch.getText().toString().toLowerCase();
        for(EventsItem eventsItem: eventsItemList){
                if(eventsItem.getName().toLowerCase().contains(searchText))
                    filterEventsItem.add(eventsItem);
        }
    }


    private void loadEvents() {
        adapterEventsItem = new AdapterEventsItem(this.getActivity(), 0, filterEventsItem);
        lvEvents.setAdapter(adapterEventsItem);
    }

}
