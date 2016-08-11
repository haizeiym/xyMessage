package com.ca.tongxunlu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ca.tongxunlu.BaseActivity;
import com.ca.tongxunlu.MyApplication;
import com.ca.tongxunlu.R;
import com.ca.tongxunlu.adapter.ContactAdapter;
import com.ca.tongxunlu.model.ContactModel;
import com.ca.tongxunlu.utils.Utils;

import java.util.ArrayList;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-19 下午 1:40
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: 添加联系人，显示联系人界面
 */
public class ShowContactUI extends BaseActivity {
    ListView autoListView;
    ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.contactxml);
        init();
        showListView(autoListView);
    }

    private void showListView(ListView autoListView) {
        adapter = new ContactAdapter(this, Utils.getPeopleInPhone(this));
        autoListView.setAdapter(adapter);
        setListent(Utils.getPeopleInPhone(this));
    }

    private void init() {
        autoListView = (ListView) findViewById(R.id.contact);
    }

    private void setListent(final ArrayList<ContactModel> list) {
        autoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShowContactUI.this, CallMenuActivity.class);
                intent.putExtra("num", list.get(position).num);
                startActivity(intent);
            }
        });
    }
}
