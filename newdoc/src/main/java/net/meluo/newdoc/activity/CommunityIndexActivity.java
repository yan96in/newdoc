package net.meluo.newdoc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dadao.view.TitleBar;
import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.beans.Source;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.login.LoginListener;
import com.umeng.comm.core.utils.Log;
import com.umeng.comm.ui.fragments.CommunityMainFragment;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.way.util.PreferenceConstants;
import com.ypy.eventbus.EventBus;

import net.meluo.core.User;
import net.meluo.newdoc.R;
import net.meluo.newdoc.app.NDApp;
import net.meluo.newdoc.app.NDEvent;

public class CommunityIndexActivity extends BaseActivity {

    TitleBar mTitleBar;
    TextView mTvEmpty;

    CommunityMainFragment mFeedsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.a_commnity_index);
        initView();


    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mFeedsFragment",
                mFeedsFragment);
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);


    }


    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void initView() {

        mFeedsFragment = new CommunityMainFragment();
        // 设置Feed流页面的返回按钮不可见
        mFeedsFragment.setBackButtonVisibility(View.INVISIBLE);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.aci_content, mFeedsFragment)
                .commit();

        if (User.isLogin()) {
            useCustomLogin();
        } else {
            useCustomLogout();
        }
        new UMWXHandler(this, PreferenceConstants.WX_APP_ID, PreferenceConstants.WX_APP_SECRET).addToSocialSDK();
        UMWXHandler wxCircleHandler = new UMWXHandler(this, PreferenceConstants.WX_APP_ID, PreferenceConstants.WX_APP_SECRET);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    public void onEvent(NDEvent event) {

        switch (event.getEvent()) {
            case NDEvent.E_GET_INFO:
                useCustomLogin();
                break;
            case NDEvent.E_CLEAR_INFO:
                useCustomLogout();
                break;

        }
    }

    protected void useCustomLogin() {
        // 管理器
        CommunitySDK sdk = CommunityFactory.getCommSDK(this);
        CommUser user = new CommUser();
        user.name = NDApp.getInstance().user.getName();
        // user.iconUrl = NDApp.getInstance().user.getPicture_url();
        user.source = Source.SELF_ACCOUNT;
//        if( NDApp.getInstance().user.getSex()==0){
//            user.gender = CommUser.Gender.MALE;
//        }else{
//            user.gender = CommUser.Gender.FEMALE;
//        }

        user.id = NDApp.getInstance().user.getMobile();
        sdk.loginToUmengServer(this, user, new LoginListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(int stCode, CommUser commUser) {
                Log.d("tag", "login result is" + stCode); // 获取登录结果状态码
                if (true) {
                    // 在此处可以跳转到任何一个你想要的activity

                }

            }
        });
    }

    protected void useCustomLogout() {
        // 管理器
        CommunitySDK sdk = CommunityFactory.getCommSDK(this);
        CommUser loginedUser = new CommUser("1"); // 用户id
        loginedUser.name = "昵称"; // 用户昵称
        loginedUser.source = Source.SELF_ACCOUNT;// 账户系统来源
        loginedUser.gender = CommUser.Gender.FEMALE;// 用户性别
        loginedUser.level = 1; // 用户等级，非必须字段
        loginedUser.score = 0;// 积分，非必须字段
        sdk.loginToUmengServer(this, loginedUser, new LoginListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(int stCode, CommUser commUser) {
                Log.d("tag", "login result is" + stCode); // 获取登录结果状态码
                if (true) {
                    // 在此处可以跳转到任何一个你想要的activity

                }

            }
        });
    }

}
