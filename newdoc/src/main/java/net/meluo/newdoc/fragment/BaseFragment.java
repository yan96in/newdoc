package net.meluo.newdoc.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.PopupWindow;

import com.ypy.eventbus.EventBus;

import net.meluo.newdoc.app.NDEvent;

public class BaseFragment extends Fragment implements View.OnClickListener {

    PopupWindow mLoadingDialog = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    public void onEvent(NDEvent event) {
    }

}
