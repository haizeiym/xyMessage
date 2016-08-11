package com.ca.tongxunlu.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.ca.tongxunlu.R;
import com.ca.tongxunlu.TBaseAdapter;
import com.ca.tongxunlu.model.ContactModel;
import com.ca.tongxunlu.model.PinYinTurn;
import com.ca.tongxunlu.ui.CallMenuActivity;
import com.ca.tongxunlu.utils.CharacterParser;
import com.ca.tongxunlu.utils.PinyinComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-18 下午 9:23
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class ContactAdapter extends TBaseAdapter implements SectionIndexer {
    Context context;
    List<ContactModel> models;
    public boolean isLongClick = false;
    //    public boolean cleanBox;
    public List<PinYinTurn> pinYinTurnList = new ArrayList<>();
    public static Set<PinYinTurn> selectPinYinTurnList = new HashSet<>();
    // 根据拼音来排列ListView里面的数据类
    private PinyinComparator pinyinComparator;
    //汉字转拼音的类
    private CharacterParser characterParser;

    public ContactAdapter(Context context, List<ContactModel> models) {
        this.context = context;
        this.models = models;
        pinyinComparator = new PinyinComparator();
        characterParser = CharacterParser.getInstance();
        pinYinTurnList = filledChar(models);
        Collections.sort(pinYinTurnList, pinyinComparator);
    }

    @Override
    public int setCount() {
        return pinYinTurnList.size();
    }

    @Override
    public View setView(final int position, View convertView) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.contactitemxml, null);
            holder.ischeck = (CheckBox) convertView.findViewById(R.id.isCheck);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.num = (TextView) convertView.findViewById(R.id.num);
            holder.sor = (TextView) convertView.findViewById(R.id.frist_chart);
            holder.lastPage = (LinearLayout) convertView.findViewById(R.id.lastPage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.num.setText(pinYinTurnList.get(position).num);
        holder.name.setText(pinYinTurnList.get(position).name);
        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.sor.setVisibility(View.VISIBLE);
            holder.sor.setText(pinYinTurnList.get(position).sortLetters);
        } else {
            holder.sor.setVisibility(View.GONE);
        }
        if (isLongClick) {
            holder.ischeck.setVisibility(View.VISIBLE);
        } else {
            holder.ischeck.setVisibility(View.GONE);
            holder.lastPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CallMenuActivity.class);
                    intent.putExtra("num", pinYinTurnList.get(position).num);
                    intent.putExtra("contact", true);
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    private List<PinYinTurn> filledChar(List<ContactModel> models) {
        List<PinYinTurn> listC = new ArrayList<>();
        for (int i = 0; i < models.size(); i++) {
            String name = models.get(i).name;
            String num = models.get(i).num;
            //汉字转换成拼音
            String pinyin;
            pinyin = characterParser.getSelling(models.get(i).name);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            String sortLetter;
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortLetter = sortString.toUpperCase();
            } else {
                sortLetter = "#";
            }
            PinYinTurn pinYinTurn = new PinYinTurn(name, sortLetter, num);
            listC.add(pinYinTurn);
        }
        return listC;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < pinYinTurnList.size(); i++) {
            String sortStr = pinYinTurnList.get(i).sortLetters;
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return pinYinTurnList.get(position).sortLetters.charAt(0);
    }

    public void refresh(List<ContactModel> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        public TextView name, num, sor;
        public CheckBox ischeck;
        public LinearLayout lastPage;
    }
}
