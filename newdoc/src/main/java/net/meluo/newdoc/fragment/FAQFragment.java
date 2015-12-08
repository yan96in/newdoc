package net.meluo.newdoc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dadao.view.TitleBar;

import net.meluo.newdoc.R;
import net.meluo.newdoc.bean.FAQ;

public final class FAQFragment extends BaseFragment {

    Context mContext;
    TitleBar mTitleBar;
    FAQ f;

    TextView mTvContent;

    Button btnLogout;

    public static FAQFragment newInstance(FAQ f) {
        FAQFragment fragment = new FAQFragment(f);
        return fragment;
    }

    public FAQFragment(FAQ f) {
        this.f = f;
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
        layout = (RelativeLayout) inflater.inflate(R.layout.f_faq, null);
        initView(layout);
        return layout;
    }

    protected void initView(RelativeLayout root) {
        mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
        mTitleBar.setTitleText("常见问题");

        mTitleBar.enableLeftButton(true);
        mTitleBar.getLeftButton().setOnClickListener(this);

        mTvContent = (TextView)root.findViewById(R.id.ff_tv_content);
        mTvContent.setText(f.getAnswer());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);

        switch (v.getId()) {
            case R.id.btn_left:
                this.getActivity().getSupportFragmentManager().popBackStack();
                break;

        }
    }
}
