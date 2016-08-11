package com.ca.tongxunlu.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ca.tongxunlu.R;
import com.ca.tongxunlu.contact.XYConstant;
import com.ca.tongxunlu.ui.AcceptCallUI;
import com.ca.tongxunlu.utils.SPutils;
import com.ca.tongxunlu.utils.Utils;

/**
 * Created by Administrator on 2016/6/13.
 * 滑动接听
 */
public class CallShowView extends Service implements View.OnClickListener {
    public static Context context = null;
    public static String phoneNumBerStr = null;
    public static Intent intent = null;
    //定义浮动窗口布局
    private LinearLayout mFloatLayout;
    //创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams wmParams;
    private TextView fromNum, call_cancle;
    LinearLayout call_status;
    //电话接听模式
    LinearLayout call_answer_model_one, call_answer_model_two;
    //按键接听电话
    TextView answer_one, cancle_two;
    GestureDetector gestureDetector;
    //扬声器切换
    TextView phoneSoundTurn;
    //是否打开扬声器
    boolean isTurn = true;

    GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            float x = e2.getX() - e1.getX();

            if (x > 399) {
                Utils.answerCall(context);
                call_cancle.setVisibility(View.VISIBLE);
                call_status.setVisibility(View.GONE);
            } else if (x < 0 && Math.abs(x) > 399) {
                Utils.endCall(context);
                stopService(new Intent(context, CallShowView.class));
            }
            return true;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        if (!TextUtils.isEmpty(phoneNumBerStr) && context != null) {
            createFloatView();
        }
    }

    private void createFloatView() {
        init();
        method();
    }

    private void method() {
        new SPutils(context, XYConstant.ANSWER) {
            @Override
            public void getSaveData(SharedPreferences sharedPreferences) {
                boolean isSlide = sharedPreferences.getBoolean("slide", true);
                if (isSlide) {
                    call_answer_model_one.setVisibility(View.VISIBLE);
                    call_answer_model_two.setVisibility(View.GONE);
                } else {
                    call_answer_model_one.setVisibility(View.GONE);
                    call_answer_model_two.setVisibility(View.VISIBLE);
                }
            }
        };

        call_status.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    private void init() {
        gestureDetector = new GestureDetector(context, onGestureListener);
        wmParams = new WindowManager.LayoutParams();
        //获取WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        //设置window type[优先级]
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;//窗口的type类型决定了它的优先级，优先级越高显示越在顶层
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为顶部水平居中
        wmParams.gravity = Gravity.CENTER;
        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 0;
        wmParams.y = 0;
        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.show, null);
        call_answer_model_one = (LinearLayout) mFloatLayout.findViewById(R.id.answer_model_one);
        call_answer_model_two = (LinearLayout) mFloatLayout.findViewById(R.id.answer_model_two);
        answer_one = (TextView) mFloatLayout.findViewById(R.id.call_answer_one);
        cancle_two = (TextView) mFloatLayout.findViewById(R.id.call_cancle_two);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        fromNum = (TextView) mFloatLayout.findViewById(R.id.fromNum);
        call_cancle = (TextView) mFloatLayout.findViewById(R.id.call_cancle);
        call_status = (LinearLayout) mFloatLayout.findViewById(R.id.call_status);
        phoneSoundTurn = (TextView) mFloatLayout.findViewById(R.id.phoneSoundTurn);
        call_cancle.setOnClickListener(this);
        answer_one.setOnClickListener(this);
        cancle_two.setOnClickListener(this);
        phoneSoundTurn.setOnClickListener(this);
        fromNum.setText("来电号码：" + phoneNumBerStr);
        Utils.toast(context, phoneNumBerStr);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.call_cancle:
                Utils.endCall(context);
                stopService(new Intent(v.getContext(), CallShowView.class));
                call_cancle.setVisibility(View.GONE);
                call_status.setVisibility(View.VISIBLE);
                break;
            case R.id.call_answer_one:
                Utils.answerCall(context);
                break;
            case R.id.call_cancle_two:
                Utils.endCall(context);
                stopService(new Intent(v.getContext(), CallShowView.class));
                break;
            case R.id.phoneSoundTurn:
                if (AcceptCallUI.instance == null) {
                    phoneSoundTurn.setText("请先接听电话");
                    return;
                }
                if (isTurn) {
                    isTurn = false;
                    phoneSoundTurn.setText("扬声器已开启");
                } else {
                    isTurn = true;
                    phoneSoundTurn.setText("扬声器未开启");
                }
                AcceptCallUI.instance.handler.sendEmptyMessage(XYConstant.OUT_SPEACK);
                break;
        }
    }

    //onDestroy()关闭悬浮窗[当该service停止的时候也执行该方法]
    @Override
    public void onDestroy()//关闭悬浮窗
    {
        super.onDestroy();
        if (mFloatLayout != null) {
            mWindowManager.removeView(mFloatLayout);
        }
    }
}
