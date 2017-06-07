package com.heima.gooview;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 *
 */
public class BubbleLayout extends FrameLayout {
    public BubbleLayout(Context context) {
        super(context);
    }

    public BubbleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BubbleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    float x, y;

    public void setBubblePosition(PointF point) {
        x = point.x;
        y = point.y;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
        View view = getChildAt(0);
        int l = (int) (x - view.getMeasuredWidth() / 2);
        int t = (int) (y - view.getMeasuredHeight() / 2);

        view.layout(l, t, l + view.getMeasuredWidth(), t + view.getMeasuredHeight());
    }
}
