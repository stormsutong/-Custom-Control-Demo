package com.heima.quickindex;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/3/22.
 */

public class QickIndexBar extends View{
    private String[] indexArr = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"};
    private Paint paint;//画笔
    private OnLetterOnChangeListener listener;
    private int COLOR_DEFAULT = Color.WHITE;
    private int COLOR_PRESSED = Color.DKGRAY;

    public QickIndexBar(Context context) {
        super(context);
        init();
    }

    public QickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public QickIndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    //初始化
    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);//去据此
        int textsize = getResources().getDimensionPixelSize(R.dimen.textsize);
        paint.setTextSize(textsize);
        paint.setColor(Color.WHITE);
        //文字的默认起点是文字的左下角
        //设置文字的起点为起始点的中心
        paint.setTextAlign(Paint.Align.CENTER);
    }
    private int cellHeight;//格子的高度
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //每个格子的高度
        cellHeight = getMeasuredHeight()/indexArr.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < indexArr.length; i++) {
            int x = getMeasuredWidth()/2;
            //y轴的值是格子高度一半+文字高度的一半+i*格子的高度
            int y = cellHeight/2+getTextHeight(indexArr[i])/2+i*cellHeight;
            //当重回字母的时候，可以判断当前正在绘制的和按下的是否是同一个
            paint.setColor(i == touchIndex ? COLOR_PRESSED : COLOR_DEFAULT);
            canvas.drawText(indexArr[i],x,y,paint);

        }


    }
    private int touchIndex = -1;//触摸的是哪个字母索引
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //计算当前触摸是哪个字母
                //判断如果不是当前触摸的索引再进来执行
                if (touchIndex != (int) (event.getY()/cellHeight)) {
                    //当前触摸的字母索引
                    touchIndex = (int) (event.getY()/cellHeight);
                    //得到字母的索引是为了让listview滑动时显示，所以提供一个对外薄露的接口
                    //为了代码的健壮性
                    if (touchIndex >= 0 && touchIndex < indexArr.length) {
                        if (listener != null) {//根据索引获得对应的字母
                            listener.onListenerChange(indexArr[touchIndex]);
                        }
                    }
                }

                break;

            case MotionEvent.ACTION_UP:

                break;
        }

        //重新绘制，改变文字的颜色
        invalidate();//会走ondraw方法
        return super.onTouchEvent(event);
    }

    //文字的高度
    private int getTextHeight(String s) {
        //得到格子的矩形
        Rect bounds = new Rect();
        paint.getTextBounds(s,0,s.length(),bounds);//这个方法之后，rect就有了相对应的值
        //取出文字的高度就是bottom - top
        return bounds.height();

    }
    //提供set方法
    public void setOnLetterOnChangeListener(OnLetterOnChangeListener listener){
        this.listener = listener;
    }

    //对外薄露的接口
    public interface OnLetterOnChangeListener{
        //传入一个参数是字母
        void onListenerChange(String letter);
    }
}
