package com.underdusken.kulturekalendar.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: pavelarteev
 * Date: 2/12/13
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleTimeFormat {

    // 2012-10-09T19:00:00+0200
    public static String serverDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    // Tuesday, 23 may 2013, 13:45
    public static String userDateFullFormat = "E, d MMMMM yyyy, HH:mm";
    // 24 may 2013
    public static String userDateHeadFormat = "d MMMMM yyyy";
    // 23:14
    public static String userDateTimeFormat = "HH:mm";

    private Date date = null;

    public SimpleTimeFormat(String serverDate) {
        String parseTime = serverDate;
        if (serverDate.contains("Z")) parseTime = serverDate.substring(0, 23);
        parseTime += "+0000";
        SimpleDateFormat format = new SimpleDateFormat(serverDateFormat);
        try {
            date = format.parse(parseTime);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public long getMs() {
        if (date != null) {
            return date.getTime();
        } else {
            return 0;
        }
    }

    public String getUserFullDate() {
        if (date != null) {
            SimpleDateFormat format = new SimpleDateFormat(userDateFullFormat);
            return format.format(date);
        } else {
            return null;
        }
    }

    public String getUserHeaderDate() {
        if (date != null) {
            SimpleDateFormat format = new SimpleDateFormat(userDateHeadFormat);
            return format.format(date);
        } else {
            return null;
        }
    }

    public String getUserTimeDate() {
        if (date != null) {
            SimpleDateFormat format = new SimpleDateFormat(userDateTimeFormat);
            return format.format(date);
        } else {
            return null;
        }
    }
}
