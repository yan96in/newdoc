package net.meluo.newdoc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import net.meluo.newdoc.R;
import net.meluo.newdoc.bean.PositionNode;
import net.meluo.newdoc.fragment.DocRoomListFragment;

import java.util.ArrayList;

public class PositionNodeAdapter extends BaseAdapter {

    public static final int INDEX_PROVINCE = 0;
    public static final int INDEX_CITY = 1;
    public static final int INDEX_COUNTY = 2;

    ArrayList<PositionNode> list;
    private Context context;
    Typeface typeFace;
    private final LayoutInflater mInflater;
    private int index;
    DocRoomListFragment fragment;

    public int check = 0;

    // 构造函数
    public PositionNodeAdapter(DocRoomListFragment fragment,
                               ArrayList<PositionNode> list, int index) {

        this.fragment = fragment;
        this.context = fragment.getActivity();
        this.list = list;
        mInflater = LayoutInflater.from(this.context);
        this.index = index;

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
    public View getView(final int position, View convertView,
                        final ViewGroup parent) {

        final ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_position_node, null);

            viewHolder.name = (RadioButton) convertView
                    .findViewById(R.id.ipn_rb_name);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final PositionNode PositionNode = (PositionNode) getItem(position);

        viewHolder.name.setText(PositionNode.getName());

        if (position == check) {
            viewHolder.name.setChecked(true);
        } else {
            viewHolder.name.setChecked(false);
        }
        viewHolder.name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                check = position;
                PositionNodeAdapter.this.notifyDataSetChanged();

                switch (index) {
                    case INDEX_PROVINCE:
                        parent.setTag(PositionNode.getName());
                        fragment.loadCityData(PositionNode.getName());
                        fragment.clearCountyList();
                        fragment.tvPostion.setText(PositionNode.getName());
                        break;
                    case INDEX_CITY:
                        parent.setTag(PositionNode.getName());
                        fragment.loadCountyData(PositionNode.getName());
                        fragment.tvPostion.setText(fragment.lv0.getTag() + " "
                                + PositionNode.getName());
                        break;
                    case INDEX_COUNTY:
                        parent.setTag(PositionNode.getName());
                        fragment.tvPostion.setText(fragment.lv0.getTag() + " "
                                + fragment.lv1.getTag() + " "
                                + fragment.lv2.getTag());

                        fragment.popPosition.dismiss();
                        // TODO 切换位置

                        fragment.getGeoPointBystr(fragment.tvPostion.getText()
                                .toString());
                        break;
                }

            }
        });

        return convertView;
    }

    private static class ViewHolder {
        RadioButton name;
    }
}
