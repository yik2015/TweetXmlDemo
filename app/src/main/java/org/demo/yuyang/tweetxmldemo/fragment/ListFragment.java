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
public class ListFragment extends Fragment {
    private int mCurrentPage = 0;
    private ParserTask mParserTask;

    @InjectView(R.id.swiperefreshlayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.listview)
    protected ListView mListView;

//    protected ListBaseAdapter<Tweet> mAdapter;
    protected ListFragmentAdapter mAdapter;

    private int mCatalog = -1;

    protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            if (isAdded()) {
                executeParserTask(responseBody);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

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

                list = data.getList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            executeOnLoadDataSuccess(list);
        }
    }


    protected TweetsList parseList(InputStream inputStream) throws Exception {
        return XmlUtils.toBean(TweetsList.class, inputStream);
    }

    protected void sendRequestData() {
        OSChinaApi.getTweetList(mCatalog, mCurrentPage, mHandler);
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
        ButterKnife.inject(this, view);
        initView(view);
    }

    public void initView(View view) {
        // TODO swipeRefresh

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
            // TODO
//            mAdapter.clear();
        }

        mAdapter.addData(data);
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
