package com.ca.tongxunlu;

import android.view.View;
import android.view.ViewGroup;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-13 上午 11:55
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: adapter基类, 适用于List集合
 */
public class TBaseAdapter extends android.widget.BaseAdapter {
    @Override
    public int getCount() {
        return setCount();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return setView(position, convertView);
    }

    //设置count
    public int setCount() {
        return 0;
    }

    //设置视图
    public View setView(int position, View convertView) {
        return null;
    }
}
