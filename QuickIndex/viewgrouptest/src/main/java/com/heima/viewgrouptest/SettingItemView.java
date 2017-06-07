package com.heima.viewgrouptest;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/3/23.
 */

public class SettingItemView extends RelativeLayout{

    private ImageView iv_toggle;

    public SettingItemView(Context context) {
        this(context,null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);

    }
    //初始化
    private void init(AttributeSet attrs) {
        //加载布局,this代表挂载布局
        View view = View.inflate(getContext(), R.layout.view_setting_item, this);
        //从布局文件里找到相关的自定义控件
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SettingItemView);
        //从ta取到自定义属性的名字,第二个参数是0默认值
        int my_background = ta.getInt(R.styleable.SettingItemView_my_background, 0);
        //取到具体的属性值
        int bg = 0;
        switch (my_background) {
            case 1:
            bg = R.drawable.seting_first_selector;
                break;

            case 2:
            bg = R.drawable.seting_middle_selector;
                break;
            case 3:
            bg = R.drawable.seting_last_selector;
                break;

        }
        //设置文本,从自定义里面取到属性
        String title = ta.getString(R.styleable.SettingItemView_my_title);
        //找到布局文件里面的title
        TextView tv = (TextView) this.findViewById(R.id.tv_title);
        iv_toggle = (ImageView) this.findViewById(R.id.iv_toggle);
        boolean isToggle = ta.getBoolean(R.styleable.SettingItemView_my_toggle,false);
        //设置开关的状态
        setToggle(isToggle);
        //设置
        tv.setText(title);
        //设置背景
        view.setBackgroundResource(bg);
        //使用之后释放资源
        ta.recycle();
    }

    //控制开关的状态，如果是打开就关闭，则相反,在主方法中直接调用
    public void toggle(){
        setToggle(!isToggle);
    }
    //定义一个成员变量记录当前的状态,默认值是关的
    private boolean isToggle = false;
    //记录当前的开关的状态
    public void setToggle(boolean isToggle){
        //设置图片
        iv_toggle.setImageResource(isToggle ? R.drawable.on : R.drawable.off);
        //成员变量用于记录当前的开关的状态
        this.isToggle = isToggle;
    }
}
