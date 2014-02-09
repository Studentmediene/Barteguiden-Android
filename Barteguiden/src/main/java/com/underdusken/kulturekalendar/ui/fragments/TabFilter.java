package com.underdusken.kulturekalendar.ui.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.data.EventItem;
import com.underdusken.kulturekalendar.data.db.DatabaseManager;
import com.underdusken.kulturekalendar.mainhandler.BroadcastNames;
import com.underdusken.kulturekalendar.sharedpreference.UserFilterPreference;
import com.underdusken.kulturekalendar.ui.activities.EventsDescription;
import com.underdusken.kulturekalendar.ui.adapters.AdapterEventsItem;
import com.underdusken.kulturekalendar.ui.receivers.NotificationUpdateReceiver;
import com.underdusken.kulturekalendar.utils.ToDo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class TabFilter extends Fragment implements SearchView.OnQueryTextListener {

    private static final String TAG = "TabFilter";
    // private receivers
    private NotificationUpdateReceiver notificationUpdateReceiver = null;

    // Items for list
    private AdapterEventsItem adapterEventsItem = null;
    private List<EventItem> eventItemList = new ArrayList<EventItem>();
    private List<EventItem> filterEventItem = new ArrayList<EventItem>();

    private TextView emptyList;

    // ui
    private StickyListHeadersListView lvEvents = null;

    //data
    private long lastEventsId = -1;      // for getting only new events

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab_filter, container, false);

    }


    @Override
    public void onStart() {
        super.onStart();
        loadEventsFromDb();
        if (eventItemList.size() <= 0) {
            Log.d(TAG, eventItemList.size() + "");
            emptyList.setVisibility(View.VISIBLE);
        } else {
            emptyList.setVisibility(View.GONE);
        }
        updateFilter("");
        updateView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lvEvents = (StickyListHeadersListView) getActivity().findViewById(R.id.list_events_filter);
        emptyList = (TextView) getActivity().findViewById(R.id.text_no_events_msg);

        notificationUpdateReceiver = new NotificationUpdateReceiver(new Handler(), new ToDo() {
            @Override
            public void doSomething() {
                loadEventsFromDb();
                updateFilter("");
                updateView();
            }
        });

        adapterEventsItem = new AdapterEventsItem(this.getActivity(), 0, filterEventItem);
        lvEvents.setAdapter(adapterEventsItem);

        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TabFilter.this.getActivity(), EventsDescription.class);
                intent.putExtra("events_id", filterEventItem.get(i).getId());
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        // Reciever for update notifications
        IntentFilter intentFilterNotificationUpdate = new IntentFilter(BroadcastNames.BROADCAST_NEW_DATA);
        getActivity().registerReceiver(notificationUpdateReceiver, intentFilterNotificationUpdate);
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
            updateFilter("");
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
    private void updateFilter(String searchText) {
        getUserFilters();
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
                    }

                    filterEventItem.add(eventItem);
                }
            }
        }
    }

    private void loadEventsFromDb() {
        DatabaseManager databaseManager = new DatabaseManager(getActivity());
        try {
            databaseManager.open();
            List<EventItem> newEventItemList = databaseManager.getAllFutureEventsItem();
            databaseManager.close();

            if (newEventItemList == null) {
                return;
            }
            if (newEventItemList.size() <= 0) {
                return;
            }

            eventItemList.clear();
            for (EventItem eventItem : newEventItemList) {
                eventItemList.add(eventItem);
            }
            eventItemList = DatabaseManager.sortEventsByDate(eventItemList);

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    // update view
    private void updateView() {
        adapterEventsItem.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        updateFilter(s.toLowerCase());
        updateView();
        return false;
    }
}
