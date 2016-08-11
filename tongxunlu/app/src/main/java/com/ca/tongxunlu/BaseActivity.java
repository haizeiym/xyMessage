package com.ca.tongxunlu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-17 下午 12:02
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class BaseActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initData();
        initView();
        setListener();
    }

    //初始化数据
    public void initData() {
    }

    //初始化控件
    public void initView() {
    }

    // 设置控件监听
    public void setListener() {
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            finish();
        }
        setOnClick(v);
    }

    //控件点击事件
    public void setOnClick(View v) {

    }

}
