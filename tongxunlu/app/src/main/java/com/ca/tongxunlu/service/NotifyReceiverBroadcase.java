package com.ca.tongxunlu.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2016/6/29.
 */
public class NotifyReceiverBroadcase extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent startServiceIntent = new Intent(context, OpenPhoneStart.class);
            startServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(startServiceIntent);
        }
    }
}
