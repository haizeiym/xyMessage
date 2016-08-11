package com.ca.tongxunlu.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ca.tongxunlu.BaseActivity;
import com.ca.tongxunlu.MyApplication;
import com.ca.tongxunlu.R;
import com.ca.tongxunlu.adapter.ContactAdapter;
import com.ca.tongxunlu.contact_manger.ADContacts;
import com.ca.tongxunlu.model.ContactModel;
import com.ca.tongxunlu.model.PinYinTurn;
import com.ca.tongxunlu.utils.Utils;
import com.ca.tongxunlu.view.AutoListView;

import java.util.List;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-19 下午 1:40
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class DelOrAddContactUI extends BaseActivity {
    AutoListView autoListView;
    ContactAdapter adapter;
    Button sure, cancle;
    LinearLayout ll_del;
    List<ContactModel> list;
    public static boolean isdel = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.contactxml);
        init();
        showListView(autoListView);
    }

    private void init() {
        autoListView = (AutoListView) findViewById(R.id.contact_autionListView);
        sure = (Button) findViewById(R.id.sure);
        cancle = (Button) findViewById(R.id.canle);
        ll_del = (LinearLayout) findViewById(R.id.ll_del);
        ll_del.setVisibility(View.VISIBLE);
        sure.setOnClickListener(this);
        cancle.setOnClickListener(this);
        list = Utils.getPeopleInPhone(DelOrAddContactUI.this);
    }

    private void showListView(AutoListView autoListView) {
        adapter = new ContactAdapter(this, list);
        adapter.isLongClick = true;
        autoListView.setAdapter(adapter);
        autoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactAdapter.ViewHolder viewHolder = (ContactAdapter.ViewHolder) view.getTag();
                viewHolder.ischeck.toggle();
                if (!adapter.pinYinTurnList.isEmpty() && viewHolder.ischeck.isChecked()) {
                    String name = adapter.pinYinTurnList.get(position).name;
                    String number = adapter.pinYinTurnList.get(position).num;
                    ContactAdapter.selectPinYinTurnList.add(new PinYinTurn(name, number));
                } else {
                    if (adapter.pinYinTurnList.size() > 0) {
                        for (PinYinTurn pinYinTurn : ContactAdapter.selectPinYinTurnList) {
                            if (adapter.pinYinTurnList.get(position).num.equals(pinYinTurn.num)) {
                                ContactAdapter.selectPinYinTurnList.remove(pinYinTurn);
                                break;
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.sure:
                if (isdel && adapter != null && ContactAdapter.selectPinYinTurnList.size() > 0) {
                    for (PinYinTurn pinYinTurn : ContactAdapter.selectPinYinTurnList) {
                        ADContacts.deleteContact(DelOrAddContactUI.this, pinYinTurn.name);
                    }
                    Utils.toast(DelOrAddContactUI.this, "联系人已删除");
                    ContactAdapter.selectPinYinTurnList.clear();
                    finish();
                    Utils.toast(DelOrAddContactUI.this, "联系人管理界面已关闭");
                } else if (!isdel && ContactAdapter.selectPinYinTurnList.size() > 0) {
                    Utils.toast(DelOrAddContactUI.this, "联系人已选择");
                    finish();
                } else {
                    Utils.toast(DelOrAddContactUI.this, "请选择联系人");
                }
                break;
            case R.id.canle:
                ContactAdapter.selectPinYinTurnList.clear();
                finish();
                Utils.toast(DelOrAddContactUI.this, "页面已退出");
                break;
        }
    }
}
