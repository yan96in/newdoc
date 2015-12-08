package net.meluo.newdoc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders.Any.B;
import com.way.util.L;
import com.way.util.PreferenceConstants;
import com.way.util.T;

import net.meluo.core.User;
import net.meluo.newdoc.R;
import net.meluo.newdoc.activity.BaseActivity;
import net.meluo.newdoc.bean.Doc;
import net.meluo.newdoc.fragment.DocRoomBespeakFragment;

import java.util.ArrayList;

public class DocAdapter extends BaseAdapter {

    ArrayList<Doc> list;
    private Context mContext;
    Typeface typeFace;
    private final LayoutInflater mInflater;
    int id;

    // 构造函数
    public DocAdapter(Context context, ArrayList<Doc> list, int id) {
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

    @SuppressLint({"InflateParams"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_doc, null);
            viewHolder.head = (ImageView) convertView
                    .findViewById(R.id.md_iv_head);
            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.md_tv_name);
            viewHolder.department = (TextView) convertView
                    .findViewById(R.id.md_tv_department);
            viewHolder.specialty = (TextView) convertView
                    .findViewById(R.id.md_tv_specialty);
            viewHolder.time = (TextView) convertView
                    .findViewById(R.id.id_tv_time);
            viewHolder.bespeack = (Button) convertView
                    .findViewById(R.id.id_btn_speak);
            viewHolder.btnFocus = (Button) convertView
                    .findViewById(R.id.md_btn_focus);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Doc doc = (Doc) getItem(position);
        Ion.with(mContext).load(doc.getHead()).withBitmap()
                .error(R.drawable.ic_tem_head).intoImageView(viewHolder.head);
        L.d(doc.getHead() + "_____");
        viewHolder.name.setText(doc.getName());
        viewHolder.department.setText("科室：" + doc.getDepartment());
        viewHolder.specialty.setText("擅长：" + doc.getSpecialty());
        if (User.isLogin()) {
            if (doc.isFocus()) {
                viewHolder.btnFocus.setText("取消关注");
                viewHolder.btnFocus
                        .setBackgroundResource(R.drawable.bg_btn_def_xml_1);
            } else {
                viewHolder.btnFocus.setText("关注");
                viewHolder.btnFocus
                        .setBackgroundResource(R.drawable.bg_btn_yellow_xml);
            }
        }

        viewHolder.btnFocus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!User.isLogin()) {
                    T.showShort(mContext, "请先登录");
                } else {
                    focus(v, doc);
                }
            }
        });

        viewHolder.bespeack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 进入预约界面

                ((BaseActivity) mContext).addFragmentToStack(id,
                        DocRoomBespeakFragment.newInstance(doc, id), true);
            }
        });

        return convertView;
    }

    private void focus(final View v, final Doc doc) {

        B b = Ion.with(mContext, PreferenceConstants.BASE_URL + "/1/Focus");

        if (doc.isFocus()) {
            b.setBodyParameter("action", "del");
        }
        b.setBodyParameter("doctorid", doc.getId() + "").asJsonObject()
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
                        if (doc.isFocus()) {
                            doc.setFocus(false);
                            ((Button) v).setText("关注");
                            ((Button) v)
                                    .setBackgroundResource(R.drawable.bg_btn_yellow_xml);

                        } else {
                            doc.setFocus(true);
                            ((Button) v).setText("取消关注");
                            ((Button) v)
                                    .setBackgroundResource(R.drawable.bg_btn_def_xml_1);
                        }
                    }
                });
    }

    private static class ViewHolder {
        ImageView head;
        TextView name;
        TextView department;
        TextView specialty;
        TextView time;
        Button bespeack;
        Button btnFocus;
    }
}
