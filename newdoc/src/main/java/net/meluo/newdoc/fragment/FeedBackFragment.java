package net.meluo.newdoc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.bigd.utl.Checking;
import com.dadao.view.TitleBar;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.way.util.PreferenceConstants;
import com.way.util.T;

import net.meluo.newdoc.R;

import org.json.JSONException;
import org.json.JSONObject;

public final class FeedBackFragment extends BaseFragment {

	Context mContext;
	TitleBar mTitleBar;

	EditText etContent;

	Button btnOK;

	public static FeedBackFragment newInstance() {

		FeedBackFragment fragment = new FeedBackFragment();
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

		layout = (RelativeLayout) inflater.inflate(R.layout.f_feedback, null);

		initView(layout);
		return layout;
	}

	protected void initView(RelativeLayout root) {
		mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
		mTitleBar.setTitleText("意见反馈");

		mTitleBar.enableLeftButton(true);
		mTitleBar.getLeftButton().setOnClickListener(this);

		etContent = (EditText) root.findViewById(R.id.ff_et_content);

		btnOK = (Button) root.findViewById(R.id.ff_btn_ok);
		btnOK.setOnClickListener(this);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void feedback() {

		if (Checking.isNullorBlank(etContent.getText().toString())) {
			T.showShort(FeedBackFragment.this.getActivity(), "请编辑反馈内容。");
			return;
		}

		Ion.with(this.getActivity(),
				PreferenceConstants.BASE_URL + "/1/Feedbacks")
				.setBodyParameter("content", etContent.getText().toString())
				.asString().setCallback(new FutureCallback<String>() {

					@Override
					public void onCompleted(Exception arg0, String data) {

						if (Checking.isNullorBlank(data)) {
							T.showShort(FeedBackFragment.this.getActivity(),
									"网络错误，请稍后重试");
							return;
						}
						try {
							JSONObject o = new JSONObject(data);
							if (o.getInt("retcode") == 0) {
								T.showShort(mContext, "反馈成功");

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

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);

		switch (v.getId()) {
		case R.id.btn_left:
			this.getActivity().getSupportFragmentManager().popBackStack();
			break;
		case R.id.ff_btn_ok:
			feedback();
			break;
		}
	}
}
