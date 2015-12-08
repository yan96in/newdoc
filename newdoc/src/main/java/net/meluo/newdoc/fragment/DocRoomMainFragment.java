package net.meluo.newdoc.fragment;

import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigd.utl.Checking;
import com.dadao.view.TitleBar;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.way.util.PreferenceConstants;
import com.way.util.T;

import net.meluo.newdoc.R;
import net.meluo.newdoc.adapter.DepartmentAdapter;
import net.meluo.newdoc.adapter.DocAdapter;
import net.meluo.newdoc.bean.Catalog;
import net.meluo.newdoc.bean.Doc;
import net.meluo.newdoc.bean.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class DocRoomMainFragment extends BaseFragment {

    TitleBar mTitleBar;
    TextView mTvEmpty;
    TextView tvName, tvLocation, tvFunctions;

    PullToRefreshListView mPullListView;
    ListView mListView;
    Button btnDepartment;
    Context mContext;

    ArrayList<Doc> list = new ArrayList<Doc>();
    DocAdapter adapter = null;

    public PopupWindow popDepartment = null;
    View vPosition = null;
    ListView lvDepartment;
    ArrayList<Catalog> listDepartment = new ArrayList<Catalog>();
    DepartmentAdapter adpDepartment = null;
    ProgressBar pb;
    Room room = null;
    int id;

    public static DocRoomMainFragment newInstance(Room room, int id) {

        DocRoomMainFragment fragment = new DocRoomMainFragment(room, id);

        return fragment;
    }

    public static void actionStart() {

    }

    public DocRoomMainFragment(Room room, int id) {
        this.room = room;
        mContext = this.getActivity();
        this.id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = this.getActivity();
        RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.f_doc_room_main, null);
        initView(layout);
        return layout;
    }

    protected void initView(RelativeLayout root) {
        mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
        mTitleBar.setTitleText("预约挂号");
        mTitleBar.enableLeftButton(true);
        mTitleBar.getLeftButton().setOnClickListener(this);

        this.tvName = (TextView) root.findViewById(R.id.fdrm_tv_name);
        this.tvLocation = (TextView) root.findViewById(R.id.fdrm_tv_location);
        this.tvFunctions = (TextView) root.findViewById(R.id.fdrm_tv_functions);

        tvName.setText(room.getName());
        tvLocation.setText(room.getAddress());
        tvFunctions.setText(room.getDetail());

        mPullListView = (PullToRefreshListView) root
                .findViewById(R.id.fdrm_plv_list);
        mPullListView.setScrollLoadEnabled(false);
        mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                refresh("0");
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // refresh();
            }
        });
        mListView = mPullListView.getRefreshableView();
        mListView.setDivider(null);
        adapter = new DocAdapter(this.getActivity(), list, R.id.adri_content);
        mListView.setAdapter(adapter);

        btnDepartment = (Button) root.findViewById(R.id.fdrm_btn_department);
        btnDepartment.setOnClickListener(this);
        // 切换位置弹出框
        vPosition = LayoutInflater.from(mContext).inflate(
                R.layout.pop_department, null, false);
        lvDepartment = (ListView) vPosition.findViewById(R.id.pd_list);
        pb = (ProgressBar) vPosition.findViewById(R.id.pd_pb);
        popDepartment = new PopupWindow(vPosition, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        popDepartment.setFocusable(true);
        popDepartment.setBackgroundDrawable(new PaintDrawable());
        popDepartment.setAnimationStyle(R.style.AnimTop);

        adpDepartment = new DepartmentAdapter(this, listDepartment);
        lvDepartment.setAdapter(adpDepartment);

        // 加载医生列表
        if (list.isEmpty()) {
            mPullListView.doPullRefreshing(true, 1);
        }

    }

    private void loadCatalog() {

        pb.setVisibility(View.VISIBLE);
        listDepartment.clear();
        Ion.with(
                this.getActivity(),
                PreferenceConstants.BASE_URL + "/1/Rooms/" + room.getId()
                        + "?action=index").asString()
                .setCallback(new FutureCallback<String>() {

                    @Override
                    public void onCompleted(Exception arg0, String data) {
                        pb.setVisibility(View.GONE);
                        if (Checking.isNullorBlank(data)) {
                            T.showShort(mContext, "网络错误，请稍后重试");
                            return;
                        }
                        try {
                            JSONObject o = new JSONObject(data);
                            JSONObject d = o.getJSONObject("data");
                            JSONArray a = d.getJSONArray("catalogs");
                            int num = a.length();
                            for (int i = 0; i < num; i++) {
                                Catalog c = new Catalog();
                                c.fromJSONObject(a.getJSONObject(i));
                                listDepartment.add(c);
                            }
                            adpDepartment.notifyDataSetChanged();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });

    }

    public void refresh(String catalogid) {

        list.clear();
        Ion.with(
                this.getActivity(),
                PreferenceConstants.BASE_URL + "/1/Rooms/" + room.getId()
                        + "?action=doclist" + "&catalogid=" + catalogid)
                .asString().setCallback(new FutureCallback<String>() {

            @Override
            public void onCompleted(Exception arg0, String data) {
                mPullListView.onPullDownRefreshComplete();
                if (Checking.isNullorBlank(data)) {
                    T.showShort(mContext, "网络错误，请稍后重试");
                    return;
                }
                try {
                    JSONObject o = new JSONObject(data);
                    JSONObject d = o.getJSONObject("data");
                    JSONArray a = d.getJSONArray("subs");
                    int num = a.length();
                    for (int i = 0; i < num; i++) {
                        Doc c = new Doc();
                        c.fromJSONObject(a.getJSONObject(i));
                        list.add(c);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_left:
                this.getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.fdrm_btn_department:
                popDepartment.showAsDropDown(btnDepartment);
                loadCatalog();
                break;
        }
    }
}
