package net.meluo.newdoc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.meluo.newdoc.R;
import net.meluo.newdoc.bean.Com;

import java.util.ArrayList;

public class ComAdapter extends BaseAdapter {

    ArrayList<Com> list;
    private Context context;
    Typeface typeFace;
    private final LayoutInflater mInflater;

    // 构造函数
    public ComAdapter(Context context, ArrayList<Com> list) {
        this.context = context;
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
            convertView = mInflater.inflate(R.layout.item_com, null);
            viewHolder.head = (ImageView) convertView
                    .findViewById(R.id.ic_iv_head);
            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.ic_tv_name);
            viewHolder.time = (TextView) convertView
                    .findViewById(R.id.ic_tv_time);
            viewHolder.content = (TextView) convertView
                    .findViewById(R.id.ic_tv_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Com c = (Com) getItem(position);
        String headUrl = c.getHead();
        viewHolder.name.setText(c.getName());
        viewHolder.time.setText(c.getTime());
        viewHolder.content.setText(c.getContent());
        return convertView;
    }

    private static class ViewHolder {
        ImageView head;
        TextView name;
        TextView time;
        TextView content;
    }
}
