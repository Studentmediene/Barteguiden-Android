package com.underdusken.kulturekalendar.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.data.EventsItem;
import com.underdusken.kulturekalendar.data.db.ManageDataBase;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: pavelarteev
 * Date: 10/24/12
 * Time: 7:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventsDescription extends Activity{

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

        TextView tvName = (TextView)findViewById(R.id.event_name);
        TextView tvDateStart = (TextView)findViewById(R.id.date_start);
        TextView tvDateEnd = (TextView)findViewById(R.id.date_end);
        TextView tvAddress = (TextView)findViewById(R.id.address);
        TextView tvDescription = (TextView)findViewById(R.id.description);
        TextView tvRecommendation = (TextView)findViewById(R.id.recommendation);


        tvName.setText(eventsItem.getName());
        tvDateStart.setText(eventsItem.getDateStart());
        tvDateEnd.setText(eventsItem.getDateEnd());
        tvAddress.setText(eventsItem.getAddress());
        tvDescription.setText(eventsItem.getDescriptionEnglish());
        tvRecommendation.setText(eventsItem.getWeekendRecommendationEnglish());
    }
}
