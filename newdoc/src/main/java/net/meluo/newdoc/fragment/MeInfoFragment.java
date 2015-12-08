package net.meluo.newdoc.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigd.utl.ImgUtl;
import com.dadao.view.DialogUtil;
import com.dadao.view.TitleBar;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.way.util.L;
import com.way.util.PreferenceConstants;
import com.way.util.T;
import com.ypy.eventbus.EventBus;

import net.meluo.newdoc.R;
import net.meluo.newdoc.activity.BaseActivity;
import net.meluo.newdoc.app.NDApp;
import net.meluo.newdoc.app.NDEvent;
import net.meluo.newdoc.bean.UserBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Iterator;

public final class MeInfoFragment extends BaseFragment {

	private static final int PICK_PHOTO = 0;
	Context mContext;
	TitleBar mTitleBar;

	RelativeLayout rlNick, mRlHead, mRlName, mRlSex, mRlCheck, mRlRpw;
	ImageView mIvHead, mIvHeadMask;

	TextView mTvName, mTvSex;
	PopupWindow mLoadingDialog = null;

	public static MeInfoFragment newInstance() {

		MeInfoFragment fragment = new MeInfoFragment();
		return fragment;
	}

	// 在需要分享的地方添加代码：

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
		layout = (RelativeLayout) inflater.inflate(R.layout.f_me_info, null);
		initView(layout);
		return layout;
	}

	protected void initView(RelativeLayout root) {
		mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
		mTitleBar.setTitleText("个人中心");

		mTitleBar.enableLeftButton(true);
		mTitleBar.getLeftButton().setOnClickListener(this);

		mRlHead = (RelativeLayout) root.findViewById(R.id.fmi_rl_head);
		mRlHead.setOnClickListener(this);
		mRlName = (RelativeLayout) root.findViewById(R.id.fmi_rl_name);
		mRlName.setOnClickListener(this);
		mRlSex = (RelativeLayout) root.findViewById(R.id.fmi_rl_sex);
		mRlSex.setOnClickListener(this);
		mRlCheck = (RelativeLayout) root.findViewById(R.id.fmi_rl_check);
		mRlCheck.setOnClickListener(this);
		mRlRpw = (RelativeLayout) root.findViewById(R.id.fmi_rl_rpw);
		mRlRpw.setOnClickListener(this);

		mIvHead = (ImageView) root.findViewById(R.id.fmi_iv_head);
		mIvHeadMask = (ImageView) root.findViewById(R.id.fmi_iv_head_mask);
		mIvHeadMask.setOnClickListener(this);

		this.mTvName = (TextView) root.findViewById(R.id.fmi_tv_name);
		this.mTvSex = (TextView) root.findViewById(R.id.fmi_tv_sex);

		try {
			mTvName.setText(NDApp.getInstance().user.getName());
			String[] sex = { "男", "女" };
			mTvSex.setText(sex[NDApp.getInstance().user.getSex()]);

			Ion.with(mContext).load(NDApp.getInstance().user.getPicture_url())
					.withBitmap().error(R.drawable.ic_tem_head)
					.intoImageView(mIvHead);

		} catch (Exception e) {
			e.printStackTrace();
		}

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
		case NDEvent.E_SEX_REFRESH:
			String[] sex = { "男", "女" };
			mTvSex.setText(sex[NDApp.getInstance().user.getSex()]);
			// TODO 同步到服务器
			break;
		case NDEvent.E_NAME_REFRESH:

			mTvName.setText(NDApp.getInstance().user.getName());
			// TODO 同步到服务器
			break;
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

		case R.id.fmi_rl_head:
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
			startActivityForResult(i, PICK_PHOTO);
			break;
		case R.id.fmi_rl_name:
			((BaseActivity) this.getActivity()).addFragmentToStack(
					R.id.ami_content, MeInfoEditNameFragment.newInstance(),
					true);
			break;
		case R.id.fmi_rl_sex:
			((BaseActivity) this.getActivity())
					.addFragmentToStack(R.id.ami_content,
							MeInfoEditSexFragment.newInstance(), true);
			break;
		case R.id.fmi_rl_check:
			if (NDApp.getInstance().user.getDoctor_status() == UserBean.D_S_CHECKED) {
				T.showShort(mContext, "已完成实名认证");
				return;
			}
			if (NDApp.getInstance().user.getDoctor_status() == UserBean.D_S_CHECKING) {
				T.showShort(mContext, "认证审核中");
				return;
			}

			((BaseActivity) this.getActivity()).addFragmentToStack(
					R.id.ami_content,
					MeInfoRealNameCheckFragment.newInstance(), true);
			break;
		case R.id.fmi_rl_rpw:
			((BaseActivity) this.getActivity()).addFragmentToStack(
					R.id.ami_content, RPWFragment.newInstance(), true);
			break;

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
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
				L.d("图库", picturePath);

				// 发送图片类型信息

				sendHeadImg(picturePath);
			}
			break;
		default:

		}
	}

	private void sendHeadImg0(final String picturePath) {
		// TODO Auto-generated method stub
		mLoadingDialog = DialogUtil.createLoadingDialog2(getActivity(),
				"头像上传...");
		mLoadingDialog.showAtLocation(this.getActivity().getCurrentFocus(),
				Gravity.CENTER, 0, 0);
		L.d(picturePath);
		try {

			JSONArray a = new JSONArray();
			UserBean user = new UserBean();
			user.setPicture_url(ImgUtl.bitmapToString(picturePath));
			a.put(user.toJSONObject());
			Ion.with(mContext, PreferenceConstants.BASE_URL + "/1/Clients")
					.setBodyParameter("items", a.toString()).asJsonObject()
					.setCallback(new FutureCallback<JsonObject>() {
						@Override
						public void onCompleted(Exception e, JsonObject result) {
							mLoadingDialog.dismiss();
							if (result.isJsonNull()) {
								T.showShort(mContext, "网络错误，请稍后重试。");
								return;
							}
							if (result.get("retcode").getAsInt() != 0) {
								T.showShort(mContext, "网络错误，请稍后重试。");
								return;
							}

							EventBus.getDefault().post(
									new NDEvent(NDEvent.E_HEAD_REFRESH));

							Ion.with(mContext)
									.load(NDApp.getInstance().user
											.getPicture_url()).withBitmap()
									.error(R.drawable.ic_tem_head)
									.intoImageView(mIvHead);
						}
					});

		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

	private void sendHeadImg(final String picturePath) {
		// TODO Auto-generated method stub
		mLoadingDialog = DialogUtil.createLoadingDialog2(getActivity(),
				"头像上传...");
		mLoadingDialog.showAtLocation(this.getActivity().getCurrentFocus(),
				Gravity.CENTER, 0, 0);
		L.d(picturePath);
		try {

			JSONArray a = new JSONArray();
			JSONObject jdata = new JSONObject();
			jdata.put("pic_data", ImgUtl.bitmapToString(picturePath));
			jdata.put("pic_name", new Date().getTime() + ".png");
			a.put(jdata);

			Ion.with(mContext, PreferenceConstants.BASE_URL + "/1/Images")
					.setBodyParameter("items", a.toString()).asJsonObject()
					.setCallback(new FutureCallback<JsonObject>() {
						@Override
						public void onCompleted(Exception e, JsonObject result) {

							if (result.isJsonNull()) {
								mLoadingDialog.dismiss();
								T.showShort(mContext, "网络错误，请稍后重试。");
								return;
							}
							if (result.get("retcode").getAsInt() != 0) {
								mLoadingDialog.dismiss();
								T.showShort(mContext, "网络错误，请稍后重试。");
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
									NDApp.getInstance().user
											.setPicture_url(data.get(str)
													.getAsString());

									try {

										JSONArray a = new JSONArray();
										UserBean user = new UserBean();
										user.setPicture_url(ImgUtl
												.bitmapToString(picturePath));
										a.put(NDApp.getInstance().user
												.toJSONObject());
										Ion.with(
												mContext,
												PreferenceConstants.BASE_URL
														+ "/1/Clients")
												.setBodyParameter("items",
														a.toString())
												.asJsonObject()
												.setCallback(
														new FutureCallback<JsonObject>() {
															@Override
															public void onCompleted(
																	Exception e,
																	JsonObject result) {
																mLoadingDialog
																		.dismiss();
																if (result
																		.isJsonNull()) {
																	T.showShort(
																			mContext,
																			"网络错误，请稍后重试。");
																	return;
																}
																if (result
																		.get("retcode")
																		.getAsInt() != 0) {
																	T.showShort(
																			mContext,
																			"网络错误，请稍后重试。");
																	return;
																}

																EventBus.getDefault()
																		.post(new NDEvent(
																				NDEvent.E_HEAD_REFRESH));

																Ion.with(
																		mContext)
																		.load(NDApp
																				.getInstance().user
																				.getPicture_url())
																		.withBitmap()
																		.error(R.drawable.ic_tem_head)
																		.intoImageView(
																				mIvHead);
															}
														});

									} catch (Exception e2) {
										// TODO Auto-generated catch block
										e2.printStackTrace();
									}
								}
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					});

		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

}
