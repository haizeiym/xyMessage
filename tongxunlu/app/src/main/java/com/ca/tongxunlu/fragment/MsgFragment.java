package com.ca.tongxunlu.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ca.tongxunlu.R;
import com.ca.tongxunlu.msgLog.ReadMsg;
import com.ca.tongxunlu.ui.MsgUI;
import com.ca.tongxunlu.ui.SendMsgUI;
import com.ca.tongxunlu.utils.Utils;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-11 上午 11:54
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class MsgFragment extends Fragment implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.isSystemAPK(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.msgxml, null);
        init(view);
        return view;
    }

    private void init(View view) {
        TextView send = (TextView) view.findViewById(R.id.sendMsg);
        TextView inbox = (TextView) view.findViewById(R.id.inboxmsg);
        TextView draft = (TextView) view.findViewById(R.id.draftmsg);
        TextView sent = (TextView) view.findViewById(R.id.sentmsg);
        send.setOnClickListener(this);
        sent.setOnClickListener(this);
        draft.setOnClickListener(this);
        inbox.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.sendMsg:
                Utils.simpleIntent(getActivity(), SendMsgUI.class);
                break;
            case R.id.inboxmsg:
                intent.putExtra("msgtype", ReadMsg.SMS_URI_INBOX);
                intent.setClass(getActivity(), MsgUI.class);
                startActivity(intent);
                break;
            case R.id.sentmsg:
                intent.putExtra("msgtype", ReadMsg.SMS_URI_SEND);
                intent.setClass(getActivity(), MsgUI.class);
                startActivity(intent);
                break;
            case R.id.draftmsg:
                intent.putExtra("msgtype", ReadMsg.SMS_URI_DRAFT);
                intent.setClass(getActivity(), MsgUI.class);
                startActivity(intent);
                break;
        }
    }
}
