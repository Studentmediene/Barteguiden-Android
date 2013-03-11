package com.underdusken.kulturekalendar.mainhandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import com.underdusken.kulturekalendar.data.EventsItem;
import com.underdusken.kulturekalendar.data.db.ManageDataBase;
import com.underdusken.kulturekalendar.json.JsonParseEvents;
import com.underdusken.kulturekalendar.network.NetworkRequest;
import com.underdusken.kulturekalendar.utils.NetworkLinks;

import java.sql.SQLException;
import java.util.List;

/**
 *  Class for update network data
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
     *
     */
     public static MainHandler getInstance(Context context) {
        if (mainHandler == null)
            if (context == null)
                return null;
            else
                mainHandler = new MainHandler(context);
        return mainHandler;
    }


    /**
     * Create instatnce of class
     *
     * @param activity
     */
    public static void createInstance(Activity activity) {
        if (mainHandler == null)
            if (activity == null)
                return;
            else
                mainHandler = new MainHandler(activity);
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


    private class UpdateDataThread extends AsyncTask<Void, String, String> {

        private boolean blockThread = false;

        @Override
        protected String doInBackground(Void... voids) {

            //while (true) {

                // If no internet connection
                /*
                while(NO_INTERNET_CONNECTION){
                    try{
                        Thread.sleep(WAIT_INTERNET_CONNECTION);
                    }catch(InterruptedException e){
                        return null;
                    }
                 }
                 */
                //TODO try to load throw the internet
                // if something happened
                // progress

                // For test internet connection
                NetworkRequest networkRequest = new NetworkRequest();
                NetworkRequest.setActivity(context);
                String result = networkRequest.getInputStreamFromUrl(NetworkLinks.JSON_DATA);

                if(result!=null){
                    blockThread = true;
                    return result;
                    //publishProgress(result);
                }

                // waiting while we add new data to dataBase
                /*while(blockThread){
                    try{
                        Thread.sleep(WAIT_UPDATE_DATA_BASE);
                    }catch(InterruptedException e){
                        return null;
                    }
                }*/


                if (isCancelled())
                    return null;

                try{
                    Thread.sleep(UPDATE_TIME);
                }catch(InterruptedException e){
                    return null;
                }
                return null;

           // }
        }


        @Override
        protected void onProgressUpdate(String... values) {

/*
           if(values != null)

               if(values.length > 0){

               List<EventsItem> newEventsList = JsonParseEvents.parse(values[0]);   // parse incoming events

               if(newEventsList!=null){
                    for(EventsItem eventsItem: newEventsList){              // add information to DB
                        ManageDataBase manageDataBase = new ManageDataBase(context);
                        try {
                            manageDataBase.open();
                            EventsItem checkEventItem = manageDataBase.addEventItem(eventsItem);
                            manageDataBase.close();
                        } catch (SQLException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }

                    }
                   Intent i = new Intent(BroadcastNames.BROADCAST_NEW_DATA);
                   context.sendBroadcast(i);

                   //TODO delete test!!!
                   Toast.makeText(context, "New data", Toast.LENGTH_SHORT).show();
               }
            }
            blockThread = false;*/
        }

        @Override
        protected void onPostExecute(String values) {

            if(values != null){

                    List<EventsItem> newEventsList = JsonParseEvents.parse(values);   // parse incoming events

                    if(newEventsList!=null){
                        for(EventsItem eventsItem: newEventsList){              // add information to DB
                            ManageDataBase manageDataBase = new ManageDataBase(context);
                            try {
                                manageDataBase.open();
                                EventsItem checkEventItem = manageDataBase.addEventItem(eventsItem);
                                manageDataBase.close();
                            } catch (SQLException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }

                        }
                        Intent i = new Intent(BroadcastNames.BROADCAST_NEW_DATA);
                        context.sendBroadcast(i);

                        //TODO delete test!!!
                        Toast.makeText(context, "New data", Toast.LENGTH_SHORT).show();
                    }
            }
            blockThread = false;
        }

    }

}
