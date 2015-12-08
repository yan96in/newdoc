package net.meluo.newdoc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.bigd.utl.Checking;
import com.bigd.utl.MD5;
import com.dadao.view.TitleBar;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.way.util.L;
import com.way.util.PreferenceConstants;
import com.way.util.T;

import net.meluo.newdoc.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

public final class SignUpFragment extends BaseFragment {

	Context mContext;
	TitleBar mTitleBar;

	EditText etPhone, etPw, etRpw, etCheck;

	Button btnCheck, btnSignUp;

	private TimeCount time;

	public static SignUpFragment newInstance() {

		SignUpFragment fragment = new SignUpFragment();
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

		layout = (RelativeLayout) inflater.inflate(R.layout.f_sign_up, null);

		initView(layout);
		return layout;
	}

	protected void initView(RelativeLayout root) {
		mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
		mTitleBar.setTitleText("注册");

		mTitleBar.enableLeftButton(true);
		mTitleBar.getLeftButton().setOnClickListener(this);

		etPhone = (EditText) root.findViewById(R.id.fsu_id);
		etPw = (EditText) root.findViewById(R.id.fsu_pw);

		etRpw = (EditText) root.findViewById(R.id.fsu_rpw);

		etCheck = (EditText) root.findViewById(R.id.fsu_check);

		btnCheck = (Button) root.findViewById(R.id.fsu_btn_check);
		btnCheck.setOnClickListener(this);

		btnSignUp = (Button) root.findViewById(R.id.fsu_btn_sign_up);
		btnSignUp.setOnClickListener(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void getCheck() {

		if (Checking.isNullorBlank(etPhone.getText().toString())) {
			T.showShort(SignUpFragment.this.getActivity(), "请输入手机号");
			return;
		}

		time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
		time.start();

		Ion.with(this.getActivity(),
				PreferenceConstants.BASE_URL0 + "/Common/1/sms")
				.setMultipartParameter("mobile", etPhone.getText().toString())
				.setMultipartParameter("f", "reg").asString()
				.setCallback(new FutureCallback<String>() {

					@Override
					public void onCompleted(Exception arg0, String data) {
						time.cancel();
						btnCheck.setClickable(true);
						if (Checking.isNullorBlank(data)) {
							btnCheck.setText("获取失败，点击重试");
							return;
						}
						L.d("getCheck", data);
						try {
							JSONObject o = new JSONObject(data);
							if (o.getInt("retcode") == 0) {
								T.showShort(mContext, "获取成功，验证码稍后会发送到您的手机上。");
								btnCheck.setText("重新获取");
							} else {
								// TODO 获取失败，通知用户再次获取
								T.showShort(mContext, o.getString("errmsg"));
								btnCheck.setText("获取失败，点击重试");
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}

	private void signUp() {

		if (Checking.isNullorBlank(etPhone.getText().toString())) {
			T.showShort(SignUpFragment.this.getActivity(), "请输入手机号");
			return;
		}
		if (Checking.isNullorBlank(etPw.getText().toString())) {
			T.showShort(SignUpFragment.this.getActivity(), "请输入密码");
			return;
		}

		if (Checking.isNullorBlank(etRpw.getText().toString())) {
			T.showShort(SignUpFragment.this.getActivity(), "请重复密码");
			return;
		}
		if (!etRpw.getText().toString().equals(etPw.getText().toString())) {
			T.showShort(SignUpFragment.this.getActivity(), "请确认两次输入密码一致");
			return;
		}
		if (Checking.isNullorBlank(etCheck.getText().toString())) {
			T.showShort(SignUpFragment.this.getActivity(), "请输入验证码");
			return;
		}
		try {
			Ion.with(this.getActivity(),
					PreferenceConstants.BASE_URL0 + "/Common/1/reg")
					.setMultipartParameter("newdocid",
							etPhone.getText().toString())
					.setMultipartParameter("passwdMd5",
							MD5.getMD5(etPw.getText().toString()))
					.setMultipartParameter("mobile",
							etPhone.getText().toString())
					.setMultipartParameter("smstoken",
							etCheck.getText().toString()).asString()
					.setCallback(new FutureCallback<String>() {

						@Override
						public void onCompleted(Exception arg0, String data) {

							if (Checking.isNullorBlank(data)) {
								T.showShort(SignUpFragment.this.getActivity(),
										"网络错误，请稍后重试");
								return;
							}
							L.d("getSignUp", data);
							try {
								JSONObject o = new JSONObject(data);
								if (o.getInt("retcode") == 0) {
									// 获取成功,自动登录
									T.showShort(mContext, "注册成功");
									SignUpFragment.this.getActivity()
											.getSupportFragmentManager()
											.popBackStack();

								} else {
									// TODO 获取失败，通知用户再次获取
									T.showShort(mContext, o.getString("errmsg"));
								}

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		case R.id.fsu_btn_check:
			// TODO 获取验证码
			// TODO 开启倒计时

			getCheck();
			break;

		case R.id.fsu_btn_sign_up:

			// TODO 注册请求
			signUp();

			break;
		}
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			btnCheck.setText("重新验证");
			btnCheck.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			btnCheck.setClickable(false);
			btnCheck.setText(millisUntilFinished / 1000 + "秒");
		}
	}
}
