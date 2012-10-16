package com.underdusken.kulturekalendar.mainhandler;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

/**
 *  Class for update network data
 */
public class MainHandler {
    private Activity activity = null;

    static private final int UPDATE_TIME = 20000;
    static private final int WAIT_INTERNET_CONNECTION = 5000;
    static private final int WAIT_UPDATE_DATA_BASE = 1000;

    // loading data thread
    private UpdateDataThread updateDataThread = null;

    private MainHandler(Activity activity) {
        this.activity = activity;
    }

    private static MainHandler mainHandler = null;

    /**
     * Get instance of class
     *
     */
     public static MainHandler getInstance(Activity activity) {
        if (mainHandler == null)
            if (activity == null)
                return null;
            else
                mainHandler = new MainHandler(activity);
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


    private class UpdateDataThread extends AsyncTask<Void, String, Void> {

        private boolean blockThread = false;

        @Override
        protected Void doInBackground(Void... voids) {

            while (true) {

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
                while(blockThread){
                    try{
                        Thread.sleep(WAIT_UPDATE_DATA_BASE);
                    }catch(InterruptedException e){
                        return null;
                    }
                }


                if (isCancelled())
                    return null;

                try{
                    Thread.sleep(UPDATE_TIME);
                }catch(InterruptedException e){
                    return null;
                }

            }
        }


        @Override
        protected void onProgressUpdate(String... values) {
            blockThread = true;
           // get results from DB
           if(values != null){
               // TODO update database and send broadcast
               /*ManageDataBase manageDataBase = new ManageDataBase(activity);
               manageDataBase.open();
               tablePaymentItemList = manageDataBase.addEvent();
               manageDataBase.close();*/


               Intent i = new Intent(BroadcastNames.BROADCAST_NEW_DATA);
               activity.sendBroadcast(i);
           }
            blockThread = false;
        }

        @Override
        protected void onPostExecute(Void result) {

        }

    }



}
