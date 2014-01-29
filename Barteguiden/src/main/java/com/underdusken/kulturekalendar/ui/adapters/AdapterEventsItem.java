package com.underdusken.kulturekalendar.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.data.EventItem;
import com.underdusken.kulturekalendar.utils.SimpleTimeFormat;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pavelarteev
 * Date: 10/2/12
 * Time: 1:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdapterEventsItem extends ArrayAdapter<EventItem> {

    private Context context = null;
    private List<EventItem> items;
    private LayoutInflater vi = null;

    public AdapterEventsItem(Context context, int textViewResourceId, List<EventItem> objects) {
        super(context, textViewResourceId, objects);
        this.items = objects;
        this.context = context;
        this.vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    private static class ViewHolder {
        FrameLayout headerLayout;
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
            viewHolder.headerLayout = (FrameLayout) v.findViewById(R.id.events_header);
            viewHolder.tvName = (TextView) v.findViewById(R.id.events_title);
            viewHolder.tvDate = (TextView) viewHolder.headerLayout.findViewById(R.id.header_text);
            viewHolder.tvPrice = (TextView) v.findViewById(R.id.events_price);
            viewHolder.tvPlace = (TextView) v.findViewById(R.id.events_place);
            viewHolder.ivPicture = (ImageView) v.findViewById(R.id.events_image);
            viewHolder.tvDate.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto-li.ttf"));
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        if (position >= 0 && position < items.size()) {

            final EventItem eventItem = items.get(items.size() - position - 1);

            String datePrevHeader = "";
            SimpleTimeFormat dateNow = null;
            String dateNowHeader = "";

            // Set group header (Date)
            boolean _header;
            dateNow = new SimpleTimeFormat(eventItem.getDateStart());
            dateNowHeader = dateNow.getUserHeaderDate();
            if (position == 0) {
                _header = true;
            } else {
                EventItem eventItemPrev = items.get(items.size() - position);
                datePrevHeader = new SimpleTimeFormat(eventItemPrev.getDateStart()).getUserHeaderDate();
                if (datePrevHeader != null)
                    _header = !datePrevHeader.equalsIgnoreCase(dateNowHeader);
                else _header = false;
            }

            if (_header) {
                viewHolder.headerLayout.setVisibility(View.VISIBLE);
                viewHolder.tvDate.setText(dateNowHeader);
            } else {
                viewHolder.headerLayout.setVisibility(View.GONE);
            }


            viewHolder.tvName.setText(eventItem.getTitle());

            // Set price for event
            if (eventItem.getPrice() == 0) {
                viewHolder.tvPrice.setText("Free");
            } else {
                viewHolder.tvPrice.setText((int) (eventItem.getPrice()) + " Kr");
            }

            // Set events place
            viewHolder.tvPlace.setText(dateNow.getUserTimeDate() + "  " + eventItem.getPlaceName());

            if (eventItem.getCategoryID().equals("SPORT"))
                viewHolder.ivPicture.setImageResource(R.drawable.category_sport);
            else if (eventItem.getCategoryID().equals("PERFORMANCES"))
                viewHolder.ivPicture.setImageResource(R.drawable.category_performances);
            else if (eventItem.getCategoryID().equals("MUSIC"))
                viewHolder.ivPicture.setImageResource(R.drawable.category_music);
            else if (eventItem.getCategoryID().equals("EXHIBITIONS"))
                viewHolder.ivPicture.setImageResource(R.drawable.category_exhibitions);
            else if (eventItem.getCategoryID().equals("NIGHTLIFE"))
                viewHolder.ivPicture.setImageResource(R.drawable.category_nightlife);
            else if (eventItem.getCategoryID().equals("PRESENTATIONS"))
                viewHolder.ivPicture.setImageResource(R.drawable.category_presentations);
            else if (eventItem.getCategoryID().equals("DEBATE"))
                viewHolder.ivPicture.setImageResource(R.drawable.category_debate);
            else if (eventItem.getCategoryID().equals("OTHER"))
                viewHolder.ivPicture.setImageResource(R.drawable.category_other);
        }

        return v;
    }
}