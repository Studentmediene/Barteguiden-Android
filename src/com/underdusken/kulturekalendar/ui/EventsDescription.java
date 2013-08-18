package com.underdusken.kulturekalendar.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.data.EventsItem;
import com.underdusken.kulturekalendar.data.db.ManageDataBase;
import com.underdusken.kulturekalendar.mainhandler.BroadcastNames;
import com.underdusken.kulturekalendar.utils.ServiceLoadImage;
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

    private ServiceLoadImage serviceLoadImage = null;

    // UI
    private ImageView ivEventImage;
    private TextView tvTitle;
    private TextView tvPlaceName;
    private TextView tvAgeLimit;
    private ImageView ivCategoryId;
    private TextView tvPrice;
    private TextView tvDate;
    private ImageView ivFavorite;
    private TextView tvDescriptition;
    private RelativeLayout btMap;
    private RelativeLayout btWeb;


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

        serviceLoadImage = new ServiceLoadImage(this);

        initUI();

        // set data to UI

        //TODO  Check langugage
        String curLangugage = "nbk";
        setData(eventsItem, curLangugage);
    }

    // initialization UI
    private void initUI(){
        // Button add to favorite


        /*
        Button btNotification = (Button) findViewById(R.id.bt_add_notifications);
        btNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCalendar();
        }});
          */


        /*
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
        });*/

        ivEventImage = (ImageView)findViewById(R.id.event_image);
        tvTitle = (TextView)findViewById(R.id.event_title);
        tvPlaceName = (TextView)findViewById(R.id.event_place_name);
        tvAgeLimit = (TextView)findViewById(R.id.event_age);
        ivCategoryId = (ImageView)findViewById(R.id.event_category_id);
        tvPrice = (TextView)findViewById(R.id.event_price);
        tvDate = (TextView)findViewById(R.id.event_date);
        ivFavorite = (ImageView)findViewById(R.id.add_to_favorite);
        tvDescriptition = (TextView)findViewById(R.id.event_description);
        btMap = (RelativeLayout)findViewById(R.id.bt_map);
        btWeb = (RelativeLayout)findViewById(R.id.bt_web);

        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!eventsItem.getFavorite()){
                    eventsItem.setFavorite(true);
                    ManageDataBase manageDataBase = new ManageDataBase(EventsDescription.this);
                    try {
                        manageDataBase.open();
                        EventsItem testEventsItem = manageDataBase.updateEventsItemFavorites(eventsItem.getId(), true);
                        manageDataBase.close();
                    } catch (SQLException e) {}

                }else{
                    eventsItem.setFavorite(false);
                    ManageDataBase manageDataBase = new ManageDataBase(EventsDescription.this);
                    try {
                        manageDataBase.open();
                        EventsItem testEventsItem = manageDataBase.updateEventsItemFavorites(eventsItem.getId(), false);
                        manageDataBase.close();
                    } catch (SQLException e) {}

                }
                if(eventsItem.getFavorite())
                    ivFavorite.setImageResource(R.drawable.fav_hurt_on);
                else
                    ivFavorite.setImageResource(R.drawable.fav_hurt_off);
                Intent i = new Intent(BroadcastNames.BROADCAST_NEW_DATA);
                EventsDescription.this.sendBroadcast(i);
            }
        });

    }

    // set data to UI
    private void setData(EventsItem eventsItem, String language){
        tvTitle.setText(eventsItem.getTitle());
        tvPlaceName.setText(eventsItem.getPlaceName());
        tvAgeLimit.setText("" + eventsItem.getAgeLimit() + "+");

        if(eventsItem.getCategoryID().equals("SPORT"))
            ivCategoryId.setImageResource(R.drawable.category_sport_big);
        else if(eventsItem.getCategoryID().equals("PERFORMANCES"))
            ivCategoryId.setImageResource(R.drawable.category_performances_big);
        else if(eventsItem.getCategoryID().equals("MUSIC"))
            ivCategoryId.setImageResource(R.drawable.category_music_big);
        else if(eventsItem.getCategoryID().equals("EXHIBITIONS"))
            ivCategoryId.setImageResource(R.drawable.category_exhibitions_big);
        else if(eventsItem.getCategoryID().equals("NIGHTLIFE"))
            ivCategoryId.setImageResource(R.drawable.category_nightlife_big);
        else if(eventsItem.getCategoryID().equals("PRESENTATIONS"))
            ivCategoryId.setImageResource(R.drawable.category_presentations_big);
        else if(eventsItem.getCategoryID().equals("DEBATE"))
            ivCategoryId.setImageResource(R.drawable.category_debate_big);
        else if(eventsItem.getCategoryID().equals("OTHER"))
            ivCategoryId.setImageResource(R.drawable.category_other_big);

        int price = (int) eventsItem.getPrice();
        if(price>=0){
            if(price==0)
                tvPrice.setText("Free");
            else
                tvPrice.setText(""+price+" kr");
        }


        tvDescriptition.setText(eventsItem.getDescriptionNorwegian());
        SimpleTimeFormat stf = new SimpleTimeFormat(eventsItem.getDateStart());
        tvDate.setText(stf.getUserHeaderDate());

        if(eventsItem.getFavorite())
            ivFavorite.setImageResource(R.drawable.fav_hurt_on);
        else
            ivFavorite.setImageResource(R.drawable.fav_hurt_off);

        if(serviceLoadImage!=null){
            serviceLoadImage.loadImage(eventsItem.getImageURL(), ivEventImage, R.drawable.test_bg);
        }

        //TODO languages
        /*
        if (language.equals("nbk")){
            tvDescription.setText(eventsItem.getDescriptionNorwegian());
        }else{
            tvDescription.setText(eventsItem.getDescriptionEnglish());
        } */

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
