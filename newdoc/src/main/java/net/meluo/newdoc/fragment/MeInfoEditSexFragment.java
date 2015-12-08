package net.meluo.newdoc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

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

public final class MeInfoEditSexFragment extends BaseFragment {

    Context mContext;
    TitleBar mTitleBar;
    RadioGroup mRg;
    RadioButton mRbtn0, mRbtn1;
    Button btnOK;

    public static MeInfoEditSexFragment newInstance() {

        MeInfoEditSexFragment fragment = new MeInfoEditSexFragment();

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
        layout = (RelativeLayout) inflater
                .inflate(R.layout.f_me_edit_sex, null);
        initView(layout);
        return layout;
    }

    protected void initView(RelativeLayout root) {
        mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
        mTitleBar.setTitleText("性别");

        mTitleBar.enableLeftButton(true);
        mTitleBar.getLeftButton().setOnClickListener(this);

        mRbtn0 = (RadioButton) root.findViewById(R.id.radio_0);
        mRbtn1 = (RadioButton) root.findViewById(R.id.radio_1);
        try {
            if (NDApp.getInstance().user.getSex() == 1) {
                mRbtn1.setChecked(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mRg = (RadioGroup) root.findViewById(R.id.fmes_rg_sex);
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                switch (checkedId) {
                    case R.id.radio_0:
                        // NDApp.getInstance().user.setSex(0);
                        break;
                    case R.id.radio_1:
                        // NDApp.getInstance().user.setSex(1);
                        break;

                }
            }
        });

        btnOK = (Button) root.findViewById(R.id.fmes_btn_ok);
        btnOK.setOnClickListener(this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void ChangeSex() {

        mLoadingDialog = DialogUtil.createLoadingDialog2(getActivity(),
                "修改性别...");
        mLoadingDialog.showAtLocation(this.getActivity().getCurrentFocus(),
                Gravity.CENTER, 0, 0);
        final int sex;
        if (mRbtn0.isChecked()) {
            sex = 0;
        } else {
            sex = 1;
        }
        try {

            JSONArray a = new JSONArray();
            JSONObject o = new JSONObject();

            try {

                o.put("sex", sex);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            a.put(o);
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
                            T.showShort(mContext, "修改成功");
                            NDApp.getInstance().user.setSex(sex);
                            EventBus.getDefault().post(
                                    new NDEvent(NDEvent.E_SEX_REFRESH));
                        }
                    });

        } catch (Exception e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
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
            case R.id.fmes_btn_ok:

                ChangeSex();
                break;
        }

    }
}
