package com.underdusken.kulturekalendar.ui.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.mainhandler.BroadcastNames;
import com.underdusken.kulturekalendar.mainhandler.MainHandler;

public class SetupActivity extends Activity {
    private NotificationUpdateReciever reciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup);

        MainHandler.getInstance(this.getApplicationContext()).onStartApplication();

        reciever = new NotificationUpdateReciever();
        IntentFilter intentFilter = new IntentFilter(BroadcastNames.BROADCAST_NEW_DATA);
        registerReceiver(reciever, intentFilter);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(reciever);
    }

    class NotificationUpdateReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent arg1) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(SetupActivity.this);
                    p.edit().putBoolean("needSetup", false);
                    long time = System.currentTimeMillis();
                    p.edit().putLong("last_update", time).commit();
                    SetupActivity.this.finish();
                }
            });
        }
    }
}
