package com.heima.gooviewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Msg> msgList = new ArrayList<>();
        //设置数据
        for (int i = 0; i < 30; i++) {
            msgList.add(new Msg("标题"+i,i));
        }
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        //设置布局管理器
        rv.setLayoutManager(new LinearLayoutManager(this));
        //设置适配器
        rv.setAdapter(new MsgAdapter(msgList,this));
    }
}
