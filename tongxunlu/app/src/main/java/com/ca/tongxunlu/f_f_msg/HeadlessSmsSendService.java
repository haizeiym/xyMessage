package com.ca.tongxunlu.f_f_msg;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by hanj on 14-10-31.
 */
public class HeadlessSmsSendService extends Service{
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
