package org.demo.yuyang.tweetxmldemo;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.demo.yuyang.tweetxmldemo.util.TLog;

import java.util.Locale;

import cz.msebera.android.httpclient.client.params.ClientPNames;

/**
 * Created on 3/13/16.
 */
public class ApiHttpClient {
    private static final String HOST = "www.oschina.net";
    private static String API_URL = "http://www.oschina.net/%s";

    public static AsyncHttpClient client;

    public ApiHttpClient() {
    }

    public static AsyncHttpClient getHttpClient() {
        return client;
    }

    public static void delete(String partUrl, AsyncHttpResponseHandler handler) {
        client.delete(getAbsoluteApiUrl(partUrl), handler);

        log(new StringBuilder("DELETE ").append(partUrl).toString());
    }

    public static void get(String partUrl, AsyncHttpResponseHandler handler) {
        client.get(getAbsoluteApiUrl(partUrl), handler);

        log(new StringBuilder("GET ").append(partUrl).toString());
    }

    public static void get(String partUrl, RequestParams params,
                           AsyncHttpResponseHandler handler) {
        client.get(getAbsoluteApiUrl(partUrl), params, handler);
        log(new StringBuilder("GET ").append(partUrl).append("&")
                .append(params).toString());
    }

    /**
     * Format partUrl to absoluteUrl.
     * For example, partUrl is "action/api/tweet_list",
     * then return "http://www.oschina.net/action/api/tweet_list"
     *
     * @param partUrl
     * @return
     */
    public static String getAbsoluteApiUrl(String partUrl) {
        String url = partUrl;
        if (!partUrl.startsWith("http:") && !partUrl.startsWith("https:")) {
            url = String.format(API_URL, partUrl);
        }

        Log.d("base_client", "request:" + url);
        return url;
    }

    public static void getDirect(String url, AsyncHttpResponseHandler handler) {
        client.get(url, handler);
        log(new StringBuilder("GET ").append(url).toString());
    }

    public static void post(String partUrl, AsyncHttpResponseHandler handler) {
        client.post(getAbsoluteApiUrl(partUrl), handler);
        log(new StringBuilder("POST ").append(partUrl).toString());
    }

    public static void post(String partUrl, RequestParams params,
                            AsyncHttpResponseHandler handler) {
        client.post(getAbsoluteApiUrl(partUrl), params, handler);
        log(new StringBuilder("POST ").append(params).append("&")
                .append(params).toString());
    }

    public static void put(String partUrl, AsyncHttpResponseHandler handler) {
        client.put(getAbsoluteApiUrl(partUrl), handler);
        log(new StringBuilder("PUT ").append(partUrl).toString());
    }

    public static void put(String partUrl, RequestParams params,
                           AsyncHttpResponseHandler handler) {
        client.put(getAbsoluteApiUrl(partUrl), params, handler);
        log(new StringBuilder("PUT ").append(params).append("&")
                .append(params).toString());
    }

    public static void setHttpClient(AsyncHttpClient c) {
        client = c;
        client.addHeader("Accept-Language", Locale.getDefault().toString());
        client.addHeader("Host", HOST);
        client.addHeader("Connection", "Keep-Alive");
        /**
         * public abstract cz.msebera.android.httpclient.params.HttpParams getParams()
         *
         * Obtains the parameters for this client. These parameters will
         * become defaults for all requests being executed with this client,
         * and for the parameters of dependent objects in this client.
         *
         * @deprecated (4.3) use
         * {@link cz.msebera.android.httpclient.client.config.RequestConfig}
         *
         * @returns the default parameters
         */
        client.getHttpClient().getParams()
                .setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

        setUserAgent(ApiClientHelper.getUserAgent(AppContext.getInstance()));
    }

    public static void setUserAgent(String userAgent) {
        client.setUserAgent(userAgent);
    }

    public static void setCookie(String cookie) {
        client.addHeader("Cookie", cookie);
    }

    private static String appCookie;

    public static void cleanCookie() {
        appCookie = "";
    }

    public static String getCookie(AppContext appContext) {
        if (appCookie == null || appCookie == "") {
            appCookie = appContext.getProperty("cookie");
        }
        return appCookie;
    }

    public static void log(String log) {
        Log.d("baseApi", log);
        TLog.log("Test", log);
    }
}
