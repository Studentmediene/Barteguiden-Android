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
import android.util.Log;

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        reciever = new NotificationUpdateReciever();
        IntentFilter intentFilter = new IntentFilter(BroadcastNames.BROADCAST_NEW_DATA);
        registerReceiver(reciever, intentFilter);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.abc_slide_out_top);
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
                    Log.d("huheuehuehue", "onRecieve");
                    SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(SetupActivity.this);

                    SharedPreferences.Editor e = p.edit();
                    e.putBoolean("needSetup", false);
                    long time = System.currentTimeMillis();
                    e.putLong("last_update", time);
                    e.commit();

                    SetupActivity.this.finish();
                }
            });
        }
    }
}
