package com.heima.shichi;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/3/23.
 */

public class MyListView extends ListView{
    private ImageView iv_child;
    private int orignalHeight;
    private int srcHeight;

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //重写listview的overscrollby方法

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if (deltaY < 0 && isTouchEvent) {
            //才可以下拉,当前的新的高度
            int newHeight = iv_child.getHeight() + Math.abs(deltaY);
            if (newHeight < srcHeight) {//如果当前的高度小于原图的高度
                iv_child.getLayoutParams().height = newHeight;
                //重新设置布局
                iv_child.requestLayout();

            }
        }

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }
    //传递图片
    public void setParallaxImage(ImageView iv_child) {

        this.iv_child = iv_child;
        //得到当前测量的图片的高度
        orignalHeight = iv_child.getMeasuredHeight();
        //得到图片原始的高度
        srcHeight = iv_child.getDrawable().getIntrinsicHeight();
    }

    //c重写ontouch方法

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //当手抬起的时候，让图片弹回原来的高度
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
            //先得到当前图片的高度
                int currentHeight = iv_child.getHeight();
                //让图片弹回去设置一个属性动画，设置当前动画的
                ValueAnimator animator = ValueAnimator.ofInt(currentHeight,orignalHeight);
                //更新动画d的监听器
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        //获得动画的比例值
                        float fraction = animation.getAnimatedFraction();
                        //获得中间的比例值
                        Integer animatedValue = (Integer) animation.getAnimatedValue();
                        //重新设置布局
                        iv_child.getLayoutParams().height = animatedValue;
                        iv_child.requestLayout();
                    }
                });
                //设置差值器
                animator.setInterpolator(new OvershootInterpolator(3));
                animator.setDuration(500);
                //开启动画
                animator.start();
                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
}
