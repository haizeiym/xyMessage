package com.ca.tongxunlu.contact;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-14 上午 11:12
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class XYConstant {
    //接听模式
    public static final int ANSWER = 0x0000000124;
    //抬手
    public static final int UP_HOVER = 0x0000000125;
    //表名
    public static final String ANSWER_MODEL = "answer_model";
    public static final String UP_PING = "up_ping";
    //短信接收
    public static final int RECEIVE_MSG = 1;
    //短信发送
    public static final int SEND_MSG = 2;
    //短信草稿箱
    public static final int DRAFT_MSG = 3;
    //有新短信时提醒
    public static final int NEW_MSG = 0x345;
    //判断是否点击了粘贴
    public static boolean isCtrl_v = false;
    //扬声器切换
    public static final int OUT_SPEACK = 0x567;
    //跳转到系统通话界面
    public static final int TO_SYSTEM_PAGER = 0X789;
    //删除短信时刷新
    public static final int REFRESH_MSG = 0x123;
    //删除通话记录时刷新
    public static final int REFRESH_CALL_LOGS = 0x456;
    //已接电话
    public static final String DIALED_CALLS = "已接电话";
    //已拨电话
    public static final String RECEIVED_CALLS = "已拨电话";
    //未接来电
    public static final String MISSED_CALLS = "未接电话";
    //弹窗等待时间
    public static final int WAITING_WINDOW = 379;

}
