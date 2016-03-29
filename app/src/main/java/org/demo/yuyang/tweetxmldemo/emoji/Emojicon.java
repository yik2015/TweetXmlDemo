package org.demo.yuyang.tweetxmldemo.emoji;

/**
 * Created on 3/28/16.
 */
public class Emojicon {
    private final int resId; // 图片资源地址
    private final int value; // 一个emoji对应唯一一个value
    private final String emojiStr; // emoji在互联网传递的字符串
    private final String remote;

    public Emojicon(int resId, int value, String emojiStr, String remote) {
        this.resId = resId;
        this.value = value;
        this.emojiStr = emojiStr;
        this.remote = remote;
    }

    public int getResId() {
        return resId;
    }

    public int getValue() {
        return value;
    }

    public String getEmojiStr() {
        return emojiStr;
    }

    public String getRemote() {
        return remote;
    }
}
