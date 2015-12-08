package net.meluo.newdoc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dadao.view.DialogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.way.util.L;
import com.way.util.PreferenceConstants;
import com.way.util.T;
import com.ypy.eventbus.EventBus;

import net.meluo.core.User;
import net.meluo.newdoc.R;
import net.meluo.newdoc.activity.BaseActivity;
import net.meluo.newdoc.app.NDApp;
import net.meluo.newdoc.app.NDEvent;
import net.meluo.newdoc.bean.UserBean;

public final class MeIndexFragment extends BaseFragment {

    Context mContext;

    TextView tvLogin, tvNick, tvPhone;

    RelativeLayout rlMeInfo, rlNick, rlMeDoc, rlMeBespeak, rlMeAsk, rlMelist,
            rlMeRoom, rlSet;

    ImageView mIvHead;

    PopupWindow mLoadingDialog = null;

    public static MeIndexFragment newInstance() {

        MeIndexFragment fragment = new MeIndexFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

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

        ScrollView layout;

        layout = (ScrollView) inflater.inflate(R.layout.f_me_index, null);

        initView(layout);
        return layout;
    }

    protected void initView(ScrollView root) {

        mIvHead = (ImageView) root.findViewById(R.id.fmi_iv_head);

        tvLogin = (TextView) root.findViewById(R.id.fmi_tv_login);
        tvLogin.setOnClickListener(this);

        tvNick = (TextView) root.findViewById(R.id.fmi_tv_nick);
        tvPhone = (TextView) root.findViewById(R.id.fmi_tv_phone);
        rlNick = (RelativeLayout) root.findViewById(R.id.fmi_rl_nick);

        rlMeInfo = (RelativeLayout) root.findViewById(R.id.fmi_rl_info);
        rlMeInfo.setOnClickListener(this);
        rlMeDoc = (RelativeLayout) root.findViewById(R.id.fmi_rl_doc);
        rlMeDoc.setOnClickListener(this);

        rlMeBespeak = (RelativeLayout) root.findViewById(R.id.fmi_rl_bespeak);
        rlMeBespeak.setOnClickListener(this);

        rlMeAsk = (RelativeLayout) root.findViewById(R.id.fmi_rl_ask);
        rlMeAsk.setOnClickListener(this);

        rlMelist = (RelativeLayout) root.findViewById(R.id.fmi_rl_list);
        rlMelist.setOnClickListener(this);

        rlMeRoom = (RelativeLayout) root.findViewById(R.id.fmi_rl_room);
        rlMeRoom.setOnClickListener(this);

        rlSet = (RelativeLayout) root.findViewById(R.id.fmi_rl_set);
        rlSet.setOnClickListener(this);

        if (User.isLogin()) {
            LoginRefresh();
        }

    }

    public void LoginRefresh() {
        if (!User.isLogin()) {
            return;
        }
        Ion.with(mContext)
                .load(NDApp.getInstance().user.getPicture_url()).withBitmap()
                .error(R.drawable.ic_tem_head).intoImageView(mIvHead);
        tvNick.setText(NDApp.getInstance().user.getName());
        tvPhone.setText(NDApp.getInstance().user.getMobile());
        rlNick.setVisibility(View.VISIBLE);
        tvLogin.setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onEvent(NDEvent event) {

        switch (event.getEvent()) {
            case NDEvent.E_ND_LOGIN:
                getUserInfo();
                break;
            case NDEvent.E_LOGOUT:
                ClearUserInfo();
                break;
            case NDEvent.WX_LOADING:
                mLoadingDialog = DialogUtil.createLoadingDialog2(getActivity(),
                        "正在拉起微信...");
                mLoadingDialog.showAtLocation(this.getActivity().getCurrentFocus(),
                        Gravity.CENTER, 0, 0);
                break;
            case NDEvent.E_WX_LOGIN:
                mLoadingDialog.dismiss();
                sendTokenToS();
                break;

            case NDEvent.E_NAME_REFRESH:
                tvNick.setText(NDApp.getInstance().user.getName());

            case NDEvent.E_HEAD_REFRESH:
                Ion.with(mContext)
                        .load(NDApp.getInstance().user.getPicture_url())
                        .withBitmap().error(R.drawable.ic_tem_head)
                        .intoImageView(mIvHead);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);

        switch (v.getId()) {
            case R.id.fmi_rl_info:
                if (!User.isLogin()) {
                    T.showShort(mContext, "请登录");
                    return;
                }
                ((BaseActivity) this.getActivity()).addFragmentToStack(
                        R.id.ami_content, MeInfoFragment.newInstance(), true);

                break;
            case R.id.fmi_rl_doc:
                if (!User.isLogin()) {
                    T.showShort(mContext, "请登录");
                    return;
                }
                ((BaseActivity) this.getActivity()).addFragmentToStack(
                        R.id.ami_content, MeDocFragment.newInstance(), true);

                break;
            case R.id.fmi_rl_bespeak:
                if (!User.isLogin()) {
                    T.showShort(mContext, "请登录");
                    return;
                }
                ((BaseActivity) this.getActivity()).addFragmentToStack(
                        R.id.ami_content, MeBespeakFragment.newInstance(), true);

                break;
            case R.id.fmi_rl_ask:
                if (!User.isLogin()) {
                    T.showShort(mContext, "请登录");
                    return;
                }
                ((BaseActivity) this.getActivity()).addFragmentToStack(
                        R.id.ami_content, MeAskFragment.newInstance(), true);

                break;
            case R.id.fmi_rl_list:
                if (!User.isLogin()) {
                    T.showShort(mContext, "请登录");
                    return;
                }
                ((BaseActivity) this.getActivity()).addFragmentToStack(
                        R.id.ami_content, MeListFragment.newInstance(), true);

                break;
            case R.id.fmi_rl_room:
                if (!User.isLogin()) {
                    T.showShort(mContext, "请登录");
                    return;
                }
                ((BaseActivity) this.getActivity()).addFragmentToStack(
                        R.id.ami_content, MeRoomFragment.newInstance(), true);

                break;
            case R.id.fmi_rl_set:
                ((BaseActivity) this.getActivity()).addFragmentToStack(
                        R.id.ami_content, SetFragment.newInstance(), true);

                break;
            case R.id.fmi_tv_login:
                ((BaseActivity) this.getActivity()).addFragmentToStack(
                        R.id.ami_content, LoginFragment.newInstance(), true);

                break;
        }
    }

    private void getUserInfo() {
        mLoadingDialog = DialogUtil.createLoadingDialog2(getActivity(),
                "登录中...");
        mLoadingDialog.showAtLocation(this.getActivity().getCurrentFocus(),
                Gravity.CENTER, 0, 0);
        Ion.with(mContext,
                PreferenceConstants.BASE_URL + "/1/Clients?action=index")
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                mLoadingDialog.dismiss();
                if (e != null) {
                    T.showShort(mContext, "网络错误，请稍后重试。");
                    return;
                }
                if (result.isJsonNull()) {
                    T.showShort(mContext, "网络错误，请稍后重试。");
                    return;
                }
                if (result.get("retcode").getAsInt() != 0) {
                    T.showShort(mContext, "网络错误，请稍后重试。");
                    return;
                }
                try {

                    L.d(result.get("data").getAsJsonObject().toString());
                    NDApp.getInstance().user = new Gson().fromJson(
                            result.get("data").getAsJsonObject()
                                    .toString(), UserBean.class);

                    EventBus.getDefault().post(new NDEvent(NDEvent.E_GET_INFO));
                } catch (Exception e0) {
                    e0.printStackTrace();
                }
                LoginRefresh();
            }
        });
    }

    private void ClearUserInfo() {
        NDApp.getInstance().user = null;
        mIvHead.setBackground(this.getActivity().getResources()
                .getDrawable(R.drawable.ic_tem_head));
        tvLogin.setOnClickListener(this);
        rlNick.setVisibility(View.GONE);
        tvLogin.setVisibility(View.VISIBLE);
        EventBus.getDefault().post(new NDEvent(NDEvent.E_CLEAR_INFO));
    }

    // 微信code发送到后台，获取token

    // 参数名 是否必需 说明
    // ———— ———— —————————————
    // type 必须 填app
    // code 必须 填前端拿到的临时票据 code
    //
    // COOKIE参数
    // －－－－－－－
    // COOKIE参数OPENID失效
    //
    //
    // 正常情况下，服务端会返回下述JSON数据包，同时setcookie openid
    // {“retcode”:”0”, “openid”: “xxxxxxx”, “authkey”:”xxxxxxxxxxx”}
    //
    //
    // 其他情况，可能会返回
    // 错误类型 返回值 建议处理
    // ———————————— ———— ————————————
    // POST参数不全 2
    // 用户未注册 9
    // 其他错误 12 ＊无法删除
    //
    private void sendTokenToS() {
        mLoadingDialog = DialogUtil.createLoadingDialog2(getActivity(),
                "获取微信token...");
        mLoadingDialog.showAtLocation(this.getActivity().getCurrentFocus(),
                Gravity.CENTER, 0, 0);
        Ion.with(mContext, PreferenceConstants.BASE_URL0 + "/Common/1/wxlogin")
                .setBodyParameter("type", "android")
                .setBodyParameter("code", NDApp.getInstance().WXCode)
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                mLoadingDialog.dismiss();
                // 获取用户信息
                Ion.with(
                        mContext,
                        PreferenceConstants.BASE_URL
                                + "/1/Clients?action=index")
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e,
                                                    JsonObject result) {
                                if (result.isJsonNull()) {
                                    T.showShort(mContext, "网络错误，请稍后重试。");
                                    return;
                                }
                                if (result.get("retcode").getAsInt() != 0) {
                                    T.showShort(mContext, "网络错误，请稍后重试。");
                                    return;
                                }
                                NDApp.getInstance().user = new Gson()
                                        .fromJson(result.get("data")
                                                        .getAsJsonObject()
                                                        .toString(),
                                                UserBean.class);
                                LoginRefresh();
                            }
                        });
            }
        });
    }

    private void getInfoFromWebChat() {
        Ion.with(mContext,
                PreferenceConstants.BASE_URL + "/1/Clients?action=index")
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (result.isJsonNull()) {
                    T.showShort(mContext, "网络错误，请稍后重试。");
                    return;
                }
                if (result.get("retcode").getAsInt() != 0) {
                    T.showShort(mContext, "网络错误，请稍后重试。");
                    return;
                }
                NDApp.getInstance().user = new Gson().fromJson(result
                                .get("data").getAsJsonObject().toString(),
                        UserBean.class);
                LoginRefresh();
            }
        });
    }

}
