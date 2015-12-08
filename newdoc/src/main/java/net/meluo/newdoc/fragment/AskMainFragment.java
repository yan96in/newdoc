package net.meluo.newdoc.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.bigd.utl.Checking;
import com.bigd.utl.ImgUtl;
import com.bigd.utl.InListView;
import com.bigd.utl.SizeUtl;
import com.dadao.view.DialogUtil;
import com.dadao.view.TitleBar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.way.util.PreferenceConstants;
import com.way.util.T;

import net.meluo.core.User;
import net.meluo.newdoc.R;
import net.meluo.newdoc.activity.BaseActivity;
import net.meluo.newdoc.adapter.CatalogAdapter;
import net.meluo.newdoc.adapter.FAQAdapter;
import net.meluo.newdoc.bean.Catalog;
import net.meluo.newdoc.bean.FAQ;
import net.meluo.newdoc.bean.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public final class AskMainFragment extends BaseFragment {
	private static final int PICK_PHOTO = 0;
	Context mContext;
	TitleBar mTitleBar;

	RadioGroup mRg;
	RadioButton mRbtn0, mRbtn1;

	Button btnAdd, btnOk;

	EditText mEtContent, mAge;

	PullToRefreshListView mPullListView;
	ListView mListView;

	FAQAdapter mFAQAdapter;
	ArrayList<FAQ> listFAQ = new ArrayList<FAQ>();
	ArrayList<String> urls = new ArrayList<String>();
	LinearLayout imgList = null;
	InListView mListCq;

	public PopupWindow popDepartment = null;
	View vPosition = null;
	ListView lvDepartment;
	ArrayList<Catalog> listDepartment = new ArrayList<Catalog>();
	CatalogAdapter adpDepartment = null;
	ProgressBar pb;
	public Button btnDepartment;

	public static AskMainFragment newInstance() {

		AskMainFragment fragment = new AskMainFragment();
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

		layout = (RelativeLayout) inflater.inflate(R.layout.f_ask_index, null);

		initView(layout);
		return layout;
	}

	protected void initView(RelativeLayout root) {
		mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
		mTitleBar.setTitleText("在线问答");

		mTitleBar.enableLeftButton(true);
		mTitleBar.getLeftButton().setOnClickListener(this);

		Drawable dTop = getResources().getDrawable(R.drawable.ic_message);
		dTop.setBounds(0, 0, dTop.getMinimumWidth(), dTop.getMinimumHeight());
		mTitleBar.getRightButton().setCompoundDrawables(null, null, dTop, null);
		mTitleBar.getRightButton().setOnClickListener(this);

		this.mListCq = (InListView) root.findViewById(R.id.fai_lv_cq);
		mFAQAdapter = new FAQAdapter(this, listFAQ);
		mListCq.setAdapter(mFAQAdapter);
		refresh();

		btnDepartment = (Button) root.findViewById(R.id.fai_btn_catalog);
		btnDepartment.setTag(2);
		btnDepartment.setOnClickListener(this);
		vPosition = LayoutInflater.from(mContext).inflate(
				R.layout.pop_department2, null, false);
		lvDepartment = (ListView) vPosition.findViewById(R.id.pd2_list);
		pb = (ProgressBar) vPosition.findViewById(R.id.pd2_pb);
		popDepartment = new PopupWindow(vPosition, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// popDepartment.setFocusable(true);
		// popDepartment.setBackgroundDrawable(new PaintDrawable());
		popDepartment.setAnimationStyle(R.style.AnimTop);

		adpDepartment = new CatalogAdapter(this, listDepartment);
		lvDepartment.setAdapter(adpDepartment);

		imgList = (LinearLayout) root.findViewById(R.id.img_list);

		mRbtn0 = (RadioButton) root.findViewById(R.id.radio_0);
		mRbtn1 = (RadioButton) root.findViewById(R.id.radio_1);
		mRg = (RadioGroup) root.findViewById(R.id.fai_rg_sex);

		mAge = (EditText) root.findViewById(R.id.fai_et_age);
		mEtContent = (EditText) root.findViewById(R.id.fai_et_question);

		btnAdd = (Button) root.findViewById(R.id.fai_btn_add);
		btnAdd.setOnClickListener(this);

		btnOk = (Button) root.findViewById(R.id.fai_btn_ok);
		btnOk.setOnClickListener(this);

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

	private void ask() {
		if (Checking.isNullorBlank(mAge.getText().toString())) {
			T.showShort(mContext, "请编辑年龄");
			return;
		}
		if (Checking.isNullorBlank(mEtContent.getText().toString())) {
			T.showShort(mContext, "请编辑问题");
			return;
		}

		final Question q = new Question();

		q.setSex(0);
		if (mRbtn1.isChecked()) {
			q.setSex(1);
		}
		q.setAge(Integer.parseInt(mAge.getText().toString()));
		q.setCatalogid((Integer) btnDepartment.getTag());
		q.setContent(mEtContent.getText().toString());
		final JSONArray a = new JSONArray();

		mLoadingDialog = DialogUtil.createLoadingDialog2(getActivity(),
				"问题提交...");
		mLoadingDialog.showAtLocation(this.getActivity().getCurrentFocus(),
				Gravity.CENTER, 0, 0);
		int num = urls.size();
		if (num > 0) {
			// 有图片
			JSONArray ia = new JSONArray();
			for (int i = 0; i < num; i++) {
				JSONObject jdata = new JSONObject();
				try {
					jdata.put("pic_data", ImgUtl.bitmapToString(urls.get(i)));
					jdata.put("pic_name", new Date().getTime() + ".png");
					ia.put(jdata);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Ion.with(mContext, PreferenceConstants.BASE_URL + "/1/Images")
					.setMultipartParameter("items", ia.toString())
					.asJsonObject()
					.setCallback(new FutureCallback<JsonObject>() {

						@Override
						public void onCompleted(Exception arg0,
								JsonObject result) {

							if (result == null) {
								mLoadingDialog.dismiss();
								T.showShort(mContext, "网络错误，请稍后重试");
								return;
							}
							if (result.get("retcode").getAsInt() != 0) {
								mLoadingDialog.dismiss();
								T.showShort(mContext, result.get("errmsg")
										.getAsString());
								return;
							}
							JsonObject data = result.get("data")
									.getAsJsonObject();
							try {
								JSONObject o = new JSONObject(data.toString());
								o.keys();
								for (Iterator iter = o.keys(); iter.hasNext();) {
									String str = (String) iter.next();
									System.out.println(str);
									q.getImgs()
											.add(data.get(str).getAsString());
								}
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							a.put(q.toJson());
							Ion.with(
									mContext,
									PreferenceConstants.BASE_URL
											+ "/1/Consults")
									.setMultipartParameter("items",
											a.toString())
									.asJsonObject()
									.setCallback(
											new FutureCallback<JsonObject>() {

												@Override
												public void onCompleted(
														Exception arg0,
														JsonObject data) {
													mLoadingDialog.dismiss();
													if (data == null) {
														T.showShort(mContext,
																"网络错误，请稍后重试");
														return;
													}
													if (data.get("retcode")
															.getAsInt() != 0) {
														T.showShort(
																mContext,
																data.get(
																		"errmsg")
																		.getAsString());
														return;
													}
													T.showLong(mContext,
															"问题已经提交，请等待医生回复。");
													resetView();
												}
											});

						}
					});
		} else {
			a.put(q.toJson());
			Ion.with(mContext, PreferenceConstants.BASE_URL + "/1/Consults")
					.setMultipartParameter("items", a.toString())
					.asJsonObject()
					.setCallback(new FutureCallback<JsonObject>() {

						@Override
						public void onCompleted(Exception arg0, JsonObject data) {
							mLoadingDialog.dismiss();
							if (data == null) {
								T.showShort(mContext, "网络错误，请稍后重试");
								return;
							}
							if (data.get("retcode").getAsInt() != 0) {
								T.showShort(mContext, data.get("errmsg")
										.getAsString());
								return;
							}
							T.showLong(mContext, "问题已经提交，请等待医生回复。");
							resetView();
						}
					});
		}
	}

	private void resetView() {
		mRbtn0.setChecked(true);
		mRbtn1.setChecked(false);
		mAge.setText("");
		mEtContent.setText("");
		btnDepartment.setText("全科");
		int num = urls.size();
		if (num > 0) {
			urls.clear();
			while (imgList.getChildCount() > 1) {
				imgList.removeViewAt(imgList.getChildCount() - 2);
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case PICK_PHOTO:

			if (data != null && data.getData() != null) {
				Uri selectedImage = data.getData();
				String[] filePathColumns = { MediaStore.Images.Media.DATA };
				Cursor c = this
						.getActivity()
						.getContentResolver()
						.query(selectedImage, filePathColumns, null, null, null);
				c.moveToFirst();
				int columnIndex = c.getColumnIndex(filePathColumns[0]);
				String picturePath = c.getString(columnIndex);
				c.close();
				urls.add(picturePath);
				View view = new ImageView(mContext);
				FrameLayout.LayoutParams lytp = new FrameLayout.LayoutParams(
						SizeUtl.dip2px(mContext, 70), SizeUtl.dip2px(mContext,
								60));
				view.setLayoutParams(lytp);
				Bitmap photo = ImgUtl.getImageThumbnail(picturePath,
						SizeUtl.dip2px(mContext, 60),
						SizeUtl.dip2px(mContext, 60));
				((ImageView) view).setImageBitmap(photo);
				imgList.addView(view, imgList.getChildCount() - 1);
				view.setTag(imgList.getChildCount() - 2);
				view.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						openMenu((Integer) v.getTag());
					}
				});

			}
			break;
		default:

		}
	}

	private void openMenu(final int id) {
		View vMenu = LayoutInflater.from(mContext).inflate(R.layout.pop_menu,
				null, false);

		final PopupWindow popMenu = new PopupWindow(vMenu,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		popMenu.setFocusable(true);
		popMenu.setBackgroundDrawable(new BitmapDrawable());
		popMenu.setAnimationStyle(R.style.AnimBottom);

		popMenu.showAtLocation(this.getActivity().getCurrentFocus(),
				Gravity.CENTER, 0, 0);

		Button btnDelete = (Button) vMenu.findViewById(R.id.pm_btn_delete);
		btnDelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imgList.removeViewAt(id);
				urls.remove(id);
				popMenu.dismiss();
			}
		});
	}

	private void loadCatalog() {

		pb.setVisibility(View.VISIBLE);
		listDepartment.clear();
		Ion.with(this.getActivity(),
				PreferenceConstants.BASE_URL + "/1/catalog").asString()
				.setCallback(new FutureCallback<String>() {

					@Override
					public void onCompleted(Exception arg0, String data) {
						pb.setVisibility(View.GONE);
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
								Catalog c = new Catalog();
								c.fromJSONObject(a.getJSONObject(i));
								listDepartment.add(c);
							}
							adpDepartment.notifyDataSetChanged();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (popDepartment.isShowing()) {
			this.popDepartment.dismiss();
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
			if (!User.isLogin()) {
				T.showShort(mContext, "请先登录");
				return;
			}
			((BaseActivity) this.getActivity()).addFragmentToStack(
					R.id.adri_content, PatientAskFragment.newInstance(), true);
			break;
		case R.id.fai_btn_ok:
			if (!User.isLogin()) {
				T.showShort(mContext, "请先登录");
				return;
			}
			ask();
			// ChatActivity.actionStart(mContext, 1 + "", "张三", 1 + "");
			break;
		case R.id.fai_btn_add:
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
			startActivityForResult(i, PICK_PHOTO);
			break;
		case R.id.fai_btn_catalog:
			if (popDepartment.isShowing()) {
				this.popDepartment.dismiss();
			} else {
				this.popDepartment.showAsDropDown(btnDepartment);
				loadCatalog();
			}
			break;
		}
	}
}
