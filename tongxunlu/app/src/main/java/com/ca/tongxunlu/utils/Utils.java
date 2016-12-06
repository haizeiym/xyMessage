package com.ca.tongxunlu.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.ca.tongxunlu.MainActivity;
import com.ca.tongxunlu.R;
import com.ca.tongxunlu.contact.XYConstant;
import com.ca.tongxunlu.fragment.DialFragment;
import com.ca.tongxunlu.model.CallLogMsg;
import com.ca.tongxunlu.model.ContactModel;
import com.ca.tongxunlu.msgLog.ReadMsg;
import com.ca.tongxunlu.ui.AcceptCallUI;
import com.ca.tongxunlu.ui.MsgUI;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-12 上午 10:15
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class Utils {
    /**
     * 发送与接收的广播
     **/
    public static String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    public static String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
    //所有短信记录
    public static List<CallLogMsg> msgListAll = new ArrayList<>();
    //所有通话记录
    public static List<CallLogMsg> callLogMsgsAll = new ArrayList<>();
    UtilsInterface utilsInterface;
    //拨号键盘
    public static String[] dialog_item = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#", "拨打", "拨打", "菜单"};
    static boolean isOn = false;

    public Utils(UtilsInterface utilsInterface) {
        this.utilsInterface = utilsInterface;
    }

    //去电窗
    public static WindowManager mWindowManager;
    public static LinearLayout mFloatLayout;

    //toast工具
    public static void toast(Context context, String content) {
        final Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        toast.show();
        Timer timer = new Timer();
        TimerTask t = new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
            }
        };
        timer.schedule(t, 41);
    }

    //简单的跳转
    public static void simpleIntent(Context context, Class<?> cls) {
        context.startActivity(new Intent(context, cls));
    }

    //判断是否全是数字
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str.replace(" ", ""));
        return isNum.matches();
    }

    //实现文本复制功能
    public static void copy(String content) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) MainActivity.instance.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData textCd = ClipData.newPlainText("xinyang", content);
        cmb.setPrimaryClip(textCd);
    }

    //清空粘贴板
    public static void cleanCtrl(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData textCd = ClipData.newPlainText("xinyang", null);
        cmb.setPrimaryClip(textCd);
    }

    //实现粘贴功能
    public static String paste() {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) MainActivity.instance.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData.Item item;
        //无数据时直接返回
        if (!cmb.hasPrimaryClip()) {
            return "";
        }
        //如果是文本信息
        if (cmb.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData cdText = cmb.getPrimaryClip();
            item = cdText.getItemAt(0);
            //此处是TEXT文本信息
            if (item.getText() == null) {
                return "";
            } else {
                return item.getText().toString();
            }
        }
        return "";
    }

    //判断是否有粘贴内容
    public static boolean isCtrl_v() {
        return !TextUtils.isEmpty(Utils.paste());
    }


    //获取剪切板的内容
    public static String getctrl_v() {
        if (Utils.isCtrl_v()) {
            return Utils.paste();
        }
        return "没有数据";
    }

    //通话记录删除
    public static void delCallLog(String num, String dataTime) {
        Cursor cs = null;
        try {
            ContentResolver resolver = MainActivity.instance.getContentResolver();
            cs = resolver.query(
                    CallLog.Calls.CONTENT_URI, //系统方式获取通讯录存储地址
                    new String[]{CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DATE, CallLog.Calls.DURATION}, null, null,
                    CallLog.Calls.DEFAULT_SORT_ORDER);
            if (cs != null) {
                while (cs.moveToNext()) {
                    String nums = cs.getString(cs.getColumnIndex(CallLog.Calls.NUMBER));
                    //拨打时间
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date callDate = new Date(Long.parseLong(cs.getString(cs.getColumnIndex(CallLog.Calls.DATE))));
                    String callDateStr = sdf.format(callDate);
                    if (num.equals(nums) && dataTime.equals(callDateStr)) {
                        resolver.delete(CallLog.Calls.CONTENT_URI, CallLog.Calls.NUMBER + "=?", new String[]{nums});
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
    }

    //删除短信
    public static void deleteSMS(Context context, String type, String num, String dataTime, boolean isDelAll) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(Uri.parse(type), new String[]{"_id", "thread_id", "address", "person", "date", "body"}, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    long threadId = cursor.getLong(1);
                    if (!isDelAll) {
                        long data = cursor.getLong(cursor.getColumnIndex("date"));
                        String number = cursor.getString(cursor.getColumnIndex("address"));
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Date d = new Date(data);
                        String strDate = dateFormat.format(d);
                        if (dataTime.equals(strDate) && num.equals(number)) {
                            Uri mUri = Uri.parse("content://sms/conversations/" + threadId);
                            context.getContentResolver().delete(mUri, null, null);
                            Utils.toast(context, "刪除成功");
                            break;
                        }
                    } else {
                        Uri mUri = Uri.parse("content://sms/conversations/" + threadId);
                        context.getContentResolver().delete(mUri, null, null);
                        Utils.toast(context, "刪除全部信息成功");
                    }
                }
                //发消息通知更新
                MsgUI.instance.handler.sendEmptyMessage(XYConstant.REFRESH_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    //打电话的服务
    public static void callPhone(Activity activity, String num) {
        Intent callIntent = new Intent();
        callIntent.setAction("android.intent.action.CALL");
        callIntent.addCategory("android.intent.category.DEFAULT");
        callIntent.setData(Uri.parse("tel:" + num));
        activity.startActivity(callIntent);
    }

    //发送短信

    /**
     * 参数说明
     * destinationAddress:收信人的手机号码
     * scAddress:发信人的手机号码
     * text:发送信息的内容
     * sentIntent:发送是否成功的回执，用于监听短信是否发送成功。
     * DeliveryIntent:接收是否成功的回执，用于监听短信对方是否接收成功。
     */
    public static void sendSMS(Context context, String phoneNumber, String message) {
        // ---sends an SMS message to another device---
        SmsManager sms = SmsManager.getDefault();
        // create the sentIntent parameter
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sentIntent, 0);
        // create the deilverIntent parameter
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0, deliverIntent, 0);
        //如果短信内容超过70个字符 将这条短信拆成多条短信发送出去
        if (message.length() > 70) {
            ArrayList<String> msgs = sms.divideMessage(message);
            for (String msg : msgs) {
                insertMsg(context, XYConstant.SEND_MSG, phoneNumber, message);
                sms.sendTextMessage(phoneNumber, null, msg, sentPI, deliverPI);
            }
        } else {
            insertMsg(context, XYConstant.SEND_MSG, phoneNumber, message);
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliverPI);
        }
        MainActivity.instance.handler.sendEmptyMessage(XYConstant.NEW_MSG);
    }

    //短信发送成功后返回
    public BroadcastReceiver sendMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //判断短信是否发送成功
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    if (utilsInterface != null) {
                        utilsInterface.setData();
                    }
                    break;
                default:
                    Utils.toast(context, "短信发送失败");
                    break;
            }
        }
    };


    //短信插入到数据库
    public static void insertMsg(Context context, int msgType, String address, String body) {
        try {
            ContentResolver resolver = context.getContentResolver();
            Uri uri = Uri.parse("content://sms/");
            ContentValues values = new ContentValues();
            values.put("address", address);
            values.put("type", msgType);
            values.put("read ", 1);
            values.put("date", System.currentTimeMillis());
            values.put("body", body);
            resolver.insert(uri, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取手机联系人号码
    public static ArrayList<ContactModel> getPeopleInPhone(Context context) {
        Set<String> have = new HashSet<>();
        ArrayList<ContactModel> list = null;
        Cursor cursor = null;
        try {
            list = new ArrayList<>();
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);     // 获取手机联系人
            while (cursor != null && cursor.moveToNext()) {
                int indexPeopleName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);    // people name
                int indexPhoneNum = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);            // phone number
                String strPeopleName = cursor.getString(indexPeopleName);
                String strPhoneNum = cursor.getString(indexPhoneNum);
                if (!have.contains(strPhoneNum)) {
                    have.add(strPhoneNum);
                    ContactModel map = new ContactModel(strPeopleName, strPhoneNum, false);
                    list.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                have.clear();
            }
        }
        return list;
    }

    //根据名字取出数据库中联系人号码
    public static String ContactNum(String name, List<ContactModel> isIn) {
        for (ContactModel contactModel : isIn) {
            if (name.equals(contactModel.name)) {
                return contactModel.name;
            }
        }
        return "";
    }

    //系统数据库删除短信的类型
    public static String delType(String type) {
        String t = "";
        switch (type) {
            case ReadMsg.SMS_URI_INBOX:
                t = ReadMsg.SMS_URI_INBOXT;
                break;
            case ReadMsg.SMS_URI_SEND:
                t = ReadMsg.SMS_URI_SENDT;
                break;
            case ReadMsg.SMS_URI_DRAFT:
                t = ReadMsg.SMS_URI_DRAFTT;
                break;
        }
        return t;
    }

    //系统短信是否为自己的应用
    public static void isSystemAPK(Context context) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //在4.4以上的版本需要应用程序设置为短信默认程序
                String packName = context.getPackageName();
                if (!Telephony.Sms.getDefaultSmsPackage(context).equals(packName)) {
                    Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                    intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packName);
                    context.startActivity(intent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //震动
    private static void sharkCall(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 100, 0, 0};   // 停止 开启 停止 开启
        vibrator.vibrate(pattern, -1);
    }

    //挂断电话
    public static void endCall(Context context) {
        if (context == null) {
            return;
        }
        TelephonyManager telMag = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Class<TelephonyManager> c = TelephonyManager.class;
        Method mthEndCall;
        try {
            Utils.sharkCall(context);
            mthEndCall = c.getDeclaredMethod("getITelephony", (Class[]) null);
            mthEndCall.setAccessible(true);
            ITelephony iTel = (ITelephony) mthEndCall.invoke(telMag, (Object[]) null);
            iTel.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //接听电话
    public static void answerCall(Context context) {
        if (context == null) {
            return;
        }
        Utils.sharkCall(context);
        Intent intent = new Intent(context, AcceptCallUI.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }

   /* //去电弹窗（暂时不加，分机号未解决）
    public static void outingWindow(final Context context) {
        if (context == null) {
            return;
        }
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //创建浮动窗口设置布局参数的对象
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        //获取WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //设置window type[优先级]
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;//窗口的type类型决定了它的优先级，优先级越高显示越在顶层
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为顶部水平居中
        wmParams.gravity = Gravity.BOTTOM;
        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 0;
        wmParams.y = 0;
        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mFloatLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.outing_window, null);
        TextView endCall = (TextView) mFloatLayout.findViewById(R.id.outing_call_end);
        TextView hands_free = (TextView) mFloatLayout.findViewById(R.id.outing_call_hands_free);
        mWindowManager.addView(mFloatLayout, wmParams);
        endCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.endCall(context);
                removeDia();
                DialFragment.numSum = "";
                MainActivity.instance.handler.sendEmptyMessage(XYConstant.REFRESH_CALL_LOGS);
            }
        });
        hands_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOn) {
                    audioManager.setMode(AudioManager.STREAM_RING);
                    // 打开扬声器
                    audioManager.setSpeakerphoneOn(true);
                    isOn = true;
                } else {
                    //关闭扬声器
                    audioManager.setSpeakerphoneOn(false);
                    audioManager.setMode(AudioManager.MODE_IN_CALL);
                    isOn = false;
                }
            }
        });
    }*/

    //去除去电弹窗
    public static void removeDia() {
        if (mWindowManager != null && mFloatLayout != null) {
            mWindowManager.removeView(mFloatLayout);
            mWindowManager = null;
            mFloatLayout = null;
        }
    }

    //获取短信内容
    public static void systemMsg(Context context) {
        //清空数据
        msgListAll.clear();
        Cursor cur = null;
        try {
            Uri uri = Uri.parse("content://sms/");
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
            cur = context.getContentResolver().query(uri, projection, null, null, null);      // 获取手机内部短信
            if (cur != null && cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int type = cur.getColumnIndex("type");
                do {
                    String strAddress = cur.getString(index_Address);
                    int intType = cur.getInt(type);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date d = new Date(longDate);
                    String strDate = dateFormat.format(d);
                    CallLogMsg callLogMsg = new CallLogMsg();
                    callLogMsg.name = strAddress;
                    callLogMsg.howTime = strDate;
                    callLogMsg.type = intType + "";
                    callLogMsg.msgcontent = strbody;
                    msgListAll.add(callLogMsg);
                } while (cur.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cur != null) {
                cur.close();
            }
        }
    }

    //获取通话记录
    public static void getCallHistoryList(Context context) {
        callLogMsgsAll.clear();
        Cursor cs = null;
        try {
            ContentResolver cr = context.getContentResolver();
            cs = cr.query(CallLog.Calls.CONTENT_URI, //系统方式获取通讯录存储地址
                    new String[]{
                            CallLog.Calls.CACHED_NAME,  //姓名0
                            CallLog.Calls.NUMBER,    //号码1
                            CallLog.Calls.TYPE,  //呼入/呼出(2)/未接2
                            CallLog.Calls.DATE,  //拨打时间3
                            CallLog.Calls.DURATION   //通话时长4
                    }, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
            if (cs != null && cs.getCount() > 0) {
                while (cs.moveToNext()) {
                    String callName = cs.getString(cs.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    String callNumber = cs.getString(cs.getColumnIndex(CallLog.Calls.NUMBER));
                    //通话类型
                    int callType = cs.getInt(cs.getColumnIndex(CallLog.Calls.TYPE));
                    String callTypeStr = "";
                    switch (callType) {
                        case CallLog.Calls.INCOMING_TYPE:
                            callTypeStr = XYConstant.RECEIVED_CALLS;
                            break;
                        case CallLog.Calls.OUTGOING_TYPE:
                            callTypeStr = XYConstant.DIALED_CALLS;
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            callTypeStr = XYConstant.MISSED_CALLS;
                            break;
                    }
                    //拨打时间 
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date callDate = new Date(cs.getLong(cs.getColumnIndex(CallLog.Calls.DATE)));
                    String callDateStr = sdf.format(callDate);
                    //通话时长
                    int callDuration = cs.getInt(cs.getColumnIndex(CallLog.Calls.DURATION));
                    int min = callDuration / 60;
                    int sec = callDuration % 60;
                    String callDurationStr = min + "分" + sec + "秒";
                    CallLogMsg callLogMsg = new CallLogMsg();
                    callLogMsg.name = callName;
                    callLogMsg.num = callNumber;
                    callLogMsg.howTime = callDateStr;
                    callLogMsg.callTime = callDurationStr;
                    callLogMsg.type = callTypeStr;
                    callLogMsgsAll.add(callLogMsg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
    }

    //获取通话类型（已接听 ，已拨打， 未接听  ）
    public static List<CallLogMsg> getCallLogMsg(String type) {
        List<CallLogMsg> list = new ArrayList<>();
        for (int i = 0; i < callLogMsgsAll.size(); i++) {
            if (callLogMsgsAll.get(i).type.equals(type)) {
                CallLogMsg msg = new CallLogMsg();
                msg.type = callLogMsgsAll.get(i).type;
                msg.callTime = callLogMsgsAll.get(i).callTime;
                msg.howTime = callLogMsgsAll.get(i).howTime;
                msg.num = callLogMsgsAll.get(i).num;
                msg.name = callLogMsgsAll.get(i).name;
                list.add(msg);
            }
        }
        return list;
    }

    //权限申请
    @TargetApi(Build.VERSION_CODES.M)
    public static void applyForPermission(Activity activity) {
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission_group.SMS,
                Manifest.permission_group.CONTACTS,
                Manifest.permission_group.PHONE,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        for (String aPERMISSIONS_STORAGE : PERMISSIONS_STORAGE) {
            int permission = ActivityCompat.checkSelfPermission(activity, aPERMISSIONS_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, 1);
                break;
            }
        }
    }

    /*//版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }
    */
    //版本号
    public static String getVersionCode(Context context) {
        return getPackageInfo(context).versionName;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }

    public interface UtilsInterface {
        void setData();
    }

}
