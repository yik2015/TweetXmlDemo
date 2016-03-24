package org.demo.yuyang.tweetxmldemo.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.demo.yuyang.tweetxmldemo.R;
import org.demo.yuyang.tweetxmldemo.bean.Entity;
import org.demo.yuyang.tweetxmldemo.bean.Tweet;
import org.demo.yuyang.tweetxmldemo.util.StringUtils;
import org.demo.yuyang.tweetxmldemo.widget.AvatarView;
import org.demo.yuyang.tweetxmldemo.widget.MyLinkMovementMethod;
import org.demo.yuyang.tweetxmldemo.widget.TweetTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created on 3/24/16.
 */
public class ListFragmentAdapter<T extends Entity> extends BaseAdapter {

    protected ArrayList<T> mDatas = new ArrayList<T>();

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        if (mDatas.size() > position) {
            return mDatas.get(position);
        }
        return null;
    }

    public void addData(List<T> data) {
        if (mDatas != null && data != null && !data.isEmpty()) {
            mDatas.addAll(data);
        }
        notifyDataSetChanged();
    }

    // Get the row id associated with the specified position in the list.
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getRealView(position, convertView, parent);
    }

    private Context context;

    private View getRealView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        final ViewHolder vh;
        if (convertView == null || convertView.getTag() == null) {
            convertView = View.inflate(context, R.layout.list_cell_tweet, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final Tweet tweet = (Tweet) mDatas.get(position);

        vh.del.setVisibility(View.GONE);

        vh.face.setUserInfo(tweet.getAuthorid(), tweet.getAuthor());
        vh.face.setAvatarUrl(tweet.getPortrait());
        vh.author.setText(tweet.getAuthor());
        vh.time.setText(StringUtils.friendly_time(tweet.getPubDate()));
        vh.content.setMovementMethod(MyLinkMovementMethod.a());
        vh.content.setFocusable(false);
        vh.content.setDispatchToParent(true);
        vh.content.setLongClickable(false);

        Spanned span = Html.fromHtml(tweet.getBody().trim());
        vh.content.setText(span);

        vh.commentcount.setText(tweet.getCommentCount());

        tweet.setLikeUsers(context, vh.likeUsers, true);

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.tv_tweet_name)
        TextView author;
        @InjectView(R.id.tv_tweet_time)
        TextView time;
        @InjectView(R.id.tweet_item)
        TweetTextView content;
        @InjectView(R.id.tv_tweet_comment_count)
        TextView commentcount;
        @InjectView(R.id.tv_tweet_platform)
        TextView platform;
        @InjectView(R.id.iv_tweet_face)
        AvatarView face;
        @InjectView(R.id.iv_tweet_image)
        ImageView image;
        @InjectView(R.id.tv_like_state)
        TextView tvLikeState;
        @InjectView(R.id.tv_del)
        TextView del;
        @InjectView(R.id.tv_likeusers)
        TextView likeUsers;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = (String) v.getTag();
                    int index = url.lastIndexOf("?");
                    if (index > 0) {
                        url = url.substring(0, index);
                    }
                    // TODO ImagePreviewActivity.showImagePrivew(v.getContext(), 0, new String[]{url});
                }
            });
        }
    }
}
