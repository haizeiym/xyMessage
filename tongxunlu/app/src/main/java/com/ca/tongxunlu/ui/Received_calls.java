package com.ca.tongxunlu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
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
public class Received_calls extends BaseActivity implements AdapterView.OnItemClickListener {
    ListView rec_listView;
    Button cancle, sure;
    RecAdapter recAdapter;
    List<CallLogMsg> rec;
    LinearLayout cors;
    TextView delall;
    public static Received_calls instance;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case XYConstant.REFRESH_CALL_LOGS:
                    Utils.getCallHistoryList(instance);
                    rec = Utils.getCallLogMsg(XYConstant.RECEIVED_CALLS);
                    recAdapter.refresh(rec);
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
        rec_listView = (ListView) findViewById(R.id.rec_lv_msg);
        cors = (LinearLayout) findViewById(R.id.CORS);
        cancle = (Button) findViewById(R.id.cancle);
        sure = (Button) findViewById(R.id.sure);
        View headView = View.inflate(instance, R.layout.del_all_headview, null);
        delall = (TextView) headView.findViewById(R.id.delAll_call_log);
        delall.setOnClickListener(this);
        cancle.setOnClickListener(this);
        sure.setOnClickListener(this);
        rec_listView.setOnItemClickListener(this);
        rec = Utils.getCallLogMsg(XYConstant.RECEIVED_CALLS);
        recAdapter = new RecAdapter(Received_calls.this, rec);
        rec_listView.setAdapter(recAdapter);
        rec_listView.addHeaderView(headView);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.cancle:
                recAdapter.isLongClick = false;
                cors.setVisibility(View.GONE);
                recAdapter.notifyDataSetChanged();
                break;
            case R.id.sure:

                break;
            case R.id.delAll_call_log:
                Utils.toast(instance, "已进入删除全部记录界面，请谨慎操作");
                ViewUtils.showDialog(instance, new Del() {
                    @Override
                    public void sure() {
                        for (int i = 0; i < rec.size(); i++) {
                            Utils.delCallLog(rec.get(i).num, rec.get(i).howTime);
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
        if (!recAdapter.isLongClick) {
            Intent intent = new Intent(Received_calls.this, CallMenuActivity.class);
            intent.putExtra("num", rec.get(position - 1).num);
            intent.putExtra("dataTime", rec.get(position - 1).howTime);
            intent.putExtra("whoHandler", "R");
            startActivity(intent);
        }
    }
}
