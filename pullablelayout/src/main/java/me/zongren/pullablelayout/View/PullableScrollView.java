package me.zongren.pullablelayout.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import me.zongren.pullablelayout.Constant.Side;
import me.zongren.pullablelayout.Inteface.Pullable;

/**
 * Created by 宗仁 on 16/5/31.
 * All Rights Reserved By 宗仁.
 */
public class PullableScrollView extends ScrollView implements Pullable {
    private static final String TAG = "PullableScrollView";

    public PullableScrollView(Context context) {
        this(context, null);
    }

    public PullableScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean reachEdgeOfBottom() {
        if (getChildCount() == 0) {
            return true;
        }
        if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean reachEdgeOfSide(Side side) {
        if ((side.getValue() & Side.TOP.getValue()) > 0) {
            return reachEdgeOfTop();
        } else if ((side.getValue() & Side.BOTTOM.getValue()) > 0) {
            return reachEdgeOfBottom();
        } else {
            return true;
        }
    }

    public boolean reachEdgeOfTop() {
        if (getScrollY() == 0) {
            return true;
        }
        return false;
    }
}
