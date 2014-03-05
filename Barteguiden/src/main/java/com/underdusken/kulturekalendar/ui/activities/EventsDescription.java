package com.underdusken.kulturekalendar.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.data.EventItem;
import com.underdusken.kulturekalendar.data.db.DatabaseManager;
import com.underdusken.kulturekalendar.mainhandler.BroadcastNames;
import com.underdusken.kulturekalendar.utils.ImageLoader;
import com.underdusken.kulturekalendar.utils.SimpleTimeFormat;

import java.sql.SQLException;
import java.util.Locale;

public class EventsDescription extends Activity {

    private static final String TAG = "EventsDescription";
    private ImageLoader imageLoader;

    private ImageView image;
    private TextView title;
    private TextView place;
    private TextView age;
    private ImageView category;
    private TextView price;
    private TextView date;
    private ImageView favoriteIcon;
    private TextView description;

    private EventItem eventItem = null;

    private boolean isNorwegianPhone = true;
    private String globalEventText = "";

    private RelativeLayout calendarButton;

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

        DatabaseManager databaseManager = new DatabaseManager(this);
        try {
            databaseManager.open();
            eventItem = databaseManager.getEventsItemById(eventsId);
            databaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        initUI();

        setData(eventItem);
    }

    // initialization UI
    private void initUI() {
        image = (ImageView) findViewById(R.id.event_image);
        title = (TextView) findViewById(R.id.event_title);
        place = (TextView) findViewById(R.id.event_place_name);
        age = (TextView) findViewById(R.id.event_age);
        category = (ImageView) findViewById(R.id.event_category_id);
        price = (TextView) findViewById(R.id.event_price);
        date = (TextView) findViewById(R.id.event_date);
        favoriteIcon = (ImageView) findViewById(R.id.add_to_favorite);
        description = (TextView) findViewById(R.id.event_description);

        favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!eventItem.getFavorite()) {
                    eventItem.setFavorite(true);
                    DatabaseManager databaseManager = new DatabaseManager(EventsDescription.this);
                    try {
                        databaseManager.open();
                        EventItem testEventItem = databaseManager.updateEventsItemFavorites(
                                eventItem.getId(), true);
                        databaseManager.close();
                    } catch (SQLException e) {
                    }

                } else {
                    eventItem.setFavorite(false);
                    DatabaseManager databaseManager = new DatabaseManager(EventsDescription.this);
                    try {
                        databaseManager.open();
                        EventItem testEventItem = databaseManager.updateEventsItemFavorites(
                                eventItem.getId(), false);
                        databaseManager.close();
                    } catch (SQLException e) {
                    }

                }
                if (eventItem.getFavorite()) {
                    favoriteIcon.setImageResource(R.drawable.ic_action_important);
                } else favoriteIcon.setImageResource(R.drawable.ic_action_not_important);
                Intent i = new Intent(BroadcastNames.BROADCAST_NEW_DATA);
                EventsDescription.this.sendBroadcast(i);
            }
        });
        calendarButton = (RelativeLayout) findViewById(R.id.add_to_calendar);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCalendar();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.desc_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // set data to UI
    private void setData(final EventItem eventItem) {
        title.setText(eventItem.getTitle());
        place.setText(eventItem.getPlaceName());
        age.setText("" + eventItem.getAgeLimit() + "+");

        String category = eventItem.getCategoryID();
        int imageResource = 0;
        if (category.equals("SPORT"))
            imageResource = R.drawable.category_sport;
        else if (category.equals("PERFORMANCES"))
            imageResource = R.drawable.category_performances;
        else if (category.equals("MUSIC"))
            imageResource = R.drawable.category_music;
        else if (category.equals("EXHIBITIONS"))
            imageResource = R.drawable.category_exhibitions;
        else if (category.equals("NIGHTLIFE"))
            imageResource = R.drawable.category_nightlife;
        else if (category.equals("PRESENTATIONS"))
            imageResource = R.drawable.category_presentations;
        else if (category.equals("DEBATE"))
            imageResource = R.drawable.category_debate;
        else if (category.equals("OTHER"))
            imageResource = R.drawable.category_other;
        this.category.setImageResource(imageResource);

        int price = (int) eventItem.getPrice();
        if (price >= 0) {
            if (price == 0) this.price.setText("Free");
            else this.price.setText("" + price + " kr");
        }


        SimpleTimeFormat stf = new SimpleTimeFormat(eventItem.getDateStart());
        date.setText(stf.getUserHeaderDate());

        if (eventItem.getFavorite()) {
            favoriteIcon.setImageResource(R.drawable.ic_action_important);
        } else {
            favoriteIcon.setImageResource(R.drawable.ic_action_not_important);
        }

        imageLoader.setImageViewResourceAlphaAnimated(image, eventItem.getImageURL());

        if (eventItem.getNotificationId() != 0) {
            TextView calText = (TextView) findViewById(R.id.calendar_text);
            calText.setText(R.string.calendar_added);
            calText.setTextColor(getResources().getColor(R.color.green));
        }

        // Get correct language text
        if (isNorwegianPhone) {
            if (eventItem.getDescriptionNorwegian().length() > 0) {
                globalEventText = eventItem.getDescriptionNorwegian();
            } else {
                globalEventText = eventItem.getDescriptionEnglish();
            }
        } else {
            if (eventItem.getDescriptionEnglish().length() > 0) {
                globalEventText = eventItem.getDescriptionEnglish();
            } else {
                globalEventText = eventItem.getDescriptionNorwegian();
            }
        }

        description.setText(globalEventText);
    }

    private void addToCalendar() {
        if (eventItem.getNotificationId() == 0) {

            eventItem.setNotificationId(1);

            long eventStartTime = new SimpleTimeFormat(eventItem.getDateStart()).getMs();
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra("beginTime", eventStartTime);
            intent.putExtra("endTime", eventStartTime + 1000 * 60 * 60);
            intent.putExtra("title", eventItem.getTitle());
            intent.putExtra("eventLocation", eventItem.getPlaceName());
            intent.putExtra("description", globalEventText);

            startActivity(intent);
            DatabaseManager databaseManager = new DatabaseManager(EventsDescription.this);
            try {
                databaseManager.open();
                databaseManager.updateEventsItemCalendar(eventItem.getId(),
                        eventItem.getNotificationId());
                databaseManager.close();
            } catch (SQLException e) {
            }
            TextView calText = (TextView) findViewById(R.id.calendar_text);
            calText.setText(R.string.calendar_added);
            calText.setTextColor(getResources().getColor(R.color.green));
        }
    }

    private void share() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        String shareBody = getString(R.string.join_me) + eventItem.getTitle();
        if (eventItem.getEventURL().length() > 0) {
            shareBody += " (" + eventItem.getEventURL() + ")";
        }
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.event_in_trondheim));
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.desc_share:
                share();
                break;
            case R.id.desc_web:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(eventItem.getEventURL())));
                break;
            case R.id.desc_map:
                Intent intent = new Intent(EventsDescription.this, EventsMap.class);
                intent.putExtra("events_latitude", eventItem.getGeoLatitude());
                intent.putExtra("events_longitude", eventItem.getGeoLongitude());
                intent.putExtra("events_title", eventItem.getTitle());
                intent.putExtra("events_place_name", eventItem.getPlaceName());
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
