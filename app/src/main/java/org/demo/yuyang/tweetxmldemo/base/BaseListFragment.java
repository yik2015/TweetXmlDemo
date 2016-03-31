package org.demo.yuyang.tweetxmldemo.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import android.widget.AdapterView;

import org.demo.yuyang.tweetxmldemo.bean.Entity;

/**
 * Created on 3/31/16.
 */
public abstract class BaseListFragment<T extends Entity> extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener,
        AbsListView.OnScrollListener {

        public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";
}
