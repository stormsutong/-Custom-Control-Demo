package com.heima.viewgrouptest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private SettingItemView siv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        siv = (SettingItemView) findViewById(R.id.siv);
        siv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接调用方法
            siv.toggle();
            }
        });
    }
}
