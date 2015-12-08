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

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders.Any.B;
import com.way.util.PreferenceConstants;
import com.way.util.T;

import net.meluo.core.User;
import net.meluo.newdoc.R;
import net.meluo.newdoc.activity.BaseActivity;
import net.meluo.newdoc.bean.Room;
import net.meluo.newdoc.fragment.DocRoomMainFragment;

import java.util.ArrayList;

public class RoomAdapter extends BaseAdapter {

	ArrayList<Room> list;
	private Context mContext;
	Typeface typeFace;
	private final LayoutInflater mInflater;
	int id;

	// 构造函数
	public RoomAdapter(Context context, ArrayList<Room> list, int id) {
		this.mContext = context;
		this.list = list;
		mInflater = LayoutInflater.from(this.mContext);
		this.id = id;
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
			convertView = mInflater.inflate(R.layout.item_room, null);

			viewHolder.name = (TextView) convertView
					.findViewById(R.id.ir_tv_name);
			viewHolder.functions = (TextView) convertView
					.findViewById(R.id.ir_tv_functions);
			viewHolder.location = (TextView) convertView
					.findViewById(R.id.ir_tv_location);
			viewHolder.btn = (Button) convertView
					.findViewById(R.id.ir_btn_speak);
			viewHolder.btnBound = (Button) convertView
					.findViewById(R.id.ir_btn_bound);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final Room room = (Room) getItem(position);
		viewHolder.name.setText(room.getName());
		viewHolder.functions.setText(room.getDetail());
		viewHolder.location.setText(room.getAddress());

		if (User.isLogin()) {
			if (room.getIsBound()) {
				viewHolder.btnBound.setText("取消绑定");
				viewHolder.btnBound
						.setBackgroundResource(R.drawable.bg_btn_def_xml_1);
			} else {
				viewHolder.btnBound.setText("绑定诊室");
				viewHolder.btnBound
						.setBackgroundResource(R.drawable.bg_btn_green_xml);
			}
		}
		viewHolder.btnBound.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!User.isLogin()) {
					T.showShort(mContext, "请先登录");
				} else {
					bound(v, room);
				}
			}
		});

		viewHolder.btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 诊室简介
				((BaseActivity) mContext).addFragmentToStack(id,
						DocRoomMainFragment.newInstance(room, id), true);
			}
		});

		return convertView;
	}

	private void bound(final View v, final Room room) {

		B b = Ion.with(mContext, PreferenceConstants.BASE_URL + "/1/Binding");

		if (room.getIsBound()) {
			b.setBodyParameter("action", "del");
		}
		b.setBodyParameter("roomid", room.getId() + "").asJsonObject()
				.setCallback(new FutureCallback<JsonObject>() {
					@Override
					public void onCompleted(Exception e, JsonObject result) {

						if (e != null) {
							T.showShort(mContext, "网络错误，请稍后重试");
							return;
						}
						if (result.isJsonNull()) {
							T.showShort(mContext, "网络错误，请稍后重试");
							return;
						}
						if (result.get("retcode").getAsInt() != 0) {
							T.showShort(mContext, result.get("errmsg")
									.getAsString());
							return;
						}
						if (room.getIsBound()) {
							room.setIsBound(false);
							((Button) v).setText("绑定诊室");
							((Button) v)
									.setBackgroundResource(R.drawable.bg_btn_green_xml);

						} else {
							room.setIsBound(true);
							((Button) v).setText("取消绑定");
							((Button) v)
									.setBackgroundResource(R.drawable.bg_btn_def_xml_1);
						}
					}
				});
	}

	private static class ViewHolder {

		TextView name;
		TextView functions;
		TextView location;
		Button btn;
		Button btnBound;
	}
}
