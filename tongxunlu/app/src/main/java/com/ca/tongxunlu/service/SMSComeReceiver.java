package com.ca.tongxunlu.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.ca.tongxunlu.contact.XYConstant;
import com.ca.tongxunlu.ui.MsgUI;
import com.ca.tongxunlu.utils.Utils;

/**
 * Created by Administrator on 2016/7/2.
 * 收到短信时操作
 */
public class SMSComeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("android.provider.Telephony.SMS_RECEIVED".equals(action) || "android.provider.Telephony.SMS_DELIVER".equals(action)) {
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                Object[] smsObj = (Object[]) bundle.get("pdus");
                if (smsObj != null) {
                    //在这里写自己的逻辑,设置来电铃声
                    try {
                        for (Object object : smsObj) {
                            SmsMessage msg = SmsMessage.createFromPdu((byte[]) object);
                            Utils.insertMsg(context, XYConstant.RECEIVE_MSG, msg.getOriginatingAddress(), msg.getDisplayMessageBody());
                        }
                        playRingTone(context, RingtoneManager.TYPE_NOTIFICATION);
                        //有短信时发消息通知更新
                        /*MainActivity.instance.handler.sendEmptyMessage(XYConstant.NEW_MSG);*/
                        MsgUI.instance.handler.sendEmptyMessage(XYConstant.NEW_MSG);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // 获取的是铃声的Uri
    private Uri getDefaultRingtoneUri(Context ctx, int type) {
        return RingtoneManager.getActualDefaultRingtoneUri(ctx, type);
    }

    // 播放铃声
    private void playRingTone(final Context ctx, final int type) {
        final MediaPlayer mMediaPlayer = MediaPlayer.create(ctx, getDefaultRingtoneUri(ctx, type));
        mMediaPlayer.start();
    }
}
