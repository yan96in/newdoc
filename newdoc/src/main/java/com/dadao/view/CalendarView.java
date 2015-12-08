package com.dadao.view;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.meluo.newdoc.R;

public class CalendarView extends RelativeLayout {

    private TextView titleText;
    private Button btnLeft;
    private Button btnRight;

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.moudle_title, this, true);
        titleText = (TextView) findViewById(R.id.title);
        btnLeft = (Button) findViewById(R.id.btn_left);
        btnRight = (Button) findViewById(R.id.btn_right);
    }

    public void setTitleText(String text) {
        titleText.setText(text);
    }

    public void enableLeftButton(Boolean isEnable) {
        if (isEnable) {
            btnLeft.setVisibility(View.VISIBLE);
        } else {
            btnLeft.setVisibility(View.GONE);
        }
    }

    public Button getLeftButton() {
        btnLeft.setVisibility(View.VISIBLE);
        return btnLeft;
    }

    public void enableRightButton(Boolean isEnable) {
        if (isEnable) {
            btnRight.setVisibility(View.VISIBLE);
        } else {
            btnRight.setVisibility(View.GONE);
        }
    }

    public void setRightButtonBg(Drawable d) {
        d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
        btnRight.setCompoundDrawables(null, null, d, null);
    }

    public Button getRightButton() {
        btnRight.setVisibility(View.VISIBLE);
        return btnRight;
    }

    public void setTypeface(Typeface tf) {

        titleText.setTypeface(tf);
    }

}
