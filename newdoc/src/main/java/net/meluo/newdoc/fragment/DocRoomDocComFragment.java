package net.meluo.newdoc.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigd.utl.Checking;
import com.dadao.view.TitleBar;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.way.util.PreferenceConstants;
import com.way.util.T;

import net.meluo.newdoc.R;
import net.meluo.newdoc.adapter.ComAdapter;
import net.meluo.newdoc.adapter.RoomListPagerAdapter;
import net.meluo.newdoc.bean.Com;
import net.meluo.newdoc.bean.Doc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.smilecity.viewpagerindicator.UnderlinePageIndicator;

public final class DocRoomDocComFragment extends BaseFragment {

	TitleBar mTitleBar;
	TextView mTvEmpty;

	PullToRefreshListView mPullListView;
	ListView mListView;
	ArrayList<Com> listCom = new ArrayList<Com>();
	ComAdapter mComAdapter;

	PullToRefreshListView mPullListView2;
	ListView mListView2;
	ArrayList<Com> listCom2 = new ArrayList<Com>();
	ComAdapter mComAdapter2;

	RadioGroup radioGroup;
	RadioButton rbtnCom;
	RadioButton rbtnMyCom;

	RoomListPagerAdapter mPagerAdapter;
	ViewPager mPager;
	UnderlinePageIndicator mIndicator;

	EditText mCommon;
	Button mBtnSend;

	Activity mContext;

	Doc doc;

	int type = 0;

	TextView tvname, tvCatalog, tvGoodAt;

	ImageView mIvHead;

	public static DocRoomDocComFragment newInstance(Doc doc) {
		DocRoomDocComFragment fragment = new DocRoomDocComFragment(doc);
		return fragment;
	}
	public DocRoomDocComFragment() {

	}

	public DocRoomDocComFragment(Doc doc) {
		this.doc = doc;
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
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.f_doc_room_doc_com, null);
		initView(layout);
		return layout;
	}

	protected void initView(RelativeLayout root) {
		mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
		mTitleBar.setTitleText("用户评价");
		mTitleBar.enableLeftButton(true);
		mTitleBar.getLeftButton().setOnClickListener(this);

		this.tvname = (TextView) root.findViewById(R.id.md_tv_name);
		this.tvCatalog = (TextView) root.findViewById(R.id.md_tv_department);
		this.tvGoodAt = (TextView) root.findViewById(R.id.md_tv_specialty);
		tvname.setText(doc.getName());
		tvCatalog.setText("科室：" + doc.getDepartment());
		tvGoodAt.setText("擅长：" + doc.getSpecialty());

		this.mIvHead = (ImageView)root.findViewById(R.id.md_iv_head);

		Ion.with(mContext).load(doc.getHead()).withBitmap()
				.error(R.drawable.ic_tem_head).intoImageView(this.mIvHead);

		radioGroup = (RadioGroup) root.findViewById(R.id.fdrdc_rg_menu);
		rbtnCom = (RadioButton) root.findViewById(R.id.fdrdc_rb_com);
		rbtnMyCom = (RadioButton) root.findViewById(R.id.fdrdc_rb_my_com);

		LayoutInflater lf = mContext.getLayoutInflater();
		View view1 = lf.inflate(R.layout.f_doc_room_doc_com_list, null);
//		View viewRecommendRoom = lf.inflate(R.layout.f_doc_room_doc_com_mime,
//				null);
		View viewRecommendRoom = lf.inflate(R.layout.f_doc_room_doc_com_list,
				null);
		ArrayList<View> viewList = new ArrayList<View>();
		viewList.add(view1);
		viewList.add(viewRecommendRoom);

		mPagerAdapter = new RoomListPagerAdapter(viewList);

		mPager = (ViewPager) root.findViewById(R.id.fdrdc_pager);
		mPager.setAdapter(mPagerAdapter);

		mIndicator = (UnderlinePageIndicator) root
				.findViewById(R.id.fdrdc_indicator);
		mIndicator.setFades(false);
		mIndicator.setViewPager(mPager);

		mIndicator
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						switch (arg0) {
							case 0:
								rbtnCom.setChecked(true);
								rbtnMyCom.setChecked(false);
								break;
							case 1:
								rbtnCom.setChecked(false);
								rbtnMyCom.setChecked(true);
								break;
						}
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {

					}
				});

		rbtnCom.setOnClickListener(this);
		rbtnMyCom.setOnClickListener(this);

		mPullListView = (PullToRefreshListView) view1
				.findViewById(R.id.fdrdcl_plv_list);
		mPullListView.setScrollLoadEnabled(true);
		mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				loadCom();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// refresh();
			}
		});
		mListView = mPullListView.getRefreshableView();

		mListView.setDivider(null);

		mComAdapter = new ComAdapter(mContext, listCom);
		mListView.setAdapter(mComAdapter);
		loadCom();


		mPullListView2 = (PullToRefreshListView) viewRecommendRoom
				.findViewById(R.id.fdrdcl_plv_list);
		mPullListView2.setScrollLoadEnabled(true);
		mPullListView2.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				loadCom2();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// refresh();
			}
		});
		mListView2 = mPullListView2.getRefreshableView();

		mListView2.setDivider(null);

		mComAdapter2 = new ComAdapter(mContext, listCom2);
		mListView2.setAdapter(mComAdapter2);
		loadCom2();

//		mCommon = (EditText) viewRecommendRoom
//				.findViewById(R.id.fdrdcm_et_comments);
//		mBtnSend = (Button) viewRecommendRoom
//				.findViewById(R.id.fdrdcm_btn_comments);
//		mBtnSend.setOnClickListener(this);
	}

	private void loadCom() {
		listCom.clear();
		Ion.with(
				this.getActivity(),
				PreferenceConstants.BASE_URL + "/1/Doctors/" + doc.getId()
						+ "?action=comments").asString()
				.setCallback(new FutureCallback<String>() {

					@Override
					public void onCompleted(Exception arg0, String data) {
						mPullListView.onPullDownRefreshComplete();
						if (Checking.isNullorBlank(data)) {
							T.showShort(mContext, "网络错误，请稍后重试");
							return;
						}
						try {
							JSONObject o = new JSONObject(data);
							JSONObject d = o.getJSONObject("data");
							JSONArray a = d.getJSONArray("subs");
							int num = a.length();
							for (int i = 0; i < num; i++) {
								Com c = new Com();
								c.fromJSONObject(a.getJSONObject(i));
								listCom.add(c);
							}
							mComAdapter.notifyDataSetChanged();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}
	private void loadCom2() {
		listCom.clear();
		Ion.with(
				this.getActivity(),
				PreferenceConstants.BASE_URL + "/1/Doctors/" + doc.getId()
						+ "?action=mycomments").asString()
				.setCallback(new FutureCallback<String>() {

					@Override
					public void onCompleted(Exception arg0, String data) {
						mPullListView2.onPullDownRefreshComplete();
						if (Checking.isNullorBlank(data)) {
							T.showShort(mContext, "网络错误，请稍后重试");
							return;
						}
						try {
							JSONObject o = new JSONObject(data);
							JSONObject d = o.getJSONObject("data");
							JSONArray a = d.getJSONArray("subs");
							int num = a.length();
							for (int i = 0; i < num; i++) {
								Com c = new Com();
								c.fromJSONObject(a.getJSONObject(i));
								listCom2.add(c);
							}
							mComAdapter2.notifyDataSetChanged();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	private void sendCom() {
		if (Checking.isNullorBlank(mCommon.getText().toString())) {
			T.showShort(mContext, "请编辑评论内容");
			return;
		}

		Ion.with(mContext,
				PreferenceConstants.BASE_URL + "/1/Appointments/" + doc.getId())
				.setBodyParameter("doctor_comment",
						mCommon.getText().toString()).asJsonObject()
				.setCallback(new FutureCallback<JsonObject>() {

					@Override
					public void onCompleted(Exception arg0, JsonObject data) {
						mPullListView.onPullDownRefreshComplete();
						if (data == null) {
							T.showShort(mContext, "网络错误，请稍后重试");
							return;
						}

						if (data.get("retcode").getAsInt() != 0) {
							T.showShort(mContext, "评论失败");
							return;
						}
						mCommon.setText("");
						loadCom();
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
		switch (v.getId()) {
		case R.id.btn_left:
			this.getActivity().getSupportFragmentManager().popBackStack();
			break;
		case R.id.fdrdc_rb_com:
			this.rbtnCom.setChecked(true);
			this.rbtnMyCom.setChecked(false);
			this.mPager.setCurrentItem(0);
			break;
		case R.id.fdrdc_rb_my_com:
			this.rbtnCom.setChecked(false);
			this.rbtnMyCom.setChecked(true);
			this.mPager.setCurrentItem(1);
			break;
		case R.id.fdrdcm_btn_comments:
			sendCom();
			break;
		}
	}
}
