package com.heima.gooviewtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sszz on 2016/12/13.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MyViewHolder> {
	private List<Msg> msgList;


	public MsgAdapter(List<Msg> msgList,Context context) {
		listener=new OnGooViewTouchListener(context);
		this.msgList = msgList;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rlv_item,parent,false);//TODO
		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		holder.tv_title.setText(msgList.get(position).title);
		int unReadMsgCount = msgList.get(position).unReadMsgCount;
		if (unReadMsgCount == 0) {
			holder.tv_unReadMsgCount.setVisibility(View.INVISIBLE);
		} else {
			holder.tv_unReadMsgCount.setVisibility(View.VISIBLE);
			holder.tv_unReadMsgCount.setText(unReadMsgCount + "");
		}
		//给文本设置手触摸的事件
		holder.tv_unReadMsgCount.setOnTouchListener(listener);

	}

	private OnGooViewTouchListener listener;

	@Override
	public int getItemCount() {
		return msgList.size();
	}

	public static class MyViewHolder extends RecyclerView.ViewHolder {
		private TextView tv_title;
		private TextView tv_unReadMsgCount;

		public MyViewHolder(View itemView) {
			super(itemView);
			tv_title = (TextView) itemView.findViewById(R.id.tv_title);
			tv_unReadMsgCount = (TextView) itemView.findViewById(R.id.tv_unReadMsgCount);
		}
	}
}
