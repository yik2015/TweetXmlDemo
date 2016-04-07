package org.demo.yuyang.tweetxmldemo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import org.demo.yuyang.tweetxmldemo.AppContext;
import org.demo.yuyang.tweetxmldemo.R;
import org.demo.yuyang.tweetxmldemo.adapter.ListBaseAdapter;
import org.demo.yuyang.tweetxmldemo.adapter.TweetAdapter;
import org.demo.yuyang.tweetxmldemo.api.remote.OSChinaApi;
import org.demo.yuyang.tweetxmldemo.base.BaseListFragment;
import org.demo.yuyang.tweetxmldemo.bean.ListEntity;
import org.demo.yuyang.tweetxmldemo.bean.Tweet;
import org.demo.yuyang.tweetxmldemo.bean.TweetsList;
import org.demo.yuyang.tweetxmldemo.interf.OnTabReselectListener;
import org.demo.yuyang.tweetxmldemo.ui.empty.EmptyLayout;
import org.demo.yuyang.tweetxmldemo.util.XmlUtils;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Created on 4/6/16.
 */
public class TweetsFragment extends BaseListFragment<Tweet>
        implements AdapterView.OnItemLongClickListener, OnTabReselectListener {

    protected static final String TAG = TweetsFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "tweetslist_";

    // TODO delete tweet handler

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mCatalog > 0) {
            // TODO user change?
        }
    }

    @Override
    public void onDestroy() {
        if (mCatalog > 0) {
            //TODO unregisterReceiver
        }
        super.onDestroy();
    }

    protected ListBaseAdapter<Tweet> getListAdapter() {
        return new TweetAdapter();
    }

    @Override
    public String getCacheKeyPrefix() {
        // TODO getCacheKeyPrefix
        return CACHE_KEY_PREFIX + mCatalog;
    }

    public String getTopic() {
        // TODO getTopic
        return null;
    }

    @Override
    protected ListEntity<Tweet> parseList(InputStream is) throws Exception {
        return XmlUtils.toBean(TweetsList.class, is);
    }

    @Override
    protected ListEntity<Tweet> readList(Serializable seri) {
        return (TweetsList) seri;
    }

    @Override
    protected void sendRequestData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String str = bundle.getString("topic");
            if (str != null) {
                // TODO get topic list
            }
        }

        OSChinaApi.getTweetList(mCatalog, mCurrentPage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Tweet tweet = mAdapter.getItem(position);
        if (tweet != null) {
            //TODO show tweet detail.
        }
    }

    //TODO receiver.
    // TODO setupContent

    @Override
    protected void requestData(boolean refresh) {
        if (mCatalog > 0) {
            if (AppContext.getInstance().isLogin()) {
                mCatalog = AppContext.getInstance().getLoginUid();
                super.requestData(refresh);
            } else {
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                mErrorLayout.setErrorMessage(getString(R.string.unlogin_tip));
            }
        } else super.requestData(refresh);
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mListView.setOnItemClickListener(this);
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCatalog > 0) {
                    if (AppContext.getInstance().isLogin()) {
                        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                        requestData(true);
                    } else {
                        // TODO show login activity.
                    }
                } else {
                    mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                    requestData(true);
                }
            }
        });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO onItemLongClick
        return false;
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onTabReselect() {
        onRefresh();
    }

}
