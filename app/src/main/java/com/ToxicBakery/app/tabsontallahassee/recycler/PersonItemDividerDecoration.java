package com.ToxicBakery.app.tabsontallahassee.recycler;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

public class PersonItemDividerDecoration extends ItemDecoration {

    final Paint paint;

    public PersonItemDividerDecoration(int color, float width) {
        paint = new Paint();
        paint.setStyle(Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(width);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, State state) {
        final int childCount = parent.getChildCount();
        final LayoutManager layoutManager = parent.getLayoutManager();
        final float halfStroke = paint.getStrokeWidth() / 2f;

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int left = layoutManager.getDecoratedLeft(child);
            int right = layoutManager.getDecoratedRight(child);
            int top = layoutManager.getDecoratedTop(child);
            int bottom = layoutManager.getDecoratedBottom(child);

            canvas.drawRect(
                    left + halfStroke,
                    top + halfStroke,
                    right - halfStroke,
                    bottom - halfStroke,
                    paint
            );
        }
    }

}
