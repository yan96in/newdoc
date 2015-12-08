package com.meluo.newdoc.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.meluo.newdoc.R;

public class EditText extends RelativeLayout {

    private TextView et;
    private TextView lineTop;
    private TextView lineBottom;

    public final int LINE_TOP = 0;
    public final int LINE_NONE = 1;
    public final int LINE_BOTTOM = 2;

    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.moudle_edit_text, this,
                true);
        et = (TextView) findViewById(R.id.met_et);
        lineTop = (Button) findViewById(R.id.line0);
        lineBottom = (Button) findViewById(R.id.line1);
    }

    private void setDLeft(int id) {
        Drawable dLeft;
        dLeft = getResources().getDrawable(id);
        dLeft.setBounds(0, 0, dLeft.getMinimumWidth(), dLeft.getMinimumHeight());

        et.setCompoundDrawables(dLeft, null, null, null);

    }

    private void setHit(String hit) {
        // et.setHit(hit);
    }

    private void setLine(int type) {
        switch (type) {
            case LINE_TOP:
                lineTop.setVisibility(View.VISIBLE);
                lineBottom.setVisibility(View.GONE);
                break;
            case LINE_NONE:
                lineTop.setVisibility(View.GONE);
                lineBottom.setVisibility(View.GONE);
                break;
            case LINE_BOTTOM:
                lineTop.setVisibility(View.GONE);
                lineBottom.setVisibility(View.VISIBLE);
                break;

        }
    }

}
