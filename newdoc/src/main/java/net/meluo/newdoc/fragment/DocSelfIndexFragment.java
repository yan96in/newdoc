package net.meluo.newdoc.fragment;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dadao.view.TitleBar;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.meluo.newdoc.db.DiseaseProvider;
import com.way.util.L;
import com.zcw.togglebutton.ToggleButton;
import com.zcw.togglebutton.ToggleButton.OnToggleChanged;

import net.meluo.newdoc.R;
import net.meluo.newdoc.adapter.DiseaseAdapter;
import net.meluo.newdoc.adapter.RoomListPagerAdapter;
import net.meluo.newdoc.adapter.SelfPointAdapter;
import net.meluo.newdoc.app.NDEvent;

import java.util.ArrayList;

import cn.smilecity.viewpagerindicator.UnderlinePageIndicator;

public final class DocSelfIndexFragment extends BaseFragment {

	TitleBar mTitleBar;
	TextView mTvEmpty;

	PullToRefreshListView mPullListView;
	ListView mListView;

	Button btnDocSelf, btnQA, btnBespeak;

	RadioGroup radioGroup;
	RadioButton rbtnCom;
	RadioButton rbtnMyCom;

	RoomListPagerAdapter mPagerAdapter;
	ViewPager mPager;
	UnderlinePageIndicator mIndicator;

	ToggleButton toggleBtn;
	boolean isTBOn = false;
	ImageView mIvBody;

	Activity mContext;

	ArrayList<String> mListPoint = new ArrayList<String>();
	ListView mLvPoint, mLvDisease;
	SelfPointAdapter mSA;

	private static final String[] PROJECTION_FROM = new String[] {
			BaseColumns._ID, DiseaseProvider.DiseaseConstants.NAME,
			DiseaseProvider.DiseaseConstants.DETAIL,
			DiseaseProvider.DiseaseConstants.TREAT,
			DiseaseProvider.DiseaseConstants.OTHER,
			DiseaseProvider.DiseaseConstants.TYPE_ID };// 查询字段
	private ContentResolver mContentResolver;

	public static DocSelfIndexFragment newInstance() {

		DocSelfIndexFragment fragment = new DocSelfIndexFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContentResolver = this.getActivity().getContentResolver();
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
		layout = (RelativeLayout) inflater.inflate(R.layout.f_doc_self_index,
				null);
		initView(layout);
		loadPoint();
		return layout;
	}

	protected void initView(final RelativeLayout root) {
		mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
		mTitleBar.setTitleText("新医自诊");

		mTitleBar.enableLeftButton(true);
		mTitleBar.getLeftButton().setOnClickListener(this);

		radioGroup = (RadioGroup) root.findViewById(R.id.fdrdc_rg_menu);
		rbtnCom = (RadioButton) root.findViewById(R.id.fdrdc_rb_com);
		rbtnMyCom = (RadioButton) root.findViewById(R.id.fdrdc_rb_my_com);

		LayoutInflater lf = mContext.getLayoutInflater();
		final View view1 = lf.inflate(R.layout.f_doc_self_body, null);
		View view2 = lf.inflate(R.layout.f_doc_self_list, null);
		ArrayList<View> viewList = new ArrayList<View>();
		viewList.add(view1);
		viewList.add(view2);

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

		mIvBody = (ImageView) view1.findViewById(R.id.fdsb_iv_body);
		toggleBtn = (ToggleButton) view1.findViewById(R.id.fdsb_tb);
		toggleBtn.setOnToggleChanged(new OnToggleChanged() {
			@Override
			public void onToggle(boolean on) {
				if (on) {
					isTBOn = true;
					mIvBody.setImageResource(R.drawable.ic_body_1);
				} else {
					isTBOn = false;
					mIvBody.setImageResource(R.drawable.ic_body_0);
				}
			}
		});

		mIvBody.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 当按下时获取到屏幕中的xy位置
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					L.e("point", event.getX() + "," + event.getY());
					int h = v.getHeight();
					int w = v.getWidth();
					int x = (int) event.getX();
					int y = (int) event.getY();

					int left = w / 2 - 592 * h / 2244;
					L.d(left + "left");
					x -= left;
					if (!isTBOn) {

						if (y < 200 * h / 1122 && x < 410 * h / 1122
								&& x > 170 * h / 1122) {
							// 头
							mIvBody.setImageResource(R.drawable.ic_body_head);
							mPager.scrollBy(w, 0);
							mSA.setCheck(0);
							rbtnCom.setChecked(false);
							rbtnMyCom.setChecked(true);
							mIndicator.scrollBy(w, 0);

						} else if (200 * h / 1122 < y && y < 640 * h / 1122
								&& x < 170 * h / 1122) {
							// 左臂
							mIvBody.setImageResource(R.drawable.ic_body_arm);
							mPager.scrollBy(w, 0);
							mSA.setCheck(3);
							rbtnCom.setChecked(false);
							rbtnMyCom.setChecked(true);
							mIndicator.scrollBy(w, 0);
						} else if (200 * h / 1122 < y && y < 640 * h / 1122
								&& x > 410 * h / 1122) {
							// 右臂
							mIvBody.setImageResource(R.drawable.ic_body_arm);
							mPager.scrollBy(w, 0);
							mSA.setCheck(3);
							rbtnCom.setChecked(false);
							rbtnMyCom.setChecked(true);
							mIndicator.scrollBy(w, 0);
						} else if (200 * h / 1122 < y && y < 375 * h / 1122
								&& x < 410 * h / 1122 && x > 170 * h / 1122) {
							// 胸
							mIvBody.setImageResource(R.drawable.ic_body_chest);
							mPager.scrollBy(w, 0);
							mSA.setCheck(1);
							rbtnCom.setChecked(false);
							rbtnMyCom.setChecked(true);
							mIndicator.scrollBy(w, 0);
						} else if (375 * h / 1122 < y && y < 520 * h / 1122
								&& x < 410 * h / 1122 && x > 170 * h / 1122) {
							// 腹部
							mIvBody.setImageResource(R.drawable.ic_body_stomach);
							mPager.scrollBy(w, 0);
							mSA.setCheck(2);
							rbtnCom.setChecked(false);
							rbtnMyCom.setChecked(true);
							mIndicator.scrollBy(w, 0);
						} else if (520 * h / 1122 < y && y < 667 * h / 1122
								&& x < 410 * h / 1122 && x > 170 * h / 1122) {
							// 盆腔
							mIvBody.setImageResource(R.drawable.ic_body_pelvic);
							mPager.scrollBy(w, 0);
							mSA.setCheck(6);
							rbtnCom.setChecked(false);
							mIndicator.scrollBy(w, 0);
							rbtnMyCom.setChecked(true);
						} else if (667 * h / 1122 < y && x < 410 * h / 1122
								&& x > 170 * h / 1122) {
							// 腿
							mIvBody.setImageResource(R.drawable.ic_body_leg);
							mPager.scrollBy(w, 0);
							mSA.setCheck(4);
							rbtnCom.setChecked(false);
							rbtnMyCom.setChecked(true);
							mIndicator.scrollBy(w, 0);
						}
					} else {
						if (x < 410 * h / 1122 && x > 170 * h / 1122) {
							mIvBody.setImageResource(R.drawable.ic_body_back);
							mPager.scrollBy(w, 0);
							mSA.setCheck(5);
							rbtnCom.setChecked(false);
							rbtnMyCom.setChecked(true);
							mIndicator.scrollBy(w, 0);
						}
					}
				}

				return false;
			}
		});

		mLvPoint = (ListView) view2.findViewById(R.id.fdsl_list_0);
		mLvDisease = (ListView) view2.findViewById(R.id.fdsl_list_1);
		mSA = new SelfPointAdapter(this.getActivity(), mListPoint);

		mLvPoint.setAdapter(mSA);
	}

	private void setDiseaseAdapter(String name) {
		String selection = DiseaseProvider.DiseaseConstants.TYPE_ID + "='"
				+ name + "'";

		// 异步查询数据库
		new AsyncQueryHandler(mContentResolver) {

			@Override
			protected void onQueryComplete(int token, Object cookie,
					Cursor cursor) {
				L.d("cursor.getCount():"+cursor.getCount());
				DiseaseAdapter adapter = new DiseaseAdapter(mContext, cursor,
						PROJECTION_FROM);
				mLvDisease.setAdapter(adapter);
			}

		}.startQuery(0, null, DiseaseProvider.CONTENT_URI, PROJECTION_FROM,
				selection, null, null);
	}

	private void loadPoint() {
		mListPoint.add("头/颈部");
		mListPoint.add("胸部");
		mListPoint.add("腹部");
		mListPoint.add("上肢");
		mListPoint.add("下肢");
		mListPoint.add("腰背部");
		mListPoint.add("骨盆");
		mSA.notifyDataSetChanged();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onEvent(NDEvent event) {
		// TODO Auto-generated method stub
		super.onEvent(event);
		switch (event.getEvent()) {
		case NDEvent.E_Body_POINT_CHANGE:
			L.d(event.getData() + "");
			setDiseaseAdapter((String) event.getData());
			break;
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
			this.rbtnCom.setChecked(true);
			this.rbtnMyCom.setChecked(false);
			this.mPager.setCurrentItem(0);
			break;
		case R.id.fdrdc_rb_my_com:
			this.rbtnCom.setChecked(false);
			this.rbtnMyCom.setChecked(true);
			this.mPager.setCurrentItem(1);

			break;

		}
	}
}
