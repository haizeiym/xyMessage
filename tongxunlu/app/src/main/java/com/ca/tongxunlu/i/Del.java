package com.ca.tongxunlu.i;

import android.widget.TextView;

/**
 * Created by Administrator on 2016/6/12.
 */
public interface Del {
    /**
     * 修改名称
     */
    void replaceName(TextView sure, TextView cancle);

    /**
     * 确认删除
     */
    void sure();

    /**
     * 取消之后的操作
     */
    void cancle();


}
