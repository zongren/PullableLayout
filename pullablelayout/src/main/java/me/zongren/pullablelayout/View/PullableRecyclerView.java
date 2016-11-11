package me.zongren.pullablelayout.View;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import me.zongren.pullablelayout.Constant.Side;
import me.zongren.pullablelayout.Inteface.Pullable;

/**
 * Created by 宗仁 on 16/6/13.
 * All Rights Reserved By 宗仁.
 */
public class PullableRecyclerView extends RecyclerView implements Pullable {
    private static final String TAG = "PullableRecyclerView";

    public PullableRecyclerView(Context context) {
        super(context);
    }

    public PullableRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean reachEdgeOfBottom() {
        if (getChildCount() == 0) {
            return true;
        }
        LayoutManager layoutManager = getLayoutManager();

        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                return true;
            } else {
                View lastChild = linearLayoutManager.getChildAt(linearLayoutManager.getChildCount()-1);
                int top = lastChild.getTop();
                int height = lastChild.getHeight();
                return top+height<=getHeight();
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            if (staggeredGridLayoutManager.getOrientation() == StaggeredGridLayoutManager.HORIZONTAL) {
                return true;
            } else {
                if (staggeredGridLayoutManager.getSpanCount() == 0) {
                    return true;
                } else {
                    int[] positions = staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(null);
                    return positions[staggeredGridLayoutManager.getSpanCount() - 1] == (staggeredGridLayoutManager.getChildCount() - 1);
                }
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
        } else if ((side.getValue() & Side.LEFT.getValue()) > 0) {
            return reachEdgeOfLeft();
        } else if ((side.getValue() & Side.RIGHT.getValue()) > 0) {
            return reachEdgeOfRight();
        } else {
            return false;
        }
    }

    public boolean reachEdgeOfTop() {
        if (getChildCount() == 0) {
            return true;
        }
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                return true;
            } else {
                return linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            if (staggeredGridLayoutManager.getOrientation() == StaggeredGridLayoutManager.HORIZONTAL) {
                return true;
            } else {
                if (staggeredGridLayoutManager.getSpanCount() == 0) {
                    return true;
                } else {
                    int[] positions = staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(null);
                    return positions[0] == 0;
                }
            }
        }
        return false;
    }

    private boolean reachEdgeOfLeft() {
        if (getChildCount() == 0) {
            return true;
        }
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                return true;
            } else {
                return linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            if (staggeredGridLayoutManager.getOrientation() == StaggeredGridLayoutManager.VERTICAL) {
                return true;
            } else {
                if (staggeredGridLayoutManager.getSpanCount() == 0) {
                    return true;
                } else {
                    int[] positions = staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(null);
                    return positions[0] == 0;
                }
            }

        }
        return false;
    }

    private boolean reachEdgeOfRight() {
        if (getChildCount() == 0) {
            return true;
        }
        LayoutManager layoutManager = getLayoutManager();

        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                return true;
            } else {
                return linearLayoutManager.findLastCompletelyVisibleItemPosition() == (getAdapter().getItemCount() - 1);
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            if (staggeredGridLayoutManager.getOrientation() == StaggeredGridLayoutManager.VERTICAL) {
                return true;
            } else {
                if (staggeredGridLayoutManager.getSpanCount() == 0) {
                    return true;
                } else {
                    int[] positions = staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(null);
                    return positions[staggeredGridLayoutManager.getSpanCount() - 1] == (staggeredGridLayoutManager.getChildCount() - 1);
                }
            }
        }
        return false;
    }
}