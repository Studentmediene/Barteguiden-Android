package com.underdusken.kulturekalendar.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.data.EventItem;
import com.underdusken.kulturekalendar.data.db.DatabaseManager;
import com.underdusken.kulturekalendar.ui.activities.EventsDescription;
import com.underdusken.kulturekalendar.ui.adapters.AdapterEventsItem;
import com.underdusken.kulturekalendar.ui.receivers.NotificationUpdateReceiver;
import com.underdusken.kulturekalendar.utils.ToDo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TabFavorite extends Fragment {
    // private receivers
    private NotificationUpdateReceiver notificationUpdateReceiver = null;

    // Items for list
    private AdapterEventsItem adapterEventsItem = null;
    private List<EventItem> eventItemList = new ArrayList<EventItem>();
    private List<EventItem> filterEventItem = new ArrayList<EventItem>();

    // ui
    private ListView lvEvents = null;
    private EditText etSearch = null;


    //data
    private long lastEventsId = -1;      // for getting only new events

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab4, container, false);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lvEvents = (ListView) getActivity().findViewById(R.id.tab4_events_list);
        etSearch = (EditText) getActivity().findViewById(R.id.tab4_search_field);

        Button btClear = (Button) getActivity().findViewById(R.id.tab4_search_clear);
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearch.setText("");
            }
        });
        //text change listener for search
        etSearch.addTextChangedListener(filterTextWatcher);

        // Create new notification receiver
        notificationUpdateReceiver = new NotificationUpdateReceiver(new Handler(), new ToDo() {
            @Override
            public void doSomething() {
                loadEventsFromDb();
                updateFilter();
                updateView();
            }
        });


        // Load events from Data Base
        loadEventsFromDb();

        // Update Filter list
        updateFilter();

        // Set view
        createAdapter();

        if (eventItemList.size() == 0) {
            getActivity().findViewById(R.id.text_no_events).setVisibility(View.VISIBLE);
        } else {
            getActivity().findViewById(R.id.text_no_events).setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        loadEventsFromDb();
        updateFilter();
        updateView();

        if (eventItemList.size() == 0) {
            getActivity().findViewById(R.id.text_no_events).setVisibility(View.VISIBLE);
        } else {
            getActivity().findViewById(R.id.text_no_events).setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // unregister reciever
        /// getActivity().unregisterReceiver(notificationUpdateReceiver);


    }

    /**
     * callback update search information
     */
    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            updateFilter();
            updateView();
        }
    };


    /**
     * Update filter list by search name
     */
    private void updateFilter() {
        String searchText = etSearch.getText().toString().toLowerCase();
        filterEventItem.clear();
        if (eventItemList != null) if (searchText.equals("")) {
            filterEventItem.addAll(eventItemList);
        } else {
            for (EventItem eventItem : eventItemList) {
                if (eventItem.getTitle().toLowerCase().contains(searchText)) {
                    filterEventItem.add(eventItem);
                }
            }
        }
    }

    /**
     * Load data from DataBase (all)
     */
    private void loadEventsFromDb() {
        DatabaseManager databaseManager = new DatabaseManager(getActivity());
        try {
            databaseManager.open();
            List<EventItem> newEventItemList = databaseManager.getAllEventsFavorites();
            eventItemList.clear();
            if (newEventItemList != null) {
                if (newEventItemList.size() > 0) {
                    //Delete no events title
                    getActivity().findViewById(R.id.text_no_events).setVisibility(View.GONE);

                    lastEventsId = newEventItemList.get(newEventItemList.size() - 1).getId();

                    for (EventItem eventItem : newEventItemList) {
                        eventItemList.add(eventItem);
                    }
                    eventItemList = DatabaseManager.sortEventsByDate(eventItemList);
                }
            }
            databaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    /**
     * Create new list Adapter
     */
    private void createAdapter() {
        adapterEventsItem = new AdapterEventsItem(this.getActivity(), 0, filterEventItem);
        lvEvents.setAdapter(adapterEventsItem);
        // Open event description

        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TabFavorite.this.getActivity(), EventsDescription.class);

                intent.putExtra("events_id", filterEventItem.get(filterEventItem.size() - i - 1).getId());

                startActivityForResult(intent, 1);
            }
        });
    }

    // update view
    private void updateView() {
        adapterEventsItem.notifyDataSetChanged();
    }

}
