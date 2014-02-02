package com.underdusken.kulturekalendar.ui.fragments;

import android.content.Intent;
import android.content.IntentFilter;
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
import com.underdusken.kulturekalendar.mainhandler.BroadcastNames;
import com.underdusken.kulturekalendar.sharedpreference.UserFilterPreference;
import com.underdusken.kulturekalendar.ui.activities.EventsDescription;
import com.underdusken.kulturekalendar.ui.activities.UserFilter;
import com.underdusken.kulturekalendar.ui.adapters.AdapterEventsItem;
import com.underdusken.kulturekalendar.ui.receivers.NotificationUpdateReceiver;
import com.underdusken.kulturekalendar.utils.ToDo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pavelarteev
 * Date: 9/25/12
 * Time: 8:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class TabFilter extends Fragment {

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
        return inflater.inflate(R.layout.tab_my_events, container, false);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lvEvents = (ListView) getActivity().findViewById(R.id.tab3_events_list);
        etSearch = (EditText) getActivity().findViewById(R.id.tab3_search_field);

        Button btClear = (Button) getActivity().findViewById(R.id.tab3_search_clear);
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearch.setText("");
            }
        });

        Button btSetup = (Button) getActivity().findViewById(R.id.tab3_user_filter);
        btSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TabFilter.this.getActivity(), UserFilter.class);
                startActivity(intent);
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
            getActivity().findViewById(R.id.text_noevents).setVisibility(View.VISIBLE);
        } else {
            getActivity().findViewById(R.id.text_noevents).setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        // Reciever for update notifications
        IntentFilter intentFilterNotificationUpdate = new IntentFilter(BroadcastNames.BROADCAST_NEW_DATA);
        getActivity().registerReceiver(notificationUpdateReceiver, intentFilterNotificationUpdate);

        updateFilter();
        updateView();

        if (eventItemList.size() == 0) {
            getActivity().findViewById(R.id.text_noevents).setVisibility(View.VISIBLE);
        } else {
            getActivity().findViewById(R.id.text_noevents).setVisibility(View.GONE);
        }
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

    private boolean _cat1 = false;
    private boolean _cat2 = false;
    private boolean _cat3 = false;
    private boolean _cat4 = false;
    private boolean _cat5 = false;
    private boolean _cat6 = false;
    private boolean _cat7 = false;
    private boolean _cat8 = false;

    private int _myAge = 0;
    private int _ageLimit = 0;
    private int _price = 0;


    private void getUserFilters() {
        UserFilterPreference userFilterPreference = new UserFilterPreference(getActivity());

        _cat1 = userFilterPreference.isChk1();
        _cat2 = userFilterPreference.isChk2();
        _cat3 = userFilterPreference.isChk3();
        _cat4 = userFilterPreference.isChk4();
        _cat5 = userFilterPreference.isChk5();
        _cat6 = userFilterPreference.isChk6();
        _cat7 = userFilterPreference.isChk7();
        _cat8 = userFilterPreference.isChk8();

        _myAge = userFilterPreference.getMyAge();
        _ageLimit = userFilterPreference.getAgeLimit();
        _price = userFilterPreference.getPrice();

    }

    /**
     * Update filter list by search name
     */
    private void updateFilter() {
        getUserFilters();

        String searchText = etSearch.getText().toString().toLowerCase();
        filterEventItem.clear();
        if (eventItemList != null) {
            for (EventItem eventItem : eventItemList) {
                if (eventItem.getTitle().toLowerCase().contains(searchText) || searchText.equals("")) {
                    // start user filters
                    // price
                    switch (_price) {
                        case 1:
                            if (eventItem.getPrice() == 0) continue;
                            break;
                        case 2:
                            if (eventItem.getPrice() != 0) continue;
                            break;
                    }
                    // age limit
                    if (_ageLimit == 1) if (eventItem.getAgeLimit() > _myAge) continue;

                    //"SPORT", "PERFORMANCES", "MUSIC", "EXHIBITIONS", "NIGHTLIFE", "PRESENTATIONS",
                    // "DEBATE", "OTHER"
                    // categories
                    String eventType = eventItem.getCategoryID();
                    if (eventType.equals("SPORT")) {
                        if (!_cat1) continue;
                    } else if (eventType.equals("PERFORMANCES")) {
                        if (!_cat2) continue;
                    } else if (eventType.equals("MUSIC")) {
                        if (!_cat3) continue;
                    } else if (eventType.equals("EXHIBITIONS")) {
                        if (!_cat4) continue;
                    } else if (eventType.equals("NIGHTLIFE")) {
                        if (!_cat5) continue;
                    } else if (eventType.equals("PRESENTATIONS")) {
                        if (!_cat6) continue;
                    } else if (eventType.equals("DEBATE")) {
                        if (!_cat7) continue;
                    } else if (eventType.equals("OTHER")) {
                        if (!_cat8) continue;
                    } else {
                        continue;
                    }

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
            List<EventItem> newEventItemList = databaseManager.getAllFutureEventsItem();

            if (newEventItemList != null) if (newEventItemList.size() > 0) {
                //Delete no events title
                getActivity().findViewById(R.id.text_noevents).setVisibility(View.GONE);

                lastEventsId = newEventItemList.get(newEventItemList.size() - 1).getId();
                for (EventItem eventItem : newEventItemList) {
                    eventItemList.add(eventItem);
                }
                eventItemList = DatabaseManager.sortEventsByDate(eventItemList);

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
                Intent intent = new Intent(TabFilter.this.getActivity(), EventsDescription.class);
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
