package net.meluo.newdoc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bigd.utl.ImgUtl;
import com.bigd.utl.SizeUtl;

import net.meluo.newdoc.R;

import java.util.ArrayList;

public class ImgAdapter extends BaseAdapter {

    ArrayList<String> list;
    private Context mContext;
    Typeface typeFace;
    private final LayoutInflater mInflater;

    public ImgAdapter(Context context, ArrayList<String> list) {
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

    @SuppressLint({"InflateParams"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_img, null);
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Bitmap img = ImgUtl.getImageThumbnail(list.get(position),
                SizeUtl.dip2px(mContext, 60), SizeUtl.dip2px(mContext, 60));
        viewHolder.iv.setImageBitmap(img);
        viewHolder.iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 诊室简介

            }
        });

        return convertView;
    }

    private static class ViewHolder {

        ImageView iv;
    }
}
