package com.ca.tongxunlu;

import android.app.Activity;
import android.app.Application;

import com.ca.tongxunlu.crash.CrashHandler;

import java.util.LinkedList;
import java.util.List;

import im.fir.sdk.FIR;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-11 上午 11:42
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        FIR.init(this);
        CrashHandler.getInstance().init(instance);
//        CrashReport.initCrashReport(getApplicationContext(), "900037054", false);
    }

    //对于新增和删除操作add和remove，LinedList比较占优势，因为ArrayList实现了基于动态数组的数据结构，要移动数据。LinkedList基于链表的数据结构,便于增加删除
    private List<Activity> activityList = new LinkedList<>();

    //单例模式中获取唯一的MyApplication实例
    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }

    //添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    //遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
