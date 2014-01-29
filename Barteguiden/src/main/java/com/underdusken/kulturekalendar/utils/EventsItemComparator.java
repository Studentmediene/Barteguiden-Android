package com.underdusken.kulturekalendar.utils;

import android.util.Log;

import com.underdusken.kulturekalendar.data.EventItem;

import java.util.Comparator;

public class EventsItemComparator implements Comparator<EventItem> {
    private static final String TAG = "EventsItemComparator";
    private final String type;

    public EventsItemComparator(String type) {
        this.type = type;
    }

    public int compare(EventItem e1, EventItem e2) {
        if (type.equals("getDateStartMS")) {
            return ((Long) e1.getDateStartMS()).compareTo(e2.getDateStartMS());
        }
        if (type.equals("getDateStartDay")) {
            return e1.getDateStartDay().compareTo(e2.getDateStartDay());
        }
        Log.w(TAG, "Field not found: " + type);
        return ((Long) e1.getId()).compareTo(e2.getId());
    }
}
