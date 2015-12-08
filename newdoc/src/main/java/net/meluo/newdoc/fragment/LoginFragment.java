package net.meluo.newdoc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigd.utl.Checking;
import com.dadao.view.DialogUtil;
import com.dadao.view.TitleBar;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.way.util.PreferenceConstants;
import com.way.util.T;
import com.ypy.eventbus.EventBus;

import net.meluo.newdoc.R;
import net.meluo.newdoc.activity.BaseActivity;
import net.meluo.newdoc.app.NDApp;
import net.meluo.newdoc.app.NDEvent;

public final class LoginFragment extends BaseFragment {

	Context mContext;
	TitleBar mTitleBar;

	Button btnLogin, btnSignUp;

	ImageView btnWebchat;
	EditText etId, etPw;
	TextView mTvFindBack;

	PopupWindow mLoadingDialog = null;

	public static LoginFragment newInstance() {

		LoginFragment fragment = new LoginFragment();
		return fragment;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

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
		layout = (RelativeLayout) inflater.inflate(R.layout.f_login, null);
		initView(layout);
		return layout;
	}

	protected void initView(RelativeLayout root) {

		mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
		mTitleBar.setTitleText("登录");

		mTitleBar.enableLeftButton(true);
		mTitleBar.getLeftButton().setOnClickListener(this);

		btnLogin = (Button) root.findViewById(R.id.fl_btn_login);
		btnLogin.setOnClickListener(this);

		btnSignUp = (Button) root.findViewById(R.id.fl_btn_sign_up);
		btnSignUp.setOnClickListener(this);

		btnWebchat = (ImageView) root.findViewById(R.id.fl_btn_webchat);
		btnWebchat.setOnClickListener(this);

		etId = (EditText) root.findViewById(R.id.fl_id);
		etPw = (EditText) root.findViewById(R.id.fl_pw);

		mTvFindBack = (TextView) root.findViewById(R.id.fl_btn_find_back_pw);
		mTvFindBack.setOnClickListener(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void login() {

		if (Checking.isNullorBlank(etId.getText().toString())) {
			T.showShort(LoginFragment.this.getActivity(), "请输入用户名");
			return;
		}
		if (Checking.isNullorBlank(etPw.getText().toString())) {
			T.showShort(LoginFragment.this.getActivity(), "请输入密码");
			return;
		}

		mLoadingDialog = DialogUtil.createLoadingDialog2(getActivity(),
				"登录中...");
		mLoadingDialog.showAtLocation(this.getActivity().getCurrentFocus(),
				Gravity.CENTER, 0, 0);
		try {
			Ion.with(this.getActivity(),
					PreferenceConstants.BASE_URL0 + "/Common/1/login")
					.basicAuthentication(etId.getText().toString(),
							etPw.getText().toString())
					.setBodyParameter("newdocid", etId.getText().toString())
					.asJsonObject()
					.setCallback(new FutureCallback<JsonObject>() {
						@Override
						public void onCompleted(Exception e, JsonObject result) {
							mLoadingDialog.dismiss();
							if (e != null) {
								T.showShort(mContext, "网络错误，请稍后重试");
								return;
							}
							if (result.isJsonNull()) {
								T.showShort(mContext, "网络错误，请稍后重试");
								return;
							}
							if (result.get("retcode").getAsInt() != 0) {
								T.showShort(mContext, "登录失败，请稍检查登录名/密码是否正确。");
								return;
							}

							NDApp.getInstance().newdocid = result
									.get("retcode").getAsString();
							refreshLogin();
						}
					});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void refreshLogin() {
		EventBus.getDefault().post(new NDEvent(NDEvent.E_ND_LOGIN));
		this.getActivity().getSupportFragmentManager().popBackStack();
		// Intent msgIntent = new Intent();
		// msgIntent.setAction(MeIndexFragment.ND_LOGIN_ACTION);
		// this.getActivity().sendBroadcast(msgIntent);
	}

	@Override
	public void onEvent(NDEvent event) {
		// TODO Auto-generated method stub
		super.onEvent(event);
		switch (event.getEvent()) {
		case NDEvent.E_WX_LOGIN:
			mLoadingDialog.dismiss();
			this.getActivity().getSupportFragmentManager().popBackStack();
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);

		switch (v.getId()) {
		case R.id.btn_left:
			this.getActivity().getSupportFragmentManager().popBackStack();
			break;
		case R.id.fl_btn_login:
			login();
			break;
		case R.id.fl_btn_webchat:
			final SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "carjob_wx_login";
			NDApp.getInstance().getWXApi().sendReq(req);

			EventBus.getDefault().post(new NDEvent(NDEvent.WX_LOADING));
			this.getActivity().getSupportFragmentManager().popBackStack();
			break;
		case R.id.fl_btn_sign_up:
			((BaseActivity) this.getActivity()).addFragmentToStack(
					R.id.ami_content, SignUpFragment.newInstance(), true);
			break;
		case R.id.fl_btn_find_back_pw:
			((BaseActivity) this.getActivity()).addFragmentToStack(
					R.id.ami_content, FindBackPWFragment.newInstance(), true);
			break;
		}
	}

}
