package org.demo.yuyang.tweetxmldemo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.demo.yuyang.tweetxmldemo.R;
import org.demo.yuyang.tweetxmldemo.bean.Entity;
import org.demo.yuyang.tweetxmldemo.util.StringUtils;
import org.demo.yuyang.tweetxmldemo.util.TDevice;
import org.demo.yuyang.tweetxmldemo.widget.MyLinkMovementMethod;
import org.demo.yuyang.tweetxmldemo.widget.TweetTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 3/30/16.
 */
public class ListBaseAdapter<T extends Entity> extends BaseAdapter {

    /**
     * Same as {@link #STATE_NETWORK_ERROR} except
     * the text's content is {@link #_noDateText}.
     * At this state {@link #getCount()} return {@link #getDataSizePlus1()}
     */
    public static final int STATE_EMPTY_ITEM = 0;

    /**
     * Page is loading, show the ProgressBar and Text.
     * Text's content is {@link #_loadmoreText}
     * At this state {@link #getCount()} return {@link #getDataSizePlus1()}
     */
    public static final int STATE_LOAD_MORE = 1;

    /**
     * Same as {@link #STATE_NETWORK_ERROR} except
     * the text's content is {@link #_loadFinishText}.
     * At this state {@link #getCount()} return {@link #getDataSizePlus1()}
     */
    public static final int STATE_NO_MORE = 2;

    /**
     * At this state {@link #getCount()} return 1.
     */
    public static final int STATE_NO_DATA = 3;

    /**
     * At this state {@link #getCount()} return {@link #getDataSize()}(no Footerview)
     */
    public static final int STATE_LESS_ONE_PAGE = 4;

    /**
     * Network error state. Hide the ProgressBar and
     * display the Text. Text's content is based on
     * device's online state.
     * At this state {@link #getCount()} return {@link #getDataSizePlus1()}
     */
    public static final int STATE_NETWORK_ERROR = 5;
    public static final int STATE_OTHER = 6;

    protected int state = STATE_LESS_ONE_PAGE;

    protected int _loadmoreText;
    protected int _loadFinishText;
    protected int _noDateText;
    protected int mScreenWidth;

    private LayoutInflater mInflater;

    protected LayoutInflater getLayoutInflater(Context context) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        return mInflater;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    protected ArrayList<T> mDatas = new ArrayList<>();

    public ListBaseAdapter() {
        _loadmoreText = R.string.loading;
        _loadFinishText = R.string.loading_no_more;
        _noDateText = R.string.error_view_no_data;
    }

    @Override
    public int getCount() {
        switch (getState()) {
            case STATE_EMPTY_ITEM:
                return getDataSizePlus1();
            case STATE_NETWORK_ERROR:
            case STATE_LOAD_MORE:
                return getDataSizePlus1();
            case STATE_NO_DATA:
                return 1;
            case STATE_NO_MORE:
                return getDataSizePlus1();
            case STATE_LESS_ONE_PAGE:
                return getDataSize();
            default:
                break;
        }
        return getDataSize();
    }

    public int getDataSizePlus1() {
        if (hasFooterView()) {
            return getDataSize() + 1;
        }
        return getDataSize();
    }

    public int getDataSize() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        if (mDatas.size() > position) {
            return mDatas.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(ArrayList<T> data) {
        mDatas = data;
        notifyDataSetChanged();
    }

    public ArrayList<T> getData() {
        return mDatas == null ? (mDatas = new ArrayList<>()) : mDatas;
    }

    public void addData(List<T> data) {
        /**
         * Returns whether this List contains no elements.
         *
         * @return true if this list has no elements, false otherwise.
         */
        if (mDatas != null && data != null && !data.isEmpty()) {

            /**
             * public boolean addAll(Collection<? extends E> collection);
             * Adds the objects in the specified collection to this ArrayList.
             */
            mDatas.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void addItem(int pos, T obj) {
        if (mDatas != null) {
            mDatas.add(pos, obj);
        }
        notifyDataSetChanged();
    }

    public void removeItem(Object object) {
        mDatas.remove(object);
        notifyDataSetChanged();
    }

    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void setNoDataText(int noDataText) {
        _noDateText = noDataText;
    }

    protected boolean loadMoreHasBg() {
        return true;
    }

    private LinearLayout mFooterView;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == getCount() - 1 && hasFooterView()) {
            if (getState() == STATE_LOAD_MORE
                    || getState() == STATE_NO_MORE
                    || getState() == STATE_EMPTY_ITEM
                    || getState() == STATE_NETWORK_ERROR) {

//                TODO mFooterView = (LinearLayout) LayoutInflater.from(
//                        parent.getContext()).inflate(
//                        R.layout.list_cell_footer, null);

                mFooterView = (LinearLayout) getLayoutInflater(
                        parent.getContext()).inflate(
                        R.layout.list_cell_footer, null);

                if (!loadMoreHasBg()) {
                    mFooterView.setBackgroundDrawable(null);
                }

                ProgressBar progressBar = (ProgressBar)
                        mFooterView.findViewById(R.id.progressbar);
                TextView textView = (TextView)
                        mFooterView.findViewById(R.id.text);

                switch (getState()) {
                    case STATE_LOAD_MORE:
                        setFooterViewLoading();
                        break;

                    case STATE_NO_MORE:
                        mFooterView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        textView.setText(_loadFinishText);
                        break;

                    case STATE_EMPTY_ITEM:
                        mFooterView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        textView.setText(_noDateText);
                        break;

                    case STATE_NETWORK_ERROR:
                        mFooterView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        if (TDevice.hasInternet()) {
                            textView.setText("加载出错了");
                        } else {
                            textView.setText("没有可用的网络");
                        }
                        break;

                    default:
                        mFooterView.setVisibility(View.GONE);
                        break;
                }

                return mFooterView;
            }
        }

        if (position < 0) {
            position = 0;
        }

        return getRealView(position, convertView, parent);
    }

    protected View getRealView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    protected boolean hasFooterView() {
        return true;
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterViewLoading(String loadMsg) {
        ProgressBar progress = (ProgressBar) mFooterView
                .findViewById(R.id.progressbar);
        TextView text = (TextView) mFooterView.findViewById(R.id.text);
        mFooterView.setVisibility(View.VISIBLE);

        if (StringUtils.isEmpty(loadMsg)) {
            text.setText(_loadmoreText);
        } else {
            text.setText(loadMsg);
        }
    }

    public void setFooterViewLoading() {
        setFooterViewLoading("");
    }

    protected void setContent(TweetTextView contentView, String content) {
        contentView.setMovementMethod(MyLinkMovementMethod.a());
        contentView.setFocusable(false);
        contentView.setDispatchToParent(true);
        contentView.setLongClickable(false);

        //TODO Span
    }

    protected void setText(TextView textView, String text, boolean needGone) {
        if (text == null || TextUtils.isEmpty(text)) {
            if (needGone) {
                textView.setVisibility(View.GONE);
            }
        } else {
            textView.setText(text);
        }
    }

    protected void setText(TextView textView, String text) {
        setText(textView, text, false);
    }

//    class Footer {
//        ProgressBar progressBar;
//        TextView textView;
//    }

}
