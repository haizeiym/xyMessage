package com.ca.tongxunlu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ca.tongxunlu.BaseActivity;
import com.ca.tongxunlu.MyApplication;
import com.ca.tongxunlu.R;
import com.ca.tongxunlu.contact.XYConstant;
import com.ca.tongxunlu.i.Del;
import com.ca.tongxunlu.utils.Utils;
import com.ca.tongxunlu.utils.ViewUtils;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-14 下午 12:19
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: 电话，信息页面
 */
public class CallMenuActivity extends BaseActivity {
    TextView call, send, copy_num, del_call_logs;
    String number, dataTime, whoHandler;
    boolean contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.menuxml);
        init();
    }

    private void init() {
        call = (TextView) findViewById(R.id.callPhone);
        send = (TextView) findViewById(R.id.sendMsg);
        copy_num = (TextView) findViewById(R.id.copy_number);
        del_call_logs = (TextView) findViewById(R.id.del_call_logs);
        call.setOnClickListener(this);
        send.setOnClickListener(this);
        del_call_logs.setOnClickListener(this);
        copy_num.setOnClickListener(this);
        number = getIntent().getStringExtra("num");
        dataTime = getIntent().getStringExtra("dataTime");

        whoHandler = getIntent().getStringExtra("whoHandler");
        contact = getIntent().getBooleanExtra("contact", false);
        if (contact) {
            del_call_logs.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.callPhone:
                Utils.callPhone(this, number);
                break;
            case R.id.sendMsg:
                Intent intent = new Intent(CallMenuActivity.this, SendMsgUI.class);
                intent.putExtra("num", number);
                startActivity(intent);
                break;
            case R.id.copy_number:
                Utils.copy(number);
                Utils.toast(CallMenuActivity.this, "复制完成");
                break;
            case R.id.del_call_logs:
                //删除记录
                ViewUtils.showDialog(CallMenuActivity.this, new Del() {

                    @Override
                    public void replaceName(TextView sure, TextView cancle) {

                    }

                    @Override
                    public void sure() {
                        Utils.delCallLog(number, dataTime);
                        if (whoHandler.equals("D")) {
                            Dialed_calls.instance.handler.sendEmptyMessage(XYConstant.REFRESH_CALL_LOGS);
                        } else if (whoHandler.equals("R")) {
                            Received_calls.instance.handler.sendEmptyMessage(XYConstant.REFRESH_CALL_LOGS);
                        } else if (whoHandler.equals("M")) {
                            Missed_calls.instance.handler.sendEmptyMessage(XYConstant.REFRESH_CALL_LOGS);
                        }
                        finish();
                    }

                    @Override
                    public void cancle() {

                    }

                });
                break;
        }
    }
}
