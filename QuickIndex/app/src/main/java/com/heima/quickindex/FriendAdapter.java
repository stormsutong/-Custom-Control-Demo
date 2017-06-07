package com.heima.quickindex;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/22.
 */

public class FriendAdapter extends BaseAdapter {
    ArrayList<Friend> friends;

    public FriendAdapter(ArrayList<Friend> friends) {
        this.friends = friends;
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.adpter_list, null);
            viewHolder = new ViewHolder(convertView);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Friend friend = friends.get(position);
        viewHolder.name.setText(friend.name);
        //显示首字母
        String firstLetter = friend.pingYin.charAt(0)+"";
        if(position>0){
            //上一个条目的首字母
            String lastLetter = friends.get(position - 1).pingYin.charAt(0)+"";
            //如果当前首字母和上一个相同，则隐藏首字母
            if(firstLetter.equalsIgnoreCase(lastLetter)){
                viewHolder.letter.setVisibility(View.GONE);
            }else {
                //说明不相等，直接设置
                //由于是复用的，所以当需要显示的时候，要设置为可见
                viewHolder.letter.setVisibility(View.VISIBLE);
                viewHolder.letter.setText(firstLetter);
            }
        }else {
            viewHolder.letter.setVisibility(View.VISIBLE);
            viewHolder.letter.setText(firstLetter);
        }

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.letter)
        TextView letter;
        @Bind(R.id.name)
        TextView name;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
