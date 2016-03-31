package org.demo.yuyang.tweetxmldemo.interf;

import android.app.ProgressDialog;

/**
 * Created on 3/30/16.
 */
public interface DialogControl {
    void hideWaitDialog();

    ProgressDialog showWaitDialog();

    ProgressDialog showWaitDialog(int resId);

    ProgressDialog showWaitDialog(String text);
}
