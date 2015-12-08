package net.meluo.newdoc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import net.meluo.newdoc.R;
import net.meluo.newdoc.bean.Room;
import net.meluo.newdoc.fragment.DocRoomListFragment;

import java.util.ArrayList;

public class RoomIndexAdapter extends BaseAdapter {

    ArrayList<Room> list;
    private Context context;
    private final LayoutInflater mInflater;
    DocRoomListFragment fragment;
    public int check = -1;

    // 构造函数
    public RoomIndexAdapter(DocRoomListFragment fragment, ArrayList<Room> list) {

        this.fragment = fragment;
        this.context = fragment.getActivity();
        this.list = list;
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

    @SuppressLint({"InflateParams"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

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
        final Room room = (Room) getItem(position);

        viewHolder.name.setText(room.getName());

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
                RoomIndexAdapter.this.notifyDataSetChanged();
                fragment.loadRoomCatalog(room);
                fragment.currentRoom = room;
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        RadioButton name;
    }
}
