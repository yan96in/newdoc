package net.meluo.newdoc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.meluo.newdoc.R;
import net.meluo.newdoc.activity.ChatActivity;
import net.meluo.newdoc.bean.Question;

import java.util.ArrayList;

public class QuestionAdapter extends BaseAdapter {

	ArrayList<Question> list;
	private Context mContext;
	Typeface typeFace;
	private final LayoutInflater mInflater;
	Fragment fragment;
	int from;

	// 构造函数
	public QuestionAdapter(Fragment fragment, ArrayList<Question> list, int from) {
		this.mContext = fragment.getActivity();
		this.list = list;
		mInflater = LayoutInflater.from(this.mContext);
		this.fragment = fragment;
		this.from = from;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint({ "InflateParams" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder viewHolder;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_question, null);

			viewHolder.name = (TextView) convertView
					.findViewById(R.id.iq_tv_name);
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.iq_tv_time);
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.iq_tv_content);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final Question f = (Question) getItem(position);
		viewHolder.name.setText(f.getContent());
		viewHolder.time.setText(f.getStart_date());
		viewHolder.content.setText(f.getContent());
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 进入聊天页面

				ChatActivity.actionStart(mContext, f.getId() + "", from);
			}
		});

		return convertView;
	}

	private static class ViewHolder {

		TextView name;
		TextView time;
		TextView content;

	}
}
