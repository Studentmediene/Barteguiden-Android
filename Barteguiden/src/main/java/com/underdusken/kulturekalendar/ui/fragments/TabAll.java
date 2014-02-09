package com.underdusken.kulturekalendar.ui.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

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

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class TabAll extends Fragment implements SearchView.OnQueryTextListener {
    private static final String TAG = "TabAll";
    // private receivers
    private NotificationUpdateReceiver notificationUpdateReceiver = null;

    // Items for list
    private AdapterEventsItem adapterEventsItem = null;
    private List<EventItem> eventItemList = new ArrayList<EventItem>();
    private List<EventItem> filterEventItem = new ArrayList<EventItem>();

    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    // ui
    private int priceInclude = -1;          // -1 all 0 free 1 paid

    private StickyListHeadersListView lvEvents = null;

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

        lvEvents = (StickyListHeadersListView) getActivity().findViewById(R.id.list_events_all);
        progressBar = (ProgressBar) getActivity().findViewById(R.id.all_progress);

        // Create new notification receiver
        notificationUpdateReceiver = new NotificationUpdateReceiver(new Handler(), new ToDo() {
            @Override
            public void doSomething() {
                new DatabaseTask().execute();
                updateFilter("");
                updateView();
            }
        });

        // TODO: change this back
        adapterEventsItem = new AdapterEventsItem(this.getActivity(), 0, filterEventItem);
        lvEvents.setAdapter(adapterEventsItem);
        // Open event description

        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TabAll.this.getActivity(), EventsDescription.class);
                intent.putExtra("events_id", filterEventItem.get(i).getId());
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        new DatabaseTask().execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilterNotificationUpdate = new IntentFilter(BroadcastNames.BROADCAST_NEW_DATA);
        getActivity().registerReceiver(notificationUpdateReceiver, intentFilterNotificationUpdate);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(notificationUpdateReceiver);
    }

    private void updateFilter(String filter) {
        String searchText = filter.toLowerCase();
        filterEventItem.clear();
        if (eventItemList != null) {
            if (searchText.equals("") && priceInclude == -1) {
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
    }

    private void updateView() {
        adapterEventsItem.notifyDataSetChanged();
        if (eventItemList.size() <= 0) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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

    private class DatabaseTask extends AsyncTask<Void, Void, List<EventItem>> {

        @Override
        protected void onPostExecute(List<EventItem> eventItems) {
            super.onPostExecute(eventItems);
            eventItemList = eventItems;
            updateFilter("");
            updateView();
        }

        @Override
        protected List<EventItem> doInBackground(Void... params) {
            List<EventItem> list = new ArrayList<EventItem>();
            DatabaseManager databaseManager = new DatabaseManager(getActivity());
            try {
                databaseManager.open();
                list = databaseManager.getAllFutureEventsItem();
                databaseManager.close();
                if (list == null) {
                    throw new IllegalStateException("DatabaseManager.getAllFutureEventsItem returned null.");
                }
                if (list.size() <= 0) {
                    return null;
                }
                DatabaseManager.sortEventsByDate(list);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return list;
        }
    }
}
