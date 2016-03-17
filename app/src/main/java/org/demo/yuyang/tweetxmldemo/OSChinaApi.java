package org.demo.yuyang.tweetxmldemo;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created on 3/13/16.
 */
public class OSChinaApi {

    public static void getTweetList(int uid, int page,
                                    AsyncHttpResponseHandler handler) {
        RequestParams params=new RequestParams();
        params.put("uid", uid);
        params.put("pageIndex", page);
        params.put("pageSize", 20);

        ApiHttpClient.get("action/api/tweet_list", params, handler);
    }
}
