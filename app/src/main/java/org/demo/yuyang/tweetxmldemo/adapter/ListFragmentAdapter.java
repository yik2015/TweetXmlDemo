package org.demo.yuyang.tweetxmldemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.demo.yuyang.tweetxmldemo.AppContext;
import org.demo.yuyang.tweetxmldemo.R;
import org.demo.yuyang.tweetxmldemo.bean.Entity;
import org.demo.yuyang.tweetxmldemo.bean.Tweet;
import org.demo.yuyang.tweetxmldemo.emoji.InputHelper;
import org.demo.yuyang.tweetxmldemo.util.ImageUtils;
import org.demo.yuyang.tweetxmldemo.util.StringUtils;
import org.demo.yuyang.tweetxmldemo.util.TypefaceUtils;
import org.demo.yuyang.tweetxmldemo.widget.AvatarView;
import org.demo.yuyang.tweetxmldemo.widget.MyLinkMovementMethod;
import org.demo.yuyang.tweetxmldemo.widget.TweetTextView;
import org.kymjs.kjframe.Core;
import org.kymjs.kjframe.utils.DensityUtils;

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

    public ArrayList<T> getData() {
        return mDatas == null ? (mDatas = new ArrayList<>()) : mDatas;
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

    private Bitmap recordBitmap;
    private Context context;

    private void initRecordImg(Context context) {
        recordBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.audio3);

        recordBitmap = ImageUtils.zoomBitmap(recordBitmap,
                DensityUtils.dip2px(context, 20f),
                DensityUtils.dip2px(context, 20f));
    }

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

        if (tweet.getAuthorid() == AppContext.getInstance().getLoginUid()) {
            vh.del.setVisibility(View.VISIBLE);
            vh.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO
                }
            });
        } else {
            vh.del.setVisibility(View.GONE);
        }

        vh.face.setUserInfo(tweet.getAuthorid(), tweet.getAuthor());
        vh.face.setAvatarUrl(tweet.getPortrait());
        vh.author.setText(tweet.getAuthor());
        vh.time.setText(StringUtils.friendly_time(tweet.getPubDate()));
        vh.content.setMovementMethod(MyLinkMovementMethod.a());
        vh.content.setFocusable(false);
        vh.content.setDispatchToParent(true);
        vh.content.setLongClickable(false);

        // Spanned fromHtml(String)
        // Returns displayable styled text from the provided HTML string.
        Spanned span = Html.fromHtml(tweet.getBody().trim());
//        vh.content.setText(span);

        if (!StringUtils.isEmpty(tweet.getAttach())) {
            if (recordBitmap == null) {
                initRecordImg(context);
            }

            ImageSpan recordImg = new ImageSpan(context, recordBitmap);

            SpannableString str = new SpannableString("c");
            // int SPAN_INCLUSIVE_EXCLUSIVE
            // Non-0-length spans of type SPAN_INCLUSIVE_EXCLUSIVE
            // expand to include text inserted at their starting point
            // but not at their ending point.
            str.setSpan(recordImg, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

            vh.content.setText(str);

            span = InputHelper.displayEmoji(context.getResources(), span);

            vh.content.append(span);
        } else {
            span = InputHelper.displayEmoji(context.getResources(), span);
            vh.content.setText(span);
        }

        vh.commentcount.setText(tweet.getCommentCount());

        showTweetImage(vh, tweet.getImgSmall(), tweet.getImgBig());
        tweet.setLikeUsers(context, vh.likeUsers, true);

        if (tweet.getLikeUser() == null) {
            vh.tvLikeState.setVisibility(View.GONE);
        } else {
            vh.tvLikeState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppContext.getInstance().isLogin()) {
                        // TODO updateLikeState
                    } else {
                        AppContext.showToast("先登陆再赞吧亲");
                        // TODO showLoginActivity
                    }
                }
            });
        }

        TypefaceUtils.setTypeface(vh.tvLikeState);

        if (tweet.getIsLike() == 1) {
            vh.tvLikeState.setTextColor(AppContext.getInstance()
                    .getResources().getColor(R.color.day_colorPrimary));
        } else {
            vh.tvLikeState.setTextColor(AppContext.getInstance()
                    .getResources().getColor(R.color.gray));
        }

        // TODO display platform
        return convertView;
    }

    private void showTweetImage(ViewHolder vh, String imgSmall, String imgBig) {
        if (!TextUtils.isEmpty(imgBig)) {
            vh.image.setTag(imgBig);
            new Core.Builder().view(vh.image).size(300, 300)
                    .url(imgBig + "?300X300")
                    .loadBitmapRes(R.drawable.pic_bg).doTask();

            vh.image.setVisibility(View.VISIBLE);
        } else {
            vh.image.setVisibility(View.GONE);
        }
    }

    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
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
