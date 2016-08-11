package com.ca.tongxunlu.model;

import java.io.Serializable;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-13 下午 2:46
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class CallLogMsg implements Serializable {
    public String name;
    public String num;
    public String howTime;
    public String callTime;
    public String type;
    public String msgcontent;

    public CallLogMsg() {

    }
}
