package net.meluo.newdoc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.meluo.newdoc.R;
import net.meluo.newdoc.bean.Catalog;
import net.meluo.newdoc.fragment.AskMainFragment;

import java.util.ArrayList;

public class CatalogAdapter extends BaseAdapter {

    ArrayList<Catalog> list;
    private Context context;
    Typeface typeFace;
    private final LayoutInflater mInflater;
    AskMainFragment fragment;

    // 构造函数
    public CatalogAdapter(AskMainFragment fragment, ArrayList<Catalog> list) {
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

    @SuppressLint({"InflateParams"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_catalog, null);
            viewHolder.btn = (RelativeLayout) convertView
                    .findViewById(R.id.id_wrap);
            viewHolder.department = (TextView) convertView
                    .findViewById(R.id.id_tv_department);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Catalog c = list.get(position);
        viewHolder.department.setText(c.getName());

        viewHolder.btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 筛选医生列表
                fragment.btnDepartment.setText(c.getName());
                fragment.btnDepartment.setTag(c.getId());
                fragment.popDepartment.dismiss();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        RelativeLayout btn;
        TextView department;
    }
}
