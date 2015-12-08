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
import net.meluo.newdoc.activity.BaseActivity;
import net.meluo.newdoc.bean.FAQ;
import net.meluo.newdoc.fragment.FAQFragment;

import java.util.ArrayList;

public class FAQAdapter extends BaseAdapter {

	ArrayList<FAQ> list;
	private Context mContext;
	Typeface typeFace;
	private final LayoutInflater mInflater;
	Fragment fragment;

	// 构造函数
	public FAQAdapter(Fragment fragment, ArrayList<FAQ> list) {
		this.mContext = fragment.getActivity();
		this.list = list;
		mInflater = LayoutInflater.from(this.mContext);
		this.fragment = fragment;
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
			convertView = mInflater.inflate(R.layout.item_faq, null);

			viewHolder.name = (TextView) convertView
					.findViewById(R.id.if_tv_name);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final FAQ f = (FAQ) getItem(position);
		viewHolder.name.setText(f.getQuestion());
		viewHolder.name.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 进入预约界面
				((BaseActivity) mContext).addFragmentToStack(R.id.adri_content,
						FAQFragment.newInstance(f), true);
			}
		});

		return convertView;
	}

	private static class ViewHolder {

		TextView name;

	}
}
