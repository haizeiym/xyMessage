package com.ca.tongxunlu.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ca.tongxunlu.R;
import com.ca.tongxunlu.TBaseAdapter;
import com.ca.tongxunlu.model.CallLogMsg;
import com.ca.tongxunlu.model.ContactModel;

import java.util.List;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-16 下午 3:46
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: 信息显示界面
 */
public class MsgAdapter extends TBaseAdapter {
    Context context;
    List<CallLogMsg> callLogMsgs;
    List<ContactModel> isIn;

    public MsgAdapter(Context context, List<CallLogMsg> callLogMsgs, List<ContactModel> isIn) {
        this.context = context;
        this.callLogMsgs = callLogMsgs;
        this.isIn = isIn;
    }

    @Override
    public int setCount() {
        return callLogMsgs.size();
    }

    @Override
    public View setView(int position, View convertView) {
        ViewHolde holder;
        if (convertView == null) {
            holder = new ViewHolde();
            convertView = View.inflate(context, R.layout.msgitem, null);
            holder.content = (TextView) convertView.findViewById(R.id.msgcontent);
            holder.num = (TextView) convertView.findViewById(R.id.num);
            holder.msgTime = (TextView) convertView.findViewById(R.id.msgTime);
            holder.isCheck = (CheckBox) convertView.findViewById(R.id.isCheck);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolde) convertView.getTag();
        }
        String name;
        Uri personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, callLogMsgs.get(position).name);
        Cursor localCursor = context.getContentResolver().query(personUri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (localCursor != null && localCursor.getCount() != 0) {
            localCursor.moveToFirst();
            name = localCursor.getString(localCursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        } else {
            name = callLogMsgs.get(position).name;
        }
        holder.num.setText(name);
        holder.content.setText(callLogMsgs.get(position).msgcontent);
        holder.msgTime.setText(callLogMsgs.get(position).howTime);
        holder.isCheck.setVisibility(View.GONE);
        return convertView;
    }

    class ViewHolde {
        TextView num, content, msgTime;
        CheckBox isCheck;
    }

    public void refresh(List<CallLogMsg> callLogMsgs, List<ContactModel> isIn) {
        this.callLogMsgs = callLogMsgs;
        this.isIn = isIn;
        notifyDataSetChanged();
    }
}
