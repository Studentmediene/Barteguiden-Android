package com.underdusken.kulturekalendar.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.underdusken.kulturekalendar.data.EventsItem;

import java.sql.SQLException;
import java.util.ArrayList;
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

    private String[] allColumnsEvents = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_EVENTS_ID,
        MySQLiteHelper.COLUMN_EVENTS_NAME,
        MySQLiteHelper.COLUMN_EVENTS_TYPE,
        MySQLiteHelper.COLUMN_EVENTS_ADDRESS,
        MySQLiteHelper.COLUMN_EVENTS_GEO_LAT,
        MySQLiteHelper.COLUMN_EVENTS_GEO_LON,
        MySQLiteHelper.COLUMN_EVENTS_DATE_START,
        MySQLiteHelper.COLUMN_EVENTS_DATE_END,
        MySQLiteHelper.COLUMN_EVENTS_PRICE,
        MySQLiteHelper.COLUMN_EVENTS_AGE_LIMITS,
        MySQLiteHelper.COLUMN_EVENTS_FAVORITE,
        MySQLiteHelper.COLUMN_EVENTS_BEER_PRICE,
        MySQLiteHelper.COLUMN_EVENTS_DESCRIPTION_ENG,
        MySQLiteHelper.COLUMN_EVENTS_DESCRIPTION_NO,
        MySQLiteHelper.COLUMN_EVENTS_PICTURE,
        MySQLiteHelper.COLUMN_EVENTS_SMALL_PICTURE,
        MySQLiteHelper.COLUMN_EVENTS_WEEKEND_RECOMMENDATION_ENGLISH,
        MySQLiteHelper.COLUMN_EVENTS_WEEKEND_RECOMMENDATION_NORWEGIAN,
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
        eventsItem.setName(cursor.getString(2));
        eventsItem.setType(cursor.getString(3));
        eventsItem.setAddress(cursor.getString(4));
        eventsItem.setGeoLatitude(cursor.getFloat(5));
        eventsItem.setGeoLongitude(cursor.getFloat(6));
        eventsItem.setDateStart(cursor.getString(7));
        eventsItem.setDateEnd(cursor.getString(8));
        eventsItem.setPrice(cursor.getInt(9));
        eventsItem.setAgeLimit(cursor.getInt(10));
        eventsItem.setFavorite(cursor.getInt(11)==0?false:true);
        eventsItem.setBeerPrice(cursor.getInt(12));
        eventsItem.setDescriptionEnglish(cursor.getString(13));
        eventsItem.setDescriptionNorwegian(cursor.getString(14));
        eventsItem.setPicture(cursor.getString(15));
        eventsItem.setSmallPicture(cursor.getString(16));
        eventsItem.setWeekendRecommendationEnglish(cursor.getString(17));
        eventsItem.setWeekendRecommendationNorwegian(cursor.getString(18));
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

        return eventsItemList;
    }

    //Get All Events from Data Base from Id
    public List<EventsItem> getAllEventsByName(long id, String name){
        List<EventsItem> eventsItemList = new ArrayList<EventsItem>();

        name = name.toLowerCase();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENTS,
                allColumnsEvents, "lower("+MySQLiteHelper.COLUMN_EVENTS_NAME + ") LIKE '%" +name +"%' AND " + MySQLiteHelper.COLUMN_ID + ">" + id , null, null, null, null);

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

    public EventsItem addEventItem(EventsItem eventsItem){

        //TODO need to add verification that we have no this event in our DB

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_EVENTS_ID, eventsItem.getEventsId());
        values.put(MySQLiteHelper.COLUMN_EVENTS_NAME, eventsItem.getName());
        values.put(MySQLiteHelper.COLUMN_EVENTS_TYPE, eventsItem.getType());
        values.put(MySQLiteHelper.COLUMN_EVENTS_ADDRESS, eventsItem.getAddress());
        values.put(MySQLiteHelper.COLUMN_EVENTS_GEO_LAT, eventsItem.getGeoLatitude());
        values.put(MySQLiteHelper.COLUMN_EVENTS_GEO_LON, eventsItem.getGeoLongitude());
        values.put(MySQLiteHelper.COLUMN_EVENTS_DATE_START, eventsItem.getDateStart());
        values.put(MySQLiteHelper.COLUMN_EVENTS_DATE_END, eventsItem.getDateEnd());
        values.put(MySQLiteHelper.COLUMN_EVENTS_PRICE, eventsItem.getPrice());
        values.put(MySQLiteHelper.COLUMN_EVENTS_AGE_LIMITS, eventsItem.getAgeLimit());
        values.put(MySQLiteHelper.COLUMN_EVENTS_FAVORITE, eventsItem.getFavorite());
        values.put(MySQLiteHelper.COLUMN_EVENTS_BEER_PRICE, eventsItem.getBeerPrice());
        values.put(MySQLiteHelper.COLUMN_EVENTS_DESCRIPTION_ENG, eventsItem.getDescriptionEnglish());
        values.put(MySQLiteHelper.COLUMN_EVENTS_DESCRIPTION_NO, eventsItem.getDescriptionNorwegian());
        values.put(MySQLiteHelper.COLUMN_EVENTS_PICTURE, eventsItem.getPicture());
        values.put(MySQLiteHelper.COLUMN_EVENTS_SMALL_PICTURE, eventsItem.getSmallPicture());
        values.put(MySQLiteHelper.COLUMN_EVENTS_WEEKEND_RECOMMENDATION_ENGLISH, eventsItem.getWeekendRecommendationEnglish());
        values.put(MySQLiteHelper.COLUMN_EVENTS_WEEKEND_RECOMMENDATION_NORWEGIAN, eventsItem.getWeekendRecommendationNorwegian());
        values.put(MySQLiteHelper.COLUMN_EVENTS_NOTIFICATION_ID, eventsItem.getNotificationId());

        long insertId = database.insert(MySQLiteHelper.TABLE_EVENTS, null, values);

        // Check that we add information to DB
        Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENTS,
                allColumnsEvents, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        EventsItem checkEventsItem = cursorToEventsItem(cursor);

        cursor.close();

        return checkEventsItem;
    }


    public void updateEventsItem(long id, EventsItem eventsItem){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_EVENTS_ID, eventsItem.getEventsId());
        values.put(MySQLiteHelper.COLUMN_EVENTS_NAME, eventsItem.getName());
        values.put(MySQLiteHelper.COLUMN_EVENTS_TYPE, eventsItem.getType());
        values.put(MySQLiteHelper.COLUMN_EVENTS_ADDRESS, eventsItem.getAddress());
        values.put(MySQLiteHelper.COLUMN_EVENTS_GEO_LAT, eventsItem.getGeoLatitude());
        values.put(MySQLiteHelper.COLUMN_EVENTS_GEO_LON, eventsItem.getGeoLongitude());
        values.put(MySQLiteHelper.COLUMN_EVENTS_DATE_START, eventsItem.getDateStart());
        values.put(MySQLiteHelper.COLUMN_EVENTS_DATE_END, eventsItem.getDateEnd());
        values.put(MySQLiteHelper.COLUMN_EVENTS_PRICE, eventsItem.getPrice());
        values.put(MySQLiteHelper.COLUMN_EVENTS_AGE_LIMITS, eventsItem.getAgeLimit());
        values.put(MySQLiteHelper.COLUMN_EVENTS_FAVORITE, eventsItem.getFavorite());
        values.put(MySQLiteHelper.COLUMN_EVENTS_BEER_PRICE, eventsItem.getBeerPrice());
        values.put(MySQLiteHelper.COLUMN_EVENTS_DESCRIPTION_ENG, eventsItem.getDescriptionEnglish());
        values.put(MySQLiteHelper.COLUMN_EVENTS_DESCRIPTION_NO, eventsItem.getDescriptionNorwegian());
        values.put(MySQLiteHelper.COLUMN_EVENTS_PICTURE, eventsItem.getPicture());
        values.put(MySQLiteHelper.COLUMN_EVENTS_SMALL_PICTURE, eventsItem.getSmallPicture());
        values.put(MySQLiteHelper.COLUMN_EVENTS_WEEKEND_RECOMMENDATION_ENGLISH, eventsItem.getWeekendRecommendationEnglish());
        values.put(MySQLiteHelper.COLUMN_EVENTS_WEEKEND_RECOMMENDATION_NORWEGIAN, eventsItem.getWeekendRecommendationNorwegian());
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

    /*

    public EventsItem addDashboardItem(String type, String label, String icon, int page, int index, String json) {

        if (testEventsItem(json, type))
            return null;

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DASHBOARD_TYPE, type);
        values.put(MySQLiteHelper.COLUMN_DASHBOARD_LABEL, label);
        values.put(MySQLiteHelper.COLUMN_DASHBOARD_ICON, icon);
        values.put(MySQLiteHelper.COLUMN_DASHBOARD_PAGE, page);
        values.put(MySQLiteHelper.COLUMN_DASHBOARD_INDEX, index);
        values.put(MySQLiteHelper.COLUMN_DASHBOARD_JSON_DATA, json);

        long insertId = database.insert(MySQLiteHelper.TABLE_DASHBOARD, null,
                values);

        Cursor cursor = database.query(MySQLiteHelper.TABLE_DASHBOARD,
                allColumnsDashboard, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        TableDashboardItem tableDashboardItem = cursorToTableDashboardItem(cursor);
        cursor.close();

        return tableDashboardItem;
    }




    public void updateDashboardJson(long id, String jsonData){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DASHBOARD_JSON_DATA, jsonData);

        database.update(MySQLiteHelper.TABLE_DASHBOARD, values, MySQLiteHelper.COLUMN_ID + " = '" + id + "'", null);
    }

    public void updateDashboardIconPosition(long id, int page, int index) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DASHBOARD_PAGE, page);
        values.put(MySQLiteHelper.COLUMN_DASHBOARD_INDEX, index);

        database.update(MySQLiteHelper.TABLE_DASHBOARD, values, MySQLiteHelper.COLUMN_ID + " = '" + id + "'", null);

    }

    public void deleteDashboardItem(TableDashboardItem dashboardItem) {
        long id = dashboardItem.getId();
        System.out.println("Dashboard item deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_DASHBOARD, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteDashboardItem(long id) {
        System.out.println("Dashboard item deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_DASHBOARD, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }




    // PAYMENT
    private String[] allColumnsPayment = {MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_PAYMENT_TYPE, MySQLiteHelper.COLUMN_PAYMENT_TO, MySQLiteHelper.COLUMN_PAYMENT_FROM,
            MySQLiteHelper.COLUMN_PAYMENT_STATUS, MySQLiteHelper.COLUMN_PAYMENT_DATE, MySQLiteHelper.COLUMN_PAYMENT_AMOUNT,
            MySQLiteHelper.COLUMN_PAYMENT_REAL_ID, MySQLiteHelper.COLUMN_PAYMENT_JSON, MySQLiteHelper.COLUMN_PAYMENT_CONTRACT};


    public TablePaymentItem addPayment(String type, String to, String from, String status, String date, float amount,
                                       String realPaymentId, String json, String contract) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PAYMENT_TYPE, type);
        values.put(MySQLiteHelper.COLUMN_PAYMENT_TO, to);
        values.put(MySQLiteHelper.COLUMN_PAYMENT_FROM, from);
        values.put(MySQLiteHelper.COLUMN_PAYMENT_STATUS, status);
        values.put(MySQLiteHelper.COLUMN_PAYMENT_DATE, date);
        values.put(MySQLiteHelper.COLUMN_PAYMENT_AMOUNT, amount);
        values.put(MySQLiteHelper.COLUMN_PAYMENT_REAL_ID, realPaymentId);
        values.put(MySQLiteHelper.COLUMN_PAYMENT_JSON, json);
        values.put(MySQLiteHelper.COLUMN_PAYMENT_CONTRACT, contract);

        long insertId = database.insert(MySQLiteHelper.TABLE_PAYMENT, null,
                values);


        Cursor cursor = database.query(MySQLiteHelper.TABLE_PAYMENT,
                allColumnsPayment, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        TablePaymentItem tablePaymentItem = cursorToTablePaymentItem(cursor);
        cursor.close();

        return tablePaymentItem;
    }

    public void updatePayment(String id, String status, String resource) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PAYMENT_STATUS, status);
        if (resource != null)
            values.put(MySQLiteHelper.COLUMN_PAYMENT_CONTRACT, resource);

        database.update(MySQLiteHelper.TABLE_PAYMENT, values, MySQLiteHelper.COLUMN_PAYMENT_REAL_ID + " = '" + id + "'", null);

    }

    public void updatePayment(long id, String status) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PAYMENT_STATUS, status);
        database.update(MySQLiteHelper.TABLE_PAYMENT, values, MySQLiteHelper.COLUMN_ID + " = " + id, null);

    }

    private TablePaymentItem cursorToTablePaymentItem(Cursor cursor) {
        TablePaymentItem tablePaymentItem = new TablePaymentItem();

        tablePaymentItem.setId(cursor.getLong(0));
        tablePaymentItem.setType(cursor.getString(1));
        tablePaymentItem.setTo(cursor.getString(2));
        tablePaymentItem.setFrom(cursor.getString(3));
        tablePaymentItem.setStatus(cursor.getString(4));
        tablePaymentItem.setDate(cursor.getString(5));
        tablePaymentItem.setAmount(cursor.getFloat(6));
        tablePaymentItem.setRealId(cursor.getString(7));
        tablePaymentItem.setReserved(cursor.getString(8));
        tablePaymentItem.setContract(cursor.getString(9));


        return tablePaymentItem;
    }

    public void deletePaymentItemWhereStatus(String status) {
        database.delete(MySQLiteHelper.TABLE_PAYMENT, MySQLiteHelper.COLUMN_PAYMENT_STATUS
                + "=" + status, null);
    }

    public void deletePaymentItem(String id) {
        database.delete(MySQLiteHelper.TABLE_PAYMENT, MySQLiteHelper.COLUMN_PAYMENT_REAL_ID
                + "= " + id, null);
    }

    public void deletePaymentItem(long id) {
        database.delete(MySQLiteHelper.TABLE_PAYMENT, MySQLiteHelper.COLUMN_ID
                + "= " + id, null);
    }

    public void deletePaymentItem(TablePaymentItem paymentItem) {
        long id = paymentItem.getId();
        System.out.println("Dashboard item deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_PAYMENT, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<TablePaymentItem> getAllPaymentItem() {
        List<TablePaymentItem> paymentItems = new ArrayList<TablePaymentItem>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_PAYMENT,
                allColumnsPayment, null, null, null, null, MySQLiteHelper.COLUMN_ID + " DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TablePaymentItem paymentItem = cursorToTablePaymentItem(cursor);
            paymentItems.add(paymentItem);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();

        return paymentItems;
    }

    public List<TablePaymentItem> getAllPaymentItemConfirmed() {
        List<TablePaymentItem> paymentItems = new ArrayList<TablePaymentItem>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_PAYMENT,
                allColumnsPayment, MySQLiteHelper.COLUMN_PAYMENT_STATUS + " = 'confirmed'", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TablePaymentItem paymentItem = cursorToTablePaymentItem(cursor);
            paymentItems.add(paymentItem);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();

        cursor = database.query(MySQLiteHelper.TABLE_PAYMENT,
                allColumnsPayment, MySQLiteHelper.COLUMN_PAYMENT_STATUS + " = 'inprogress'", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TablePaymentItem paymentItem = cursorToTablePaymentItem(cursor);
            paymentItems.add(paymentItem);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();

        return paymentItems;
    }*/

}
