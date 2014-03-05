package com.underdusken.kulturekalendar.mainhandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.underdusken.kulturekalendar.data.EventItem;
import com.underdusken.kulturekalendar.data.db.DatabaseManager;
import com.underdusken.kulturekalendar.json.JsonParseEvents;
import com.underdusken.kulturekalendar.network.NetworkRequest;
import com.underdusken.kulturekalendar.utils.NetworkLinks;

import java.sql.SQLException;
import java.util.List;

/**
 * Class for update network data
 */
public class MainHandler {
    private Context context = null;

    static private final int UPDATE_TIME = 5000;
    static private final int WAIT_INTERNET_CONNECTION = 5000;
    static private final int WAIT_UPDATE_DATA_BASE = 1000;

    // loading data thread
    private UpdateDataThread updateDataThread = null;

    private MainHandler(Context context) {
        this.context = context;
    }

    private static MainHandler mainHandler = null;

    /**
     * Get instance of class
     */
    public static MainHandler getInstance(Context context) {
        if (mainHandler == null) if (context == null) return null;
        else mainHandler = new MainHandler(context);
        return mainHandler;
    }


    /**
     * Create instatnce of class
     *
     * @param activity
     */
    public static void createInstance(Activity activity) {
        if (mainHandler == null) if (activity == null) return;
        else mainHandler = new MainHandler(activity);
    }


    /**
     * On start registration test for TRIGGERS
     */
    public void onStartApplication() {
        updateDataThread = new UpdateDataThread();
        updateDataThread.execute();
    }


    // TODO
    public void onCloseApplication() {
        updateDataThread.cancel(true);
    }


    private class UpdateDataThread extends AsyncTask<Void, Void, List<EventItem>> {

        @Override
        protected List<EventItem> doInBackground(Void... voids) {
            // For test internet connection
            NetworkRequest networkRequest = new NetworkRequest();
            NetworkRequest.setActivity(context);
            String result = networkRequest.getInputStreamFromUrl(NetworkLinks.JSON_DATA);

            if (result != null) {
                List<EventItem> newEventsList = JsonParseEvents.parse(result);
                return newEventsList;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<EventItem> list) {
            DatabaseManager databaseManager = new DatabaseManager(context);
            try {
                databaseManager.open();
                for (EventItem e : list) {
                    databaseManager.addEventItem(e);
                }
                databaseManager.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Intent i = new Intent(BroadcastNames.BROADCAST_NEW_DATA);
            context.sendBroadcast(i);
        }

    }

}
