package com.underdusken.kulturekalendar.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.data.EventsItem;
import com.underdusken.kulturekalendar.data.db.ManageDataBase;
import com.underdusken.kulturekalendar.mainhandler.BroadcastNames;
import com.underdusken.kulturekalendar.sharedpreference.UserFilterPreference;
import com.underdusken.kulturekalendar.utils.ImageLoader;
import com.underdusken.kulturekalendar.utils.SimpleTimeFormat;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Locale;

public class EventsDescription extends Activity {

    private static final String TAG = "EventsDescription";
    private ImageLoader imageLoader;

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
    private RelativeLayout btCalendar;


    private EventsItem eventsItem = null;

    private boolean isNorwegianPhone = true;
    private String globalEventText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_description);

        //get locale
        Locale current = getResources().getConfiguration().locale;
        String localeLanguage = current.getDisplayLanguage();
        if (localeLanguage.contains("norsk")) isNorwegianPhone = true;
        else isNorwegianPhone = false;

        imageLoader = new ImageLoader(this);

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

        if (eventsItem == null) finish();
        // initialization UI
        initUI();

        // set data to UI

        setData(eventsItem);

        findViewById(R.id.bt_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Join me at " + eventsItem.getTitle();
                if (eventsItem.getEventURL().length() > 0) {
                    shareBody += " (" + eventsItem.getEventURL() + ")";
                }
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Event in Trondheim");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });


    }

    // initialization UI
    private void initUI() {
        // Button add to favorite


        /*
        Button btNotification = (Button) findViewById(R.id.bt_add_notifications);
        btNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCalendar();
        }});
          */


        ivEventImage = (ImageView) findViewById(R.id.event_image);
        tvTitle = (TextView) findViewById(R.id.event_title);
        tvPlaceName = (TextView) findViewById(R.id.event_place_name);
        tvAgeLimit = (TextView) findViewById(R.id.event_age);
        ivCategoryId = (ImageView) findViewById(R.id.event_category_id);
        tvPrice = (TextView) findViewById(R.id.event_price);
        tvDate = (TextView) findViewById(R.id.event_date);
        ivFavorite = (ImageView) findViewById(R.id.add_to_favorite);
        tvDescriptition = (TextView) findViewById(R.id.event_description);
        btMap = (RelativeLayout) findViewById(R.id.bt_map);
        btWeb = (RelativeLayout) findViewById(R.id.bt_web);

        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!eventsItem.getFavorite()) {
                    eventsItem.setFavorite(true);
                    ManageDataBase manageDataBase = new ManageDataBase(EventsDescription.this);
                    try {
                        manageDataBase.open();
                        EventsItem testEventsItem = manageDataBase.updateEventsItemFavorites(
                                eventsItem.getId(), true);
                        manageDataBase.close();
                    } catch (SQLException e) {
                    }

                } else {
                    eventsItem.setFavorite(false);
                    ManageDataBase manageDataBase = new ManageDataBase(EventsDescription.this);
                    try {
                        manageDataBase.open();
                        EventsItem testEventsItem = manageDataBase.updateEventsItemFavorites(
                                eventsItem.getId(), false);
                        manageDataBase.close();
                    } catch (SQLException e) {
                    }

                }
                if (eventsItem.getFavorite()) {
                    ivFavorite.setImageResource(R.drawable.fav_hurt_on);
                    if (new UserFilterPreference(EventsDescription.this).isAutoAddToCalendar())
                        addToCalendar();
                } else ivFavorite.setImageResource(R.drawable.fav_hurt_off);
                Intent i = new Intent(BroadcastNames.BROADCAST_NEW_DATA);
                EventsDescription.this.sendBroadcast(i);
            }
        });

        btCalendar = (RelativeLayout) findViewById(R.id.add_to_calendar);
        btCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCalendar();
            }
        });

    }

    // set data to UI
    private void setData(final EventsItem eventsItem) {
        tvTitle.setText(eventsItem.getTitle());
        tvPlaceName.setText(eventsItem.getPlaceName());
        tvAgeLimit.setText("" + eventsItem.getAgeLimit() + "+");

        String category = eventsItem.getCategoryID();
        int imageResource = 0;
        if (category.equals("SPORT"))
            imageResource = R.drawable.category_sport_big;
        else if (category.equals("PERFORMANCES"))
            imageResource = R.drawable.category_performances_big;
        else if (category.equals("MUSIC"))
            imageResource = R.drawable.category_music_big;
        else if (category.equals("EXHIBITIONS"))
            imageResource = R.drawable.category_exhibitions_big;
        else if (category.equals("NIGHTLIFE"))
            imageResource = R.drawable.category_nightlife_big;
        else if (category.equals("PRESENTATIONS"))
            imageResource = R.drawable.category_presentations_big;
        else if (category.equals("DEBATE"))
            imageResource = R.drawable.category_debate_big;
        else if (category.equals("OTHER"))
            imageResource = R.drawable.category_other_big;
        ivCategoryId.setImageResource(imageResource);

        int price = (int) eventsItem.getPrice();
        if (price >= 0) {
            if (price == 0) tvPrice.setText("Free");
            else tvPrice.setText("" + price + " kr");
        }


        SimpleTimeFormat stf = new SimpleTimeFormat(eventsItem.getDateStart());
        tvDate.setText(stf.getUserHeaderDate());

        if (eventsItem.getFavorite()){
            ivFavorite.setImageResource(R.drawable.fav_hurt_on);
        }
        else {
            ivFavorite.setImageResource(R.drawable.fav_hurt_off);
        }

        imageLoader.setImageViewResource(ivEventImage, eventsItem.getImageURL());
        Log.d(TAG, "ImageURL: " + eventsItem.getImageURL());

        // Web button
        if (eventsItem.getEventURL().length() > 1) {
            btWeb.setVisibility(View.VISIBLE);
            btWeb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(eventsItem.getEventURL())));
                }
            });
        } else {
            btWeb.setVisibility(View.GONE);
        }

        // Map button
        if (eventsItem.getIsGeo()) {
            btMap.setVisibility(View.VISIBLE);
            btMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(EventsDescription.this, EventsMap.class);
                    intent.putExtra("events_latitude", eventsItem.getGeoLatitude());
                    intent.putExtra("events_longitude", eventsItem.getGeoLongitude());
                    intent.putExtra("events_title", eventsItem.getTitle());
                    intent.putExtra("events_place_name", eventsItem.getPlaceName());

                    startActivity(intent);
                }
            });
        } else {
            btMap.setVisibility(View.GONE);
        }

        if (eventsItem.getNotificationId() != 0) {
            ImageView iv = (ImageView) findViewById(R.id.ic_calendar);
            iv.setImageResource(R.drawable.ic_calendar_on);
            findViewById(R.id.calendar_text).setVisibility(View.GONE);
        }


        // Get correct language text
        if (isNorwegianPhone) {
            if (eventsItem.getDescriptionNorwegian().length() > 0) {
                globalEventText = eventsItem.getDescriptionNorwegian();
            } else {
                globalEventText = eventsItem.getDescriptionEnglish();
            }
        } else {
            if (eventsItem.getDescriptionEnglish().length() > 0) {
                globalEventText = eventsItem.getDescriptionEnglish();
            } else {
                globalEventText = eventsItem.getDescriptionNorwegian();
            }
        }

        tvDescriptition.setText(globalEventText);
    }

    private void addToCalendar() {
        if (eventsItem.getNotificationId() == 0) {

            eventsItem.setNotificationId(1);


            long eventStartTime = new SimpleTimeFormat(eventsItem.getDateStart()).getMs();
            Calendar cal = Calendar.getInstance();
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra("beginTime", eventStartTime);
            intent.putExtra("endTime", eventStartTime + 1000 * 60 * 60);
            intent.putExtra("title", eventsItem.getTitle());
            intent.putExtra("eventLocation", eventsItem.getPlaceName());
            intent.putExtra("description", globalEventText);
            try {
                startActivity(intent);
                ManageDataBase manageDataBase = new ManageDataBase(EventsDescription.this);
                try {
                    manageDataBase.open();
                    EventsItem testEventsItem = manageDataBase.updateEventsItemCalendar(eventsItem.getId(),
                            eventsItem.getNotificationId());
                    manageDataBase.close();
                } catch (SQLException e) {
                }
                ImageView iv = (ImageView) findViewById(R.id.ic_calendar);
                iv.setImageResource(R.drawable.ic_calendar_on);
                findViewById(R.id.calendar_text).setVisibility(View.GONE);
            } catch (Exception e) {

            }
        }
    }


}
