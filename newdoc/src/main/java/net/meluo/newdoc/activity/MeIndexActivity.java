package net.meluo.newdoc.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.dadao.view.TitleBar;
import com.lee.pullrefresh.ui.PullToRefreshListView;

import net.meluo.newdoc.R;
import net.meluo.newdoc.fragment.MeIndexFragment;

public class MeIndexActivity extends BaseActivity {

    TitleBar mTitleBar;
    TextView mTvEmpty;

    PullToRefreshListView mPullListView;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_me_index);
        initView();
    }

    @Override
    protected void initView() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ami_content, MeIndexFragment.newInstance())
                .commit();

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}
