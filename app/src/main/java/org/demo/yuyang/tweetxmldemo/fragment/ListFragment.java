package org.demo.yuyang.tweetxmldemo.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.demo.yuyang.tweetxmldemo.R;
import org.demo.yuyang.tweetxmldemo.adapter.ListFragmentAdapter;
import org.demo.yuyang.tweetxmldemo.api.remote.OSChinaApi;
import org.demo.yuyang.tweetxmldemo.bean.Entity;
import org.demo.yuyang.tweetxmldemo.bean.ListEntity;
import org.demo.yuyang.tweetxmldemo.bean.Tweet;
import org.demo.yuyang.tweetxmldemo.bean.TweetsList;
import org.demo.yuyang.tweetxmldemo.util.XmlUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.msebera.android.httpclient.Header;

/**
 * Created on 3/13/16.
 */
public class ListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";

    /**
     * Content is empty, need to fetch from web or cache. (or Content is finish loaded?)
     */
    public static final int STATE_NONE = 0;
    /**
     * SwipeRefresh, or EmptyLayout's onClick.
     */
    public static final int STATE_REFRESH = 1;
    /**
     * Scroll to end, loading more.
     */
    public static final int STATE_LOADMORE = 2;
    public static final int STATE_NOMORE = 3;
    public static final int STATE_PRESSNONE = 4;// 正在下拉但还没有到刷新的状态
    public static int mState = STATE_NONE;

    private int mCurrentPage = 0;
    private ParserTask mParserTask;

    @InjectView(R.id.swiperefreshlayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.listview)
    protected ListView mListView;

    protected ListFragmentAdapter mAdapter;

    private int mCatalog = 0;

    protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            if (isAdded()) {
                if (mState == STATE_REFRESH) {
                    onrefreshNetworkSuccess();
                }
                executeParserTask(responseBody);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private void onrefreshNetworkSuccess() {
    }

    @Override
    public void onRefresh() {
        if (mState == STATE_REFRESH) {
            return;
        }

        mListView.setSelection(0);
        setSwipeRefreshLoadingState();
        mCurrentPage = 0;
        mState = STATE_REFRESH;

        requestData(true);
    }

    protected void requestData(boolean refresh) {
        // TODO cache.

        sendRequestData();
    }

    protected void setSwipeRefreshLoadingState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    protected void setSwipeRefreshLoadedState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

    class ParserTask extends AsyncTask<Void, Void, String> {
        private final byte[] responseData;
        private boolean parserError;
        private List<Tweet> list;

        public ParserTask(byte[] data) {
            responseData = data;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                ListEntity data = parseList(new ByteArrayInputStream(responseData));
                // TODO Save cache.

                list = data.getList();

                // TODO handle error.
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

    protected void executeOnLoadFinish() {
        setSwipeRefreshLoadedState();
        mState = STATE_NONE;
    }


    protected TweetsList parseList(InputStream inputStream) throws Exception {
        return XmlUtils.toBean(TweetsList.class, inputStream);
    }

    protected void sendRequestData() {
        OSChinaApi.getTweetList(mCatalog, mCurrentPage, mHandler);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mCatalog = args.getInt(BUNDLE_KEY_CATALOG);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_layout, null);
//        View view = inflater.inflate(R.layout.list_cell_tweet, null);
        View view = inflater.inflate(R.layout.fragment_pull_refresh_listview, null);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mCatalog > 0) return;

        ButterKnife.inject(this, view);
        initView(view);
    }

    public void initView(View view) {
        // TODO swipeRefresh
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4
        );

        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter = getListAdapter();
            mListView.setAdapter(mAdapter);
        }

        sendRequestData();
    }

    private ListFragmentAdapter getListAdapter() {
        return new ListFragmentAdapter();
    }

    protected void executeOnLoadDataSuccess(List<Tweet> data) {
        if (data == null) {
            data = new ArrayList<>();
        }

        if (mCurrentPage == 0) {
            mAdapter.clear();
        }

        for (int i = 0; i < data.size(); i++) {
            if (compareTo(mAdapter.getData(), data.get(i))) {
                data.remove(i);
                i--;
            }
        }

        mAdapter.addData(data);
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


}
