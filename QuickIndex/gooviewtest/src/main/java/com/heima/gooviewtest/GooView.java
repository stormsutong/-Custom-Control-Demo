package com.heima.gooviewtest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by sszz on 2016/12/13.
 */
//两个准备工作:
	//1,让GooView显示文本
public class GooView extends View {

	private Paint paint;
	private Path path;
	private int statusBarHeight;

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
	//定义"空壳"矩形
	private Rect textRect;
	private void init() {
		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setTextSize(25);
		path = new Path();
		textRect=new Rect();
	}
	//设置一个text方法让mainactivity调用
	private String text="";
	public void setText(String text){
		this.text=text;
	}

	public void initGooViewPosition(float x,float y){
		//将拖拽圆和固定圆的坐标全部设置为参数对应的坐标
		stableCenter.set(x,y);
		dragCenter.set(x,y);
		invalidate();
	}


	//固定圆圆心
	private PointF stableCenter = new PointF(200f, 200f);
	//拖拽圆圆心
	private PointF dragCenter = new PointF(100f, 100f);
	//固定圆半径
	private float stableRadius = 20f;
	//拖拽圆半径
	private float dragRadius = 20f;
	//固定圆的两个附着点
	private PointF[] stablePoints = new PointF[]{
			new PointF(200f, 300f),
			new PointF(200f, 350f)
	};
	//拖拽圆的两个附着点
	private PointF[] dragPoints = new PointF[]{
			new PointF(100f, 300f),
			new PointF(100f, 350f)
	};

	private PointF controlPoint = new PointF(150f, 325f);
	//拖拽圆移动的最大距离
	private float maxDragDistance = 200f;
	//固定圆变化的最小半径
	private float minStableRadius = 5f;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		canvas.translate(0, -statusBarHeight);
		//随着拖拽圆和固定圆的圆心距离越来越大,固定圆的半径越来越小
		//拖拽圆和固定圆圆心的距离百分比变化==固定圆半径的百分比变化
		float distance = GeometryUtil.getDistanceBetween2Points(dragCenter, stableCenter);
		float percent = distance / maxDragDistance;
		float tempRadius = GeometryUtil.evaluateValue(percent, stableRadius, minStableRadius);
		if (tempRadius < minStableRadius) {
			tempRadius = minStableRadius;
		}
		if (!isDisappear) {
			if (!isOutOfRange) {
				//计算两组附着点
				//1,获取两个圆圆心连线的斜率
				float dx = dragCenter.x - stableCenter.x;
				float dy = dragCenter.y - stableCenter.y;
				//定义斜率
				double lineK = 0;
				if (dx != 0) {
					lineK = dy / dx;
				}
				//将计算后的数据赋值给原有的静态数据
				dragPoints = GeometryUtil.getIntersectionPoints(dragCenter, dragRadius, lineK);
				stablePoints = GeometryUtil.getIntersectionPoints(stableCenter, tempRadius, lineK);
				controlPoint = GeometryUtil.getMiddlePoint(dragCenter, stableCenter);


				//绘制中间图形的步骤:
				//1,移动到固定圆的附着点1
				path.moveTo(stablePoints[0].x, stablePoints[0].y);
				//2,向拖拽圆的附着点1绘制贝塞尔曲线
				path.quadTo(controlPoint.x, controlPoint.y, dragPoints[0].x, dragPoints[0].y);
				//3,向拖拽圆的附着点2绘制直线
				path.lineTo(dragPoints[1].x, dragPoints[1].y);
				//4,向固定圆的附着点2绘制贝塞尔曲线
				path.quadTo(controlPoint.x, controlPoint.y, stablePoints[1].x, stablePoints[1].y);
				//5,闭合
				path.close();
				canvas.drawPath(path, paint);
				path.reset();
				canvas.drawCircle(stableCenter.x, stableCenter.y, tempRadius, paint);
			}
			canvas.drawCircle(dragCenter.x, dragCenter.y, dragRadius, paint);
			drawText(canvas);
		}
		canvas.restore();
	}
	//绘制文本
	private void drawText(Canvas canvas) {
		paint.setColor(Color.WHITE);
		//获取文本的边界:原理是将文本套入一个"空壳"矩形,这个矩形的宽高就是文本的宽高
		paint.getTextBounds(text,0,text.length(),textRect);
		float x=dragCenter.x-textRect.width()*0.5f;//为拖拽圆圆心点横坐标+文本宽度*0.5finit
		float y=dragCenter.y+textRect.height()*0.5f;//为拖拽圆圆心点纵坐标+文本宽度*0.5f
		canvas.drawText(text,x,y,paint);
		paint.setColor(Color.RED);
	}

	//定义是否超出最大范围
	private boolean isOutOfRange = false;
	//定义是否全部消失
	private boolean isDisappear = false;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				isOutOfRange = false;
				isDisappear=false;
				//getX:点击的点距离当前自定义控件的左边缘的距离
				//getRawX:点击的点距离当前手机屏幕的左边缘的距离
				float rawX = event.getRawX();
				float rawY = event.getRawY();
				dragCenter.set(rawX, rawY);
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				rawX = event.getRawX();
				rawY = event.getRawY();
				dragCenter.set(rawX, rawY);
				//当拖拽超出一定范围后,固定圆和中间图形都消失了
				//获取拖拽的距离,判断距离是否超出最大距离
				float distance = GeometryUtil.getDistanceBetween2Points(dragCenter, stableCenter);
				if (distance > maxDragDistance) {
					//不再对固定圆和中间图形进行绘制
					isOutOfRange = true;
				}
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				distance = GeometryUtil.getDistanceBetween2Points(dragCenter, stableCenter);
				//判断在move的时候是否超出过最大范围
				if (isOutOfRange) {
					//判断up的时候是否在最大范围外
					if (distance > maxDragDistance) {
						isDisappear = true;
						if(listener!=null){
							listener.onDisappear();
						}
					}else{
						dragCenter.set(stableCenter.x,stableCenter.y);
						//做重置操作
						if(listener!=null){
							listener.onReset();
						}
					}
				} else {
					//move的时候未超出最大范围,up的时候也在最大范围内
					//处理:让拖拽圆向固定圆圆心的方向做平移运动,当到达固定圆圆心位置后,超出一段距离,再弹回
					//ValueAnimator:当写入参数后,ValueAnimator会使得其中一个参数向另一个参数变化
					//将up的瞬间的拖拽圆的坐标记录
					final PointF tempPointF=new PointF(dragCenter.x,dragCenter.y);
					ValueAnimator va = ValueAnimator.ofFloat(distance, 0);
					//添加数据变化的监听
					va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
						@Override
						public void onAnimationUpdate(ValueAnimator animation) {
//							float value= (float) animation.getAnimatedValue();
//							Log.i("test",value+"");
							//获取数据变化的百分比
							float percent = animation.getAnimatedFraction();
							dragCenter = GeometryUtil.getPointByPercent(tempPointF, stableCenter, percent);
							invalidate();
						}
					});
					va.addListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							super.onAnimationEnd(animation);
							//做重置操作
							if(listener!=null){
								Log.i("test","onReset");
								listener.onReset();
							}
						}
					});
					//动画插入器:改变动画的执行效果:动画执行到目标点会超出一段距离,然后回到原位
					va.setInterpolator(new OvershootInterpolator(3));
					va.setDuration(2000);
					va.start();

				}
				invalidate();
				break;
		}
		//表示当前自定义控件想要处理事件
		return true;
	}
	private OnGooViewChangeListener listener;


	//接口是为了减少类与类之间的耦合性
	public interface OnGooViewChangeListener{
		/**
		 * GooView的消失处理
		 */
		void onDisappear();

		/**
		 * GooView重置处理
		 */
		void onReset();
	}
	//给接口设置构造参数
	public void setOnGooViewChangeListener(OnGooViewChangeListener listener){
		this.listener=listener;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		statusBarHeight = getStatusBarHeight(this);
	}

	//获取状态栏高度
	private int getStatusBarHeight(View view) {
		Rect rect = new Rect();
		//获取视图对应的可视范围,会把视图的左上右下的数据传入到一个矩形中
		view.getWindowVisibleDisplayFrame(rect);
		return rect.top;
	}
}
