package me.zongren.pullablelayout.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import me.zongren.pullablelayout.Constant.Side;
import me.zongren.pullablelayout.Inteface.Pullable;

/**
 * Created by 宗仁 on 16/6/14.
 * All Rights Reserved By 宗仁.
 */
public class PullableHorizontalScrollView extends HorizontalScrollView implements Pullable {
    public PullableHorizontalScrollView(Context context) {
        this(context, null);
    }

    public PullableHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullableHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean reachEdgeOfLeft() {
        if (getScrollX() == 0) {
            return true;
        }
        return false;
    }

    public boolean reachEdgeOfRight() {
        if (getChildCount() == 0) {
            return true;
        }
        if (getScrollX() >= (getChildAt(0).getHeight() - getMeasuredHeight())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean reachEdgeOfSide(Side side) {
        if ((side.getValue() & Side.LEFT.getValue()) > 0) {
            return reachEdgeOfLeft();
        } else if ((side.getValue() & Side.RIGHT.getValue()) > 0) {
            return reachEdgeOfRight();
        } else {
            return true;
        }
    }
}
