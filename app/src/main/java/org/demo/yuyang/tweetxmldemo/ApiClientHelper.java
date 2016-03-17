package org.demo.yuyang.tweetxmldemo;

import android.os.Build;

/**
 * Created on 3/14/16.
 */
public class ApiClientHelper {

    /**
     *
     */
    public static String getUserAgent(AppContext appContext) {
        StringBuilder sb = new StringBuilder("OSChina.NET");
        sb.append('/' + appContext.getPackageInfo().versionName + '_'
                + appContext.getPackageInfo().versionCode);
        sb.append("/Android");
        sb.append("/" + Build.VERSION.RELEASE);
        sb.append("/" + Build.MODEL);
        sb.append("/" + appContext.getAppId());

        return sb.toString();
    }
}
