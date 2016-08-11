package com.ca.tongxunlu.ui;

import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ca.tongxunlu.BaseActivity;
import com.ca.tongxunlu.MainActivity;
import com.ca.tongxunlu.MyApplication;
import com.ca.tongxunlu.R;
import com.ca.tongxunlu.adapter.ContactAdapter;
import com.ca.tongxunlu.contact.XYConstant;
import com.ca.tongxunlu.model.ContactModel;
import com.ca.tongxunlu.model.PinYinTurn;
import com.ca.tongxunlu.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-17 下午 8:17
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class SendMsgUI extends BaseActivity {
    EditText inputmsg;
    TextView msgContact, addPerson, sendmsg;
    EditText sendNumber;
    String number, content;
    //    PendingIntent sentPI;
    String name = "";
    SendMsgUI instance;
    List<String> sendNum = new ArrayList<>();
    List<ContactModel> listModel = new ArrayList<>();
    Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.sendmsgui);
        init();
    }

    private void init() {
        instance = this;
        sendmsg = (TextView) findViewById(R.id.sendMsg);
        sendmsg.setOnClickListener(this);
        msgContact = (TextView) findViewById(R.id.msgcontent);
        inputmsg = (EditText) findViewById(R.id.inputmsg);
        sendNumber = (EditText) findViewById(R.id.number);
        textChangeListener();
        sendNumber.setOnClickListener(this);
        addPerson = (TextView) findViewById(R.id.addPerson);
        addPerson.setOnClickListener(this);
        listModel = Utils.getPeopleInPhone(SendMsgUI.this);
        number = getIntent().getStringExtra("num");
        content = getIntent().getStringExtra("content");
        if (!TextUtils.isEmpty(number)) {
            if (!TextUtils.isEmpty(Utils.ContactNum(number, listModel))) {
                sendNumber.setText(Utils.ContactNum(number, listModel));
            } else {
                sendNumber.setText(number);
            }
        }
        if (!TextUtils.isEmpty(content)) {
            inputmsg.setText(content);
        }
        utils = new Utils(new Utils.UtilsInterface() {
            @Override
            public void setData() {
                Utils.toast(instance, "短信发送成功");
                inputmsg.setText(null);
                sendNumber.setText(null);
                msgContact.setText("当前手机号码为空,短信内容为空");
            }
        });
        instance.registerReceiver(utils.sendMessage, new IntentFilter(Utils.SENT_SMS_ACTION));
    }

    private void textChangeListener() {
        inputmsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    msgContact.setText(s.toString());
                } else {
                    msgContact.setText("短信内容为空");
                }
            }
        });
        sendNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    name = "";
                    if (!ContactAdapter.selectPinYinTurnList.isEmpty()) {
                        ContactAdapter.selectPinYinTurnList.clear();
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        content = inputmsg.getText().toString();
        String sendN = sendNumber.getText().toString().trim();
        switch (v.getId()) {
            case R.id.sendMsg:
                if (sendNum.size() > 0 && !TextUtils.isEmpty(content)) {
                    Utils.toast(SendMsgUI.this, "短信发送中");
                    for (int i = 0; i < sendNum.size(); i++) {
                        Utils.sendSMS(instance, sendNum.get(i), content);
                    }
                    sendNum.clear();
                    ContactAdapter.selectPinYinTurnList.clear();
                } else if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(sendN) && Utils.isNumeric(sendN)) {
                    Utils.toast(SendMsgUI.this, "短信发送中");
                    Utils.sendSMS(instance, sendN, content);
                } else if (!Utils.isNumeric(sendN)) {
                    Utils.toast(SendMsgUI.this, "请正确输入号码");
                } else {
                    Utils.toast(SendMsgUI.this, "请不要发送空信息");
                }
                break;
            case R.id.addPerson:
                DelOrAddContactUI.isdel = false;
                Utils.simpleIntent(SendMsgUI.this, DelOrAddContactUI.class);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContactAdapter.selectPinYinTurnList.size() > 0) {
            for (PinYinTurn pinYinTurn : ContactAdapter.selectPinYinTurnList) {
                name += pinYinTurn.name + ";";
                sendNum.add(pinYinTurn.num);
            }
            sendNumber.setText(name.substring(0, name.length() - 1));
            ContactAdapter.selectPinYinTurnList.clear();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String num = sendNumber.getText().toString().trim();
        String text = inputmsg.getText().toString();
        if (!TextUtils.isEmpty(num) || !TextUtils.isEmpty(text)) {
            Utils.insertMsg(instance, XYConstant.DRAFT_MSG, num, text);
            Utils.toast(instance, "已保存草稿箱中");
            MainActivity.instance.handler.sendEmptyMessage(XYConstant.NEW_MSG);
        }
        unregisterReceiver(utils.sendMessage);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
