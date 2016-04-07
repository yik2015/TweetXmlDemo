package org.demo.yuyang.tweetxmldemo.viewpagerfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.demo.yuyang.tweetxmldemo.R;
import org.demo.yuyang.tweetxmldemo.adapter.ViewPageFragmentAdapter;
import org.demo.yuyang.tweetxmldemo.base.BaseListFragment;
import org.demo.yuyang.tweetxmldemo.base.BaseViewPagerFragment;
import org.demo.yuyang.tweetxmldemo.bean.TweetsList;
import org.demo.yuyang.tweetxmldemo.fragment.TweetsFragment;
import org.demo.yuyang.tweetxmldemo.interf.OnTabReselectListener;

/**
 * Created on 3/31/16.
 */
public class TweetsViewPagerFragment extends BaseViewPagerFragment
        implements OnTabReselectListener {

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title = getResources().getStringArray(
                R.array.tweets_viewpage_arrays);

//        adapter.addTab(title[0], "new_tweets", ListFragment.class,
//                getBundle(TweetsList.CATALOG_LATEST));
//        adapter.addTab(title[1], "hot_tweets", ListFragment.class,
//                getBundle(TweetsList.CATALOG_HOT));
//        adapter.addTab(title[2], "my_tweets", ListFragment.class,
//                getBundle(TweetsList.CATALOG_ME));
        adapter.addTab(title[0], "new_tweets", TweetsFragment.class,
                getBundle(TweetsList.CATALOG_LATEST));
        adapter.addTab(title[1], "hot_tweets", TweetsFragment.class,
                getBundle(TweetsList.CATALOG_HOT));
        adapter.addTab(title[2], "my_tweets", TweetsFragment.class,
                getBundle(TweetsList.CATALOG_ME));
    }

    private Bundle getBundle(int catalog) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseListFragment.BUNDLE_KEY_CATALOG, catalog);
        return bundle;
    }

    @Override
    public void onTabReselect() {
        try {
            int currentIndex = mViewPager.getCurrentItem();

            Fragment currentFragment = getChildFragmentManager().getFragments()
                    .get(currentIndex);

            if (currentFragment != null
                    && currentFragment instanceof OnTabReselectListener) {
                ((OnTabReselectListener) currentFragment).onTabReselect();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void setScreenPageLimit() {
        mViewPager.setOffscreenPageLimit(3);
    }
}
