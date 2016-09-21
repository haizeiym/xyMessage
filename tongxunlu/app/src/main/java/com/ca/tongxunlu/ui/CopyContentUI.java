package com.ca.tongxunlu.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ca.tongxunlu.BaseActivity;
import com.ca.tongxunlu.R;
import com.ca.tongxunlu.utils.Utils;

/**
 * Created by Administrator on 2016/9/21.
 */

public class CopyContentUI extends BaseActivity {
    String content;

    @Override
    public void initView() {
        setContentView(R.layout.copy_content);
        EditText editText = (EditText) findViewById(R.id.copy_content);
        TextView copyAll = (TextView) findViewById(R.id.copy_all);
        content = getIntent().getStringExtra("content");
        if (!content.isEmpty()) {
            editText.setText(content);
            copyAll.setOnClickListener(this);
        }
    }

    @Override
    public void setOnClick(View v) {
        switch (v.getId()) {
            case R.id.copy_all:
                if (!TextUtils.isEmpty(content)) {
                    Utils.copy(content);
                    Utils.toast(CopyContentUI.this, "复制内容完成");
                } else {
                    Utils.toast(CopyContentUI.this, "内容为空");
                }
                break;
        }
    }
}
