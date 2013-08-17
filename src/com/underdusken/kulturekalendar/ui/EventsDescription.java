package com.underdusken.kulturekalendar.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.data.EventsItem;
import com.underdusken.kulturekalendar.data.db.ManageDataBase;
import com.underdusken.kulturekalendar.mainhandler.BroadcastNames;
import com.underdusken.kulturekalendar.utils.SimpleTimeFormat;

import java.sql.SQLException;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: pavelarteev
 * Date: 10/24/12
 * Time: 7:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventsDescription extends Activity{

    // UI
    private TextView tvName;
    private TextView tvDateStart;
    private TextView tvDateEnd;
    private TextView tvAddress;
    private TextView tvDescription;
    private TextView tvRecommendation;

    private EventsItem eventsItem = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_description);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
        }



        long eventsId = extras.getLong("events_id");



        ManageDataBase manageDataBase = new ManageDataBase(this);
        try {
            manageDataBase.open();
            eventsItem = manageDataBase.getEventsItemById(eventsId);
            manageDataBase.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if(eventsItem == null)
            finish();
        // initialization UI
        initUI();

        // set data to UI

        //TODO  Check langugage
        String curLangugage = "nbk";
        setData(eventsItem, curLangugage);
    }

    // initialization UI
    private void initUI(){
        // Button add to favorite
        Button btAddFavorite = (Button) findViewById(R.id.bt_add_to_favorites);
        btAddFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!eventsItem.getFavorite()){
                    eventsItem.setFavorite(true);
                    ManageDataBase manageDataBase = new ManageDataBase(EventsDescription.this);
                    try {
                        manageDataBase.open();
                        EventsItem testEventsItem = manageDataBase.updateEventsItemFavorites(eventsItem.getId(), true);
                        manageDataBase.close();
                        Toast.makeText(EventsDescription.this, "Events added to favorites", Toast.LENGTH_SHORT).show();
                    } catch (SQLException e) {}

                }else{
                    eventsItem.setFavorite(false);
                    ManageDataBase manageDataBase = new ManageDataBase(EventsDescription.this);
                    try {
                        manageDataBase.open();
                        EventsItem testEventsItem = manageDataBase.updateEventsItemFavorites(eventsItem.getId(), false);
                        manageDataBase.close();
                        Toast.makeText(EventsDescription.this, "Events deleted from favorites", Toast.LENGTH_SHORT).show();
                    } catch (SQLException e) {}

                }
                Intent i = new Intent(BroadcastNames.BROADCAST_NEW_DATA);
                EventsDescription.this.sendBroadcast(i);

            }
        });


        Button btNotification = (Button) findViewById(R.id.bt_add_notifications);
        btNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCalendar();
        }});



        Button btMap = (Button) findViewById(R.id.bt_show_on_map);
        btMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsDescription.this, EventsMap.class);
                intent.putExtra("events_latitude", eventsItem.getGeoLatitude());
                intent.putExtra("events_longitude", eventsItem.getGeoLongitude());
                intent.putExtra("events_title", eventsItem.getTitle());
                intent.putExtra("events_description", eventsItem.getAddress());

                startActivity(intent);
            }
        });

        tvName = (TextView)findViewById(R.id.event_name);
        tvDateStart = (TextView)findViewById(R.id.date_start);
        tvDateEnd = (TextView)findViewById(R.id.date_end);
        tvAddress = (TextView)findViewById(R.id.address);
        tvDescription = (TextView)findViewById(R.id.description);
        tvRecommendation = (TextView)findViewById(R.id.recommendation);
    }

    // set data to UI
    private void setData(EventsItem eventsItem, String language){
        tvName.setText(eventsItem.getTitle());
        tvDateStart.setText(new SimpleTimeFormat(eventsItem.getDateStart()).getUserFullDate());
        //tvDateEnd.setText(new SimpleTimeFormat(eventsItem.getDateEnd()).getUserFullDate());
        tvAddress.setText(eventsItem.getAddress());
        if (language.equals("nbk")){
            tvDescription.setText(eventsItem.getDescriptionNorwegian());
        }else{
            tvDescription.setText(eventsItem.getDescriptionEnglish());
        }

    }

    private void addToCalendar(){

        long eventStartTime =  new SimpleTimeFormat(eventsItem.getDateStart()).getMs();
        //long eventEndTime = new SimpleTimeFormat(eventsItem.getDateEnd()).getMs();
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", eventStartTime);
        //if(eventEndTime >= eventStartTime){
        //   intent.putExtra("endTime", eventEndTime);
        //}else{
        intent.putExtra("endTime", eventStartTime + 1000*60*60);
        //}
        intent.putExtra("title", "A Test Event from android app");
        intent.putExtra("description", "pablo.test://rest");
        startActivity(intent);
    }

    //TODO
    private void deleteFromCalendar(){

    }

}
