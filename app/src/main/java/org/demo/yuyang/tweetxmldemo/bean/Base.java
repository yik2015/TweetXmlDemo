package org.demo.yuyang.tweetxmldemo.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created on 3/15/16.
 */
public class Base implements Serializable{

    @XStreamAlias("notice")
    protected Notice notice;

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}
