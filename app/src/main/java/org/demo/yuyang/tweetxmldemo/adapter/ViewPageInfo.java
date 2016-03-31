package org.demo.yuyang.tweetxmldemo.adapter;

import android.os.Bundle;

/**
 * Created on 3/31/16.
 */
public class ViewPageInfo {

    public final String title;
    public final String tag;
    public final Class<?> cls;
    public final Bundle args;

    public ViewPageInfo(String title, String tag, Class<?> cls, Bundle args) {
        this.title = title;
        this.tag = tag;
        this.cls = cls;
        this.args = args;
    }
}
