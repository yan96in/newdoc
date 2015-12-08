package net.meluo.newdoc.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.bigd.utl.WindowUtl;

import net.meluo.newdoc.fragment.BaseFragment;

import java.util.ArrayList;

public class BaseActivity extends FragmentActivity {
    public static ArrayList<BackPressHandler> mListeners = new ArrayList<BackPressHandler>();

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        WindowUtl.setTranslucentStatus(this);
    }

    protected void initView() {

    }

    protected void initData() {

    }

    public void ReplaceFragmentToStack(int id, BaseFragment fragment,
                                       boolean isAddToStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        // ft.setCustomAnimations(R.anim.push_right_in, R.anim.push_left_out);
        ft.replace(id, fragment);
        if (isAddToStack) {
            ft.addToBackStack("fragment");
        }

        ft.commitAllowingStateLoss();
    }

    public void addFragmentToStack(int id, BaseFragment fragment,
                                   boolean isAddToStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        // ft.setCustomAnimations(R.anim.push_right_in, R.anim.push_left_out);
        ft.add(id, fragment);
        if (isAddToStack) {
            ft.addToBackStack("fragment");
        }

        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListeners.size() > 0)
            for (BackPressHandler handler : mListeners) {
                handler.activityOnResume();
            }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mListeners.size() > 0)
            for (BackPressHandler handler : mListeners) {
                handler.activityOnPause();
            }
    }

    public static abstract interface BackPressHandler {

        public abstract void activityOnResume();

        public abstract void activityOnPause();

    }

}
