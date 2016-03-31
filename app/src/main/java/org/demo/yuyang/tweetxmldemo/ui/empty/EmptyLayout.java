package org.demo.yuyang.tweetxmldemo.ui.empty;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.demo.yuyang.tweetxmldemo.R;
import org.demo.yuyang.tweetxmldemo.util.TDevice;

/**
 * EmptyLayout. Displaying when data is loading or content's empty.<br/>
 * Consists of 3 views: Text, Image, and ProgressBar.<br/>
 * <br/>
 * <p>i. Default state - loading({@link #NETWORK_LOADING}),
 * displaying this full-screen layout, Image is hided, the other two is displaying.
 * Non-clickable.</p>
 *
 * <p>ii. Network error state - {@link #NETWORK_ERROR},
 * display Image and Text, hide the ProgressBar.
 * Image and Text's content are decided by device's is online or not.
 * Clickable.</p>
 *
 * <p>iii. Content is empty(no data) - {@link #NODATA},
 * same as Network error state, except the Image and Text's content.
 * Clickable.</p>
 *
 * <p>iv. Content is loaded. - {@link #HIDE_LAYOUT},
 * no need for this view. Hide all.</p>
 */
public class EmptyLayout extends LinearLayout implements
        android.view.View.OnClickListener {// , ISkinUIObserver {

    public static final int HIDE_LAYOUT = 4;
    public static final int NETWORK_ERROR = 1;
    public static final int NETWORK_LOADING = 2;
    public static final int NODATA = 3;
    public static final int NODATA_ENABLE_CLICK = 5;
    public static final int NO_LOGIN = 6;

    private ProgressBar animProgress;
    private boolean clickEnable = true;
    private final Context context;
    public ImageView img;
    private android.view.View.OnClickListener listener;
    private int mErrorState;
    private RelativeLayout mLayout;
    private String strNoDataContent = "";
    private TextView tv;

    public EmptyLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        View view = View.inflate(context, R.layout.view_error_layout, null);
        img = (ImageView) view.findViewById(R.id.img_error_layout);
        tv = (TextView) view.findViewById(R.id.tv_error_layout);
        mLayout = (RelativeLayout) view.findViewById(R.id.pageerrLayout);
        animProgress = (ProgressBar) view.findViewById(R.id.animProgress);
        setBackgroundColor(-1);
        setOnClickListener(this);
        img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (clickEnable) {
                    // setErrorType(NETWORK_LOADING);
                    if (listener != null)
                        listener.onClick(v);
                }
            }
        });
        addView(view);
        changeErrorLayoutBgMode(context);
    }

    public void changeErrorLayoutBgMode(Context context1) {
        // mLayout.setBackgroundColor(SkinsUtil.getColor(context1,
        // "bgcolor01"));
        // tv.setTextColor(SkinsUtil.getColor(context1, "textcolor05"));
    }

    public void dismiss() {
        mErrorState = HIDE_LAYOUT;
        setVisibility(View.GONE);
    }

    public int getErrorState() {
        return mErrorState;
    }

    public boolean isLoadError() {
        return mErrorState == NETWORK_ERROR;
    }

    public boolean isLoading() {
        return mErrorState == NETWORK_LOADING;
    }

    @Override
    public void onClick(View v) {
        if (clickEnable) {
            // setErrorType(NETWORK_LOADING);
            if (listener != null)
                listener.onClick(v);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // MyApplication.getInstance().getAtSkinObserable().registered(this);
        onSkinChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // MyApplication.getInstance().getAtSkinObserable().unregistered(this);
    }

    public void onSkinChanged() {
        // mLayout.setBackgroundColor(SkinsUtil
        // .getColor(getContext(), "bgcolor01"));
        // tv.setTextColor(SkinsUtil.getColor(getContext(), "textcolor05"));
    }

    public void setDayNight(boolean flag) {}

    public void setErrorMessage(String msg) {
        tv.setText(msg);
    }

    /**
     * 新添设置背景
     *
     * @author 火蚁 2015-1-27 下午2:14:00
     *
     */
    public void setErrorImag(int imgResource) {
        try {
            img.setImageResource(imgResource);
        } catch (Exception e) {
        }
    }

    /**
     * <p>i. Default state - loading({@link #NETWORK_LOADING}),
     * displaying this full-screen layout, Image is hided, the other two is displaying.
     * Non-clickable.</p>
     *
     * <p>ii. Network error state - {@link #NETWORK_ERROR},
     * display Image and Text, hide the ProgressBar.
     * Image and Text's content are decided by device's is online or not.
     * Clickable.</p>
     *
     * <p>iii. Content is empty(no data) - {@link #NODATA},
     * same as Network error state, except the Image and Text's content.
     * Clickable.</p>
     *
     * <p>iv. Content is loaded. - {@link #HIDE_LAYOUT},
     * no need for this view. Hide all.</p>
     * @param i state.
     */
    public void setErrorType(int i) {
        setVisibility(View.VISIBLE);
        switch (i) {
        case NETWORK_ERROR:
            mErrorState = NETWORK_ERROR;
            // img.setBackgroundDrawable(SkinsUtil.getDrawable(context,"pagefailed_bg"));
            if (TDevice.hasInternet()) {
                tv.setText(R.string.error_view_load_error_click_to_refresh);
                img.setBackgroundResource(R.drawable.pagefailed_bg);
            } else {
                tv.setText(R.string.error_view_network_error_click_to_refresh);
                img.setBackgroundResource(R.drawable.page_icon_network);
            }
            img.setVisibility(View.VISIBLE);
            animProgress.setVisibility(View.GONE);
            clickEnable = true;
            break;

            // network loading, display ProgressBar and Text, hide the Image, and disable click.
        case NETWORK_LOADING:
            mErrorState = NETWORK_LOADING;
            // animProgress.setBackgroundDrawable(SkinsUtil.getDrawable(context,"loadingpage_bg"));
            animProgress.setVisibility(View.VISIBLE);
            img.setVisibility(View.GONE);
            tv.setText(R.string.error_view_loading);
            clickEnable = false;
            break;

        case NODATA:
            mErrorState = NODATA;
            // img.setBackgroundDrawable(SkinsUtil.getDrawable(context,"page_icon_empty"));
            img.setBackgroundResource(R.drawable.page_icon_empty);
            img.setVisibility(View.VISIBLE);
            animProgress.setVisibility(View.GONE);
            setTvNoDataContent();
            clickEnable = true;
            break;
        case HIDE_LAYOUT:
            setVisibility(View.GONE);
            break;
        case NODATA_ENABLE_CLICK:
            mErrorState = NODATA_ENABLE_CLICK;
            img.setBackgroundResource(R.drawable.page_icon_empty);
            // img.setBackgroundDrawable(SkinsUtil.getDrawable(context,"page_icon_empty"));
            img.setVisibility(View.VISIBLE);
            animProgress.setVisibility(View.GONE);
            setTvNoDataContent();
            clickEnable = true;
            break;
        default:
            break;
        }
    }

    public void setNoDataContent(String noDataContent) {
        strNoDataContent = noDataContent;
    }

    public void setOnLayoutClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void setTvNoDataContent() {
        if (!strNoDataContent.equals(""))
            tv.setText(strNoDataContent);
        else
            tv.setText(R.string.error_view_no_data);
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE)
            mErrorState = HIDE_LAYOUT;
        super.setVisibility(visibility);
    }
}
