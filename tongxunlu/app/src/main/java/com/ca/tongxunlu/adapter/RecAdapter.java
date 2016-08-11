package com.ca.tongxunlu.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ca.tongxunlu.R;
import com.ca.tongxunlu.TBaseAdapter;
import com.ca.tongxunlu.model.CallLogMsg;
import com.ca.tongxunlu.numfromlocation.DBLocation;
import com.ca.tongxunlu.utils.GetAssetsDB;
import com.ca.tongxunlu.utils.Utils;

import java.util.List;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-13 下午 12:48
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class RecAdapter extends TBaseAdapter {
    Context context;
    List<CallLogMsg> list;
    public boolean isLongClick = false;
    SQLiteDatabase sd;

    public RecAdapter(Context context, List<CallLogMsg> list) {
        this.context = context;
        this.list = list;
        GetAssetsDB.initManager(context);
        // 获取管理对象，因为数据库需要通过管理对象才能够获取
        GetAssetsDB mg = GetAssetsDB.getManager();
        // 通过管理对象获取数据库
        sd = mg.getDatabase("callHomeDB.db");
    }

    @Override
    public int setCount() {
        return list.size();
    }

    @Override
    public View setView(final int position, View convertView) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.recadapteritem, null);
            holder.nameornum = (TextView) convertView.findViewById(R.id.nameornum);
            holder.howTime = (TextView) convertView.findViewById(R.id.howTime);
            holder.callTime = (TextView) convertView.findViewById(R.id.callTime);
            holder.local = (TextView) convertView.findViewById(R.id.local);
            holder.isCheck = (CheckBox) convertView.findViewById(R.id.isCheck);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.howTime.setText("时间：" + list.get(position).howTime);
        holder.callTime.setText("时长：" + list.get(position).callTime);
        holder.local.setText(DBLocation.getResult(sd, list.get(position).num));
        if (TextUtils.isEmpty(list.get(position).name)) {
            holder.nameornum.setText(list.get(position).num);
        } else {
            holder.nameornum.setText(list.get(position).name);
        }
        if (isLongClick) {
            holder.isCheck.setVisibility(View.VISIBLE);
            holder.isCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Utils.toast(context, list.get(position).name);
                    }
                }
            });
        } else {
            holder.isCheck.setChecked(false);
            holder.isCheck.setVisibility(View.GONE);
        }
        notifyDataSetChanged();
        return convertView;
    }

    class ViewHolder {
        TextView nameornum, local, howTime, callTime;
        CheckBox isCheck;
    }

    public void refresh(List<CallLogMsg> list) {
        this.list = list;
        notifyDataSetChanged();
    }


}
