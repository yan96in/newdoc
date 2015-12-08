package net.meluo.newdoc.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import net.meluo.newdoc.fragment.BaseFragment;
import net.meluo.newdoc.fragment.RoomListFragment;

public class RoomListFragmentAdapter extends FragmentStatePagerAdapter {

    public RoomListFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public BaseFragment getItem(int position) {
        switch (position) {
            case 0:
                return RoomListFragment.newInstance();

            case 1:
                return RoomListFragment.newInstance();

            default:

                return RoomListFragment.newInstance();
        }

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 2;
    }

}