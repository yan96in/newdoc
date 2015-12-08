package net.meluo.newdoc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import net.meluo.newdoc.bean.UserBean;

import org.json.JSONArray;

public final class MeInfoEditNameFragment extends BaseFragment {

    Context mContext;
    TitleBar mTitleBar;
    EditText mEtName;
    Button btnOK;

    public static MeInfoEditNameFragment newInstance() {

        MeInfoEditNameFragment fragment = new MeInfoEditNameFragment();

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
        layout = (RelativeLayout) inflater.inflate(R.layout.f_r_name, null);
        initView(layout);
        return layout;
    }

    protected void initView(RelativeLayout root) {
        mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
        mTitleBar.setTitleText("修改昵称");

        mTitleBar.enableLeftButton(true);
        mTitleBar.getLeftButton().setOnClickListener(this);
        mEtName = (EditText) root.findViewById(R.id.frn_name);
        btnOK = (Button) root.findViewById(R.id.frn_btn_ok);
        btnOK.setOnClickListener(this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void ChangeName() {

        if (Checking.isNullorBlank(mEtName.getText().toString())) {
            T.showShort(MeInfoEditNameFragment.this.getActivity(), "请输入昵称");
            return;
        }

        mLoadingDialog = DialogUtil.createLoadingDialog2(getActivity(),
                "修改昵称...");
        mLoadingDialog.showAtLocation(this.getActivity().getCurrentFocus(),
                Gravity.CENTER, 0, 0);
        try {

            JSONArray a = new JSONArray();
            UserBean user = new UserBean();
            user.setName(mEtName.getText().toString());
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
                            T.showShort(mContext, "修改成功");
                            NDApp.getInstance().user.setName(mEtName.getText()
                                    .toString());
                            EventBus.getDefault().post(
                                    new NDEvent(NDEvent.E_NAME_REFRESH));

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
            case R.id.frn_btn_ok:

                ChangeName();
                break;
        }
    }
}
