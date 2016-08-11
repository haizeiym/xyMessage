package com.ca.tongxunlu.model;

import java.io.Serializable;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-18 下午 9:25
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class ContactModel implements Serializable {
    public String name;
    public String num;
    public boolean isCheck;

    public ContactModel(String name, String num, boolean isCheck) {
        this.name = name;
        this.num = num;
        this.isCheck = isCheck;
    }
}
