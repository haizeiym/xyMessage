package com.ca.tongxunlu.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ca.tongxunlu.BaseActivity;
import com.ca.tongxunlu.MyApplication;
import com.ca.tongxunlu.R;
import com.ca.tongxunlu.contact.XYConstant;
import com.ca.tongxunlu.service.OpenPhoneStart;
import com.ca.tongxunlu.i.Del;
import com.ca.tongxunlu.utils.SPutils;
import com.ca.tongxunlu.utils.Utils;
import com.ca.tongxunlu.utils.ViewUtils;

/**
 * Created by Administrator on 2016/7/8.
 * 设置界面
 */
public class SetingUI extends BaseActivity {
    SetingUI instance;
    TextView exit_system, answer_model_man, ping_model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = SetingUI.this;
        setContentView(R.layout.seting_ui);
        init();
    }

    private void init() {
        exit_system = (TextView) findViewById(R.id.exit_system);
        answer_model_man = (TextView) findViewById(R.id.answer_model_man);
        ping_model = (TextView) findViewById(R.id.ping_model);
        exit_system.setOnClickListener(this);
        answer_model_man.setOnClickListener(this);
        ping_model.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.exit_system:
                ViewUtils.showDialog(instance, new Del() {
                    @Override
                    public void sure() {
                        stopService(new Intent(instance, OpenPhoneStart.class));
                        MyApplication.getInstance().exit();
                    }

                    @Override
                    public void replaceName(TextView sure, TextView cancle) {

                    }

                    @Override
                    public void cancle() {

                    }
                });
                break;
            case R.id.answer_model_man:
                new SPutils(instance, XYConstant.ANSWER) {
                    @Override
                    public void getSaveData(SharedPreferences sharedPreferences) {
                        if (sharedPreferences.getBoolean("slide", false)) {
                            Utils.toast(instance, "当前模式为滑动接听");
                        } else {
                            Utils.toast(instance, "当前模式为点击接听");
                        }
                    }
                };

                ViewUtils.showDialog(instance, new Del() {
                    @Override
                    public void replaceName(TextView sure, TextView cancle) {
                        sure.setText("滑动接听模式");
                        cancle.setText("点击接听模式");
                    }

                    @Override
                    public void sure() {
                        new SPutils(instance, XYConstant.ANSWER) {
                            @Override
                            public void save(SharedPreferences.Editor editor) {
                                editor.putBoolean("slide", true);
                                editor.commit();
                                Utils.toast(instance, "滑动接听已选中");
                            }
                        };
                    }

                    @Override
                    public void cancle() {
                        new SPutils(instance, XYConstant.ANSWER) {
                            @Override
                            public void save(SharedPreferences.Editor editor) {
                                editor.putBoolean("slide", false);
                                editor.commit();
                                Utils.toast(instance, "点击接听已选中");
                            }
                        };
                    }
                });
                break;

            case R.id.ping_model:

                new SPutils(instance, XYConstant.UP_HOVER) {
                    @Override
                    public void getSaveData(SharedPreferences sharedPreferences) {
                        if (sharedPreferences.getBoolean("up_hover", false)) {
                            Utils.toast(instance, "当前模式抬手上屏");
                        } else {
                            Utils.toast(instance, "当前模式为双击上屏");
                        }
                    }
                };

                ViewUtils.showDialog(instance, new Del() {
                    @Override
                    public void replaceName(TextView sure, TextView cancle) {
                        sure.setText("抬手上屏");
                        cancle.setText("双击上屏");
                    }

                    @Override
                    public void sure() {
                        new SPutils(instance, XYConstant.UP_HOVER) {
                            @Override
                            public void save(SharedPreferences.Editor editor) {
                                editor.putBoolean("up_hover", true);
                                editor.commit();
                                Utils.toast(instance, "抬手上屏已选中");
                            }
                        };
                    }

                    @Override
                    public void cancle() {
                        new SPutils(instance, XYConstant.UP_HOVER) {
                            @Override
                            public void save(SharedPreferences.Editor editor) {
                                editor.putBoolean("up_hover", false);
                                editor.commit();
                                Utils.toast(instance, "双击上屏已选中");
                            }
                        };
                    }
                });
                break;
        }
    }
}
