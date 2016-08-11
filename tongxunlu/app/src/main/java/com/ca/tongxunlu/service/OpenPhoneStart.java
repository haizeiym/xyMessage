package com.ca.tongxunlu.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Administrator on 2016/6/29.
 */
public class OpenPhoneStart extends Service {
    AlarmManager mAlarmManager = null;
    PendingIntent mPendingIntent = null;

    @Override
    public void onCreate() {
        //start the service through alarm repeatly
        Intent intent = new Intent(getApplicationContext(), OpenPhoneStart.class);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mPendingIntent = PendingIntent.getService(this, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
        long now = System.currentTimeMillis();
        mAlarmManager.setInexactRepeating(AlarmManager.RTC, now, 60000, mPendingIntent);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
