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
import com.ca.tongxunlu.MyApplication;
import com.ca.tongxunlu.R;
import com.ca.tongxunlu.adapter.RecAdapter;
import com.ca.tongxunlu.contact.XYConstant;
import com.ca.tongxunlu.model.CallLogMsg;
import com.ca.tongxunlu.i.Del;
import com.ca.tongxunlu.utils.Utils;
import com.ca.tongxunlu.utils.ViewUtils;

import java.util.List;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-13 下午 2:16
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class Dialed_calls extends BaseActivity implements AdapterView.OnItemClickListener {
    ListView dia_listView;
    RecAdapter recAdapter;
    List<CallLogMsg> dia;
    TextView delall;
    public static Dialed_calls instance;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case XYConstant.REFRESH_CALL_LOGS:
                    Utils.getCallHistoryList(instance);
                    dia = Utils.getCallLogMsg(XYConstant.DIALED_CALLS);
                    recAdapter.refresh(dia);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.listviewxml);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessage(XYConstant.REFRESH_CALL_LOGS);
    }

    private void init() {
        instance = this;
        dia_listView = (ListView) findViewById(R.id.rec_lv_msg);
        View headview = View.inflate(instance, R.layout.del_all_headview, null);
        delall = (TextView) headview.findViewById(R.id.delAll_call_log);
        delall.setOnClickListener(this);
        dia_listView.setOnItemClickListener(this);
        dia = Utils.getCallLogMsg(XYConstant.DIALED_CALLS);
        recAdapter = new RecAdapter(Dialed_calls.this, dia);
        dia_listView.setAdapter(recAdapter);
        dia_listView.addHeaderView(headview);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.delAll_call_log:
                Utils.toast(instance, "已进入删除全部记录界面，请谨慎操作");
                ViewUtils.showDialog(instance, new Del() {
                    @Override
                    public void sure() {
                        for (int i = 0; i < dia.size(); i++) {
                            Utils.delCallLog(dia.get(i).num, dia.get(i).howTime);
                        }
                        Utils.toast(instance, "刪除成功");
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
        Intent intent = new Intent(Dialed_calls.this, CallMenuActivity.class);
        intent.putExtra("num", dia.get(position - 1).num);
        intent.putExtra("dataTime", dia.get(position - 1).howTime);
        intent.putExtra("whoHandler", "D");
        startActivity(intent);
    }

}
