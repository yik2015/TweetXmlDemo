package org.demo.yuyang.tweetxmldemo.base;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.demo.yuyang.tweetxmldemo.AppContext;
import org.demo.yuyang.tweetxmldemo.R;
import org.demo.yuyang.tweetxmldemo.adapter.ListBaseAdapter;
import org.demo.yuyang.tweetxmldemo.bean.Entity;
import org.demo.yuyang.tweetxmldemo.bean.ListEntity;
import org.demo.yuyang.tweetxmldemo.bean.Result;
import org.demo.yuyang.tweetxmldemo.bean.ResultBean;
import org.demo.yuyang.tweetxmldemo.ui.empty.EmptyLayout;
import org.demo.yuyang.tweetxmldemo.util.XmlUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.msebera.android.httpclient.Header;

/**
 * Created on 3/31/16.
 */
public abstract class BaseListFragment<T extends Entity> extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener,
        AbsListView.OnScrollListener {

    public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";

    @InjectView(R.id.swiperefreshlayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.listview)
    protected ListView mListView;

    protected ListBaseAdapter<T> mAdapter;

    @InjectView(R.id.error_layout)
    protected EmptyLayout mErrorLayout;

    // TODO protected int mStoreEmptyState = -1;

    protected int mCurrentPage = 0;

    protected int mCatalog = 1;

    // 错误信息
    protected Result mResult;

    // TODO private AsyncTask<String, Void, ListEntity<T>> mCacheTask;
    private ParserTask mParserTask;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pull_refresh_listview;
    }

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            // TweetsList.CATALOG_LATEST=0;
            mCatalog = args.getInt(BUNDLE_KEY_CATALOG, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        initView(view);
    }

    @Override
    public void initView(View view) {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);

        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPage = 0;
                mState = STATE_REFRESH;

                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);

                requestData(true);
            }
        });

        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);

        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        } else {
            mAdapter = getListAdapter();
            mListView.setAdapter(mAdapter);

            if (requestDataIfViewCreated()) {
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                mState = STATE_NONE;

                requestData(false);
            } else {
                mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            }
        }

        // TODO if (mStore)
    }

    protected void requestData(boolean refresh) {
        String key = getCacheKey();

        // TODO decide if read cache.

        sendRequestData();
    }

    protected void sendRequestData() {

    }

    @Override
    public void onDestroyView() {
        // TODO mStore
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        // TODO cancelTask
        super.onDestroy();
    }

    protected abstract ListBaseAdapter<T> getListAdapter();

    @Override
    public void onRefresh() {
        if (mState == STATE_REFRESH) return;

        mListView.setSelection(0);
        // TODO setswipestate
        mCurrentPage = 0;
        mState = STATE_REFRESH;
        requestData(true);
    }


    protected String getCacheKeyPrefix() {
        return null;
    }

    protected ListEntity<T> parseList(InputStream is) throws Exception {
        return null;
    }

    protected ListEntity<T> readList(Serializable seri) {
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private String getCacheKey() {
        // TODO get cache key.

        return null;
    }

    // 是否需要自动刷新
    protected boolean needAutoRefresh() {
        return true;
    }

    // TODO onTimeRefresh


    protected long getAutoRefreshTime() {
        return 12 * 60 * 60;
    }

    protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            // TODO save refresh time.

            if (isAdded()) {
                if (mState == STATE_REFRESH) {
                    onRefreshNetworkSuccess();
                }

                executeParserTask(responseBody);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            if (isAdded()) {
                // TODO read cache.
            }
        }
    };

    protected void onRefreshNetworkSuccess() {
    }

    private void executeParserTask(byte[] data) {
        cancelParserTask();
        mParserTask = new ParserTask(data);
        mParserTask.execute();
    }

    private void cancelParserTask() {
        if (mParserTask != null) {
            mParserTask.cancel(true);
            mParserTask = null;
        }
    }

    class ParserTask extends AsyncTask<Void, Void, String> {
        private final byte[] responseData;
        private boolean parserError;
        private List<T> list;

        public ParserTask(byte[] data) {
            responseData = data;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                ListEntity<T> data = parseList(
                        new ByteArrayInputStream(responseData));
                // TODO save cache.
                list = data.getList();
                if (list == null) {
                    // TODO handle result.
                    ResultBean resultBean = XmlUtils.toBean(
                            ResultBean.class, responseData);
                    if (resultBean != null) {
                        mResult = resultBean.getResult();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

                parserError = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (parserError) {
                // TODO read cache.
            } else {
                executeOnLoadDataSuccess(list);
                executeOnLoadFinish();
            }
        }
    }

    protected void executeOnLoadDataSuccess(List<T> data) {
        if (data == null) {
            data = new ArrayList<>();
        }

        if (mResult != null && !mResult.OK()) {
            // TODO log out?
        }

        mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);

        if (mCurrentPage == 0) {
            mAdapter.clear();
        }

        for (int i = 0; i < data.size(); i++) {
            if (compareTo(mAdapter.getData(), data.get(i))) {
                data.remove(i);
                i--;
            }
        }

        int adapterState;

        if (mAdapter.getCount() + data.size() == 0) {
            adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;
        } else if (data.size() == 0
                || data.size() < getPageSize() && mCurrentPage == 0) {
            adapterState = ListBaseAdapter.STATE_NO_MORE;
            // Is it has effect?
            // TODO mAdapter.notifyDataSetChanged();
        } else {
            adapterState = ListBaseAdapter.STATE_LOAD_MORE;
        }

        mAdapter.setState(adapterState);
        mAdapter.addData(data);

        if (mAdapter.getCount() == 1) {
            if (needShowEmptyNoData()) {
                mErrorLayout.setErrorType(EmptyLayout.NODATA);
            } else {
                mAdapter.setState(ListBaseAdapter.STATE_EMPTY_ITEM);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    protected void executeOnLoadFinish() {
        setSwipeRefreshLoadedState();
        mState = STATE_NONE;
    }

    protected boolean compareTo(List<? extends Entity> data, Entity entity) {
        if (entity != null) {
            for (int i = 0; i < data.size(); i++) {
                if (entity.getId() == data.get(i).getId()) {
                    return true;
                }
            }
        }

        return false;
    }

    protected void setSwipeRefreshLoadedState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

    protected int getPageSize() {
        return AppContext.PAGE_SIZE;
    }

    protected boolean needShowEmptyNoData() {
        return true;
    }

    protected boolean requestDataIfViewCreated() {
        return true;
    }
}
