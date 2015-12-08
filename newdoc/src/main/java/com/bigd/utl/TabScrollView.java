package com.bigd.utl;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class TabScrollView extends ScrollView {
    private OnScrollListener onScrollListener;

    public TabScrollView(Context context) {
        this(context, null);
    }

    public TabScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null) {
            onScrollListener.onScroll(t);
        }
    }

    public interface OnScrollListener {

        public void onScroll(int scrollY);
    }

}
