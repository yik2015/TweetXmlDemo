package org.demo.yuyang.tweetxmldemo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.demo.yuyang.tweetxmldemo.R;
import org.demo.yuyang.tweetxmldemo.adapter.ViewPageFragmentAdapter;
import org.demo.yuyang.tweetxmldemo.ui.empty.EmptyLayout;
import org.demo.yuyang.tweetxmldemo.widget.PagerSlidingTabStrip;

/**
 * Created on 3/30/16.
 */
public abstract class BaseViewPagerFragment extends BaseFragment {
    protected PagerSlidingTabStrip mTabStrip;
    protected ViewPager mViewPager;
    protected ViewPageFragmentAdapter mTabsAdapter;
    protected EmptyLayout mErrorLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.base_viewpage_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.pager_tabstrip);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mErrorLayout = (EmptyLayout) view.findViewById(R.id.error_layout);

        mTabsAdapter = new ViewPageFragmentAdapter(getChildFragmentManager(),
                mTabStrip, mViewPager);

        setScreenPageLimit();
        onSetupTabAdapter(mTabsAdapter);
    }

    protected abstract void onSetupTabAdapter(ViewPageFragmentAdapter adapter);

    protected void setScreenPageLimit() {

    }
}
