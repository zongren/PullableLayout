package me.zongren.pullablelayout.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import me.zongren.pullablelayout.Constant.Side;
import me.zongren.pullablelayout.Inteface.Pullable;

/**
 * Created by 宗仁 on 16/5/31.
 * All Rights Reserved By 宗仁.
 */
public class PullableGridView extends GridView implements Pullable {

    public PullableGridView(Context context) {
        super(context);
    }

    public PullableGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean reachEdgeOfBottom() {
        if (getCount() == 0) {
            return true;
        } else if (getLastVisiblePosition() == (getCount() - 1)) {
            if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null && getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight()) {
                return true;
            }
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
        if (getCount() == 0) {
            return true;
        } else if (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0) {
            return true;
        }
        return false;
    }
}
