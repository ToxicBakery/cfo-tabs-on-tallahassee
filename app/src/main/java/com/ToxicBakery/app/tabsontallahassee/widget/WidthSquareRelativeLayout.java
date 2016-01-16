package com.ToxicBakery.app.tabsontallahassee.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class WidthSquareRelativeLayout extends RelativeLayout {

    public WidthSquareRelativeLayout(Context context) {
        super(context);
    }

    public WidthSquareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WidthSquareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
