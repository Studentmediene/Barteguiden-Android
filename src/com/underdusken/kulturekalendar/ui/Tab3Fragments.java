package com.underdusken.kulturekalendar.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.data.EventsItem;
import com.underdusken.kulturekalendar.data.db.ManageDataBase;
import com.underdusken.kulturekalendar.ui.Adapter.AdapterMyEventsItem;

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
public class Tab3Fragments extends Fragment {

    // UI handlers
    Handler activityHandler = null;

    private AdapterMyEventsItem adapterEventsItem = null;
    private List<EventsItem> eventsItemList = null;


    private ListView lvEvents = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab3, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lvEvents = (ListView)getActivity().findViewById(R.id.tab3_events_list);

        loadEventsFromDb();

        setListViewAdapter();


    }


    private void loadEventsFromDb(){
        ManageDataBase manageDataBase = new ManageDataBase(getActivity());
        try {
            manageDataBase.open();
            eventsItemList = new ArrayList<EventsItem>();
            List<EventsItem> newEventsItemList = manageDataBase.getAllEventsFavorites();

            if(newEventsItemList!=null)
                if(newEventsItemList.size()>0){
                    eventsItemList.addAll(newEventsItemList);
                }
            manageDataBase.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private void setListViewAdapter(){
        adapterEventsItem = new AdapterMyEventsItem(this.getActivity(), 0, eventsItemList);
        lvEvents.setAdapter(adapterEventsItem);
    }

    private void updateView(){
        adapterEventsItem.notifyDataSetChanged();
    }

}
