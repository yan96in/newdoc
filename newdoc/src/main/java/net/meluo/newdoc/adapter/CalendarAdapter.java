package net.meluo.newdoc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.ypy.eventbus.EventBus;

import net.meluo.newdoc.R;
import net.meluo.newdoc.app.NDEvent;
import net.meluo.newdoc.bean.Day;
import net.meluo.newdoc.fragment.DocRoomBespeakFragment;

import java.util.ArrayList;

public class CalendarAdapter extends BaseAdapter {

	ArrayList<Day> list;
	private Context context;
	Typeface typeFace;
	private final LayoutInflater mInflater;
	DocRoomBespeakFragment fragment;
	int check = -1;

	// 构造函数
	public CalendarAdapter(DocRoomBespeakFragment fragment, ArrayList<Day> list) {
		this.context = fragment.getActivity();
		this.list = list;
		this.fragment = fragment;
		mInflater = LayoutInflater.from(this.context);

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

	@SuppressLint({ "InflateParams", "ResourceAsColor" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder viewHolder;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = mInflater
					.inflate(R.layout.item_day_in_calendar, null);
			viewHolder.name = (CheckBox) convertView
					.findViewById(R.id.idic_btn);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final Day d = (Day) getItem(position);

		viewHolder.name.setText(d.getName());

		switch (d.getStatus()) {
		case Day.STATUS_TITLE:
			viewHolder.name.setClickable(false);
			viewHolder.name.setTextSize(20);
			break;
		case Day.STATUS_IN:
			viewHolder.name.setBackgroundResource(R.drawable.ic_item_in);
			viewHolder.name.setClickable(false);
			break;
		case Day.STATUS_OUT:
			viewHolder.name.setBackgroundResource(R.drawable.ic_item_out);
			viewHolder.name.setClickable(false);
			break;
		case Day.STATUS_ON:
			viewHolder.name.setBackgroundResource(R.drawable.bg_item_on_xml);
			if (position == check) {
				viewHolder.name.setChecked(true);
			} else {
				viewHolder.name.setChecked(false);
			}
			viewHolder.name.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (viewHolder.name.isChecked()) {
						check = position;
						CalendarAdapter.this.notifyDataSetChanged();
						EventBus.getDefault().post(
								new NDEvent(NDEvent.E_DAY_CHANGE));
					}

				}
			});
			break;

		}

		return convertView;
	}

	public int getCheck() {
		return check;
	}

	public Day getCheckDay() {
		return (Day) getItem(check);
	}

	private static class ViewHolder {

		CheckBox name;

	}
}
