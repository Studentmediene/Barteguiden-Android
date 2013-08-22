package com.underdusken.kulturekalendar.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.underdusken.kulturekalendar.data.EventsItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pavelarteev
 * Date: 9/30/12
 * Time: 7:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ManageDataBase {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    private String[] allColumnsEvents = {MySQLiteHelper.COLUMN_ID,
        MySQLiteHelper.COLUMN_EVENTS_ID,
        MySQLiteHelper.COLUMN_EVENTS_TITLE,
        MySQLiteHelper.COLUMN_EVENTS_CATEGORY_ID,
        MySQLiteHelper.COLUMN_EVENTS_ADDRESS,
        MySQLiteHelper.COLUMN_EVENTS_GEO_LAT,
        MySQLiteHelper.COLUMN_EVENTS_GEO_LON,
        MySQLiteHelper.COLUMN_EVENTS_DATE_START,
        MySQLiteHelper.COLUMN_EVENTS_PRICE,
        MySQLiteHelper.COLUMN_EVENTS_AGE_LIMIT,
        MySQLiteHelper.COLUMN_EVENTS_PLACE_NAME,
        MySQLiteHelper.COLUMN_EVENTS_SHOW_DATE,
        MySQLiteHelper.COLUMN_EVENTS_FAVORITE,
        MySQLiteHelper.COLUMN_EVENTS_BEER_PRICE,
        MySQLiteHelper.COLUMN_EVENTS_DESCRIPTION_ENG,
        MySQLiteHelper.COLUMN_EVENTS_DESCRIPTION_NO,
        MySQLiteHelper.COLUMN_EVENTS_IMAGE_URL,
        MySQLiteHelper.COLUMN_EVENTS_EVENTS_URL,
        MySQLiteHelper.COLUMN_EVENTS_IS_RECOMMENDED,
        MySQLiteHelper.COLUMN_EVENTS_NOTIFICATION_ID};

    public ManageDataBase(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }


    public void close() {
        dbHelper.close();
    }

    public boolean isLockedByAnotherThread() {
        return database.isDbLockedByOtherThreads();
    }

    // Convert from Cursor to EventsItem
    private EventsItem cursorToEventsItem(Cursor cursor) {
        EventsItem eventsItem = new EventsItem();

        eventsItem.setId(cursor.getLong(0));
        eventsItem.setEventsId(cursor.getString(1));
        eventsItem.setTitle(cursor.getString(2));
        eventsItem.setCategoryID(cursor.getString(3));
        eventsItem.setAddress(cursor.getString(4));
        eventsItem.setGeoLatitude(cursor.getFloat(5));
        eventsItem.setGeoLongitude(cursor.getFloat(6));
        eventsItem.setDateStart(cursor.getString(7));
        eventsItem.setPrice(cursor.getInt(8));
        eventsItem.setAgeLimit(cursor.getInt(9));
        eventsItem.setPlaceName(cursor.getString(10));
        eventsItem.setShowDate(cursor.getString(11));
        eventsItem.setFavorite(cursor.getInt(12)==0?false:true);
        eventsItem.setBeerPrice(cursor.getInt(13));
        eventsItem.setDescriptionEnglish(cursor.getString(14));
        eventsItem.setDescriptionNorwegian(cursor.getString(15));
        eventsItem.setImageURL(cursor.getString(16));
        eventsItem.setEventURL(cursor.getString(17));
        eventsItem.setisRecomended(cursor.getInt(18)==0?false:true);
        eventsItem.setNotificationId(cursor.getInt(19));

        return eventsItem;
    }

    //Get All Events from Data Base
    public List<EventsItem> getAllEventsItem() {
        List<EventsItem> eventsItemList = new ArrayList<EventsItem>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENTS,
                allColumnsEvents, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            EventsItem eventsItem = cursorToEventsItem(cursor);
            eventsItemList.add(eventsItem);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();

        return eventsItemList;
    }

    //Get All Events from Data Base from Id
    public List<EventsItem> getAllEventsFavorites() {
        List<EventsItem> eventsItemList = new ArrayList<EventsItem>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENTS,
                allColumnsEvents, MySQLiteHelper.COLUMN_EVENTS_FAVORITE + "='1'" , null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            EventsItem eventsItem = cursorToEventsItem(cursor);
            eventsItemList.add(eventsItem);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();

        return sortEventsByDate(eventsItemList);
    }

    //Get All Events from Data Base from Id
    public List<EventsItem> getAllEventsByName(long id, String name){
        List<EventsItem> eventsItemList = new ArrayList<EventsItem>();

        name = name.toLowerCase();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENTS,
                allColumnsEvents, "lower("+MySQLiteHelper.COLUMN_EVENTS_TITLE + ") LIKE '%" +name +"%' AND " + MySQLiteHelper.COLUMN_ID + ">" + id , null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            EventsItem eventsItem = cursorToEventsItem(cursor);
            eventsItemList.add(eventsItem);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();

        return sortEventsByDate(eventsItemList);
    }

    //Get All Events from Data Base from Id
    public List<EventsItem> getAllEventsItemFromId(long id) {
        List<EventsItem> eventsItemList = new ArrayList<EventsItem>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENTS,
                    allColumnsEvents, MySQLiteHelper.COLUMN_ID + ">" + id, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            EventsItem eventsItem = cursorToEventsItem(cursor);
            eventsItemList.add(eventsItem);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();

        return eventsItemList;
    }

    public static List<EventsItem> sortEventsByDate(List<EventsItem> eventsItemList){
        List<EventsItem> sortedList = new ArrayList<EventsItem>();
        long curTime = new Date().getTime();
        for(EventsItem eventsItem:eventsItemList){
            int i=0;
            for(i=0; i<sortedList.size(); i++){
                if(eventsItem.getDateStartMS()>sortedList.get(i).getDateStartMS())
                    break;
            }
            if(eventsItem.getDateStartMS() >= curTime)
                sortedList.add(i, eventsItem);
        }
        return sortedList;
    }

    public List<EventsItem> getAllEventsFavoritesFromId(long id){
        List<EventsItem> eventsItemList = new ArrayList<EventsItem>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENTS,
                allColumnsEvents, MySQLiteHelper.COLUMN_EVENTS_FAVORITE + "='1' AND " + MySQLiteHelper.COLUMN_ID + ">" + id , null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            EventsItem eventsItem = cursorToEventsItem(cursor);
            eventsItemList.add(eventsItem);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();

        return eventsItemList;

    }

    public EventsItem getEventsItemById(long id){
        EventsItem eventsItem = null;
        Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENTS,
                allColumnsEvents, MySQLiteHelper.COLUMN_ID + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();

        eventsItem = cursorToEventsItem(cursor);

        cursor.close();
        return eventsItem;
    }

    public void addEventItem(EventsItem eventsItem){
        //TODO need to add verification that we have no this event in our DB
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_EVENTS_ID, eventsItem.getEventsId());
        values.put(MySQLiteHelper.COLUMN_EVENTS_TITLE, eventsItem.getTitle());
        values.put(MySQLiteHelper.COLUMN_EVENTS_CATEGORY_ID, eventsItem.getCategoryID());
        values.put(MySQLiteHelper.COLUMN_EVENTS_ADDRESS, eventsItem.getAddress());
        values.put(MySQLiteHelper.COLUMN_EVENTS_GEO_LAT, eventsItem.getGeoLatitude());
        values.put(MySQLiteHelper.COLUMN_EVENTS_GEO_LON, eventsItem.getGeoLongitude());
        values.put(MySQLiteHelper.COLUMN_EVENTS_DATE_START, eventsItem.getDateStart());
        values.put(MySQLiteHelper.COLUMN_EVENTS_PRICE, eventsItem.getPrice());
        values.put(MySQLiteHelper.COLUMN_EVENTS_AGE_LIMIT, eventsItem.getAgeLimit());
        values.put(MySQLiteHelper.COLUMN_EVENTS_PLACE_NAME, eventsItem.getPlaceName());
        values.put(MySQLiteHelper.COLUMN_EVENTS_SHOW_DATE, eventsItem.getShowDate());
        values.put(MySQLiteHelper.COLUMN_EVENTS_FAVORITE, eventsItem.getFavorite());
        values.put(MySQLiteHelper.COLUMN_EVENTS_BEER_PRICE, eventsItem.getBeerPrice());
        values.put(MySQLiteHelper.COLUMN_EVENTS_DESCRIPTION_ENG, eventsItem.getDescriptionEnglish());
        values.put(MySQLiteHelper.COLUMN_EVENTS_DESCRIPTION_NO, eventsItem.getDescriptionNorwegian());
        values.put(MySQLiteHelper.COLUMN_EVENTS_IMAGE_URL, eventsItem.getImageURL());
        values.put(MySQLiteHelper.COLUMN_EVENTS_EVENTS_URL, eventsItem.getEventURL());
        values.put(MySQLiteHelper.COLUMN_EVENTS_IS_RECOMMENDED, eventsItem.getIsRecommended());
        values.put(MySQLiteHelper.COLUMN_EVENTS_NOTIFICATION_ID, eventsItem.getNotificationId());

        Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENTS,
                allColumnsEvents, MySQLiteHelper.COLUMN_EVENTS_ID + " = '" + eventsItem.getEventsId() + "'", null,
                null, null, null);

        if(cursor!=null){
            if ( cursor.moveToFirst() ) {
                updateEventsItemByEventId(eventsItem);
            } else {
                database.insert(MySQLiteHelper.TABLE_EVENTS, null, values);
            }
            cursor.close();
        }else{
            database.insert(MySQLiteHelper.TABLE_EVENTS, null, values);
        }
    }

    public void updateEventsItemByEventId(EventsItem eventsItem){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_EVENTS_ID, eventsItem.getEventsId());
        values.put(MySQLiteHelper.COLUMN_EVENTS_TITLE, eventsItem.getTitle());
        values.put(MySQLiteHelper.COLUMN_EVENTS_CATEGORY_ID, eventsItem.getCategoryID());
        values.put(MySQLiteHelper.COLUMN_EVENTS_ADDRESS, eventsItem.getAddress());
        values.put(MySQLiteHelper.COLUMN_EVENTS_GEO_LAT, eventsItem.getGeoLatitude());
        values.put(MySQLiteHelper.COLUMN_EVENTS_GEO_LON, eventsItem.getGeoLongitude());
        values.put(MySQLiteHelper.COLUMN_EVENTS_DATE_START, eventsItem.getDateStart());
        values.put(MySQLiteHelper.COLUMN_EVENTS_PRICE, eventsItem.getPrice());
        values.put(MySQLiteHelper.COLUMN_EVENTS_AGE_LIMIT, eventsItem.getAgeLimit());
        values.put(MySQLiteHelper.COLUMN_EVENTS_PLACE_NAME, eventsItem.getPlaceName());
        values.put(MySQLiteHelper.COLUMN_EVENTS_SHOW_DATE, eventsItem.getShowDate());
        values.put(MySQLiteHelper.COLUMN_EVENTS_FAVORITE, eventsItem.getFavorite());
        values.put(MySQLiteHelper.COLUMN_EVENTS_BEER_PRICE, eventsItem.getBeerPrice());
        values.put(MySQLiteHelper.COLUMN_EVENTS_DESCRIPTION_ENG, eventsItem.getDescriptionEnglish());
        values.put(MySQLiteHelper.COLUMN_EVENTS_DESCRIPTION_NO, eventsItem.getDescriptionNorwegian());
        values.put(MySQLiteHelper.COLUMN_EVENTS_IMAGE_URL, eventsItem.getImageURL());
        values.put(MySQLiteHelper.COLUMN_EVENTS_EVENTS_URL, eventsItem.getEventURL());
        values.put(MySQLiteHelper.COLUMN_EVENTS_IS_RECOMMENDED, eventsItem.getIsRecommended());
        values.put(MySQLiteHelper.COLUMN_EVENTS_NOTIFICATION_ID, eventsItem.getNotificationId());
        database.update(MySQLiteHelper.TABLE_EVENTS, values, MySQLiteHelper.COLUMN_EVENTS_ID + " = '" + eventsItem.getEventsId() + "'", null);
    }

    public void updateEventsItem(long id, EventsItem eventsItem){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_EVENTS_ID, eventsItem.getEventsId());
        values.put(MySQLiteHelper.COLUMN_EVENTS_TITLE, eventsItem.getTitle());
        values.put(MySQLiteHelper.COLUMN_EVENTS_CATEGORY_ID, eventsItem.getCategoryID());
        values.put(MySQLiteHelper.COLUMN_EVENTS_ADDRESS, eventsItem.getAddress());
        values.put(MySQLiteHelper.COLUMN_EVENTS_GEO_LAT, eventsItem.getGeoLatitude());
        values.put(MySQLiteHelper.COLUMN_EVENTS_GEO_LON, eventsItem.getGeoLongitude());
        values.put(MySQLiteHelper.COLUMN_EVENTS_DATE_START, eventsItem.getDateStart());
        values.put(MySQLiteHelper.COLUMN_EVENTS_PRICE, eventsItem.getPrice());
        values.put(MySQLiteHelper.COLUMN_EVENTS_AGE_LIMIT, eventsItem.getAgeLimit());
        values.put(MySQLiteHelper.COLUMN_EVENTS_PLACE_NAME, eventsItem.getPlaceName());
        values.put(MySQLiteHelper.COLUMN_EVENTS_SHOW_DATE, eventsItem.getShowDate());
        values.put(MySQLiteHelper.COLUMN_EVENTS_FAVORITE, eventsItem.getFavorite());
        values.put(MySQLiteHelper.COLUMN_EVENTS_BEER_PRICE, eventsItem.getBeerPrice());
        values.put(MySQLiteHelper.COLUMN_EVENTS_DESCRIPTION_ENG, eventsItem.getDescriptionEnglish());
        values.put(MySQLiteHelper.COLUMN_EVENTS_DESCRIPTION_NO, eventsItem.getDescriptionNorwegian());
        values.put(MySQLiteHelper.COLUMN_EVENTS_IMAGE_URL, eventsItem.getImageURL());
        values.put(MySQLiteHelper.COLUMN_EVENTS_EVENTS_URL, eventsItem.getEventURL());
        values.put(MySQLiteHelper.COLUMN_EVENTS_IS_RECOMMENDED, eventsItem.getIsRecommended());
        values.put(MySQLiteHelper.COLUMN_EVENTS_NOTIFICATION_ID, eventsItem.getNotificationId());

        database.update(MySQLiteHelper.TABLE_EVENTS, values, MySQLiteHelper.COLUMN_ID + " = '" + id + "'", null);
    }

    public EventsItem updateEventsItemFavorites(long id, boolean state){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_EVENTS_FAVORITE, state);

        long insertId = database.update(MySQLiteHelper.TABLE_EVENTS, values, MySQLiteHelper.COLUMN_ID + " = " + id, null);

        // Check that we add information to DB
        Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENTS,
                allColumnsEvents, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        EventsItem checkEventsItem = cursorToEventsItem(cursor);

        cursor.close();

        return checkEventsItem;
    }

}
