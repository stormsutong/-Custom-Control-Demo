package com.heima.quickindex;

/**
 * Created by Administrator on 2017/3/22.
 */

public class Friend implements Comparable<Friend>{
    public String name;
    public String pingYin;

    public Friend(String name) {
        this.name = name;
        this.pingYin = PinYinUtil.getPinyin(name);
    }

    @Override
    public int compareTo(Friend another) {
       /* //得到拼音,根据名字返回拼音
        String pinyin = PinYinUtil.getPinyin(name);
        //其他的拼音首字母
        String anotherPinyin = PinYinUtil.getPinyin(another.name);*/
        return pingYin.compareTo(another.pingYin);
    }
}
