package com.underdusken.kulturekalendar.ui.reveivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.underdusken.kulturekalendar.utils.ToDo;

/**
 * Created with IntelliJ IDEA.
 * User: pavelarteev
 * Date: 11/4/12
 * Time: 2:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class NotificationUpdateReceiver extends BroadcastReceiver {

    private Handler activityHandler = null;
    private ToDo toDo = null;


    public NotificationUpdateReceiver(Handler activityHandler, ToDo toDo) {
        super();
        this.activityHandler = activityHandler;
        this.toDo = toDo;
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        activityHandler.post(new Runnable() {
            @Override
            public void run() {
                toDo.doSomething();
            }
        });
    }
}
