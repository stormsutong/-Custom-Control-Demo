package com.heima.shichi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MyListView lv = (MyListView) findViewById(R.id.lv);
        //初始化头布局
        View viewHolder = View.inflate(this, R.layout.header_view, null);
        final ImageView iv_child = (ImageView) viewHolder.findViewById(R.id.iv_child);
        //测量图片的高度
        // 等View的树状结构全部渲染完毕时候, 再设置到plv里.
        viewHolder.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //把图片传到自定义控件中
                lv.setParallaxImage(iv_child);
                //关闭视图
                lv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        lv.addHeaderView(viewHolder);
        lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Cheeses.NAMES));
    }
}
