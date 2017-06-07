package com.heima.gooviewtest;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/3/23.
 */
//自定义触摸事件实现ontouch事件，如果一个控件ontoch事件返回true，并且ontouchevent返回true，则交给ontocuh事件处理
public class OnGooViewTouchListener implements View.OnTouchListener, GooView.OnGooViewChangeListener {
    private TextView tv_unread_msg_count;
    private WindowManager manager;
   private final GooView gooView;
    private final WindowManager.LayoutParams params;

    public OnGooViewTouchListener(Context context) {
        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        gooView = new GooView(context);
        gooView.setOnGooViewChangeListener(this);
        params = new WindowManager.LayoutParams();
        //宽度和高度是
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.format = PixelFormat.TRANSLUCENT;//类型是透明
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //先让根部局线性布局请求不要拦截事件
        v.getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //让TextView消失
                tv_unread_msg_count = (TextView) v;
                tv_unread_msg_count.setVisibility(View.INVISIBLE);
                float rawX = event.getRawX();
                float rawY = event.getRawY();
                gooView.initGooViewPosition(rawX, rawY);
                gooView.setText(tv_unread_msg_count.getText().toString());
                Log.i("test", "addView");
                manager.addView(gooView, params);
                break;

            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        //自己手动调用ontouch事件，让小球拿到事件
        gooView.onTouchEvent(event);
        return true;
    }

    @Override
    public void onDisappear() {  
        //移除小球
        manager.removeView(gooView);
    }

    @Override
    public void onReset() {
        //重置操作
        //1,移除GooView
        //WindowManager的addView方法是将GooView添加到root根部局中
        //removeView是将GooView从root根部局中移除,当移除后的GooView再次尝试从root中移除就会抛出
        //View not attached to window manager这样的异常
        //是由已经被WindowManager移除的视图,再此被移除
        //如果已经添加到root上的GooView会有一个Parent(父视图),判断父视图是否为空,就可以规避这个bug
        if (gooView.getParent() != null) {
            Log.i("test", "removeView");
            manager.removeView(gooView);
            //2,让TextView显示出来
            tv_unread_msg_count.setVisibility(View.VISIBLE);
        }
    }
}
