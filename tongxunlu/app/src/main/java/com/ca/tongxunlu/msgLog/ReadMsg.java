package com.ca.tongxunlu.msgLog;

import com.ca.tongxunlu.model.CallLogMsg;
import com.ca.tongxunlu.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-16 下午 5:37
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: 读取短信
 */
public class ReadMsg {

    //获取所有信息
    public static final String SMS_URI_ALL = "content://sms/";
    //获取收件箱的细信息
    public static final String SMS_URI_INBOX = "1";
    public static final String SMS_URI_INBOXT = "content://sms/inbox";
    //获取发送的信息
    public static final String SMS_URI_SEND = "2";
    public static final String SMS_URI_SENDT = "content://sms/sent";
    //获取草稿箱的信息
    public static final String SMS_URI_DRAFT = "3";
    public static final String SMS_URI_DRAFTT = "content://sms/draft";

   /* //获取发送失败的信息
    public static final String SMS_URI_FAILED = "content://sms/failed";
    public static final String SMS_URI_QUEUED = "content://sms/queued";*/

    public static List<CallLogMsg> getSmsInPhone(String type) {
        List<CallLogMsg> logMsgListAll = Utils.msgListAll;
        List<CallLogMsg> logMsgList = new ArrayList<>();
        for (int i = 0; i < logMsgListAll.size(); i++) {
            if (type.equals(logMsgListAll.get(i).type)) {
                CallLogMsg callLogMsg = new CallLogMsg();
                callLogMsg.name = logMsgListAll.get(i).name;
                callLogMsg.type = logMsgListAll.get(i).type;
                callLogMsg.msgcontent = logMsgListAll.get(i).msgcontent;
                callLogMsg.howTime = logMsgListAll.get(i).howTime;
                logMsgList.add(callLogMsg);
            }
        }
        return logMsgList;
    }
}
