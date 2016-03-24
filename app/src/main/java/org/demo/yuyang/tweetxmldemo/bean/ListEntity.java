package org.demo.yuyang.tweetxmldemo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 3/13/16.
 */
public interface ListEntity<T extends Entity> extends Serializable {
    List<T> getList();
}
