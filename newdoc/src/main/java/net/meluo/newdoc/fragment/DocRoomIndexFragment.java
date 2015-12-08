package net.meluo.newdoc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dadao.view.TitleBar;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.way.util.T;

import net.meluo.core.User;
import net.meluo.newdoc.R;
import net.meluo.newdoc.activity.BaseActivity;

public final class DocRoomIndexFragment extends BaseFragment {

	TitleBar mTitleBar;
	TextView mTvEmpty;
	PullToRefreshListView mPullListView;
	ListView mListView;
	Button btnDocSelf, btnQA, btnBespeak;
	Context mContext;

	public static DocRoomIndexFragment newInstance() {

		DocRoomIndexFragment fragment = new DocRoomIndexFragment();
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
				R.layout.f_doc_room_index, null);
		initView(layout);

		return layout;
	}

	protected void initView(RelativeLayout root) {
		mTitleBar = (TitleBar) root.findViewById(R.id.title_bar);
		mTitleBar.setTitleText("新医诊室");

		btnDocSelf = (Button) root.findViewById(R.id.acri_btn_doc_self);
		btnDocSelf.setOnClickListener(this);
		btnQA = (Button) root.findViewById(R.id.acri_btn_qa);
		btnQA.setOnClickListener(this);
		btnBespeak = (Button) root.findViewById(R.id.acri_btn_bespeak);
		btnBespeak.setOnClickListener(this);

		RelativeLayout tem0 = (RelativeLayout) root.findViewById(R.id.tem_0);
		tem0.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((BaseActivity) mContext).addFragmentToStack(
						R.id.adri_content,
						WebFragment
								.newInstance("http://www.xinyijk.com/mpublicactivity.html"),
						true);
			}
		});
		RelativeLayout tem1 = (RelativeLayout) root.findViewById(R.id.tem_1);
		tem1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((BaseActivity) mContext).addFragmentToStack(
						R.id.adri_content,
						WebFragment
								.newInstance("http://www.xinyijk.com/meducation.html"),
						true);
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
		case R.id.acri_btn_doc_self:
			((BaseActivity) this.getActivity())
					.addFragmentToStack(R.id.adri_content,
							DocSelfIndexFragment.newInstance(), true);
			break;
		case R.id.acri_btn_qa:
			((BaseActivity) this.getActivity()).addFragmentToStack(
					R.id.adri_content, AskMainFragment.newInstance(), true);
			break;
		case R.id.acri_btn_bespeak:
			// TODO 这中添加方式还是不方便
			if (!User.isLogin()) {
				T.showShort(mContext, "请先登录");
				return;
			}
			((BaseActivity) this.getActivity()).addFragmentToStack(
					R.id.adri_content, DocRoomListFragment.newInstance(), true);
			break;
		}
	}
}
