package com.ca.tongxunlu.service;

import android.telephony.PhoneStateListener;

import com.ca.tongxunlu.contact.XYConstant;
import com.ca.tongxunlu.ui.Dialed_calls;
import com.ca.tongxunlu.ui.Missed_calls;
import com.ca.tongxunlu.ui.Received_calls;

/**
 * Created by Administrator on 2016/7/12.
 * 来电状态监听
 */
public class ComeCall extends PhoneStateListener {

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        setOther();
    }

    public void setOther() {
        if (Missed_calls.instance != null) {
            Missed_calls.instance.handler.sendEmptyMessage(XYConstant.REFRESH_CALL_LOGS);
        } else if (Received_calls.instance != null) {
            Received_calls.instance.handler.sendEmptyMessage(XYConstant.REFRESH_CALL_LOGS);
        } else if (Dialed_calls.instance != null) {
            Dialed_calls.instance.handler.sendEmptyMessage(XYConstant.REFRESH_CALL_LOGS);
        }
    }
}
