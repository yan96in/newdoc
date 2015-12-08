package net.meluo.newdoc.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;

import com.meluo.newdoc.db.DiseaseProvider;

import net.meluo.newdoc.R;
import net.meluo.newdoc.activity.BaseActivity;
import net.meluo.newdoc.bean.Disease;
import net.meluo.newdoc.fragment.DocSelfDetailFragment;

public class DiseaseAdapter extends SimpleCursorAdapter {

	private Context mContext;
	private LayoutInflater mInflater;

	public DiseaseAdapter(Context context, Cursor cursor, String[] from) {

		super(context, 0, cursor, from, null);
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Cursor cursor = this.getCursor();
		cursor.moveToPosition(position);

		String name = cursor.getString(cursor
				.getColumnIndex(DiseaseProvider.DiseaseConstants.NAME));

		final Disease d = new Disease();
		d.setName(cursor.getString(cursor
				.getColumnIndex(DiseaseProvider.DiseaseConstants.NAME)));
		d.setDetail(cursor.getString(cursor
				.getColumnIndex(DiseaseProvider.DiseaseConstants.DETAIL)));
		d.setTreat(cursor.getString(cursor
				.getColumnIndex(DiseaseProvider.DiseaseConstants.TREAT)));
		d.setOther(cursor.getString(cursor
				.getColumnIndex(DiseaseProvider.DiseaseConstants.OTHER)));

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_disease, parent,
					false);
			viewHolder.name = (Button) convertView
					.findViewById(R.id.id_btn_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.name.setText(name);

		viewHolder.name.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				((BaseActivity) mContext).addFragmentToStack(R.id.adri_content,
						DocSelfDetailFragment.newInstance(d), true);
			}
		});

		return convertView;
	}

	private static class ViewHolder {
		Button name;
	}
}
