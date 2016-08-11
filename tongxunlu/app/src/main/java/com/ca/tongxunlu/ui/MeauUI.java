package com.ca.tongxunlu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ca.tongxunlu.BaseActivity;
import com.ca.tongxunlu.MainActivity;
import com.ca.tongxunlu.MyApplication;
import com.ca.tongxunlu.R;
import com.ca.tongxunlu.contact.XYConstant;
import com.ca.tongxunlu.fragment.DialFragment;
import com.ca.tongxunlu.msgLog.ReadMsg;
import com.ca.tongxunlu.i.Del;
import com.ca.tongxunlu.utils.Utils;
import com.ca.tongxunlu.utils.ViewUtils;

/**
 * Description    : TODO
 * Author         : 李月
 * Version        : 1.2
 * Create Date    : 2016-05-21 下午 1:43
 * Update Desc    : TODO
 * Update Author  : 李月
 * Update Version : 1.2
 * Update Date    : 2016-05-21 下午 1:43
 */
public class MeauUI extends BaseActivity {
    TextView ctrl_num, ctrl_content, ctrl_v, callback, turnperson, delMsg;
    //手机号码，短信内容，短信时间
    String num, content, dateTime, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.meauui);
        init();
    }

    private void init() {
        ctrl_content = (TextView) findViewById(R.id.ctrl_content);
        ctrl_num = (TextView) findViewById(R.id.ctrl_num);
        ctrl_v = (TextView) findViewById(R.id.ctrl_v);
        callback = (TextView) findViewById(R.id.callback);
        delMsg = (TextView) findViewById(R.id.delMsg);
        turnperson = (TextView) findViewById(R.id.turnperson);
        num = getIntent().getStringExtra("num");
        content = getIntent().getStringExtra("content");
        dateTime = getIntent().getStringExtra("dateTime");
        type = getIntent().getStringExtra("type");
        if (!TextUtils.isEmpty(num)) {
            ctrl_v.setVisibility(View.GONE);
            ctrl_content.setVisibility(View.VISIBLE);
            callback.setVisibility(View.VISIBLE);
            turnperson.setVisibility(View.VISIBLE);
            delMsg.setVisibility(View.VISIBLE);
        } else {
            ctrl_content.setVisibility(View.GONE);
            callback.setVisibility(View.GONE);
            turnperson.setVisibility(View.GONE);
            delMsg.setVisibility(View.GONE);
        }
        ctrl_content.setOnClickListener(this);
        ctrl_num.setOnClickListener(this);
        ctrl_v.setOnClickListener(this);
        callback.setOnClickListener(this);
        turnperson.setOnClickListener(this);
        delMsg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ctrl_num:
                if (TextUtils.isEmpty(num) && !TextUtils.isEmpty(DialFragment.numSum)) {
                    Utils.copy(DialFragment.numSum);
                    Utils.toast(MeauUI.this, "复制号码完成");
                } else if (!TextUtils.isEmpty(num)) {
                    Utils.copy(num);
                    Utils.toast(MeauUI.this, "复制号码完成");
                } else {
                    Utils.toast(MeauUI.this, "请输入号码");
                }
                finish();
                break;
            case R.id.ctrl_content:
                if (!TextUtils.isEmpty(content)) {
                    Utils.copy(content);
                    Utils.toast(MeauUI.this, "复制内容完成");
                } else {
                    Utils.toast(MeauUI.this, "内容为空");
                }
                break;
            case R.id.ctrl_v:
                if (Utils.isCtrl_v()) {
                    XYConstant.isCtrl_v = true;
                    Utils.toast(MeauUI.this, "已粘贴完成");
                } else {
                    Utils.toast(MeauUI.this, "暂无粘贴内容");
                }
                finish();
                Utils.toast(MeauUI.this, "已返回拨号页面");
                break;
            case R.id.callback:
                intent.putExtra("num", num);
                intent.setClass(MeauUI.this, SendMsgUI.class);
                startActivity(intent);
                break;
            case R.id.turnperson:
                intent.putExtra("content", content);
                intent.setClass(MeauUI.this, SendMsgUI.class);
                startActivity(intent);
                break;
            case R.id.delMsg:
                ViewUtils.showDialog(MeauUI.this, new Del() {
                    @Override
                    public void sure() {
                        Utils.deleteSMS(MainActivity.instance, ReadMsg.SMS_URI_ALL, num, dateTime, false);
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

}
