package com.heima.quickindex;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private QickIndexBar qickIndexBar;
    ArrayList<Friend> friends = new ArrayList<>();
    private ListView listView;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        qickIndexBar = (QickIndexBar) findViewById(R.id.quickIndexBar);
        listView = (ListView) findViewById(R.id.listview);
        tv = (TextView) findViewById(R.id.letter);

        prepareData();
        //对集合数据按照拼音进行排序,让这个类实现compare接口
        Collections.sort(friends);
        listView.setAdapter(new FriendAdapter(friends));
        qickIndexBar.setOnLetterOnChangeListener(new QickIndexBar.OnLetterOnChangeListener() {
            @Override
            public void onListenerChange(String letter) {
                Log.i(TAG, "onListenerChange: "+letter);
                //  //根据letter找到列表中首字母和letter相同的条目，然后置顶
                for (int i = 0; i < friends.size(); i++) {
                    //得到每一个首字母
                    String firstWord = friends.get(i).pingYin.charAt(0) + "";
                    //判断letter和得到的首字母是否相同，相同则返回当前的字母
                    if (firstWord.equalsIgnoreCase(letter)) {
                        //说明找到了和letter同样字母的条目
                        listView.setSelection(i);
                        //找到之后立刻中断
                        break;
                    }
                }

                showLetter(letter);
                Animation animation = createAnim();
                tv.setAnimation(animation);
            }
        });
    }
    //动画效果
    private Animation createAnim() {
      /*  float fromX, float toX, float fromY, float toY,
        int pivotXType, float pivotXValue, int pivotYType, float pivotYValue*/
        ScaleAnimation scaleAnimation =new ScaleAnimation(0.0f, 0.5f, 0.0f, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(700);
        //scaleAnimation.setFillAfter(true);
        return scaleAnimation;
    }

    private Handler handler = new Handler();
    //显示字母
    private void showLetter(String letter) {
        //一开始就先移除之前的任务
        handler.removeCallbacksAndMessages(null);
        tv.setText(letter);
        tv.setVisibility(View.VISIBLE);

        //延迟操作
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //延迟的时候隐藏
                tv.setVisibility(View.INVISIBLE);
            }
        },2000);
    }

    private void prepareData() {
        // 虚拟数据
        friends.add(new Friend("李伟"));
        friends.add(new Friend("张三"));
        friends.add(new Friend("阿三"));
        friends.add(new Friend("阿四"));
        friends.add(new Friend("段誉"));
        friends.add(new Friend("段正淳"));
        friends.add(new Friend("张三丰"));
        friends.add(new Friend("陈坤"));
        friends.add(new Friend("林俊杰1"));
        friends.add(new Friend("陈坤2"));
        friends.add(new Friend("王二a"));
        friends.add(new Friend("林俊杰a"));
        friends.add(new Friend("张四"));
        friends.add(new Friend("林俊杰"));
        friends.add(new Friend("王二"));
        friends.add(new Friend("王二b"));
        friends.add(new Friend("赵四"));
        friends.add(new Friend("杨坤"));
        friends.add(new Friend("赵子龙"));
        friends.add(new Friend("杨坤1"));
        friends.add(new Friend("李伟1"));
        friends.add(new Friend("宋江"));
        friends.add(new Friend("宋江1"));
        friends.add(new Friend("李伟3"));
    }
}
