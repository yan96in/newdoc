package net.meluo.newdoc.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;

import com.bigd.utl.WindowUtl;

import net.meluo.newdoc.R;

import cn.jpush.android.api.JPushInterface;

@SuppressWarnings("deprecation")
public class MainTabActivity extends TabActivity implements
        OnCheckedChangeListener {

    public static final String LOGIN_ACTION = "cn.smilecity.action.LOGIN";

    private TabHost mTabHost;
    private Intent mAIntent;
    private Intent mBIntent;
    private Intent mCIntent;

    RadioButton btnTS;
    RadioButton btnMC;
    RadioButton btnPC;

    private int index = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tabs);
        WindowUtl.setTranslucentStatus(this);

        index = this.getIntent().getIntExtra("index", 1);
        this.mAIntent = new Intent(this, CommunityIndexActivity.class);
        this.mBIntent = new Intent(this, ConsultationRoomIndexActivity.class);
        this.mCIntent = new Intent(this, MeIndexActivity.class);
        btnTS = (RadioButton) findViewById(R.id.radio_cm);
        btnMC = (RadioButton) findViewById(R.id.radio_cl);
        btnPC = (RadioButton) findViewById(R.id.radio_me);

        btnTS.setOnCheckedChangeListener(this);
        btnMC.setOnCheckedChangeListener(this);
        btnPC.setOnCheckedChangeListener(this);
        setupIntent();

        switch (index) {
            case 0:
                break;
            case 1:
                btnMC.setChecked(true);
                break;
            case 2:
                btnPC.setChecked(true);
                break;

        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.radio_cm:
                    this.mTabHost.setCurrentTabByTag("A_TAB");
                    break;
                case R.id.radio_cl:
                    this.mTabHost.setCurrentTabByTag("B_TAB");
                    break;
                case R.id.radio_me:
                    this.mTabHost.setCurrentTabByTag("C_TAB");
                    break;
            }
        }

    }

    private void setupIntent() {
        this.mTabHost = getTabHost();
        TabHost localTabHost = this.mTabHost;

        localTabHost.addTab(buildTabSpec("A_TAB", R.string.cl,
                R.drawable.radio_cl, this.mAIntent));

        localTabHost.addTab(buildTabSpec("B_TAB", R.string.cm,
                R.drawable.radio_cm, this.mBIntent));

        localTabHost.addTab(buildTabSpec("C_TAB", R.string.me,
                R.drawable.radio_me, this.mCIntent));

    }

    private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,
                                         final Intent content) {
        return this.mTabHost
                .newTabSpec(tag)
                .setIndicator(getString(resLabel),
                        getResources().getDrawable(resIcon))
                .setContent(content);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dialog();
            return true;
        }
        return true;
    }

    protected void dialog() {
        AlertDialog.Builder builder = new Builder(MainTabActivity.this);
        builder.setMessage("确定要退出吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // AccoutList.this.finish();
                        // System.exit(1);
                        android.os.Process.killProcess(android.os.Process
                                .myPid());
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        JPushInterface.onResume(this);
    }
}