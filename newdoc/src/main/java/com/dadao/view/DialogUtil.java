package com.dadao.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.meluo.newdoc.R;

public class DialogUtil {

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param msg
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.moudle_loading, null);// 得到加载view
        RelativeLayout layout = (RelativeLayout) v
                .findViewById(R.id.loading_wrap);// 加载布局

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));// 设置布局
        return loadingDialog;
    }

    public static PopupWindow createLoadingDialog2(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.moudle_loading, null);// 得到加载view
        RelativeLayout layout = (RelativeLayout) v
                .findViewById(R.id.loading_wrap);// 加载布局
        TextView tv = (TextView) layout.findViewById(R.id.ml_tv_loading);
        tv.setText(msg);
        PopupWindow popPosition = new PopupWindow(v, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        popPosition.setFocusable(true);
        popPosition.setBackgroundDrawable(new BitmapDrawable());
        popPosition.setAnimationStyle(R.style.AnimFade);
        return popPosition;
    }
}
