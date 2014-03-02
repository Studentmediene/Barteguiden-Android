package com.underdusken.kulturekalendar.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.underdusken.kulturekalendar.data.EventItem;
import com.underdusken.kulturekalendar.data.db.DatabaseManager;
import com.underdusken.kulturekalendar.utils.ImageLoader;

import java.sql.SQLException;
import java.util.List;

public class ImageDownloaderService extends IntentService {
    private static final String TAG = "ImageDownloaderService";

    public ImageDownloaderService() {
        super("");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        download();
    }

    private void download() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


        DatabaseManager databaseManager = new DatabaseManager(this);
        try {
            databaseManager.open();
        } catch (SQLException e) {

        }
        List<EventItem> events = databaseManager.getAllEventsItem();
        databaseManager.close();
        String[] url = new String[events.size()];

        for (int i = 0; i < events.size(); i++) {
            url[i] = events.get(i).getImageURL();
        }

        ImageLoader il = new ImageLoader(this);
        int i = 0;
        while (ni.isConnected() && i < url.length) {
            if (i % 10 == 0)
                il.downloadImage(url[i++]);
        }
    }
}

