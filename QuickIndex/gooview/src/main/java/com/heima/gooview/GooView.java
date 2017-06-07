package com.heima.gooview;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/3/22.
 */

public class GooView extends View {
    private Paint paint;

    public GooView(Context context) {
        this(context, null);
    }

    public GooView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GooView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    FloatEvaluator floatEvaluator = new FloatEvaluator();
    WindowManager wm;

    //初始化
    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    }

    private PointF dragCenter = new PointF(200, 200);//drag圆的圆心
    private float dragRadius = 20;//drag圆的半径
    private PointF stickyCenter = new PointF(400, 200);//sticky圆的圆心
    private float stickyRadius = 20;//sticky圆的半径

    private PointF[] dragPoints = {new PointF(200, 180), new PointF(200, 220)};
    private PointF[] stickyPoints = {new PointF(400, 180), new PointF(400, 220)};
    private PointF controlPoint = new PointF(300, 200);//控制点

    private double lineK;//斜率

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //动态计算sticky圆的半径
        stickyRadius = caculateStickyRadius();

        //a.计算贝塞尔曲线的4个点
        float yOffset = dragCenter.y - stickyCenter.y;//2圆圆心y的差值
        float xOffset = dragCenter.x - stickyCenter.x;//2圆圆心x的差值
        if (xOffset != 0) {
            lineK = yOffset / xOffset;
        }
        //b.计算drag圆的2个点
        dragPoints = GeometryUtil.getIntersectionPoints(dragCenter, dragRadius, lineK);
        //c.计算sticky圆的2个点
        stickyPoints = GeometryUtil.getIntersectionPoints(stickyCenter, stickyRadius, lineK);
        //d.计算控制点,黄金分割点的位置
        controlPoint = GeometryUtil.getPointByPercent(dragCenter, stickyCenter, 0.618f);

        //画两个圆
        canvas.drawCircle(dragCenter.x, dragCenter.y, dragRadius, paint);

        if (!isOutOfRange) {
            canvas.drawCircle(stickyCenter.x, stickyCenter.y, stickyRadius, paint);
            //绘制两个圆连接的部分,使用贝塞尔曲线
            Path path = new Path();
            path.moveTo(stickyPoints[0].x, stickyPoints[0].y);//移动到一个起点
            path.quadTo(controlPoint.x, controlPoint.y, dragPoints[0].x, dragPoints[0].y);//绘制贝塞尔曲线
            path.lineTo(dragPoints[1].x, dragPoints[1].y);//移动到第二条曲线的起点
            path.quadTo(controlPoint.x, controlPoint.y, stickyPoints[1].x, stickyPoints[1].y);//绘制第2条曲线
            path.close();//闭合起点和终点
            //绘制path
            canvas.drawPath(path, paint);
        }

        //绘制范围的圈圈
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(stickyCenter.x, stickyCenter.y, maxDistance, paint);
        paint.setStyle(Paint.Style.FILL);
    }

    private float maxDistance = 160;//2圆圆心的距离的最大值
    private boolean isOutOfRange = false;//是否超出范围

    /**
     * 动态计算sticky圆的半径
     *
     * @return
     */
    private float caculateStickyRadius() {
        //1.计算2圆圆心距离
        float distance = GeometryUtil.getDistanceBetween2Points(dragCenter, stickyCenter);
        //2.计算百分比
        float fraction = distance / maxDistance;
        //3.根据百分比，计算sticky圆的半径
        return floatEvaluator.evaluate(fraction, 20, 3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                dragCenter.set(event.getX(), event.getY());
                //获取圆心距离
                float distance = GeometryUtil.getDistanceBetween2Points(dragCenter, stickyCenter);
                //说明需要断开，就是不在绘制2圆链接的部分
                isOutOfRange = distance > maxDistance;
                break;
            case MotionEvent.ACTION_UP:
                if (isOutOfRange) {
                    //1.在圈外抬起，应该播放爆炸的气泡
                    playBoomAnim(dragCenter);
                    //2.让drag圆和sticky圆重叠
                    dragCenter.set(stickyCenter);
                } else {
                    //在圈内抬起，应该让drag圆弹回去
                    final PointF start = new PointF(dragCenter.x, dragCenter.y);
                    ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            //得到的是动画执行的百分比进度
                            float fraction = animation.getAnimatedFraction();
                            PointF pointF = GeometryUtil.getPointByPercent(start, stickyCenter, fraction);
                            //将pointF设置给drag圆
                            dragCenter.set(pointF);
                            //刷新
                            invalidate();
                        }
                    });
                    animator.setInterpolator(new OvershootInterpolator(4));
                    animator.setDuration(400).start();
                }
//                isOutOfRange = false;
                break;
        }
        invalidate();
        return true;
        //重新绘制
    }

    private Handler handler = new Handler();

    /**
     * 播放爆炸动画
     *
     * @param dragCenter
     */
    private void playBoomAnim(PointF dragCenter) {
        //设置window的背景为透明
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.format = PixelFormat.TRANSPARENT;//设置透明背景


        final BubbleLayout frameLayout = new BubbleLayout(getContext());
        frameLayout.setBubblePosition(dragCenter);

        //创建动画的载体就是ImageVIew
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(new FrameLayout.LayoutParams(68, 68));
        imageView.setBackgroundResource(R.drawable.anim);
        //播放帧动画
        AnimationDrawable animDrawable = (AnimationDrawable) imageView.getBackground();
        animDrawable.start();//开启帧动画

        frameLayout.addView(imageView);

        wm.addView(frameLayout, params);

        //600ms后移除frameLayout
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        wm.removeViewImmediate(frameLayout);
                    }
                }, 601);
    }
}

