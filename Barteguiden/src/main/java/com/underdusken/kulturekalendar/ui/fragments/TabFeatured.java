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
import android.widget.ListView;

import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.data.EventItem;
import com.underdusken.kulturekalendar.data.db.DatabaseManager;
import com.underdusken.kulturekalendar.mainhandler.BroadcastNames;
import com.underdusken.kulturekalendar.ui.activities.EventsDescription;
import com.underdusken.kulturekalendar.ui.adapters.AdapterEventsItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TabFeatured extends Fragment {

    // UI handlers
    private Handler activityHandler = null;
    // private recievers
    private NotificationUpdateReciever notificationUpdateReciever = new NotificationUpdateReciever();


    private AdapterEventsItem adapterEventsItem = null;
    private List<EventItem> eventItemList = new ArrayList<EventItem>();

    private ListView lvEvents = null;


    //data
    private long lastEventsId = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lvEvents = (ListView) getActivity().findViewById(R.id.tab1_events_list);

        loadEventsFromDb();

        activityHandler = new Handler();

        //Initialization adapter for ListView
        setListViewAdapter();

        if (eventItemList.size() == 0) {
            getActivity().findViewById(R.id.text_noevents).setVisibility(View.VISIBLE);
        } else {
            getActivity().findViewById(R.id.text_noevents).setVisibility(View.GONE);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        //updateHistoryView();
        // Reciever for update notifications
        IntentFilter intentFilterNotificationUpdate = new IntentFilter(BroadcastNames.BROADCAST_NEW_DATA);

        getActivity().registerReceiver(notificationUpdateReciever, intentFilterNotificationUpdate);

        if (eventItemList.size() == 0) {
            getActivity().findViewById(R.id.text_noevents).setVisibility(View.VISIBLE);
        } else {
            getActivity().findViewById(R.id.text_noevents).setVisibility(View.GONE);
        }
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
            List<EventItem> newEventItemList = databaseManager.getAllEventsItemFromId(lastEventsId);

            if (newEventItemList != null) if (newEventItemList.size() > 0) {
                //Delete no events title
                getActivity().findViewById(R.id.text_noevents).setVisibility(View.GONE);

                lastEventsId = newEventItemList.get(newEventItemList.size() - 1).getId();
                for (EventItem eventItem : newEventItemList) {
                    if (eventItem.getIsRecommended()) {
                        eventItemList.add(eventItem);
                    }
                }
                eventItemList = DatabaseManager.sortEventsByDate(eventItemList);

            }
            databaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private void setListViewAdapter() {
        adapterEventsItem = new AdapterEventsItem(this.getActivity(), 0, eventItemList);

        lvEvents.setAdapter(adapterEventsItem);
        // Open event description

        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TabFeatured.this.getActivity(), EventsDescription.class);

                intent.putExtra("events_id", eventItemList.get(eventItemList.size() - i - 1).getId());

                startActivityForResult(intent, 1);
            }
        });
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
