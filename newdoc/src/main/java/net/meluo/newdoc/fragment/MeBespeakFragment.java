package net.meluo.newdoc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dadao.view.TitleBar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.way.util.PreferenceConstants;
import com.way.util.T;

import net.meluo.newdoc.R;
import net.meluo.newdoc.adapter.AppointmentAdapter;
import net.meluo.newdoc.bean.Appointment;

import java.util.ArrayList;

public final class MeBespeakFragment extends BaseFragment {

	Context mContext;
	TitleBar mTitleBar;

	PullToRefreshListView mPullListView;
	ListView mListView;
	AppointmentAdapter mAppointmentAdapter;
	ArrayList<Appointment> listAppointment = new ArrayList<Appointment>();

	public static MeBespeakFragment newInstance() {

		MeBespeakFragment fragment = new MeBespeakFragment();
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

		layout = (RelativeLayout) inflater.inflate(R.layout.f_me_bespeak, null);

		initView(layout);
		return layout;
	}

	protected void initView(RelativeLayout root) {
		mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
		mTitleBar.setTitleText("我的预约");

		mTitleBar.enableLeftButton(true);
		mTitleBar.getLeftButton().setOnClickListener(this);

		mPullListView = (PullToRefreshListView) root
				.findViewById(R.id.fmb_plv_list);

		mPullListView.setScrollLoadEnabled(false);
		mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				refresh();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// refresh();
			}
		});
		mListView = mPullListView.getRefreshableView();

		mListView.setDivider(null);

		mAppointmentAdapter = new AppointmentAdapter(mContext, listAppointment);
		mListView.setAdapter(mAppointmentAdapter);

		mPullListView.doPullRefreshing(true, 0);
	}

	private void refresh() {

		listAppointment.clear();
		Ion.with(mContext, PreferenceConstants.BASE_URL + "/1/Appointments")
				.asJsonObject().setCallback(new FutureCallback<JsonObject>() {

					@Override
					public void onCompleted(Exception arg0, JsonObject data) {
						mPullListView.onPullDownRefreshComplete();
						if (data == null) {
							T.showShort(mContext, "网络错误，请稍后重试");
							return;
						}

						if (data.get("retcode").getAsInt() != 0) {
							T.showShort(mContext, "暂无预约");
							return;
						}

						if (data.get("data").getAsJsonObject().get("cnt")
								.getAsInt() <= 0) {
							T.showShort(mContext, "暂无预约");
							return;
						}
						JsonArray a = data.get("data").getAsJsonObject()
								.get("subs").getAsJsonArray();
						int num = a.size();
						if (num >= 0) {
							for (int i = 0; i < num; i++) {
								Appointment d = new Appointment();
								d.fromJSONString(a.get(i).toString());
								listAppointment.add(d);
							}
							mAppointmentAdapter.notifyDataSetChanged();
						}
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
