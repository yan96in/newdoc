package net.meluo.newdoc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.bigd.utl.Checking;
import com.dadao.view.TitleBar;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.way.util.PreferenceConstants;
import com.way.util.T;

import net.meluo.newdoc.R;

public final class CommenFragment extends BaseFragment {

	Context mContext;
	TitleBar mTitleBar;

	EditText etContent;

	Button btnOK;
	String AppointmentId;

	RatingBar mRB;

	public static CommenFragment newInstance(String appointmentId) {

		CommenFragment fragment = new CommenFragment(appointmentId);
		return fragment;
	}

	public CommenFragment(String appointmentId) {
		this.AppointmentId = appointmentId;
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
		mTitleBar.setTitleText("评价");

		mTitleBar.enableLeftButton(true);
		mTitleBar.getLeftButton().setOnClickListener(this);

		etContent = (EditText) root.findViewById(R.id.ff_et_content);

		btnOK = (Button) root.findViewById(R.id.ff_btn_ok);
		btnOK.setOnClickListener(this);

		mRB = (RatingBar) root.findViewById(R.id.ff_rb);
		mRB.setStepSize(1.0f);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void sendCom() {
		if (Checking.isNullorBlank(etContent.getText().toString())) {
			T.showShort(mContext, "请编辑评论内容");
			return;
		}

		Ion.with(
				mContext,
				PreferenceConstants.BASE_URL + "/1/Appointments/"
						+ AppointmentId)
				.setBodyParameter("doctor_comment",
						etContent.getText().toString())
				.setBodyParameter("doctor_rating", (int) mRB.getRating() + "")
				.asJsonObject().setCallback(new FutureCallback<JsonObject>() {

					@Override
					public void onCompleted(Exception arg0, JsonObject data) {

						if (data == null) {
							T.showShort(mContext, "网络错误，请稍后重试");
							return;
						}

						if (data.get("retcode").getAsInt() != 0) {
							T.showShort(mContext, data.get("errmsg")
									.getAsString());
							return;
						}
						etContent.setText("");
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
			sendCom();
			break;
		}
	}
}
