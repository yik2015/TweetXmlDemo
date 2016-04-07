package org.demo.yuyang.tweetxmldemo.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created on 4/5/16.
 */
@XStreamAlias("result")
public class Result implements Serializable {

    @XStreamAlias("errorCode")
    private int errorCode;

    @XStreamAlias("errorMessage")
    private String errorMessage;

    public boolean OK() {
        return errorCode == 1;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
