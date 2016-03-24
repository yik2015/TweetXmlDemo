package org.demo.yuyang.tweetxmldemo.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 3/24/16.
 */
@XStreamAlias("oschina")
public class TweetsList extends Entity implements ListEntity<Tweet>{

    public final static int CATALOG_LATEST = 0;
    public final static int CATALOG_HOT = -1;
    public final static int CATALOG_ME = 1;

    @XStreamAlias("tweetcount")
    private int tweetCount;
    @XStreamAlias("pagesize")
    private int pagesize;
    @XStreamAlias("tweets")
    private List<Tweet> tweetList = new ArrayList<>();

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    @Override
    public List<Tweet> getList() {
        return tweetList;
    }
}
