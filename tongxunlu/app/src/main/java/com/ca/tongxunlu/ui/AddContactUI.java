package com.ca.tongxunlu.ui;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ca.tongxunlu.BaseActivity;
import com.ca.tongxunlu.MyApplication;
import com.ca.tongxunlu.R;
import com.ca.tongxunlu.contact_manger.ADContacts;
import com.ca.tongxunlu.utils.Utils;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-19 下午 2:09
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class AddContactUI extends BaseActivity {
    EditText name, num;
    TextView content;
    Button sureSave;

    @Override
    public void initView() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.addcontactxml);
        name = (EditText) findViewById(R.id.name);
        num = (EditText) findViewById(R.id.num);
        content = (TextView) findViewById(R.id.contactContent);
        sureSave = (Button) findViewById(R.id.sureSave);
        sureSave.setOnClickListener(this);
    }

    @Override
    public void setListener() {
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nunber = num.getText().toString().trim();
                content.setText("姓名：" + s.toString() + "\n" + "号码：" + nunber);
                if (TextUtils.isEmpty(num.getText().toString().trim()) && TextUtils.isEmpty(s.toString())) {
                    content.setText("联系人内容为空");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nunber = name.getText().toString().trim();
                content.setText("姓名：" + nunber + "\n" + "号码：" + s.toString());
                if (TextUtils.isEmpty(name.getText().toString().trim()) && TextUtils.isEmpty(s.toString())) {
                    content.setText("联系人内容为空");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void setOnClick(View v) {
        super.setOnClick(v);
        switch (v.getId()) {
            case R.id.sureSave:
                if (!TextUtils.isEmpty(name.getText().toString().trim())) {
                    ADContacts.insert(AddContactUI.this, name.getText().toString().trim(), num.getText().toString().trim(), "");
                    Utils.toast(AddContactUI.this, "添加成功");
                    name.setText(null);
                    num.setText(null);
                    content.setText("联系人内容为空");
                } else {
                    Utils.toast(AddContactUI.this, "请输入姓名");
                }
                break;
        }

    }
}
