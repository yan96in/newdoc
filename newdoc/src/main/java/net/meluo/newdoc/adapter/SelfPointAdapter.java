package net.meluo.newdoc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.way.util.L;
import com.ypy.eventbus.EventBus;

import net.meluo.newdoc.R;
import net.meluo.newdoc.app.NDEvent;

import java.util.ArrayList;

public class SelfPointAdapter extends BaseAdapter {

	ArrayList<String> list;
	private Context mContext;
	private final LayoutInflater mInflater;
	private int check = -1;

	// 构造函数
	public SelfPointAdapter(Context context, ArrayList<String> list) {
		this.mContext = context;
		this.list = list;
		mInflater = LayoutInflater.from(this.mContext);
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
			convertView = mInflater.inflate(R.layout.item_body_point, null);
			viewHolder.name = (RadioButton) convertView
					.findViewById(R.id.ibp_rb_name);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final String n = (String) getItem(position);
		viewHolder.name.setText(n);
		if (position == check) {
			viewHolder.name.setChecked(true);
		} else {
			viewHolder.name.setChecked(false);
		}

		viewHolder.name.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				L.d(n + "____");
				EventBus.getDefault().post(
						new NDEvent(NDEvent.E_Body_POINT_CHANGE, n));
				check = position;
				SelfPointAdapter.this.notifyDataSetChanged();
			}
		});

		return convertView;
	}

	public void setCheck(int position) {
		check = position;
		SelfPointAdapter.this.notifyDataSetChanged();
		String n = (String) getItem(position);
		EventBus.getDefault().post(new NDEvent(NDEvent.E_Body_POINT_CHANGE, n));
		check = position;
		SelfPointAdapter.this.notifyDataSetChanged();
	}

	private static class ViewHolder {

		RadioButton name;

	}
}
