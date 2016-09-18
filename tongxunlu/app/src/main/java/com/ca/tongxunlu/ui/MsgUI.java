package com.ca.tongxunlu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ca.tongxunlu.BaseActivity;
import com.ca.tongxunlu.MainActivity;
import com.ca.tongxunlu.MyApplication;
import com.ca.tongxunlu.R;
import com.ca.tongxunlu.adapter.MsgAdapter;
import com.ca.tongxunlu.contact.XYConstant;
import com.ca.tongxunlu.model.CallLogMsg;
import com.ca.tongxunlu.msgLog.ReadMsg;
import com.ca.tongxunlu.i.Del;
import com.ca.tongxunlu.utils.Utils;
import com.ca.tongxunlu.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-16 上午 9:42
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class MsgUI extends BaseActivity implements AdapterView.OnItemClickListener {
    private String type;
    MsgAdapter adapter;
    List<CallLogMsg> callLogMsgs = new ArrayList<>();
    //    ReadMsg readMsg;
    TextView delAllMsg;
    public static MsgUI instance;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case XYConstant.NEW_MSG:
                case XYConstant.REFRESH_MSG:
                    Utils.systemMsg(instance);
                    adapter.refresh(callLogMsgs = ReadMsg.getSmsInPhone(type), Utils.getPeopleInPhone(instance));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.msglayout);
        init();
    }

    private void init() {
        instance = this;
        type = getIntent().getStringExtra("msgtype");
        ListView autoListView = (ListView) findViewById(R.id.msgContent);
        View headView = View.inflate(instance, R.layout.del_head_view, null);
        delAllMsg = (TextView) headView.findViewById(R.id.delAllMsg);
        delAllMsg.setOnClickListener(this);
        callLogMsgs = ReadMsg.getSmsInPhone(type);
        adapter = new MsgAdapter(MsgUI.this, callLogMsgs, Utils.getPeopleInPhone(instance));
        autoListView.setAdapter(adapter);
        autoListView.addHeaderView(delAllMsg);
        autoListView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.delAllMsg:
                Utils.toast(MsgUI.this, "已经入删除所有短信界面，请谨慎处理");
                ViewUtils.showDialog(MsgUI.this, new Del() {
                    @Override
                    public void sure() {
                        Utils.deleteSMS(MainActivity.instance, Utils.delType(type), null, null, true);
                        finish();
                    }

                    @Override
                    public void replaceName(TextView sure, TextView cancle) {

                    }

                    @Override
                    public void cancle() {

                    }
                });
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("content", callLogMsgs.get(position - 1).msgcontent);
        intent.putExtra("num", callLogMsgs.get(position - 1).name);
        intent.putExtra("name", callLogMsgs.get(position - 1).num);
        intent.putExtra("dateTime", callLogMsgs.get(position - 1).howTime);
        intent.setClass(MsgUI.this, MeauUI.class);
        startActivity(intent);
    }
}
