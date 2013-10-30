package com.underdusken.kulturekalendar.ui;

import android.app.Application;

/**
 * Created with IntelliJ IDEA.
 * User: pavelarteev
 * Date: 11/4/12
 * Time: 12:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class KulturkalendarApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

       // Toast.makeText(this.getApplicationContext(), "Program terminated...", Toast.LENGTH_SHORT).show();
    }

}