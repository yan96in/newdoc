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
import com.koushikdutta.ion.Ion;
import com.ypy.eventbus.EventBus;

import net.meluo.newdoc.R;
import net.meluo.newdoc.activity.BaseActivity;
import net.meluo.newdoc.app.NDEvent;

public final class SetFragment extends BaseFragment {

	Context mContext;
	TitleBar mTitleBar;
	TextView mTvAS;
	Button btnLogout;

	public static SetFragment newInstance() {

		SetFragment fragment = new SetFragment();
		return fragment;
	}

	// 在需要分享的地方添加代码：

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

		layout = (RelativeLayout) inflater.inflate(R.layout.f_set, null);

		initView(layout);
		return layout;
	}

	protected void initView(RelativeLayout root) {
		mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
		mTitleBar.setTitleText("登录");

		mTitleBar.enableLeftButton(true);
		mTitleBar.getLeftButton().setOnClickListener(this);

		TextView t = (TextView) root.findViewById(R.id.fs_agreement);
		t.setVisibility(View.GONE);

		this.mTvAS = (TextView) root.findViewById(R.id.fs_about_us);
		mTvAS.setOnClickListener(this);

		btnLogout = (Button) root.findViewById(R.id.fs_btn_logout);
		btnLogout.setOnClickListener(this);

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
		case R.id.fs_about_us:
			((BaseActivity) this.getActivity()).addFragmentToStack(
					R.id.ami_content, UsIndexFragment.newInstance(), true);
			break;

		case R.id.fs_btn_logout:
			// 注销
			Ion.getDefault(mContext).getCookieMiddleware().clear();
			EventBus.getDefault().post(new NDEvent(NDEvent.E_LOGOUT));
			this.getActivity().getSupportFragmentManager().popBackStack();
			break;

		}
	}
}
