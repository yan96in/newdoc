package net.meluo.newdoc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dadao.view.TitleBar;
import com.lee.pullrefresh.ui.PullToRefreshListView;

import net.meluo.newdoc.R;

public final class CommunityIndexFragment extends BaseFragment {

    TitleBar mTitleBar;
    TextView mTvEmpty;

    PullToRefreshListView mPullListView;
    ListView mListView;

    Button btnDocSelf, btnQA, btnBespeak;

    Context mContext;

    public static CommunityIndexFragment newInstance() {

        CommunityIndexFragment fragment = new CommunityIndexFragment();
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
        RelativeLayout layout;
        layout = (RelativeLayout) inflater.inflate(R.layout.f_ds_index, null);
        initView(layout);
        return layout;
    }

    protected void initView(RelativeLayout root) {
        mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
        mTitleBar.setTitleText("社区交流");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
