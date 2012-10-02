package com.underdusken.kulturekalendar.data.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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

    private String[] allColumnsEvents = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_EVENTS_NAME, MySQLiteHelper.COLUMN_EVENTS_TYPE, MySQLiteHelper.COLUMN_EVENTS_ADDRESS,
    MySQLiteHelper.COLUMN_EVENTS_GEO_LAT, MySQLiteHelper.COLUMN_EVENTS_GEO_LON, MySQLiteHelper.COLUMN_EVENTS_DATE, MySQLiteHelper.COLUMN_EVENTS_PRICE,
    MySQLiteHelper.COLUMN_EVENTS_AGE_LIMITS, MySQLiteHelper.COLUMN_EVENTS_FAVORITE, MySQLiteHelper.COLUMN_EVENTS_BEER_PRICE, MySQLiteHelper.COLUMN_EVENTS_DESCRIPTION_ENG,
    MySQLiteHelper.COLUMN_EVENTS_DESCRIPTION_NO, MySQLiteHelper.COLUMN_EVENTS_PICTURE, MySQLiteHelper.COLUMN_EVENTS_SMALL_PICTURE,
    MySQLiteHelper.COLUMN_EVENTS_WEEKEND_RECOMMENDATION, MySQLiteHelper.COLUMN_EVENTS_NOTIFICATION_ID};


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
/*
    private boolean testDashboardItem(String json, String type) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_DASHBOARD,
                allColumnsDashboard, null, null, null, null, null);


        cursor.moveToFirst();
        if (type.equals("phone")) {
            ArticleItem testArticle = new JsonParseCatalog().parseArticle(json);
            String testPhone = TextFormats.simplePhone(testArticle.getPhoneNumber());
            while (!cursor.isAfterLast()) {
                String jsonArticle = cursor.getString(6);
                ArticleItem articleItem = new JsonParseCatalog().parseArticle(jsonArticle);
                if(articleItem!=null)
                    if(testPhone.equals(TextFormats.simplePhone(articleItem.getPhoneNumber())))
                        return true;
                cursor.moveToNext();
            }
        } else if (type.equals("service")){
            ArticleItem testArticle = new JsonParseCatalog().parseArticle(json);
            while (!cursor.isAfterLast()) {
                String jsonArticle = cursor.getString(6);
                ArticleItem articleItem = new JsonParseCatalog().parseArticle(jsonArticle);
                if(articleItem!=null)
                    if(testArticle.getTitle().equals(articleItem.getTitle()))
                        return true;
                cursor.moveToNext();
            }
        }
        // Make sure to close the cursor
        cursor.close();

        return false;
    }*//*

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


    private TableDashboardItem cursorToTableDashboardItem(Cursor cursor) {
        TableDashboardItem tableDashboardItem = new TableDashboardItem();

        tableDashboardItem.setId(cursor.getLong(0));
        tableDashboardItem.setType(cursor.getString(1));
        tableDashboardItem.setLabel(cursor.getString(2));
        tableDashboardItem.setIcon(cursor.getString(3));
        tableDashboardItem.setPage(cursor.getInt(4));
        tableDashboardItem.setIndex(cursor.getInt(5));
        tableDashboardItem.setJson(cursor.getString(6));

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

    public List<TableDashboardItem> getAllDashboardItem() {
        List<TableDashboardItem> dashboardItems = new ArrayList<TableDashboardItem>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_DASHBOARD,
                allColumnsDashboard, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TableDashboardItem dashboardItem = cursorToTableDashboardItem(cursor);
            dashboardItems.add(dashboardItem);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();

        return dashboardItems;
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
