package com.ca.tongxunlu.utils;

import com.ca.tongxunlu.model.PinYinTurn;

import java.util.Comparator;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-10 下午 3:31
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class PinyinComparator implements Comparator<PinYinTurn> {

    @Override
    public int compare(PinYinTurn o1, PinYinTurn o2) {
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
        if (o2.sortLetters.equals("#")) {
            return -1;
        } else if (o1.sortLetters.equals("#")) {
            return 1;
        } else {
            return o1.sortLetters.compareTo(o2.sortLetters);
        }
    }

}
