package com.ca.tongxunlu.model;

import java.io.Serializable;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-10 下午 5:34
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class PinYinTurn implements Serializable {
    public String name;   //名字
    public String sortLetters;  //显示数据拼音的首字母
    public String num;//号码

    public PinYinTurn(String name, String num) {
        this.name = name;
        this.num = num;
    }

    public PinYinTurn(String name, String sortLetters, String num) {
        this.name = name;
        this.sortLetters = sortLetters;
        this.num = num;
    }
}
