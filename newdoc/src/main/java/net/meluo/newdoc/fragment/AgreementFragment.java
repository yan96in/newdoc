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

public final class AgreementFragment extends BaseFragment {

	TitleBar mTitleBar;
	TextView mTvContent;

	Activity mContext;

	String content = "你好，欢迎你使用新医诊室APP版(以下简称“本平台””)。本平台是新医集团公司开发的为公众提供医生在线预约等网络服务的技术服务平台，你可通过本平台轻松进行预约医生、评价医生等。 本协议由新医集团与使用本平台服务的用户订立，所有使用本平台服务的用户必须承诺同意并遵守本协议，特别是以粗体字予以标注的条款，请你务必注意，并确保充分阅读、理解并自愿接受。你阅读本协议并点击\"同意\"按钮或以任何方式参与、注册、使用本平台的服务均应视为你完全接受本协议的所有约定，并同意成为具有约束力的本协议的一方，若你不同意接受其中的任意条款，请立即停止使用本平台。否则，你的继续使用行为将视为你接受本协议的全部条款包括更新修改后的条款。 本平台内的线上规则、声明、通知及说明（如有）均应视为本协议不可分割的组成部分，与本协议具有同等的法律效力。";

	public static AgreementFragment newInstance() {

		AgreementFragment fragment = new AgreementFragment();

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
		mTitleBar.setTitleText("服务协议");
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
