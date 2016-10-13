package com.ca.tongxunlu.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ca.tongxunlu.contact.XYConstant;
import com.ca.tongxunlu.ui.AcceptCallUI;
import com.ca.tongxunlu.utils.Utils;


/**
 * Description    : TODO
 * Author         : 李月
 * Version        : 1.2
 * Create Date    : 2016-05-27 下午 7:51
 * Update Desc    : TODO
 * Update Author  : 李月
 * Update Version : 1.2
 * Update Date    : 2016-05-27 下午 7:51
 * 来电获去电监听
 */
public class PhoneListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new ComeCall(), PhoneStateListener.LISTEN_CALL_STATE);
        /*if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            //拨打电话（分机号未解决暂时不加，以后添加）
            SystemClock.sleep(XYConstant.WAITING_WINDOW);
            Utils.outingWindow(context);
        } else {*/
        //接听电话
        comingCall(context, telephony, intent);
        /*}*/
    }

    private void comingCall(Context context, TelephonyManager telephony, Intent intent2) {
        String phoneNumber = intent2.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        int state = telephony.getCallState();
        Intent callShowView = new Intent(context, CallShowView.class);
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                //来电监听
                CallShowView.context = context;
                CallShowView.phoneNumBerStr = phoneNumber;
                CallShowView.intent = intent2;
                SystemClock.sleep(XYConstant.WAITING_WINDOW);
                context.startService(callShowView);
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //挂断电话
                if (AcceptCallUI.instance != null) {
                    AcceptCallUI.instance.finish();
                }
                if (CallShowView.context != null) {
                    CallShowView.context.stopService(callShowView);
                }
                Utils.removeDia();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //通话中不做任何操作
                break;
        }
    }
}
