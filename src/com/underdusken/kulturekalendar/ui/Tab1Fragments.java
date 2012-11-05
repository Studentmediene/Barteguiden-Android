package com.underdusken.kulturekalendar.ui;

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
import com.underdusken.kulturekalendar.data.EventsItem;
import com.underdusken.kulturekalendar.data.db.ManageDataBase;
import com.underdusken.kulturekalendar.mainhandler.BroadcastNames;
import com.underdusken.kulturekalendar.ui.Adapter.AdapterEventsItem;
import com.underdusken.kulturekalendar.utils.ServiceLoadImage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Tab1Fragments extends Fragment {

    // UI handlers
    private Handler activityHandler = null;
    // private recievers
    private NotificationUpdateReciever notificationUpdateReciever = new NotificationUpdateReciever();


    private AdapterEventsItem adapterEventsItem = null;
    private List<EventsItem> eventsItemList = new ArrayList<EventsItem>();

    private ListView lvEvents = null;


    //data
    private long lastEventsId = 0;

    //Image service
    private ServiceLoadImage serviceLoadImage = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lvEvents = (ListView)getActivity().findViewById(R.id.tab1_events_list);


        loadEventsFromDb();

        activityHandler = new Handler();

        //Initialization adapter for ListView
        setListViewAdapter();
    }


    @Override
    public void onResume() {
        super.onResume();

        //updateHistoryView();
        // Reciever for update notifications
        IntentFilter intentFilterNotificationUpdate = new IntentFilter(BroadcastNames.BROADCAST_NEW_DATA);

        getActivity().registerReceiver(notificationUpdateReciever, intentFilterNotificationUpdate);

        //Start Image loader
        serviceLoadImage = new ServiceLoadImage(getActivity());

        adapterEventsItem.setServiceLoadImage(serviceLoadImage);

    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(notificationUpdateReciever);
        adapterEventsItem.setServiceLoadImage(serviceLoadImage);
        if(serviceLoadImage!=null){
            serviceLoadImage.exit();
        }

    }


    private void loadEventsFromDb(){
        ManageDataBase manageDataBase = new ManageDataBase(getActivity());
        try {
            manageDataBase.open();
            List<EventsItem> newEventsItemList = manageDataBase.getAllEventsItemFromId(lastEventsId);

            if(newEventsItemList!=null)
                if(newEventsItemList.size()>0){
                    for(EventsItem eventsItem: newEventsItemList){
                      if(!eventsItem.getWeekendRecommendationEnglish().equals("") ||
                               !eventsItem.getWeekendRecommendationNorwegian().equals("")){
                             eventsItemList.add(eventsItem);
                      }
                    }
                    lastEventsId = newEventsItemList.get(newEventsItemList.size()-1).getId();
                }
            manageDataBase.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private void setListViewAdapter(){
        adapterEventsItem = new AdapterEventsItem(this.getActivity(), 0, eventsItemList);

        lvEvents.setAdapter(adapterEventsItem);
        // Open event description

        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Tab1Fragments.this.getActivity(), EventsDescription.class);

                intent.putExtra("events_id", eventsItemList.get(eventsItemList.size() - i - 1).getId());

                startActivityForResult(intent, 1);
            }
        });
    }

    private void updateView(){
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
