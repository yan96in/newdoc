package net.meluo.newdoc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.dadao.view.TitleBar;

import net.meluo.newdoc.R;

public final class WebFragment extends BaseFragment {

	Context mContext;
	TitleBar mTitleBar;

	WebView mWV;

	String url = "";

	public static WebFragment newInstance(String url) {

		WebFragment fragment = new WebFragment(url);
		return fragment;
	}

	public WebFragment(String url) {
		this.url = url;
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
		layout = (RelativeLayout) inflater.inflate(R.layout.f_web, null);
		initView(layout);
		return layout;
	}

	protected void initView(RelativeLayout root) {
		mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
		mTitleBar.setTitleText("活动详情");

		mTitleBar.enableLeftButton(true);
		mTitleBar.getLeftButton().setOnClickListener(this);

		this.mWV = (WebView) root.findViewById(R.id.fw_wv_content);
		mWV.loadUrl(url);
		mWV.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
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
		super.onClick(v);

		switch (v.getId()) {
		case R.id.btn_left:
			this.getActivity().getSupportFragmentManager().popBackStack();
			break;

		}
	}

}
