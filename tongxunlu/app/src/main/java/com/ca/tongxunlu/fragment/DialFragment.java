package com.ca.tongxunlu.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.ca.tongxunlu.R;
import com.ca.tongxunlu.adapter.Dialog_itemAdapter;
import com.ca.tongxunlu.contact.XYConstant;
import com.ca.tongxunlu.ui.MeauUI;
import com.ca.tongxunlu.utils.SPutils;
import com.ca.tongxunlu.utils.Utils;

import java.text.DecimalFormat;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-11 上午 11:53
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class DialFragment extends Fragment {
    TextView nowNum, delNum;
    final String NOW_EMPYT_NUM = "当前号码为：空";
    final String NOW_NOT_EMPTY_NUM = "当前号码为：";
    final String TRUE_INPUT_NUM = "请正确输入号码";
    final String EMPTY = "空";
    final String NUM_IS_EMPTY = "号码已清空";
    final String NOW_DELE = "已删除：";
    public static String numSum = "";
    GridView gridView;
    Dialog_itemAdapter adapter;
    double pxy, allX, allY, x, y;
    boolean isHover;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.dailxml, null);
        initView(view);
        itemClickListener();
        listenerType();
//        test();
        delNum();
        return view;
    }

    private void initView(View view) {
        nowNum = (TextView) view.findViewById(R.id.nowNum);
        delNum = (TextView) view.findViewById(R.id.delNum);
        gridView = (GridView) view.findViewById(R.id.dialog_gridView);
        adapter = new Dialog_itemAdapter(getActivity());
        gridView.setAdapter(adapter);
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        DecimalFormat df = new DecimalFormat("#.##");
        pxy = Double.parseDouble(df.format((double) displayMetrics.widthPixels / 1080));
        allX = pxy * 1079;
        allY = pxy * 1099;
        x = allX / 3;
        y = allY / 5;
        isHover = isHover();
    }

    //监听类型
    private void listenerType() {
        if (isHover) {
            gridView.setHovered(true);
            hoverListener();
        } else {
            gridView.setHovered(false);
            closeHover();
        }
    }

    //gridViewItem监听
    private void itemClickListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == Utils.dialog_item.length - 1) {
                    toMeau();
                } else if ((position == Utils.dialog_item.length - 2) || (position == Utils.dialog_item.length - 3)) {
                    callPhone();
                } else {
                    if (!isHover) {
                        nowNum(Utils.dialog_item[position]);
                    }
                }
            }
        });
    }

    //gridView抬手上屏监听
    private void hoverListener() {
        gridView.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                    double upX = event.getX();
                    double upY = event.getY();
                    if (!whatNum(upX, upY).equals(EMPTY)) {
                        nowNum(whatNum(upX, upY));
                        Utils.toast(getActivity(), whatNum(upX, upY));
                    }
                }
                return false;
            }
        });
    }

    //gridView关闭抬手上屏监听
    private void closeHover() {
        gridView.setOnHoverListener(null);
    }

    private void delNum() {
        delNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                deleNum();
            }
        });
        delNum.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                nowNum(EMPTY);
                Utils.toast(getActivity(), NUM_IS_EMPTY);
                return false;
            }
        });
    }

    //坐标位置点相对于1080
    private String whatNum(double upX, double upY) {
        String n = "";
        if ((upX > 0 && upX <= x) && (upY > 0 && upY <= y)) {
            n = "1";
        } else if ((upX > x && upX <= 2 * x) && (upY > 0 && upY <= y)) {
            n = "2";
        } else if ((upX > 2 * x && upX <= 3 * x) && (upY > 0 && upY <= y)) {
            n = "3";
        } else if ((upX > 0 && upX <= x) && (upY > y && upY <= 2 * y)) {
            n = "4";
        } else if ((upX > x && upX <= 2 * x) && (upY > y && upY <= 2 * y)) {
            n = "5";
        } else if ((upX > 2 * x && upX <= 3 * x) && (upY > y && upY <= 2 * y)) {
            n = "6";
        } else if ((upX > 0 && upX <= x) && (upY > 2 * y && upY <= 3 * y)) {
            n = "7";
        } else if ((upX > x && upX <= 2 * x) && (upY > 2 * y && upY <= 3 * y)) {
            n = "8";
        } else if ((upX > 2 * x && upX <= 3 * x) && (upY > 2 * y && upY <= 3 * y)) {
            n = "9";
        } else if ((upX > 0 && upX <= x) && (upY > 3 * y && upY <= 4 * y)) {
            n = "*";
        } else if ((upX > x && upX <= 2 * x) && (upY > 3 * y && upY <= 4 * y)) {
            n = "0";
        } else if ((upX > 2 * x && upX <= 3 * x) && (upY > 3 * y && upY <= 4 * y)) {
            n = "#";
        } else if (((upX > 0 && upX <= x) && (upY > 4 * y && upY <= 5 * y)) ||
                ((upX > x && upX <= 2 * x) && (upY > 4 * y && upY <= 5 * y)) ||
                ((upX > 2 * x && upX <= 3 * x) && (upY > 4 * y && upY <= 5 * y))) {
            n = EMPTY;
        }
        return n;
    }

    //跳转到菜单
    private void toMeau() {
        Utils.simpleIntent(getActivity(), MeauUI.class);
    }

    //拨打
    private void callPhone() {
        //判断号码是否正确
        if (TextUtils.isEmpty(numSum)) {
            Utils.toast(getActivity(), TRUE_INPUT_NUM);
        } else {
            Utils.callPhone(getActivity(), numSum);
        }
    }

    //删除
    private void deleNum() {
        if (!TextUtils.isEmpty(numSum)) {
            Utils.toast(getActivity(), NOW_DELE + numSum.substring(numSum.length() - 1, numSum.length()));
            numSum = numSum.substring(0, numSum.length() - 1);
            if (numSum.isEmpty()) {
                nowNum.setText(NOW_EMPYT_NUM);
            } else {
                nowNum.setText(NOW_NOT_EMPTY_NUM + numSum);
            }
        } else {
            Utils.toast(getActivity(), NOW_EMPYT_NUM);
        }
    }

    //当前号码
    private void nowNum(String num) {
        if (num.equals(EMPTY)) {
            numSum = "";
            nowNum.setText(NOW_EMPYT_NUM);
        } else {
            numSum += num;
            nowNum.setText(NOW_NOT_EMPTY_NUM + numSum);
        }
    }

    //是否为抬手上屏
    private boolean isHover() {
        final boolean[] isHover = new boolean[1];
        new SPutils(getActivity(), XYConstant.UP_HOVER) {
            @Override
            public void getSaveData(SharedPreferences sharedPreferences) {
                isHover[0] = sharedPreferences.getBoolean("up_hover", true);
            }
        };
        return isHover[0];
    }

    @Override
    public void onResume() {
        super.onResume();
        isHover = isHover();
        listenerType();
        if (XYConstant.isCtrl_v) {
            if (Utils.isCtrl_v()) {
                nowNum.setText(NOW_NOT_EMPTY_NUM + Utils.getctrl_v());
                numSum = Utils.getctrl_v();
            }
            XYConstant.isCtrl_v = false;
        } else {
            if (!TextUtils.isEmpty(numSum)) {
                nowNum.setText(NOW_NOT_EMPTY_NUM + numSum);
            } else {
                nowNum.setText(NOW_EMPYT_NUM);
            }
        }
    }

    //没有读屏软件测试用针对不同分辨率，相对于1080
    /*private void test() {
        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    double upX = (double) event.getX();
                    double upY = (double) event.getY();
                    nowNum(whatNum(upX, upY));
                }
                return false;
            }
        });
    }*/
}
