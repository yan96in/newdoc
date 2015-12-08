package net.meluo.newdoc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.meluo.newdoc.R;
import net.meluo.newdoc.activity.BaseActivity;
import net.meluo.newdoc.bean.Catalog;
import net.meluo.newdoc.fragment.DocRoomListFragment;
import net.meluo.newdoc.fragment.DocRoomMainFragment;

import java.util.ArrayList;

public class RoomCatalogAdapter extends BaseAdapter {

    ArrayList<Catalog> list;
    private Context mContext;
    Typeface typeFace;
    private final LayoutInflater mInflater;
    DocRoomListFragment fragment;

    // 构造函数
    public RoomCatalogAdapter(DocRoomListFragment fragment,
                              ArrayList<Catalog> list) {
        this.fragment = fragment;
        this.mContext = fragment.getActivity();
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

    @SuppressLint({"InflateParams"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_position_node, null);
            viewHolder.department = (TextView) convertView
                    .findViewById(R.id.ipn_rb_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Catalog c = list.get(position);
        viewHolder.department.setText(c.getName());
        viewHolder.department.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ((BaseActivity) mContext).addFragmentToStack(R.id.adri_content,
                        DocRoomMainFragment.newInstance(fragment.currentRoom,
                                R.id.adri_content), true);
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        TextView department;
    }
}
