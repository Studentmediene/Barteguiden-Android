package com.underdusken.kulturekalendar.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.data.EventsItem;
import com.underdusken.kulturekalendar.data.db.ManageDataBase;

import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pavelarteev
 * Date: 10/2/12
 * Time: 1:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdapterEventsItem extends ArrayAdapter<EventsItem> {

    private Context context = null;
    private List<EventsItem> items;

    public AdapterEventsItem(Context context, int textViewResourceId,
                              List<EventsItem> objects) {
        super(context, textViewResourceId, objects);
        this.items = objects;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.events_item, null);
        }

        if (position >= 0 && position < items.size()) {

            final EventsItem eventsItem = items.get(items.size() - position - 1);

            TextView tvName = (TextView) v.findViewById(R.id.events_text);
            TextView tvDate = (TextView) v.findViewById(R.id.events_date);

            tvName.setText(eventsItem.getId() + eventsItem.getName());
            tvDate.setText(eventsItem.getDateStart());

            // Check adding to favorites
            CheckBox chkFavorite = (CheckBox) v.findViewById(R.id.events_item_favorite_add);
            chkFavorite.setChecked(eventsItem.getFavorite());
            chkFavorite.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        ManageDataBase manageDataBase = new ManageDataBase(context);
                        eventsItem.setFavorite(true);
                        try {
                            manageDataBase.open();
                            EventsItem testEventsItem = manageDataBase.updateEventsItemFavorites(eventsItem.getId(), true);
                            manageDataBase.close();
                        } catch (SQLException e) {

                        }
                    }else{
                        ManageDataBase manageDataBase = new ManageDataBase(context);
                        eventsItem.setFavorite(false);
                        try {
                            manageDataBase.open();
                            manageDataBase.updateEventsItemFavorites(eventsItem.getId(), false);
                            manageDataBase.close();
                        } catch (SQLException e) {

                        }
                    }
                }
            });

        }

        return v;
    }
}