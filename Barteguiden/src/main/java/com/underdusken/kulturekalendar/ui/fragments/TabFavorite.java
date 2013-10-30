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
import com.underdusken.kulturekalendar.data.EventsItem;
import com.underdusken.kulturekalendar.data.db.ManageDataBase;
import com.underdusken.kulturekalendar.ui.Adapter.AdapterEventsItem;
import com.underdusken.kulturekalendar.ui.EventsDescription;
import com.underdusken.kulturekalendar.ui.Receiver.NotificationUpdateReceiver;
import com.underdusken.kulturekalendar.utils.ServiceLoadImage;
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
public class TabFavorite extends Fragment {
    // private receivers
    private NotificationUpdateReceiver notificationUpdateReceiver = null;

    // Items for list
    private AdapterEventsItem adapterEventsItem = null;
    private List<EventsItem> eventsItemList = new ArrayList<EventsItem>();
    private List<EventsItem> filterEventsItem = new ArrayList<EventsItem>();

    // ui
    private ListView lvEvents = null;
    private EditText etSearch = null;


    //data
    private long lastEventsId = -1;      // for getting only new events

    //Image service
    private ServiceLoadImage serviceLoadImage = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        if(eventsItemList.size()==0){
            getActivity().findViewById(R.id.title_no_events).setVisibility(View.VISIBLE);
        }else{
            getActivity().findViewById(R.id.title_no_events).setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        /*// Reciever for update notifications
        IntentFilter intentFilterNotificationUpdate = new IntentFilter(BroadcastNames.BROADCAST_NEW_DATA);
        getActivity().registerReceiver(notificationUpdateReceiver, intentFilterNotificationUpdate);
*/
        //Start Image loader
        serviceLoadImage = new ServiceLoadImage(getActivity());
        adapterEventsItem.setServiceLoadImage(serviceLoadImage);

        //
        loadEventsFromDb();
        updateFilter();
        updateView();

        if(eventsItemList.size()==0){
            getActivity().findViewById(R.id.title_no_events).setVisibility(View.VISIBLE);
        }else{
            getActivity().findViewById(R.id.title_no_events).setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // unregister reciever
        /// getActivity().unregisterReceiver(notificationUpdateReceiver);

        if(serviceLoadImage!=null){
            serviceLoadImage.exit();
        }

    }

    /**
     * callback update search information
     */
    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {}

        @Override
        public void afterTextChanged(Editable s) {
            updateFilter();
            updateView();
        }
    };


    /**
     * Update filter list by search name
     */
    private void updateFilter(){
        String searchText = etSearch.getText().toString().toLowerCase();
        filterEventsItem.clear();
        if(eventsItemList!=null)
            if(searchText.equals("")){
                filterEventsItem.addAll(eventsItemList);
            }else{
                for(EventsItem eventsItem:eventsItemList){
                    if(eventsItem.getTitle().toLowerCase().contains(searchText)){
                        filterEventsItem.add(eventsItem);
                    }
                }
            }
    }

    /**
     * Load data from DataBase (all)
     */
    private void loadEventsFromDb(){
        ManageDataBase manageDataBase = new ManageDataBase(getActivity());
        try {
            manageDataBase.open();
            List<EventsItem> newEventsItemList = manageDataBase.getAllEventsFavorites();
            eventsItemList.clear();
            if(newEventsItemList!=null){
                if(newEventsItemList.size()>0){
                    //Delete no events title
                    getActivity().findViewById(R.id.title_no_events).setVisibility(View.GONE);

                    lastEventsId = newEventsItemList.get(newEventsItemList.size()-1).getId();

                    for(EventsItem eventsItem: newEventsItemList){
                        eventsItemList.add(eventsItem);
                    }
                    eventsItemList = ManageDataBase.sortEventsByDate(eventsItemList);
                }
            }
            manageDataBase.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    /**
     *  Create new list Adapter
     */
    private void createAdapter(){
        adapterEventsItem = new AdapterEventsItem(this.getActivity(), 0, filterEventsItem);
        adapterEventsItem.setServiceLoadImage(serviceLoadImage);
        lvEvents.setAdapter(adapterEventsItem);
        // Open event description

        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TabFavorite.this.getActivity(), EventsDescription.class);

                intent.putExtra("events_id", filterEventsItem.get(filterEventsItem.size() - i - 1).getId());

                startActivityForResult(intent, 1);
            }
        });
    }

    // update view
    private void updateView(){
        adapterEventsItem.notifyDataSetChanged();
    }

}
