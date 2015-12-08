package net.meluo.newdoc.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigd.utl.InListView;
import com.dadao.view.TitleBar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.way.util.PreferenceConstants;
import com.way.util.T;

import net.meluo.newdoc.R;
import net.meluo.newdoc.adapter.FAQAdapter;
import net.meluo.newdoc.adapter.SelfPointAdapter;
import net.meluo.newdoc.app.NDEvent;
import net.meluo.newdoc.bean.Disease;
import net.meluo.newdoc.bean.FAQ;

import java.util.ArrayList;

public final class DocSelfDetailFragment extends BaseFragment {

	TitleBar mTitleBar;
	TextView mTvDetail, mTvTreat, mTvOther;

	Activity mContext;

	ArrayList<String> mListPoint = new ArrayList<String>();
	ListView mLvPoint, mLvDisease;
	SelfPointAdapter mSA;

	InListView mListCq;
	FAQAdapter mFAQAdapter;
	ArrayList<FAQ> listFAQ = new ArrayList<FAQ>();

	Disease disease;

	public static DocSelfDetailFragment newInstance(Disease d) {

		DocSelfDetailFragment fragment = new DocSelfDetailFragment(d);

		return fragment;
	}

	public DocSelfDetailFragment(Disease d) {
		disease = d;
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
		layout = (RelativeLayout) inflater.inflate(R.layout.f_doc_self_detail,
				null);
		initView(layout);

		return layout;
	}

	protected void initView(final RelativeLayout root) {
		mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
		mTitleBar.setTitleText(disease.getName());
		mTitleBar.enableLeftButton(true);
		mTitleBar.getLeftButton().setOnClickListener(this);

		mTitleBar.enableLeftButton(true);
		mTitleBar.getLeftButton().setOnClickListener(this);
		mTvDetail = (TextView) root.findViewById(R.id.fdsd_tv_detail);
		mTvDetail.setText(disease.getDetail());
		mTvTreat = (TextView) root.findViewById(R.id.fdsd_tv_treat);
		mTvTreat.setText(disease.getTreat());

		mTvOther = (TextView) root.findViewById(R.id.fdsd_tv_other);
		mTvOther.setText(disease.getOther());

		this.mListCq = (InListView) root.findViewById(R.id.fai_lv_cq);
		mFAQAdapter = new FAQAdapter(this, listFAQ);
		mListCq.setAdapter(mFAQAdapter);
		refresh();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void refresh() {

		Ion.with(mContext,
				PreferenceConstants.BASE_URL + "/1/Faqs?action=index")
				.asJsonObject().setCallback(new FutureCallback<JsonObject>() {

					@Override
					public void onCompleted(Exception arg0, JsonObject data) {

						if (data == null) {
							T.showShort(mContext, "网络错误，请稍后重试");
							return;
						}

						if (data.get("retcode").getAsInt() != 0) {
							T.showShort(mContext, "暂无常见问题");
							return;
						}

						if (data.get("data").getAsJsonObject().get("cnt")
								.getAsInt() <= 0) {
							T.showShort(mContext, "暂无常见问题");
							return;
						}
						JsonArray a = data.get("data").getAsJsonObject()
								.get("subs").getAsJsonArray();
						int num = a.size();
						if (num >= 0) {
							for (int i = 0; i < num; i++) {
								FAQ q = new FAQ();
								q.fromJSONString(a.get(i).toString());
								listFAQ.add(q);
							}
							mFAQAdapter.notifyDataSetChanged();
						}
					}
				});
	}

	@Override
	public void onEvent(NDEvent event) {
		// TODO Auto-generated method stub
		super.onEvent(event);
		switch (event.getEvent()) {

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_left:
			this.getActivity().getSupportFragmentManager().popBackStack();
			break;
		case R.id.fdrdc_rb_com:

			break;
		case R.id.fdrdc_rb_my_com:

			break;

		}
	}
}
