package net.meluo.newdoc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigd.utl.Checking;
import com.dadao.view.TitleBar;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.way.util.PreferenceConstants;
import com.way.util.T;

import net.meluo.newdoc.R;
import net.meluo.newdoc.bean.Doc;

import org.json.JSONException;
import org.json.JSONObject;

public final class DocRoomDocInfoFragment extends BaseFragment {

    TitleBar mTitleBar;
    TextView mTvEmpty;
    PullToRefreshListView mPullListView;
    ListView mListView;

    Context mContext;

    TextView tvName, tvDocTitle, tvCatalog, tvGoodAt, tvEducation,
            tvExperience, tvResearch, tvInfo;
    int type = 0;
    ImageView mIvHead;
    Doc doc;

    public static DocRoomDocInfoFragment newInstance(Doc doc) {
        DocRoomDocInfoFragment fragment = new DocRoomDocInfoFragment(doc);
        return fragment;
    }

    public DocRoomDocInfoFragment(Doc doc) {
        this.doc = doc;
        mContext = this.getActivity();
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
                R.layout.f_doc_room_doc_info, null);
        initView(layout);

        return layout;
    }

    protected void initView(RelativeLayout root) {
        mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
        mTitleBar.setTitleText("医生简介");
        mTitleBar.enableLeftButton(true);
        mTitleBar.getLeftButton().setOnClickListener(this);

        this.tvName = (TextView) root.findViewById(R.id.fdrdi_tv_name);
        this.tvInfo = (TextView) root.findViewById(R.id.fdrdi_tv_info);
        this.tvName.setText(doc.getName());

        this.tvExperience = (TextView) root.findViewById(R.id.fdrdi_tv_11);
        this.tvResearch = (TextView) root.findViewById(R.id.fdrdi_tv_21);

        this.mIvHead = (ImageView)root.findViewById(R.id.fdrdi_iv_head);

        Ion.with(mContext).load(doc.getHead()).withBitmap()
                .error(R.drawable.ic_tem_head).intoImageView(this.mIvHead);


        FloadDocInfo();
    }

    private void FloadDocInfo() {

        Ion.with(
                this.getActivity(),
                PreferenceConstants.BASE_URL + "/1/Doctors/" + doc.getId()
                        + "?action=intro").asString()
                .setCallback(new FutureCallback<String>() {

                    @Override
                    public void onCompleted(Exception arg0, String data) {

                        if (Checking.isNullorBlank(data)) {
                            T.showShort(mContext, "网络错误，请稍后重试");
                            return;
                        }
                        try {
                            JSONObject o = new JSONObject(data);
                            JSONObject d = o.getJSONObject("data");

                            doc.fromJSONObject(d);

                            tvInfo.setText(doc.getTitle() + "\n科室："
                                    + doc.getDepartment() + "\n擅长："
                                    + doc.getSpecialty() + "\n学历："
                                    + doc.getEducation());

                            tvExperience.setText(doc.getExperience());
                            tvResearch.setText(doc.getResearch());

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
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_left:
                this.getActivity().getSupportFragmentManager().popBackStack();
                break;

        }
    }
}
