package org.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/3
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class BaseFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;

    public BaseFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }
}
