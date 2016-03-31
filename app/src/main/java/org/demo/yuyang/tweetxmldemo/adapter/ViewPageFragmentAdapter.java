package org.demo.yuyang.tweetxmldemo.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.demo.yuyang.tweetxmldemo.R;
import org.demo.yuyang.tweetxmldemo.widget.PagerSlidingTabStrip;

import java.util.ArrayList;

/**
 * Created on 3/31/16.
 */
public class ViewPageFragmentAdapter extends FragmentStatePagerAdapter {

    private final Context mContext;
    protected PagerSlidingTabStrip mPagerStrip;
    private final ViewPager mViewPager;
    private final ArrayList<ViewPageInfo> mTabs = new ArrayList<>();

    /**
     * Set this new object for pager's adapter, set pager for pagerStrip's ViewPager.
     *
     * @param pageStrip pager need to set to it.
     * @param pager     its pager.
     */
    public ViewPageFragmentAdapter(FragmentManager fm,
                                   PagerSlidingTabStrip pageStrip,
                                   ViewPager pager) {
        super(fm);
        mContext = pager.getContext();
        mPagerStrip = pageStrip;
        mViewPager = pager;
        mViewPager.setAdapter(this);
        mPagerStrip.setViewPager(mViewPager);
    }

    public void addTab(String title, String tag, Class<?> cls, Bundle args) {
        ViewPageInfo viewPageInfo = new ViewPageInfo(title, tag, cls, args);
        addFragment(viewPageInfo);
    }

    private void addFragment(ViewPageInfo info) {
        if (info == null) {
            return;
        }

        View v = LayoutInflater.from(mContext).inflate(
                R.layout.base_viewpage_fragment_tab_item, null, false);

        TextView title = (TextView) v.findViewById(R.id.tab_title);
        title.setText(info.title);

        mPagerStrip.addTab(v);

        mTabs.add(info);

        notifyDataSetChanged();
    }

    public void remove() {
        remove(0);
    }

    public void remove(int index) {
        if (mTabs.isEmpty()) return;

        if (index < 0) index = 0;

        if (index >= mTabs.size()) index = mTabs.size() - 1;

        mTabs.remove(index);
        mPagerStrip.removeTab(index, 1);

        notifyDataSetChanged();
    }

    public void removeAll() {
        if (mTabs.isEmpty()) return;

        mPagerStrip.removeAllTab();

        mTabs.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        ViewPageInfo info = mTabs.get(position);
        return Fragment.instantiate(mContext, info.cls.getName(), info.args);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).title;
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }
}
