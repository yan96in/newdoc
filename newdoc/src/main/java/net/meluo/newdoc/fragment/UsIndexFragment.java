package net.meluo.newdoc.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigd.utl.DeviceUtil;
import com.dadao.view.TitleBar;

import net.meluo.newdoc.R;
import net.meluo.newdoc.activity.BaseActivity;

public final class UsIndexFragment extends BaseFragment {

	Context mContext;
	TitleBar mTitleBar;

	TextView mTvVersion;

	RelativeLayout mRlFeedBack, mRlPhone, mRlAgreement, mRlStatement;

	public static UsIndexFragment newInstance() {

		UsIndexFragment fragment = new UsIndexFragment();
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

		layout = (RelativeLayout) inflater
				.inflate(R.layout.f_me_us_index, null);

		initView(layout);
		return layout;
	}

	protected void initView(RelativeLayout root) {
		mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
		mTitleBar.setTitleText("关于我们");

		mTitleBar.enableLeftButton(true);
		mTitleBar.getLeftButton().setOnClickListener(this);

		mTvVersion = (TextView) root.findViewById(R.id.fmui_tv_version);
		mTvVersion.setText("新医诊室(" + DeviceUtil.getVersion(mContext) + ")");

		mRlFeedBack = (RelativeLayout) root.findViewById(R.id.fmui_rl_feedback);
		mRlFeedBack.setOnClickListener(this);
		mRlPhone = (RelativeLayout) root.findViewById(R.id.fmui_rl_phone);
		mRlPhone.setOnClickListener(this);
		mRlAgreement = (RelativeLayout) root
				.findViewById(R.id.fmui_rl_agreement);
		mRlAgreement.setOnClickListener(this);
		mRlStatement = (RelativeLayout) root
				.findViewById(R.id.fmui_rl_statement);
		mRlStatement.setOnClickListener(this);
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
		case R.id.fmui_rl_feedback:
			((BaseActivity) this.getActivity()).addFragmentToStack(
					R.id.ami_content, FeedBackFragment.newInstance(), true);
			break;
		case R.id.fmui_rl_phone:
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"));
			startActivity(intent);
			break;
		case R.id.fmui_rl_agreement:
			((BaseActivity) this.getActivity()).addFragmentToStack(
					R.id.ami_content, AgreementFragment.newInstance(), true);

			break;
		case R.id.fmui_rl_statement:
			((BaseActivity) this.getActivity()).addFragmentToStack(
					R.id.ami_content, StatementFragment.newInstance(), true);

			break;
		}
	}
}
