package com.underdusken.kulturekalendar.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.data.EventsItem;
import com.underdusken.kulturekalendar.utils.ServiceLoadImage;
import com.underdusken.kulturekalendar.utils.SimpleTimeFormat;

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
    private LayoutInflater vi = null;
    // Image Loader handler
    private ServiceLoadImage serviceLoadImage = null;

    public AdapterEventsItem(Context context, int textViewResourceId,
                              List<EventsItem> objects) {
        super(context, textViewResourceId, objects);
        this.items = objects;
        this.context = context;
        this.vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setServiceLoadImage(ServiceLoadImage serviceLoadImage){
        this.serviceLoadImage = serviceLoadImage;
    }


    private static class ViewHolder{
        RelativeLayout headerLayout;
        TextView tvName;
        TextView tvDate;
        TextView tvPrice;
        TextView tvPlace;
        ImageView ivPicture;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder viewHolder = null;

        if (v == null) {
            v = vi.inflate(R.layout.events_item, null);
            viewHolder = new ViewHolder();
            viewHolder.headerLayout = (RelativeLayout)v.findViewById(R.id.events_header);
            viewHolder.tvName = (TextView) v.findViewById(R.id.events_title);
            viewHolder.tvDate = (TextView) v.findViewById(R.id.events_date);
            viewHolder.tvPrice = (TextView)v.findViewById(R.id.events_price);
            viewHolder.tvPlace = (TextView)v.findViewById(R.id.events_place);
            viewHolder.ivPicture = (ImageView)v.findViewById(R.id.events_image);
            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }

        if (position >= 0 && position < items.size()) {

            final EventsItem eventsItem = items.get(items.size() - position - 1);

            String datePrevHeader = "";
            SimpleTimeFormat dateNow = null;
            String dateNowHeader = "";

            // Set group header (Date)
            boolean _header;
            dateNow = new SimpleTimeFormat(eventsItem.getDateStart());
            dateNowHeader = dateNow.getUserHeaderDate();
            if(position == 0){
                _header = true;
            }else{
                EventsItem eventsItemPrev = items.get(items.size() - position);
                datePrevHeader = new SimpleTimeFormat(eventsItemPrev.getDateStart()).getUserHeaderDate();
                _header = !datePrevHeader.equalsIgnoreCase(dateNowHeader);
            }

            if(_header){
                viewHolder.headerLayout.setVisibility(View.VISIBLE);
                viewHolder.tvDate.setText(dateNowHeader);
            }else{
                viewHolder.headerLayout.setVisibility(View.GONE);
            }


            viewHolder.tvName.setText(eventsItem.getId() + eventsItem.getName());

            // Set price for event
            if(eventsItem.getPrice()==0){
                viewHolder.tvPrice.setText("Free");
            }else{
                viewHolder.tvPrice.setText((int)(eventsItem.getPrice()) + " Kr");
            }

            // Set events place
            viewHolder.tvPlace.setText(dateNow.getUserTimeDate()+ "  " + eventsItem.getAddress());


            // Set Image
            if(serviceLoadImage!=null){
                serviceLoadImage.loadImage(eventsItem.getSmallPicture(), viewHolder.ivPicture, R.drawable.ic_article);
            }

            /*// Check adding to favorites
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
            });*/

        }

        return v;
    }
}