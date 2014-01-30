package com.underdusken.kulturekalendar.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.data.EventItem;
import com.underdusken.kulturekalendar.data.db.DatabaseManager;
import com.underdusken.kulturekalendar.mainhandler.BroadcastNames;
import com.underdusken.kulturekalendar.ui.activities.EventsDescription;
import com.underdusken.kulturekalendar.ui.adapters.AdapterEventsItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class TabFeatured extends Fragment {

    // UI handlers
    private Handler activityHandler = null;
    // private recievers
    private NotificationUpdateReciever notificationUpdateReciever = new NotificationUpdateReciever();


    private AdapterEventsItem adapterEventsItem = null;
    private List<EventItem> eventItemList = new ArrayList<EventItem>();

    private StickyListHeadersListView eventList = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.featured, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        eventList = (StickyListHeadersListView) getActivity().findViewById(R.id.list_events);

        activityHandler = new Handler();

        //Initialization adapter for ListView
        adapterEventsItem = new AdapterEventsItem(this.getActivity(), 0, eventItemList);
        eventList.setAdapter(adapterEventsItem);

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TabFeatured.this.getActivity(), EventsDescription.class);
                intent.putExtra("events_id", eventItemList.get(eventItemList.size() - i - 1).getId());
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        loadEventsFromDb();
        if (eventItemList.size() == 0) {
            getActivity().findViewById(R.id.text_noevents).setVisibility(View.VISIBLE);
        } else {
            getActivity().findViewById(R.id.text_noevents).setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilterNotificationUpdate = new IntentFilter(BroadcastNames.BROADCAST_NEW_DATA);
        getActivity().registerReceiver(notificationUpdateReciever, intentFilterNotificationUpdate);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(notificationUpdateReciever);
    }


    private void loadEventsFromDb() {
        DatabaseManager databaseManager = new DatabaseManager(getActivity());
        try {
            databaseManager.open();
            List<EventItem> newEventItemList = databaseManager.getAllFutureEventsItem();
            databaseManager.close();

            if (newEventItemList != null) {
                if (newEventItemList.size() <= 0) {
                    return;
                }
                for (EventItem eventItem : newEventItemList) {
                    if (eventItem.getIsRecommended()) {
                        eventItemList.add(eventItem);
                    }
                }
                eventItemList = DatabaseManager.sortEventsByDate(eventItemList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateView() {
        adapterEventsItem.notifyDataSetChanged();
    }


    class NotificationUpdateReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent arg1) {
            activityHandler.post(new Runnable() {
                @Override
                public void run() {
                    // update screen information
                    loadEventsFromDb();
                    updateView();
                }
            });
        }
    }

}
