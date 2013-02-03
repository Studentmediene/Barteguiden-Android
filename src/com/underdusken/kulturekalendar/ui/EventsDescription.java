package com.underdusken.kulturekalendar.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        setData(eventsItem);

    }

    // initialization UI
    private void initUI(){
        Button btMap = (Button) findViewById(R.id.bt_show_on_map);
        btMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsDescription.this, EventsMap.class);
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
    private void setData(EventsItem eventsItem){
        tvName.setText(eventsItem.getName());
        tvDateStart.setText(eventsItem.getDateStart());
        tvDateEnd.setText(eventsItem.getDateEnd());
        tvAddress.setText(eventsItem.getAddress());
        tvDescription.setText(eventsItem.getDescriptionEnglish());
        tvRecommendation.setText(eventsItem.getWeekendRecommendationEnglish());
    }
}
