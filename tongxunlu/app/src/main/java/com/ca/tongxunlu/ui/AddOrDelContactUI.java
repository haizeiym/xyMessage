package com.ca.tongxunlu.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ca.tongxunlu.BaseActivity;
import com.ca.tongxunlu.MyApplication;
import com.ca.tongxunlu.R;
import com.ca.tongxunlu.contact.XYConstant;
import com.ca.tongxunlu.fragment.ContactFragment;
import com.ca.tongxunlu.utils.Utils;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-19 下午 2:03
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class AddOrDelContactUI extends BaseActivity {
    TextView addContact, delContact, seting_ui;
    LinearLayout find_ll, find_in;
    AddOrDelContactUI instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.contactfragmentxml);
        init();
    }

    private void init() {
        instance = AddOrDelContactUI.this;
        addContact = (TextView) findViewById(R.id.contact);
        delContact = (TextView) findViewById(R.id.mancontact);
        find_ll = (LinearLayout) findViewById(R.id.find_ll);
        find_in = (LinearLayout) findViewById(R.id.find_in);
        seting_ui = (TextView) findViewById(R.id.seting_ui);
        find_ll.setVisibility(View.GONE);
        find_in.setVisibility(View.VISIBLE);
        seting_ui.setVisibility(View.VISIBLE);
        addContact.setOnClickListener(this);
        delContact.setOnClickListener(this);
        seting_ui.setOnClickListener(this);
        addContact.setText("添加联系人");
        delContact.setText("删除联系人");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.contact:
                Utils.simpleIntent(instance, AddContactUI.class);
                break;
            case R.id.mancontact:
                DelOrAddContactUI.isdel = true;
                Utils.simpleIntent(instance, DelOrAddContactUI.class);
                break;
            case R.id.seting_ui:
                Utils.simpleIntent(instance, SetingUI.class);
                break;
        }
    }
}
