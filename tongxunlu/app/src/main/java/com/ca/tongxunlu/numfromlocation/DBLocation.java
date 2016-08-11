package com.ca.tongxunlu.numfromlocation;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2016/6/6.
 */
public class DBLocation {

    //根据号码获取归属地
    public static String getResult(SQLiteDatabase database, String number) {
        String result = "未知号码";
        number = number.replace(" ", "");
        if (number.length() > 7) {
            String firstNum = number.substring(0, 1);
            if (number.length() >= 10) {
                if ("0".equals(firstNum)) {
                    String s1 = number.substring(1);
                    String s2;
                    String second = s1.substring(0, 1);
                    if (second.equals("1") || second.equals("2")) {
                        s2 = s1.substring(0, 2);
                    } else {
                        s2 = s1.substring(0, 3);
                    }
                    String sql = "select location from tel_location where _id = ? ";
                    String[] param = new String[]{s2};
                    if (database != null && database.isOpen()) {
                        Cursor cursor = database.rawQuery(sql, param);
                        if (cursor.moveToNext()) {
                            result = cursor.getString(0);
                        }
                        cursor.close();
                    }
                } else {
                    if (number.indexOf("+86") == 0) {
                        number = number.substring(3);
                    }
                    if (number.indexOf("86") == 0) {
                        number = number.substring(2);
                    }
                    String s1 = number.substring(0, 7);
                    String sql = "select location from mob_location where _id = ? ";
                    String[] param = new String[]{s1};
                    if (database != null && database.isOpen()) {
                        Cursor cursor = database.rawQuery(sql, param);
                        if (cursor.moveToNext()) {
                            result = cursor.getString(0);
                        }
                        cursor.close();
                    }
                }
            } else {
                result = "本地号码";
            }
        } else {
            if (number.length() < 4) {
                result = "未知号码";
            } else {
                result = "本地号码";
            }
        }
        return result;
    }
}
