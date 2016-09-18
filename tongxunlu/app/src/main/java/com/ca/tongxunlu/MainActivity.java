package com.ca.tongxunlu;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.ca.tongxunlu.contact.XYConstant;
import com.ca.tongxunlu.fragment.ContactFragment;
import com.ca.tongxunlu.fragment.DialFragment;
import com.ca.tongxunlu.fragment.MsgFragment;
import com.ca.tongxunlu.fragment.RecordFragment;
import com.ca.tongxunlu.service.OpenPhoneStart;
import com.ca.tongxunlu.utils.Utils;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    TextView dial, record, msg, contact;
    DialFragment dialFragment;
    ContactFragment contactFragment;
    MsgFragment msgFragment;
    RecordFragment recordFragment;
    public static MainActivity instance;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case XYConstant.NEW_MSG:
                    Utils.systemMsg(instance);
                    break;
            }
        }
    };

    @Override
    public void initData() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_main);
        instance = this;
        //启动服务，不让后台清除
        startService(new Intent(instance, OpenPhoneStart.class));
//        //权限申请
//        Utils.applyForPermission(instance);
        //系统短信是否为自己的应用
        Utils.isSystemAPK(instance);
        //初始化获取短信
        Utils.systemMsg(instance);
        //初始化获取通话记录
        Utils.getCallHistoryList(instance);
    }

    @Override
    public void initView() {
        dial = (TextView) findViewById(R.id.dial);
        record = (TextView) findViewById(R.id.record);
        msg = (TextView) findViewById(R.id.msg);
        contact = (TextView) findViewById(R.id.contact);
        dial.setOnClickListener(this);
        record.setOnClickListener(this);
        msg.setOnClickListener(this);
        contact.setOnClickListener(this);
        dialFragment = new DialFragment();
        contactFragment = new ContactFragment();
        msgFragment = new MsgFragment();
        recordFragment = new RecordFragment();
        setDefulFragment();
    }

    private void setDefulFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.page, dialFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void setOnClick(View v) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (v.getId()) {
            //fragment切换
            case R.id.dial:
                if (dialFragment == null) {
                    dialFragment = new DialFragment();
                }
                ft.replace(R.id.page, dialFragment);
                /*try {
                    String s = NumLocation.getMobileAddress(instance, "18503020740");
                    Log.e("sss", s);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                break;
            case R.id.record:
                if (recordFragment == null) {
                    recordFragment = new RecordFragment();
                }
                ft.replace(R.id.page, recordFragment);
                break;
            case R.id.msg:
                if (msgFragment == null) {
                    msgFragment = new MsgFragment();
                }
                ft.replace(R.id.page, msgFragment);
                break;
            case R.id.contact:
                if (contactFragment == null) {
                    contactFragment = new ContactFragment();
                }
                ft.replace(R.id.page, contactFragment);
                break;
        }
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //系统短信是否为自己的应用
        Utils.isSystemAPK(instance);
        //初始化获取短信
        Utils.systemMsg(instance);
        //初始化获取通话记录
        Utils.getCallHistoryList(instance);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //点击返回进入后台运行
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
