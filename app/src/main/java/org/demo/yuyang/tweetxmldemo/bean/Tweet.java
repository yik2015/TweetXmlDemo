package org.demo.yuyang.tweetxmldemo.bean;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import org.demo.yuyang.tweetxmldemo.AppContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 3/15/16.
 */
@XStreamAlias("tweet")
public class Tweet extends Entity implements Parcelable {

    @XStreamAlias("portrait")
    private String portrait;

    @XStreamAlias("author")
    private String author;

    @XStreamAlias("authorid")
    private int authorid;

    @XStreamAlias("body")
    private String body;

    @XStreamAlias("appclient")
    private int appclient;

    @XStreamAlias("commentCount")
    private String commentCount;

    @XStreamAlias("pubDate")
    private String pubDate;

    @XStreamAlias("imgSmall")
    private String imgSmall;

    @XStreamAlias("imgBig")
    private String imgBig;

    @XStreamAlias("attach")
    private String attach;

    @XStreamAlias("likeCount")
    private int likeCount;

    @XStreamAlias("isLike")
    private int isLike;

    @XStreamAlias("likeList")
    private List<User> likeUser = new ArrayList<>();

    private String imageFilePath;

    private String audioPath;

    public Tweet() {
    }

    protected Tweet(Parcel in) {
        id = in.readInt();
        portrait = in.readString();
        author = in.readString();
        authorid = in.readInt();
        body = in.readString();
        appclient = in.readInt();
        commentCount = in.readString();
        pubDate = in.readString();
        imgSmall = in.readString();
        imgBig = in.readString();
        attach = in.readString();
        imageFilePath = in.readString();
        audioPath = in.readString();
        isLike = in.readInt();
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(portrait);
        dest.writeString(author);
        dest.writeInt(authorid);
        dest.writeString(body);
        dest.writeInt(appclient);
        dest.writeString(commentCount);
        dest.writeString(pubDate);
        dest.writeString(imgSmall);
        dest.writeString(imgBig);
        dest.writeString(attach);
        dest.writeString(imageFilePath);
        dest.writeString(audioPath);
        dest.writeInt(isLike);
    }

    public void setLikeUsers(Context context, TextView likeUser, boolean limit) {
        if (getLikeCount() > 0 && getLikeUser() != null
                && !getLikeUser().isEmpty()) {
            likeUser.setVisibility(View.VISIBLE);
            likeUser.setMovementMethod(LinkMovementMethod.getInstance());
            likeUser.setFocusable(false);
            likeUser.setLongClickable(false);
            likeUser.setText(addClickablePart(context, limit), TextView.BufferType.SPANNABLE);
        } else {
            likeUser.setVisibility(View.GONE);
            likeUser.setText("");
        }
    }

    private SpannableStringBuilder addClickablePart(final Context context,
                                                    boolean limit) {
        StringBuilder sb = new StringBuilder();
        int showCount = getLikeUser().size();
        if (limit && showCount > 4) {
            showCount = 4;
        }

        if (getIsLike() == 1) {
            for (int i = 0; i < getLikeUser().size(); i++) {
                if (getLikeUser().get(i).getId() == AppContext.getInstance()
                        .getLoginUid()) {
                    getLikeUser().remove(i);
                }
            }

            getLikeUser().add(0, AppContext.getInstance().getLoginUser());
        }

        for (int i = 0; i < showCount; i++) {
            sb.append(getLikeUser().get(i).getName()).append("、");
        }

        String likeUsersStr = sb.substring(0, sb.lastIndexOf("、"));

        SpannableString spanStr = new SpannableString("");
        SpannableStringBuilder ssb = new SpannableStringBuilder(spanStr);

        ssb.append(likeUsersStr);

        String[] likeUsers = likeUsersStr.split("、");

        if (likeUsers.length > 0) {
            for (int i = 0; i < likeUsers.length; i++) {
                final String name = likeUsers[i];
                final int start = likeUsersStr.indexOf(name) + spanStr.length();
                final int index = i;

                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        User user = getLikeUser().get(index);
                        // TODO UIHelper.showUserCenter(...);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(false);
                    }
                }, start, start + name.length(), 0);
            }
        }

        if (likeUsers.length < getLikeCount()) {
            ssb.append("等");
            int start = ssb.length();

            String more = getLikeCount() + "人";
            ssb.append(more);

            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Bundle bundle = new Bundle();
                    // TODO bundle.putInt();
                    // TODO UIHelper.show...
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            }, start, start + more.length(), 0);
        }

        return ssb.append("觉得很赞");
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAuthorid() {
        return authorid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getAppclient() {
        return appclient;
    }

    public void setAppclient(int appclient) {
        this.appclient = appclient;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImgSmall() {
        return imgSmall;
    }

    public void setImgSmall(String imgSmall) {
        this.imgSmall = imgSmall;
    }

    public String getImgBig() {
        return imgBig;
    }

    public void setImgBig(String imgBig) {
        this.imgBig = imgBig;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public List<User> getLikeUser() {
        return likeUser;
    }

    public void setLikeUser(List<User> likeUser) {
        this.likeUser = likeUser;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }
}
