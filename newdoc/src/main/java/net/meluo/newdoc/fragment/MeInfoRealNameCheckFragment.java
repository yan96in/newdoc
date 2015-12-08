package net.meluo.newdoc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.bigd.utl.Checking;
import com.dadao.view.DialogUtil;
import com.dadao.view.TitleBar;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.way.util.PreferenceConstants;
import com.way.util.T;
import com.ypy.eventbus.EventBus;

import net.meluo.newdoc.R;
import net.meluo.newdoc.app.NDApp;
import net.meluo.newdoc.app.NDEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class MeInfoRealNameCheckFragment extends BaseFragment {

    Context mContext;
    TitleBar mTitleBar;
    EditText mEtName, mEtID, mEtSocial;
    Button btnOK;
    PopupWindow mLoadingDialog = null;

    public static MeInfoRealNameCheckFragment newInstance() {

        MeInfoRealNameCheckFragment fragment = new MeInfoRealNameCheckFragment();

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
        layout = (RelativeLayout) inflater.inflate(
                R.layout.f_me_real_name_check, null);
        initView(layout);
        return layout;
    }

    protected void initView(RelativeLayout root) {
        mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
        mTitleBar.setTitleText("实名认证");

        mTitleBar.enableLeftButton(true);
        mTitleBar.getLeftButton().setOnClickListener(this);
        mEtName = (EditText) root.findViewById(R.id.fmrnc_name);
        mEtID = (EditText) root.findViewById(R.id.fmrnc_id_number);
        mEtSocial = (EditText) root.findViewById(R.id.fmrnc_social_number);
        btnOK = (Button) root.findViewById(R.id.fmrnc_btn_ok);
        btnOK.setOnClickListener(this);

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
            case R.id.fmrnc_btn_ok:

                check();

                break;
        }

    }

    private void check() {
        if (Checking.isNullorBlank(mEtName.getText().toString())) {
            T.showShort(MeInfoRealNameCheckFragment.this.getActivity(), "请输入姓名");
            return;
        }
        if (Checking.isNullorBlank(mEtID.getText().toString())) {
            T.showShort(MeInfoRealNameCheckFragment.this.getActivity(),
                    "请输入身份证号");
            return;
        }
        if (Checking.isNullorBlank(mEtSocial.getText().toString())) {
            T.showShort(MeInfoRealNameCheckFragment.this.getActivity(),
                    "请输入社保账号");
            return;
        }

        mLoadingDialog = DialogUtil.createLoadingDialog2(getActivity(),
                "实名认证...");
        mLoadingDialog.showAtLocation(this.getActivity().getCurrentFocus(),
                Gravity.CENTER, 0, 0);
        JSONArray a = new JSONArray();

        JSONObject user = new JSONObject();
        a.put(user);
        try {
            user.put("real_name", mEtName.getText().toString());
            user.put("citizenid", mEtID.getText().toString());
            user.put("insuranceid", mEtSocial.getText().toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Ion.with(mContext, PreferenceConstants.BASE_URL + "/1/Clients")
                .setBodyParameter("action", "realname")
                .setBodyParameter("items", a.toString()).asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {

                    @Override
                    public void onCompleted(Exception arg0, JsonObject result) {
                        mLoadingDialog.dismiss();
                        if (result == null) {
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
                        T.showShort(mContext, "认证成功");
                        NDApp.getInstance().user.setName(mEtName.getText()
                                .toString());
                        NDApp.getInstance().user.setCitizenid(mEtID.getText()
                                .toString());
                        NDApp.getInstance().user.setInsuranceid(mEtSocial
                                .getText().toString());
                        EventBus.getDefault().post(
                                new NDEvent(NDEvent.E_REAL_NAME_CHECK));
                    }
                });
    }
}
