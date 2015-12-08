package net.meluo.newdoc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import net.meluo.newdoc.R;
import net.meluo.newdoc.activity.BaseActivity;
import net.meluo.newdoc.bean.Appointment;
import net.meluo.newdoc.fragment.CommenFragment;

import java.util.ArrayList;

public class AppointmentAdapter extends BaseAdapter {

	ArrayList<Appointment> list;
	private Context mContext;
	Typeface typeFace;
	private final LayoutInflater mInflater;

	// 构造函数
	public AppointmentAdapter(Context context, ArrayList<Appointment> list) {
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
			convertView = mInflater.inflate(R.layout.item_appointment, null);

			viewHolder.name = (TextView) convertView
					.findViewById(R.id.ia_tv_name);
			viewHolder.location = (TextView) convertView
					.findViewById(R.id.ia_tv_location);
			viewHolder.catalog = (TextView) convertView
					.findViewById(R.id.ia_tv_catalog);
			viewHolder.doc = (TextView) convertView
					.findViewById(R.id.ia_tv_doc);
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.ia_tv_time);
			viewHolder.com = (Button) convertView.findViewById(R.id.ia_btn_com);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final Appointment a = (Appointment) getItem(position);
		viewHolder.name.setText(a.getRoom_name());

		viewHolder.catalog.setText("科室:" + a.getCatalog_name());

		viewHolder.doc.setText("医生:" + a.getDoc_name());
		viewHolder.time.setText("预约时间:" + a.getDate() + " " + a.getStart_time()
				+ "-" + a.getEnd_time());

		viewHolder.com.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				((BaseActivity) mContext).addFragmentToStack(R.id.ami_content,
						CommenFragment.newInstance(a.getId() + ""), true);

			}
		});

		return convertView;
	}

	private static class ViewHolder {

		TextView name;
		TextView location;
		TextView catalog;
		TextView doc;
		TextView time;
		Button com;

	}
}
