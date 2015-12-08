package net.meluo.newdoc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshListView;

import net.meluo.newdoc.R;

public final class RoomListFragment extends BaseFragment {

    PullToRefreshListView mPullListView;
    ListView mListView;

    Context mContext;

    public static RoomListFragment newInstance() {

        RoomListFragment fragment = new RoomListFragment();
        return fragment;
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
                R.layout.f_doc_room_room_list, null);
        initView(layout);

        return layout;
    }

    protected void initView(RelativeLayout root) {

        mPullListView = (PullToRefreshListView) root
                .findViewById(R.id.fdrrm_plv_list);
        mPullListView.setScrollLoadEnabled(true);
        mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // refresh();
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // refresh();
            }
        });
        mListView = mPullListView.getRefreshableView();

        mListView.setDivider(null);

        // adapter = new DocAdapter(this.getActivity(), list);
        // mListView.setAdapter(adapter);

    }

    public void refresh() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.acri_btn_doc_self:
                break;
            case R.id.acri_btn_qa:
                break;
            case R.id.acri_btn_bespeak:
                // TODO 这中添加方式还是不方便

                break;
        }
    }
}
