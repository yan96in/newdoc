package net.meluo.newdoc.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dadao.view.TitleBar;

import net.meluo.newdoc.R;

public final class StatementFragment extends BaseFragment {

	TitleBar mTitleBar;
	TextView mTvContent;

	Activity mContext;

	String content = "鉴于新医集团公司以非人工检索方式、根据您键入的关键字自动生成到第三方网页的链接，除本公司注明之服务条款外，其他一切因使用新医健康这款产品而可能遭致的意外、疏忽、侵权及其造成的损失（包括因下载被搜索链接到的第三方网站内容而感染电脑病毒），新医集团对其概不负责，亦不承担任何法律责任。";

	public static StatementFragment newInstance() {

		StatementFragment fragment = new StatementFragment();

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
		layout = (RelativeLayout) inflater.inflate(R.layout.f_textview, null);
		initView(layout);

		return layout;
	}

	protected void initView(final RelativeLayout root) {
		mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
		mTitleBar.setTitleText("免责声明");
		mTitleBar.enableLeftButton(true);
		mTitleBar.getLeftButton().setOnClickListener(this);
		this.mTvContent = (TextView) root.findViewById(R.id.ft_tv_content);
		mTvContent.setText(content);
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

		}
	}
}
