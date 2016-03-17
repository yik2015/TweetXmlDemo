package org.demo.yuyang.tweetxmldemo.fragment;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.demo.yuyang.tweetxmldemo.bean.ListEntity;
import org.demo.yuyang.tweetxmldemo.bean.Tweet;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created on 3/13/16.
 */
public class ListFragment extends Fragment {
    private int mCurrentPage = 0;
    private ParserTask mParserTask;

    protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

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
        }
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

    protected ListEntity parseList(InputStream is) throws Exception {
        return null;
    }
}
