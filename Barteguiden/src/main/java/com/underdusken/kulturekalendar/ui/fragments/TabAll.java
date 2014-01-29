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
import android.widget.ListView;

import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.data.EventItem;
import com.underdusken.kulturekalendar.data.db.DatabaseManager;
import com.underdusken.kulturekalendar.mainhandler.BroadcastNames;
import com.underdusken.kulturekalendar.ui.activities.EventsDescription;
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
public class TabAll extends Fragment {
    // private receivers
    private NotificationUpdateReceiver notificationUpdateReceiver = null;

    // Items for list
    private AdapterEventsItem adapterEventsItem = null;
    private List<EventItem> eventItemList = new ArrayList<EventItem>();
    private List<EventItem> filterEventItem = new ArrayList<EventItem>();

    // ui
    private int priceInclude = -1;          // -1 all 0 free 1 paid
    private Button btFilterAll = null;
    private Button btFilterPaid = null;
    private Button btFilterFree = null;

    private ListView lvEvents = null;

    //data
    private long lastEventsId = -1;      // for getting only new events

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab_all_events, container, false);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Filter buttons
        btFilterAll = (Button) getActivity().findViewById(R.id.tab2_button_all);
        btFilterPaid = (Button) getActivity().findViewById(R.id.tab2_button_paid);
        btFilterFree = (Button) getActivity().findViewById(R.id.tab2_button_free);
        btFilterAll.setOnClickListener(onFilterClickListener);
        btFilterPaid.setOnClickListener(onFilterClickListener);
        btFilterFree.setOnClickListener(onFilterClickListener);

        lvEvents = (ListView) getActivity().findViewById(R.id.list_events);


        // Create new notification receiver
        notificationUpdateReceiver = new NotificationUpdateReceiver(new Handler(), new ToDo() {
            @Override
            public void doSomething() {
                loadEventsFromDb();
                updateFilter("");
                updateView();
            }
        });


        // Load events from Data Base
        loadEventsFromDb();

        // Update Filter list
        updateFilter("");

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

        if (eventItemList.size() == 0) {
            getActivity().findViewById(R.id.text_noevents).setVisibility(View.VISIBLE);
        } else {
            getActivity().findViewById(R.id.text_noevents).setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // unregister reciever
        getActivity().unregisterReceiver(notificationUpdateReceiver);
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


    /**
     * Update filter list by search name
     */
    private void updateFilter(String filter) {
        String searchText = filter.toLowerCase();
        filterEventItem.clear();
        if (eventItemList != null) if (searchText.equals("") && priceInclude == -1) {
            filterEventItem.addAll(eventItemList);
        } else {
            for (EventItem eventItem : eventItemList) {
                if (eventItem.getTitle().toLowerCase().contains(searchText)) {
                    if (priceInclude == -1) {
                        filterEventItem.add(eventItem);
                    } else if (priceInclude == 0) {
                        if (eventItem.getPrice() == 0) filterEventItem.add(eventItem);
                    } else if (priceInclude == 1) {
                        if (eventItem.getPrice() > 0) filterEventItem.add(eventItem);
                    }
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
            List<EventItem> newEventItemList = databaseManager.getAllEventsItemFromId(lastEventsId);

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
                Intent intent = new Intent(TabAll.this.getActivity(), EventsDescription.class);

                intent.putExtra("events_id", filterEventItem.get(filterEventItem.size() - i - 1).getId());

                startActivityForResult(intent, 1);
            }
        });
    }

    // update view
    private void updateView() {
        adapterEventsItem.notifyDataSetChanged();
    }

    private View.OnClickListener onFilterClickListener = new View.OnClickListener() {
        public void onClick(android.view.View view) {
            boolean _changes = false;
            if (view == btFilterAll) {
                if (priceInclude != -1) {
                    _changes = true;
                    priceInclude = -1;
                    btFilterAll.setBackgroundResource(R.drawable.bt_left_on);
                    btFilterPaid.setBackgroundResource(R.drawable.bt_midle_off);
                    btFilterFree.setBackgroundResource(R.drawable.bt_right_off);
                }
            } else if (view == btFilterFree) {
                if (priceInclude != 0) {
                    _changes = true;
                    priceInclude = 0;
                    btFilterAll.setBackgroundResource(R.drawable.bt_left_off);
                    btFilterPaid.setBackgroundResource(R.drawable.bt_midle_off);
                    btFilterFree.setBackgroundResource(R.drawable.bt_right_on);
                }
            } else if (view == btFilterPaid) {
                if (priceInclude != 1) {
                    _changes = true;
                    priceInclude = 1;
                    btFilterAll.setBackgroundResource(R.drawable.bt_left_off);
                    btFilterPaid.setBackgroundResource(R.drawable.bt_midle_on);
                    btFilterFree.setBackgroundResource(R.drawable.bt_right_off);
                }
            }

            if (_changes) {
                updateFilter("");
                updateView();
            }
        }
    };

}
