package com.renny.simplebrowser.view.widget.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;

import com.renny.simplebrowser.business.webview.X5WebView;

/**
 * 封装了WebView的下拉刷新
 * 
 * @author Li Hong
 * @since 2013-8-22
 */
public class PullToRefreshWebView extends PullToRefreshBase<X5WebView> {
    /**
     * 构造方法
     * 
     * @param context context
     */
    public PullToRefreshWebView(Context context) {
        this(context, null);
    }
    
    /**
     * 构造方法
     * 
     * @param context context
     * @param attrs attrs
     */
    public PullToRefreshWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    /**
     * 构造方法
     * 
     * @param context context
     * @param attrs attrs
     * @param defStyle defStyle
     */
    public PullToRefreshWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     */
    @Override
    protected X5WebView createRefreshableView(Context context, AttributeSet attrs) {
        return new X5WebView(context);
    }

    /**
     */
    @Override
    protected boolean isReadyForPullDown() {

        return mRefreshableView.getWebScrollY() == 0;
    }

    /**
     */
    @Override
    protected boolean isReadyForPullUp() {
        float exactContentHeight = (float) Math.floor(mRefreshableView.getContentHeight() * mRefreshableView.getScale());
        return mRefreshableView.getScrollY() >= (exactContentHeight - mRefreshableView.getHeight());
    }
}
