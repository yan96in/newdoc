package net.meluo.newdoc.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.bigd.utl.Checking;
import com.bigd.utl.StringUtil;
import com.dadao.view.DialogUtil;
import com.dadao.view.TitleBar;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.way.util.L;
import com.way.util.PreferenceConstants;
import com.way.util.T;

import net.meluo.newdoc.R;
import net.meluo.newdoc.activity.BaseActivity;
import net.meluo.newdoc.adapter.PositionNodeAdapter;
import net.meluo.newdoc.adapter.RoomAdapter;
import net.meluo.newdoc.adapter.RoomCatalogAdapter;
import net.meluo.newdoc.adapter.RoomIndexAdapter;
import net.meluo.newdoc.adapter.RoomListPagerAdapter;
import net.meluo.newdoc.app.NDEvent;
import net.meluo.newdoc.bean.Catalog;
import net.meluo.newdoc.bean.PositionNode;
import net.meluo.newdoc.bean.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.smilecity.viewpagerindicator.UnderlinePageIndicator;

public final class DocRoomListFragment extends BaseFragment {

	TitleBar mTitleBar;

	MapView mMapView = null;
	BaiduMap mBaiduMap;

	RelativeLayout rlContent;

	RadioGroup radioGroup;
	RadioButton rbtnRoom;
	RadioButton rbtnRecomend;

	RoomListPagerAdapter mPagerAdapter;
	ViewPager mPager;
	UnderlinePageIndicator mIndicator;

	ListView mListViewRoom;
	RoomIndexAdapter mRoomindexAdapter;
	ListView mListViewCatalog;

	PullToRefreshListView mPullListView;
	ListView mListView;
	RoomAdapter mRoomAdapter;

	Activity mContext;

	public TextView tvPostion;
	TextView btnPosition;

	RelativeLayout rlPostion;

	public PopupWindow popPosition = null;
	View vPosition = null;
	public ListView lv0, lv1, lv2;
	ProgressBar pb0, pb1, pb2, pb10, pb11;
	ArrayList<PositionNode> listProvince = new ArrayList<PositionNode>();
	ArrayList<PositionNode> listCity = new ArrayList<PositionNode>();
	ArrayList<PositionNode> listCounty = new ArrayList<PositionNode>();

	PositionNodeAdapter a0, a1, a2;

	int index = 0;

	ArrayList<Room> listRoom = new ArrayList<Room>();
	ArrayList<Catalog> listCatalog = new ArrayList<Catalog>();

	RoomCatalogAdapter mRoomCatalogAdapter;
	boolean isFirstLoc = true;// 是否首次定位

	private Marker mMarkerA;
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.ic_mask);
	BitmapDescriptor bdGround = BitmapDescriptorFactory
			.fromResource(R.drawable.ground_overlay);
	private InfoWindow mInfoWindow;

	Room r = null;

	public Room currentRoom = null;

	public ArrayList<Marker> mListMarker = new ArrayList<Marker>();

	double Latitude = 0l;
	double Longitude = 0l;

	public static DocRoomListFragment newInstance() {
		DocRoomListFragment fragment = new DocRoomListFragment();
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
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.f_doc_room_list, null);
		initView(layout);
		initLocation();
		initOverlay();
		return layout;
	}

	protected void initView(RelativeLayout root) {
		mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
		mTitleBar.setTitleText("签约诊室");

		mTitleBar.enableLeftButton(true);
		mTitleBar.getLeftButton().setOnClickListener(this);

		// 位置切换区域
		rlPostion = (RelativeLayout) root.findViewById(R.id.fdrl_rl_position);
		tvPostion = (TextView) root.findViewById(R.id.fdrl_tv_position);
		btnPosition = (TextView) root.findViewById(R.id.fdrl_btn_position);
		btnPosition.setOnClickListener(this);

		mMapView = (MapView) root.findViewById(R.id.bmapView);
		// TODO 隐藏地图按钮
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);

		rlContent = (RelativeLayout) root.findViewById(R.id.fdrl_rl_content);
		radioGroup = (RadioGroup) root.findViewById(R.id.fdrl_rg_info);

		rbtnRoom = (RadioButton) root.findViewById(R.id.fdrl_rb_room);
		rbtnRecomend = (RadioButton) root.findViewById(R.id.fdrl_rb_recommend);

		LayoutInflater lf = mContext.getLayoutInflater();
		View view1 = lf.inflate(R.layout.f_doc_room_room_list_index, null);
		View viewRecommendRoom = lf
				.inflate(R.layout.f_doc_room_room_list, null);

		this.mListViewRoom = (ListView) view1.findViewById(R.id.fdrrli_list_0);
		this.mRoomindexAdapter = new RoomIndexAdapter(this, listRoom);
		mListViewRoom.setAdapter(mRoomindexAdapter);

		mListViewCatalog = (ListView) view1.findViewById(R.id.fdrrli_list_1);
		this.mRoomCatalogAdapter = new RoomCatalogAdapter(this, listCatalog);
		mListViewCatalog.setAdapter(mRoomCatalogAdapter);

		this.pb10 = (ProgressBar) view1.findViewById(R.id.fdrrli_pb_0);
		pb10.setVisibility(View.GONE);
		this.pb11 = (ProgressBar) view1.findViewById(R.id.fdrrli_pb_1);
		pb11.setVisibility(View.GONE);

		mPullListView = (PullToRefreshListView) viewRecommendRoom
				.findViewById(R.id.fdrrm_plv_list);
		mPullListView.setScrollLoadEnabled(false);
		mPullListView.setPullRefreshEnabled(true);
		mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				loadRoom(Longitude, Latitude);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// refresh();
			}
		});
		mListView = mPullListView.getRefreshableView();

		mListView.setDivider(null);

		mRoomAdapter = new RoomAdapter(mContext, listRoom, R.id.adri_content);
		mListView.setAdapter(mRoomAdapter);

		ArrayList<View> viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
		viewList.add(view1);
		viewList.add(viewRecommendRoom);

		mPagerAdapter = new RoomListPagerAdapter(viewList);

		mPager = (ViewPager) root.findViewById(R.id.fdrl_pager);
		mPager.setAdapter(mPagerAdapter);

		mIndicator = (UnderlinePageIndicator) root
				.findViewById(R.id.fdrl_indicator);
		mIndicator.setFades(false);
		mIndicator.setViewPager(mPager);

		mIndicator
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						switch (arg0) {
							case 0:
								rbtnRoom.setChecked(true);
								rbtnRecomend.setChecked(false);
								break;
							case 1:
								rbtnRoom.setChecked(false);
								rbtnRecomend.setChecked(true);
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

		rbtnRoom.setOnClickListener(this);
		rbtnRecomend.setOnClickListener(this);

		this.rbtnRoom.setChecked(false);
		this.rbtnRecomend.setChecked(true);
		this.mPager.setCurrentItem(1);

		// 默认以list形式展现
		mTitleBar.enableRightButton(true);
		mTitleBar.getRightButton().setOnClickListener(this);
		mTitleBar.setRightButtonBg(getResources()
				.getDrawable(R.drawable.ic_map));
		rlContent.setVisibility(View.VISIBLE);

		// 切换位置弹出框
		vPosition = LayoutInflater.from(mContext).inflate(
				R.layout.pop_position, null, false);

		pb0 = (ProgressBar) vPosition.findViewById(R.id.pp_pb_0);
		pb1 = (ProgressBar) vPosition.findViewById(R.id.pp_pb_1);
		pb2 = (ProgressBar) vPosition.findViewById(R.id.pp_pb_2);
		pb0.setVisibility(View.GONE);
		pb1.setVisibility(View.GONE);
		pb2.setVisibility(View.GONE);

		lv0 = (ListView) vPosition.findViewById(R.id.pp_list_0);
		lv1 = (ListView) vPosition.findViewById(R.id.pp_list_1);
		lv2 = (ListView) vPosition.findViewById(R.id.pp_list_2);

		a0 = new PositionNodeAdapter(this, listProvince,
				PositionNodeAdapter.INDEX_PROVINCE);

		a1 = new PositionNodeAdapter(this, listCity,
				PositionNodeAdapter.INDEX_CITY);

		a2 = new PositionNodeAdapter(this, listCounty,
				PositionNodeAdapter.INDEX_COUNTY);
		lv0.setAdapter(a0);
		lv1.setAdapter(a1);
		lv2.setAdapter(a2);

		popPosition = new PopupWindow(vPosition, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		popPosition.setFocusable(true);
		popPosition.setBackgroundDrawable(new PaintDrawable());
		popPosition.setAnimationStyle(R.style.AnimTop);

	}

	private void refreshRoom(Double longitude, Double latitude) {

	};

	private void loadRoom(Double longitude, Double latitude) {
		if (!listRoom.isEmpty()) {
			return;
		}
		listRoom.clear();
		pb10.setVisibility(View.VISIBLE);
		// mPullListView.doPullRefreshing(true, 1000);
		Ion.with(
				this.getActivity(),
				PreferenceConstants.BASE_URL
						+ "/1/Rooms?action=near&longitude=" + longitude
						+ "&latitude=" + latitude + "&scope=300").asString()
				.setCallback(new FutureCallback<String>() {

					@Override
					public void onCompleted(Exception arg0, String data) {
						pb10.setVisibility(View.GONE);
						mPullListView.onPullDownRefreshComplete();
						if (Checking.isNullorBlank(data)) {
							T.showShort(DocRoomListFragment.this.getActivity(),
									"网络错误，请稍后重试");
							return;
						}
						try {
							JSONObject o = new JSONObject(data);
							JSONObject d = o.getJSONObject("data");
							JSONArray a = d.getJSONArray("subs");
							int num = a.length();
							for (int i = 0; i < num; i++) {
								Room room = new Room();
								room.fromJSONObject(a.getJSONObject(i));
								listRoom.add(room);
							}

							refreshOverlay();
							mRoomAdapter.notifyDataSetChanged();
							mRoomindexAdapter.notifyDataSetChanged();

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	private void loadRoomByName(String name) {
		pb10.setVisibility(View.VISIBLE);
		Ion.with(
				this.getActivity(),
				PreferenceConstants.BASE_URL
						+ "/1/Rooms?action=near&longitude=").asString()
				.setCallback(new FutureCallback<String>() {

					@Override
					public void onCompleted(Exception arg0, String data) {
						pb10.setVisibility(View.GONE);
						if (Checking.isNullorBlank(data)) {
							T.showShort(DocRoomListFragment.this.getActivity(),
									"网络错误，请稍后重试");
							return;
						}
						L.d("loadRoom", data);

						try {
							JSONObject o = new JSONObject(data);
							JSONObject d = o.getJSONObject("data");
							JSONArray a = d.getJSONArray("subs");
							int num = a.length();
							for (int i = 0; i < num; i++) {
								Room room = new Room();
								room.fromJSONObject(a.getJSONObject(i));
								listRoom.add(room);
							}

							refreshOverlay();
							mRoomAdapter.notifyDataSetChanged();
							mRoomindexAdapter.notifyDataSetChanged();

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	public void loadRoomCatalog(Room r) {

		pb11.setVisibility(View.VISIBLE);
		this.listCatalog.clear();
		Ion.with(
				this.getActivity(),
				PreferenceConstants.BASE_URL + "/1/Rooms/" + r.getId()
						+ "?action=index").asString()
				.setCallback(new FutureCallback<String>() {

					@Override
					public void onCompleted(Exception arg0, String data) {
						pb11.setVisibility(View.GONE);
						if (Checking.isNullorBlank(data)) {
							T.showShort(DocRoomListFragment.this.getActivity(),
									"网络错误，请稍后重试");
							return;
						}

						try {
							JSONObject o = new JSONObject(data);
							JSONObject d = o.getJSONObject("data");
							JSONArray a = d.getJSONArray("catalogs");
							int num = a.length();
							for (int i = 0; i < num; i++) {
								Catalog c = new Catalog();
								c.fromJSONObject(a.getJSONObject(i));
								listCatalog.add(c);
							}
							mRoomCatalogAdapter.notifyDataSetChanged();

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	private void loadProvinceData() {

		pb0.setVisibility(View.VISIBLE);

		// TODO
		// if(nowifi){
		// getDataFromCatch();
		// }else{
		// getDataFromServer();
		// updateCatch();
		// }

		Ion.with(this.getActivity(),
				PreferenceConstants.BASE_URL + "/1/loctree")
				.setMultipartParameter("action", "index")
						// .setMultipartParameter("province", "PROVINCE")
				.asString().setCallback(new FutureCallback<String>() {

			@Override
			public void onCompleted(Exception arg0, String data) {
				pb0.setVisibility(View.GONE);
				if (Checking.isNullorBlank(data)) {
					T.showShort(DocRoomListFragment.this.getActivity(),
							"网络错误，请稍后重试");
					return;
				}
				try {
					JSONObject o = new JSONObject(data);
					JSONObject d = o.getJSONObject("data");
					JSONArray a = d.getJSONArray("subs");
					int num = a.length();
					for (int i = 0; i < num; i++) {
						PositionNode n = new PositionNode();
						n.setSub("-1");
						n.setName(a.getString(i));
						listProvince.add(n);
					}
					a0.check = -1;
					a0.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public void loadCityData(final String province) {
		listCity.clear();
		a1.notifyDataSetChanged();

		pb1.setVisibility(View.VISIBLE);

		// TODO
		// if(nowifi){
		// getDataFromCatch();
		// }else{
		// getDataFromServer();
		// updateCatch();
		// }

		Ion.with(
				this.getActivity(),
				PreferenceConstants.BASE_URL
						+ "/1/loctree?action=index&province=" + province)
				.asString().setCallback(new FutureCallback<String>() {

			@Override
			public void onCompleted(Exception arg0, String data) {
				pb1.setVisibility(View.GONE);
				if (Checking.isNullorBlank(data)) {
					T.showShort(DocRoomListFragment.this.getActivity(),
							"网络错误，请稍后重试");
					return;
				}
				try {
					JSONObject o = new JSONObject(data);
					JSONObject d = o.getJSONObject("data");
					JSONArray a = d.getJSONArray("subs");
					int num = a.length();
					for (int i = 0; i < num; i++) {
						PositionNode n = new PositionNode();
						n.setSub(province);
						n.setName(StringUtil.decodeUnicode(a
								.getString(i)));

						L.d(n.getName());
						listCity.add(n);
					}
					a1.check = -1;
					a1.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public void clearCountyList() {
		listCounty.clear();
		a2.notifyDataSetChanged();
		pb2.setVisibility(View.GONE);
	}

	public void loadCountyData(final String city) {
		listCounty.clear();
		a2.notifyDataSetChanged();
		pb2.setVisibility(View.VISIBLE);

		// TODO
		// if(nowifi){
		// getDataFromCatch();
		// }else{
		// getDataFromServer();
		// updateCatch();
		// }

		Ion.with(
				this.getActivity(),
				PreferenceConstants.BASE_URL + "/1/loctree?action=index&city="
						+ city).asString()
				.setCallback(new FutureCallback<String>() {

					@Override
					public void onCompleted(Exception arg0, String data) {
						pb2.setVisibility(View.GONE);
						if (Checking.isNullorBlank(data)) {
							T.showShort(DocRoomListFragment.this.getActivity(),
									"网络错误，请稍后重试");
							return;
						}
						try {
							JSONObject o = new JSONObject(data);
							JSONObject d = o.getJSONObject("data");
							JSONArray a = d.getJSONArray("subs");
							int num = a.length();
							for (int i = 0; i < num; i++) {
								PositionNode n = new PositionNode();
								n.setSub(city);
								n.setName(a.getString(i));
								listCounty.add(n);
							}
							a2.check = -1;
							a2.notifyDataSetChanged();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	// 初始定位
	private void initLocation() {

		tvPostion.setText("定位中..."); // TODO 加loading动画

		LocationClient locationClient = new LocationClient(this.getActivity());
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setProdName("newdoc");
		option.setAddrType("all");
		option.setScanSpan(1000);

		locationClient.setLocOption(option);

		locationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {

				if (location == null) {
					tvPostion.setText("定位错误.."); // TODO 错误处理
					return;
				}

				tvPostion.setText(location.getAddrStr());

				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(location.getRadius())
								// 此处设置开发者获取到的方向信息，顺时针0-360
						.direction(100).latitude(location.getLatitude())
						.longitude(location.getLongitude()).build();

				mBaiduMap.setMyLocationData(locData);
				if (isFirstLoc) {
					isFirstLoc = false;
					LatLng ll = new LatLng(location.getLatitude(), location
							.getLongitude());
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
					mBaiduMap.animateMapStatus(u);
				}

				DocRoomListFragment.this.Latitude = location.getLatitude();
				DocRoomListFragment.this.Longitude = location.getLongitude();
				mPullListView.doPullRefreshing(true, 1);
				// loadRoom(location.getLongitude(), location.getLatitude());
			}

			public void onReceivePoi(BDLocation poiLocation) {
			}

		});

		locationClient.start();

	}

	public void initOverlay() {
		// add marker overlay
		// LatLng llA = new LatLng(39.963175, 116.400244);
		//
		// OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
		// .zIndex(9).draggable(true);
		// mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));

		// ArrayList<BitmapDescriptor> giflist = new
		// ArrayList<BitmapDescriptor>();
		// giflist.add(bdA);

		mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
			public void onMarkerDrag(Marker marker) {
			}

			public void onMarkerDragEnd(Marker marker) {
				Toast.makeText(
						mContext,
						"拖拽结束，新位置：" + marker.getPosition().latitude + ", "
								+ marker.getPosition().longitude,
						Toast.LENGTH_LONG).show();
			}

			public void onMarkerDragStart(Marker marker) {
			}
		});

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {

				int num = mListMarker.size();
				for (int i = 0; i < num; i++) {
					if (marker == mListMarker.get(i)) {
						r = listRoom.get(i);
					}
				}

				View test = mContext.getLayoutInflater().inflate(
						R.layout.item_info, null);
				test.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mBaiduMap.hideInfoWindow();
					}
				});

				TextView tvName = (TextView) test.findViewById(R.id.ii_tv_name);
				tvName.setText(r.getName() + "");

				TextView tvLocation = (TextView) test
						.findViewById(R.id.ii_tv_location);
				tvLocation.setText(r.getAddress() + "");

				TextView tvPhone = (TextView) test
						.findViewById(R.id.ii_iv_phone);
				tvPhone.setText(r.getPhone() + "");
				tvPhone.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(Intent.ACTION_CALL, Uri
								.parse("tel:" + ((TextView) v).getText()));
						startActivity(intent);
					}
				});

				Button btn = (Button) test.findViewById(R.id.ii_btn_info);
				btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO 诊室简介
						// ((BaseActivity) mContext).addFragmentToStack(
						// R.id.adri_content,
						// DocRoomMainFragment.newInstance(), true);

						((BaseActivity) mContext).addFragmentToStack(
								R.id.adri_content, DocRoomMainFragment
										.newInstance(r, R.id.adri_content),
								true);

					}
				});

				LatLng ll = marker.getPosition();
				mInfoWindow = new InfoWindow(test, ll, -70);
				mBaiduMap.showInfoWindow(mInfoWindow);

				return true;
			}
		});
	}

	public void refreshOverlay() {
		mListMarker.clear();
		int num = listRoom.size();

		for (int i = 0; i < num; i++) {
			Room r = listRoom.get(i);
			L.d("point", r.getLatitude() + "__" + r.getLongitude());
			LatLng llX = new LatLng(r.getLatitude(), r.getLongitude());
			OverlayOptions ooX = new MarkerOptions().position(llX).icon(bdA)
					.zIndex(9).draggable(true);

			Marker m = (Marker) mBaiduMap.addOverlay(ooX);

			mListMarker.add(m);
		}
	}

	@Override
	public void onEvent(NDEvent event) {
		// TODO Auto-generated method stub
		super.onEvent(event);
		switch (event.getEvent()) {
			case NDEvent.E_REFRESH_ROOM_BOUND:
				// 更新诊室绑定状态
				mRoomAdapter.notifyDataSetChanged();
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
			case R.id.btn_right:
				if (index == 0) {
					index = 1;
					mTitleBar.setRightButtonBg(getResources().getDrawable(
							R.drawable.ic_map));
					// mMapView.setVisibility(View.GONE);

					rlContent.setVisibility(View.VISIBLE);

				} else {
					index = 0;
					mTitleBar.setRightButtonBg(getResources().getDrawable(
							R.drawable.ic_list));
					// mMapView.setVisibility(View.VISIBLE);
					rlContent.setVisibility(View.GONE);
				}
				break;
			case R.id.fdrl_btn_position:
				// 切换位置
				popPosition.showAsDropDown(rlPostion);
				this.loadProvinceData();
				break;

			case R.id.fdrl_rb_room:
				this.rbtnRoom.setChecked(true);
				this.rbtnRecomend.setChecked(false);
				this.mPager.setCurrentItem(0);
				break;
			case R.id.fdrl_rb_recommend:
				this.rbtnRoom.setChecked(false);
				this.rbtnRecomend.setChecked(true);
				this.mPager.setCurrentItem(1);
				break;

		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mMapView.onDestroy();
	}

	public GeoPoint getGeoPointBystr(String str) {

		mLoadingDialog = DialogUtil.createLoadingDialog2(getActivity(),
				"loading...");
		mLoadingDialog.showAtLocation(this.getActivity().getCurrentFocus(),
				Gravity.CENTER, 0, 0);

		GeoPoint gpGeoPoint = null;
		if (str != null) {
			Geocoder gc = new Geocoder(mContext, Locale.CHINA);
			List<Address> addressList = null;
			try {

				addressList = gc.getFromLocationName(str, 1);

				if (!addressList.isEmpty()) {
					Address address_temp = addressList.get(0);
					// 计算经纬度
					double Latitude = address_temp.getLatitude();
					double Longitude = address_temp.getLongitude();
					System.out.println("经度：" + Latitude);
					System.out.println("纬度：" + Longitude);
					// 生产GeoPoint
					LatLng cenpt = new LatLng(Latitude, Longitude);
					// 定义地图状态
					MapStatus mMapStatus = new MapStatus.Builder()
							.target(cenpt).zoom(10).build();
					// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

					MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
							.newMapStatus(mMapStatus);
					// 改变地图状态
					mBaiduMap.setMapStatus(mMapStatusUpdate);

					mLoadingDialog.dismiss();

					// loadRoom(Longitude, Latitude);

					DocRoomListFragment.this.Latitude = Latitude;
					DocRoomListFragment.this.Longitude = Longitude;
					mPullListView.doPullRefreshing(true, 1);

					gpGeoPoint = new GeoPoint((int) Latitude, (int) Longitude);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return gpGeoPoint;
	}
}
