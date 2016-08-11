package com.ca.tongxunlu.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ca.tongxunlu.R;
import com.ca.tongxunlu.i.Del;

/**
 * Created by Administrator on 2016/6/12.
 */
public class ViewUtils {
    //刪除
    public static void showDialog(final Context context, final Del del) {
        final Dialog alertDialog = new Dialog(context);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.delemsg);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(null);
        TextView sure = (TextView) window.findViewById(R.id.sure_msg);
        TextView cancle = (TextView) window.findViewById(R.id.cancle_msg);
        if (del != null) {
            del.replaceName(sure, cancle);
        }
        
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (del != null) {
                    alertDialog.dismiss();
                    del.sure();
                }
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (del != null) {
                    alertDialog.dismiss();
                    del.cancle();
                }
            }
        });
    }
}
