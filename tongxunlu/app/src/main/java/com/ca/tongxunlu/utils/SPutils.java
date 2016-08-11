package com.ca.tongxunlu.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ca.tongxunlu.contact.XYConstant;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-14 上午 11:01
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class SPutils {

    public SPutils(Context context, int type) {
        String spName = null;
        switch (type) {
            case XYConstant.ANSWER:
                spName = XYConstant.ANSWER_MODEL;
                break;
            case XYConstant.UP_HOVER:
                spName = XYConstant.UP_PING;
                break;


        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, 1);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        save(editor);
        getSaveData(sharedPreferences);
    }

    //存放数据
    public void save(SharedPreferences.Editor editor) {

    }

    //获取存储的数据
    public void getSaveData(SharedPreferences sharedPreferences) {

    }

}
