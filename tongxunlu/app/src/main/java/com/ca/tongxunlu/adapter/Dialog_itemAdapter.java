package com.ca.tongxunlu.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ca.tongxunlu.R;
import com.ca.tongxunlu.TBaseAdapter;
import com.ca.tongxunlu.utils.Utils;

/**
 * Created by haizeiym
 * on 2016/7/21
 */
public class Dialog_itemAdapter extends TBaseAdapter {
    Context context;

    public Dialog_itemAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int setCount() {
        return Utils.dialog_item.length;
    }

    @Override
    public View setView(int position, View convertView) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.dialog_item, null);
            holder.num = (TextView) convertView.findViewById(R.id.dialNum);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.num.setText(Utils.dialog_item[position]);
        return convertView;
    }

    public class ViewHolder {
        public TextView num;
    }

}

