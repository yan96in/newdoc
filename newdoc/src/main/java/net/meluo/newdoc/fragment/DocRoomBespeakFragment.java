package net.meluo.newdoc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigd.utl.Checking;
import com.dadao.view.DialogUtil;
import com.dadao.view.TitleBar;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders.Any.B;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.way.util.L;
import com.way.util.PreferenceConstants;
import com.way.util.T;
import com.way.util.TimeUtil;
import com.ypy.eventbus.EventBus;

import net.meluo.core.User;
import net.meluo.newdoc.R;
import net.meluo.newdoc.activity.BaseActivity;
import net.meluo.newdoc.adapter.CalendarAdapter;
import net.meluo.newdoc.adapter.RoomOfDocPagerAdapter;
import net.meluo.newdoc.app.NDEvent;
import net.meluo.newdoc.bean.Day;
import net.meluo.newdoc.bean.Doc;
import net.meluo.newdoc.bean.RoomOfDoc;
import net.meluo.newdoc.bean.RoomOfDoc.Slote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public final class DocRoomBespeakFragment extends BaseFragment {

	TitleBar mTitleBar;
	TextView mTvEmpty;

	TextView tvname, tvCatalog, tvGoodAt;

	PullToRefreshListView mPullListView;
	ListView mListView;
	Button btnDocInfo, btnDocCom, btnFocus, btnAppointment;
	Context mContext;
	Doc doc;
	int id;

	ViewPager mPager;
	ArrayList<View> mListRoom = new ArrayList<View>();
	RoomOfDocPagerAdapter mRAdapter;

	ArrayList<Day> mListDay = new ArrayList<Day>();
	CalendarAdapter mCAdapter;
	GridView mGvCalendar;
	ImageButton btnLeft, btnRight;

	RadioGroup mRadioSlots;

	ImageView mIvHead;
	PopupWindow mLoadingDialog = null;

	public static DocRoomBespeakFragment newInstance(Doc doc, int id) {

		DocRoomBespeakFragment fragment = new DocRoomBespeakFragment(doc, id);
		return fragment;
	}

	public DocRoomBespeakFragment(Doc doc, int id) {
		this.doc = doc;
		this.id = id;
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
				R.layout.f_doc_room_bespeak, null);
		initView(inflater, layout);
		return layout;
	}

	protected void initView(LayoutInflater inflater, RelativeLayout root) {
		mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
		mTitleBar.setTitleText("预约挂号");
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

		this.btnDocInfo = (Button) root.findViewById(R.id.fdrb_btn_info);
		btnDocInfo.setOnClickListener(this);
		this.btnDocCom = (Button) root.findViewById(R.id.fdrb_btn_com);
		btnDocCom.setOnClickListener(this);

		btnAppointment = (Button) root.findViewById(R.id.fdrb_btn_appointment);
		btnAppointment.setOnClickListener(this);

		btnFocus = (Button) root.findViewById(R.id.md_btn_focus);
		if (User.isLogin()) {
			if (doc.isFocus()) {
				btnFocus.setText("取消关注");
				btnFocus.setBackgroundResource(R.drawable.bg_btn_def_xml_1);
			} else {
				btnFocus.setText("关注");
				btnFocus.setBackgroundResource(R.drawable.bg_btn_yellow_xml);
			}
		}
		btnFocus.setOnClickListener(this);

		mPager = (ViewPager) root.findViewById(R.id.fdrb_pager);
		mRAdapter = new RoomOfDocPagerAdapter(mListRoom);
		mPager.setAdapter(mRAdapter);

		btnLeft = (ImageButton) root.findViewById(R.id.fdrb_btn_left);
		btnLeft.setOnClickListener(this);
		btnRight = (ImageButton) root.findViewById(R.id.fdrb_btn_right);
		btnRight.setVisibility(View.GONE);
		btnRight.setOnClickListener(this);
		mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				// TODO Auto-generated method stub
				View v = mListRoom.get(index);
				RoomOfDoc r = (RoomOfDoc) v.getTag();
				initCalendar();
				addDayStatus(r);

				if (index == 0) {
					btnRight.setVisibility(View.GONE);

				} else {
					btnRight.setVisibility(View.VISIBLE);
				}
				if (index == mListRoom.size() - 1) {
					btnLeft.setVisibility(View.GONE);

				} else {
					btnLeft.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		mCAdapter = new CalendarAdapter(this, this.mListDay);
		mGvCalendar = (GridView) root.findViewById(R.id.fdrb_calendar);
		mGvCalendar.setAdapter(mCAdapter);
		mLoadingDialog = DialogUtil.createLoadingDialog2(getActivity(), null);

		mRadioSlots = (RadioGroup) root.findViewById(R.id.fdrb_rg_radio);
		mRadioSlots
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						mRadioSlots.setTag(mRadioSlots.findViewById(checkedId)
								.getTag());
					}
				});

		initCalendar();
		loadData(inflater, root);
	}

	private void loadData(final LayoutInflater inflater, View root) {

		mLoadingDialog.showAtLocation(root, Gravity.CENTER, 0, 0);

		Ion.with(
				this.getActivity(),
				PreferenceConstants.BASE_URL + "/1/Doctors/" + doc.getId()
						+ "?action=index").asString()
				.setCallback(new FutureCallback<String>() {

					@Override
					public void onCompleted(Exception arg0, String data) {
						mLoadingDialog.dismiss();
						if (Checking.isNullorBlank(data)) {
							T.showShort(mContext, "网络错误，请稍后重试");
							return;
						}
						try {
							JSONObject o = new JSONObject(data);
							JSONObject d = o.getJSONObject("data");
							JSONArray roomArray = d
									.getJSONArray("preserve_window");
							int num = roomArray.length();
							for (int i = 0; i < num; i++) {
								JSONObject item = roomArray.getJSONObject(i);
								RoomOfDoc r = new RoomOfDoc();
								r.fromJSONObject(item);

								View v = inflater.inflate(
										R.layout.item_room_of_doc, null);
								TextView name = (TextView) v
										.findViewById(R.id.irod_tv_name);
								name.setText(r.getName());

								v.setTag(r);

								mListRoom.add(v);
								if (i == 0) {
									initCalendar();
									addDayStatus(r);
								}
							}
							mRAdapter.notifyDataSetChanged();

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
		// TODO

	}

	private void initCalendar() {
		mListDay.clear();
		int today = TimeUtil.getDayOfWeek(new Date()) % 7;

		L.d(today + "___" + TimeUtil.getDayOfWeek(new Date()));

		long m = new Date().getTime();
		long day = 24 * 60 * 60 * 1000;
		for (int i = 0; i < 14; i++) {
			Day d = new Day();
			Date dt = new Date(m + i * day);
			d.setName(TimeUtil.getDayOfMonth(dt) + "");
			d.setDate(TimeUtil.getDate(dt.getTime()));
			if (true) {
				// 判断是否可预约
				d.setStatus(Day.STATUS_IN);
			}
			this.mListDay.add(d);
		}

		for (int i = 1; i < today; i++) {
			Day d = new Day();
			Date dt = new Date(m - i * day);

			d.setName(TimeUtil.getDayOfMonth(dt) + "");
			d.setDate(TimeUtil.getDate(dt.getTime()));
			if (true) {
				// 判断是否可预约
				d.setStatus(Day.STATUS_OUT);
			}
			this.mListDay.add(0, d);
		}

		int n = 21 - today - 14;

		for (int i = 0; i < n + 1; i++) {
			Day d = new Day();
			Date dt = new Date(m + (14 + i) * day);

			d.setName(TimeUtil.getDayOfMonth(dt) + "");
			d.setDate(TimeUtil.getDate(dt.getTime()));
			if (true) {
				// 判断是否可预约
				d.setStatus(Day.STATUS_OUT);
			}
			this.mListDay.add(d);
		}

		this.mListDay.add(0, new Day(0, "六", Day.STATUS_TITLE));
		this.mListDay.add(0, new Day(0, "五", Day.STATUS_TITLE));
		this.mListDay.add(0, new Day(0, "四", Day.STATUS_TITLE));
		this.mListDay.add(0, new Day(0, "三", Day.STATUS_TITLE));
		this.mListDay.add(0, new Day(0, "二", Day.STATUS_TITLE));
		this.mListDay.add(0, new Day(0, "一", Day.STATUS_TITLE));
		this.mListDay.add(0, new Day(0, "日", Day.STATUS_TITLE));
		mCAdapter.notifyDataSetChanged();
	}

	private void addDayStatus(RoomOfDoc r) {
		ArrayList<RoomOfDoc.Slote> slots = r.getSlots();
		int num = slots.size();
		for (int i = 0; i < num; i++) {
			RoomOfDoc.Slote item = slots.get(i);

			int jn = mListDay.size();
			for (int j = 7; j < jn; j++) {
				L.d(mListDay.get(j).getDate());
				L.d(item.getDate());
				if (mListDay.get(j).getDate().equals(item.getDate())) {
					if (mListDay.get(j).getStatus() == Day.STATUS_IN) {
						mListDay.get(j).setStatus(Day.STATUS_ON);
						mListDay.get(j).getSlots().add(item);
					}
				}
			}
		}
		mCAdapter.notifyDataSetChanged();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void appointment() {
		Slote slote = (Slote) mRadioSlots.getTag();

		Ion.with(mContext, PreferenceConstants.BASE_URL + "/1/Appointments")
				.setBodyParameter("timeslotid", slote.getId() + "")
				.setBodyParameter("actual_date", slote.getDate())
				.asJsonObject().setCallback(new FutureCallback<JsonObject>() {

					@Override
					public void onCompleted(Exception arg0, JsonObject data) {

						if (data == null) {
							T.showShort(mContext, "网络错误，请稍后重试");
							return;
						}

						if (data.get("retcode").getAsInt() != 0) {
							T.showShort(mContext, "预约失败");
							return;
						}
						T.showShort(mContext, "预约成功");
					}
				});

	}

	private void focus(final View v, final Doc doc) {

		B b = Ion.with(mContext, PreferenceConstants.BASE_URL + "/1/Focus");

		if (doc.isFocus()) {
			b.setBodyParameter("action", "del");
		}
		b.setBodyParameter("doctorid", doc.getId() + "").asJsonObject()
				.setCallback(new FutureCallback<JsonObject>() {
					@Override
					public void onCompleted(Exception e, JsonObject result) {

						if (e != null) {
							T.showShort(mContext, "网络错误，请稍后重试");
							return;
						}
						if (result.isJsonNull()) {
							T.showShort(mContext, "网络错误，请稍后重试");
							return;
						}
						if (result.get("retcode").getAsInt() != 0) {
							T.showShort(mContext, result.get("errmsg")
									.getAsString());
							return;
						}
						if (doc.isFocus()) {
							doc.setFocus(false);
							((Button) v).setText("关注");
							((Button) v)
									.setBackgroundResource(R.drawable.bg_btn_yellow_xml);
							EventBus.getDefault()
									.post(new NDEvent(
											NDEvent.E_DOC_FOCUS_FALSE, doc));
						} else {
							EventBus.getDefault().post(
									new NDEvent(NDEvent.E_DOC_FOCUS_TRUE, doc));
							doc.setFocus(true);
							((Button) v).setText("取消关注");
							((Button) v)
									.setBackgroundResource(R.drawable.bg_btn_def_xml_1);
						}
					}
				});
	}

	private void refreshSlote() {
		mRadioSlots.removeAllViews();
		Day day = mCAdapter.getCheckDay();
		ArrayList<RoomOfDoc.Slote> slots = day.getSlots();
		int num = slots.size();
		LayoutInflater mInflater = LayoutInflater.from(this.mContext);
		for (int i = 0; i < num; i++) {
			RadioButton radio = (RadioButton) mInflater.inflate(
					R.layout.item_slot, null);
			radio.setText(slots.get(i).getTimescope());
			radio.setTag(slots.get(i));
			if (num == 1) {
				// radio.setChecked(true);
				mRadioSlots.setTag(slots.get(i));
			}
			mRadioSlots.addView(radio);
		}
	}

	@Override
	public void onEvent(NDEvent event) {
		// TODO Auto-generated method stub
		super.onEvent(event);

		switch (event.getEvent()) {
		case NDEvent.E_DAY_CHANGE:
			refreshSlote();
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
		case R.id.fdrb_btn_info:
			((BaseActivity) this.getActivity()).addFragmentToStack(id,
					DocRoomDocInfoFragment.newInstance(doc), true);

			break;
		case R.id.fdrb_btn_com:

			((BaseActivity) this.getActivity()).addFragmentToStack(id,
					DocRoomDocComFragment.newInstance(doc), true);
			break;
		case R.id.fdrb_btn_left:
			int j = mPager.getCurrentItem();
			if (j >= mPager.getChildCount()) {
				return;
			}
			mPager.setCurrentItem(j + 1, true);
			break;
		case R.id.fdrb_btn_right:
			int i = mPager.getCurrentItem();
			if (i <= 0) {
				return;
			}
			mPager.setCurrentItem(i - 1, true);

			break;

		case R.id.md_btn_focus:
			if (!User.isLogin()) {
				T.showShort(mContext, "请先登录");
			} else {
				focus(v, doc);
			}
			break;

		case R.id.fdrb_btn_appointment:
			appointment();
			break;

		}
	}
}
