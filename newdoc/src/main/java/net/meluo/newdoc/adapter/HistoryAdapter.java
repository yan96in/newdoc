package net.meluo.newdoc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.meluo.newdoc.R;
import net.meluo.newdoc.bean.History;
import net.meluo.newdoc.fragment.BaseFragment;

import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter {

    public static final int INDEX_PROVINCE = 0;
    public static final int INDEX_CITY = 1;
    public static final int INDEX_COUNTY = 2;

    ArrayList<History> list;
    private Context context;
    Typeface typeFace;
    private final LayoutInflater mInflater;

    BaseFragment fragment;

    public int check = 0;

    // 构造函数
    public HistoryAdapter(BaseFragment fragment, ArrayList<History> list) {

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
    public View getView(final int position, View convertView,
                        final ViewGroup parent) {

        final ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_history, null);

            viewHolder.rlTitle = (RelativeLayout) convertView
                    .findViewById(R.id.ih_rl_title);
            viewHolder.title = (TextView) convertView
                    .findViewById(R.id.ih_tv_title);
            viewHolder.date = (TextView) convertView
                    .findViewById(R.id.ih_tv_date);
            viewHolder.right = (ImageView) convertView
                    .findViewById(R.id.ih_iv_right);
            viewHolder.content = (TextView) convertView
                    .findViewById(R.id.ih_tv_content);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final History h = (History) getItem(position);

        viewHolder.title.setText(h.getTitle());
        viewHolder.date.setText(h.getDate());
        viewHolder.content.setVisibility(View.GONE);
        viewHolder.rlTitle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (viewHolder.content.isShown()) {
                    viewHolder.content.setVisibility(View.GONE);

                    // TODO 获取病例数据
                } else {
                    viewHolder.content.setVisibility(View.VISIBLE);
                }

            }
        });

        return convertView;
    }

    private static class ViewHolder {
        RelativeLayout rlTitle;
        TextView title;
        TextView date;
        ImageView right;
        TextView content;
    }
}
