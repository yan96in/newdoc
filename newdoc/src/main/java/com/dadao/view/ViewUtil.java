package com.dadao.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class ViewUtil {

    // 软键盘是否弹出
    public static boolean isKeyBoardOn(Activity context) {

        View activityRootView = ((ViewGroup) context
                .findViewById(android.R.id.content)).getChildAt(0);

        int heightDiff = activityRootView.getRootView().getHeight()
                - activityRootView.getHeight();
        if (heightDiff > 100) {
            // 大小超过100时，一般为显示虚拟键盘事件
            return true;

        } else {
            // 大小小于100时，为不显示虚拟键盘或虚拟键盘隐藏
            return false;
        }
    }

    public static void applyBlur(final Context context, final View view) {
        view.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        view.getViewTreeObserver()
                                .removeOnPreDrawListener(this);
                        view.buildDrawingCache();
                        Bitmap bmp = view.getDrawingCache();
                        blur(context, bmp, view);
                        return true;
                    }
                });
    }

    @SuppressLint("NewApi")
    public static void blur(Context context, Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float radius = 20;

        Bitmap overlay = Bitmap.createBitmap((view.getMeasuredWidth()),
                (view.getMeasuredHeight()), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft(), -view.getTop());
        canvas.drawBitmap(bkg, 0, 0, null);

        RenderScript rs = RenderScript.create(context);

        Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs,
                overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(radius);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(overlay);
        view.setBackground(new BitmapDrawable(context.getResources(), overlay));
        rs.destroy();
        Log.d("", "cost " + (System.currentTimeMillis() - startMs) + "ms");
    }

}
