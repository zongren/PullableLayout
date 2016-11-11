package me.zongren.pullablelayout.View;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import me.zongren.pullablelayout.Constant.Side;
import me.zongren.pullablelayout.Inteface.Pullable;

/**
 * Created by 宗仁 on 16/5/31.
 * All Rights Reserved By 宗仁.
 */
public class PullableWebView extends WebView implements Pullable {

    public PullableWebView(Context context) {
        super(context);
    }

    public PullableWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean reachEdgeOfBottom() {
        return getScrollY() >= (getContentHeight() * getScaleY() - getMeasuredHeight());
    }

    public boolean reachEdgeOfLeft() {
        return getScrollX() == 0;
    }

    public boolean reachEdgeOfRight() {
        return getScrollX() >= (getContentHeight() * getScaleX() - getMeasuredWidth());
    }

    @Override
    public boolean reachEdgeOfSide(Side side) {
        if ((side.getValue() & Side.TOP.getValue()) > 0) {
            return reachEdgeOfTop();
        } else if ((side.getValue() & Side.LEFT.getValue()) > 0) {
            return reachEdgeOfLeft();
        } else if ((side.getValue() & Side.BOTTOM.getValue()) > 0) {
            return reachEdgeOfBottom();
        } else if ((side.getValue() & Side.RIGHT.getValue()) > 0) {
            return reachEdgeOfRight();
        } else {
            return true;
        }
    }

    public boolean reachEdgeOfTop() {
        return getScrollY() == 0;
    }
}
